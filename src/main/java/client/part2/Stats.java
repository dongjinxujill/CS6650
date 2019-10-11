package client.part2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Stats {
  private long wallTime;
  private long[] latencyStats = new long[5];
  private int requestCount;
  private int responseCount;
  private BlockingQueue<Record> records;
  private List<Long> latency;
  private long latencySum;
  private int failure;
  private Logger logger;

  public Stats(long wallTime, int requestCount, int responseCount, int failure,
      BlockingQueue<Record> records, Logger logger) {
    this.wallTime = wallTime;
    this.records = records;
    this.requestCount = requestCount;
    this.failure = failure;
    this.responseCount = responseCount;
    this.latency = new ArrayList<>();
    this.latencySum = 0;
    this.logger = logger;
  }

  public void performanceStats() {
    writeToCSV();
    System.out.println("stats of threads 1024:" );
    System.out.println("=========================================");
    System.out.println("Total number of requests sent: " + requestCount);
    System.out.println("Total number of successful responses: " + responseCount);
    System.out.println("Total number of failed responses: " + failure);
    System.out.println("Wall time: " + wallTime + " milliseconds");
    calculateStats();
    System.out.println("=========================================");
    System.out.println("The mean of all latencies: " + latencyStats[0] + " milliseconds.");
    System.out.println("The median of all latencies: " + latencyStats[1] + " milliseconds.");
    System.out.println("The throughput: " + latencyStats[2] + " milliseconds.");
    System.out.println("The 99th percentile of latencies: " + latencyStats[3] + " milliseconds.");
    System.out.println("The max response time: " + latencyStats[4] + " milliseconds.");
    System.out.println("Service ends.");
  }

  public void calculateStats() {
    Collections.sort(latency);
    latencyStats[0] = latencySum / latency.size();
    latencyStats[1] = latency.get(latency.size()/2-1);
    latencyStats[2] = requestCount/ wallTime;
    latencyStats[3] = latency.get(latency.size()*99/100-1);
    latencyStats[4] = latency.get(latency.size()-1);

  }

  private void writeToCSV() {
    final String CSV_SEPARATOR = ",";
    try {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(
              "/Users/TOKYO/Documents/GitHub/assignment1/src/main/java/client/part2/CSVFiles/records_1024threads.csv"),
          "UTF-8"));
      for (Record record : records) {
        long currLatency = record.getLatency();
        latencySum += currLatency;
        latency.add(currLatency);
        StringBuffer oneLine = new StringBuffer();
        oneLine.append(record.getStartTime());
        oneLine.append(CSV_SEPARATOR);
        oneLine.append(record.getRequestType());
        oneLine.append(CSV_SEPARATOR);
        oneLine.append(record.getLatency());
        oneLine.append(CSV_SEPARATOR);
        oneLine.append(record.getResponseCode());
        bw.write(oneLine.toString());
        bw.newLine();
      }
      bw.flush();
      bw.close();
    } catch (UnsupportedEncodingException e) {
      logger.info("File parsing to CSV errors: " + e.getMessage());
    } catch (IOException e){
      logger.info("File parsing to CSV errors: " + e.getMessage());
    }
  }
}
