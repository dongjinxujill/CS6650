package client.part2;

public class Record {
  private long startTime;
  private long latency;
  private int responseCode;
  private String requestType;

  public Record(long startTime, long latency, int responseCode) {
    this.startTime = startTime;
    this.requestType = "POST";
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public long getLatency() {
    return latency;
  }

  public long getStartTime() {
    return startTime;
  }

  public String getRequestType() {
    return requestType;
  }
}
