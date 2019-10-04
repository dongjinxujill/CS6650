package client.part1;

import client.part2.Record;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Thread extends java.lang.Thread implements Runnable {
  private int randomSkierId;
  private int randomTime;
  private int randomLiftNum;
  private int runTimes;
  private String ip;
  private String port;
  private Logger logger;
  private AtomicInteger req = new AtomicInteger();
  private AtomicInteger res = new AtomicInteger();
  private AtomicInteger failure = new AtomicInteger();
  private CountDownLatch firstCountDown;
  private CountDownLatch secondCountDown;
  private BlockingQueue<Record> records;


  public Thread(int randomSkierId, int randomTime, int randomLiftNum, int runTimes,
      CountDownLatch firstCountDown, CountDownLatch secondCountDown,
      BlockingQueue<Record> records, String ip, String port, Logger logger) {
    this.randomLiftNum = randomLiftNum;
    this.randomSkierId = randomSkierId;
    this.randomTime = randomTime;
    this.runTimes = runTimes;
    this.firstCountDown = firstCountDown;
    this.secondCountDown = secondCountDown;
    this.records = records;
    this.ip = ip;
    this.port = port;
    this.logger = logger;
  }

  @Override
  public void run(){
    try {
      firstCountDown.await();
      for (int i = 0; i < runTimes; i++) {
        try {
          String basePath = ip + ":" + port + "/assignment1";
          SkiersApi apiInstance = new SkiersApi();
          ApiClient client = apiInstance.getApiClient();
          client.setBasePath(basePath);

          long start = System.currentTimeMillis();
          ApiResponse<Integer> api = apiInstance.getSkierDayVerticalWithHttpInfo(1,
              "1", Integer.toString(randomTime), randomSkierId);
          countReq();
          long latency = System.currentTimeMillis() - start;
          records.add(new Record(start, latency, api.getStatusCode()));
          if (api.getStatusCode() / 100 == 2) {
            countRes();
          } else {
            countFailure();
            logger.info("Request Fail With Status Code" + api.getStatusCode());
          }
        } catch (Exception e) {
          logger.info("Exception Caught");
          System.out.println(e.getMessage());
        }
      }
    } catch(InterruptedException e) {
      logger.info("Exception Caught");
      System.out.println(e.getMessage());
    }
    finally {
      if (secondCountDown != null) {
        secondCountDown.countDown();
      }
    }
  }

  public void countReq() { req.incrementAndGet(); }

  public void countRes() {
    res.incrementAndGet();
  }

  public AtomicInteger getReq() {
    return req;
  }

  public AtomicInteger getRes() {
    return res;
  }

  public void countFailure() {
    failure.incrementAndGet();
  }

  public AtomicInteger getFailure() {
    return failure;
  }
}