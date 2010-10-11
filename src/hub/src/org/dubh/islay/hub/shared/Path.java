package org.dubh.islay.hub.shared;

/**
 * Defines constants for servlet paths on the server.
 * 
 * @author brianduff
 */
public final class Path {
  private Path() {}
  
  public static final String OAUTH = "oauth";
  public static final String USER = "user";
  public static final String ACTIVITY = "activity";
  public static final String OAUTH_CALLBACK = "oauth_cb";
  public static final String BASE_PATH = "/hub/";
  
  public static final String of(String servlet) {
    return BASE_PATH + servlet;
  }
}
