package org.dubh.islay.hub.shared;

/**
 * Enumeration of networks we support.
 * 
 * @author brianduff
 */
public enum Network {
  BUZZ(
      "Google Buzz",
      "image/nw_buzz.png",
      "image/nw_buzz_disabled.png"
  ),
  FACEBOOK(
      "Facebook",
      "image/nw_facebook.png",
      "image/nw_facebook_disabled.png"
  ),
  TWITTER(
      "Twitter",
      "image/nw_twitter.png",
      "image/nw_twitter_disabled.png"
  );
  
  private final String displayName;
  private final String iconUrl;
  private final String disabledIconUrl;
  
  private Network(String displayName, String iconUrl, String disabledIconUrl) {
    this.displayName = displayName;
    this.iconUrl = iconUrl;
    this.disabledIconUrl = disabledIconUrl;
  }
  
  public String displayName() {
    return displayName;
  }
  
  public String iconUrl() {
    return iconUrl;
  }
  
  public String disabledIconUrl() {
    return disabledIconUrl;
  }
}