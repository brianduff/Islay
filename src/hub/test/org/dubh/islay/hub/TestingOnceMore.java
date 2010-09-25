package org.dubh.islay.hub;

public class TestingOnceMore {

  private String uid;
  
  private static int aCounter;
  
  static {
    aCounter = 1;
  }
  
  public TestingOnceMore() {
    // New Instance
    uid = new String("Instance UID"+aCounter++);
  }
}
