package org.dubh.islay.hub.client;

import java.io.Serializable;

import com.google.gwt.http.client.URL;

/**
 * Providers of OpenId authentication.
 * 
 * @author brianduff
 */
public enum OpenIdProvider implements Serializable {
  GOOGLE(
      "image/openid_google.png",
      "Google",
      "google.com/accounts/o8/id"
  ),
  FACEBOOK(
      "image/openid_facebook.png",
      "Facebook",
      "www.facebook.com"
  );
  
  private final String imageUrl;
  private final String name;
  private final String providerId;
  
  private OpenIdProvider(String imageUrl, String name, String providerId) {
    this.imageUrl = imageUrl;
    this.name = name;
    this.providerId = providerId;
  }
  
  public String getImageUrl() {
    return imageUrl;
  }

  public String getName() {
    return name;
  }
  
  public String getProviderId() {
    return providerId;
  }
  
  public String getLoginUrl(String ourHostname, String ourAdditionalParameters) {
    // We should strictly be asking AppEngine to tell us this, but we want
    // to avoid making a roundtrip to the server just to get this URL (which
    // is predictable anyway).
    String url = "http://" + ourHostname + "/_ah/login_redir?claimid=" + providerId
        + "&continue=http://" + ourHostname;
    if (ourAdditionalParameters != null) {
      url += URL.encode("?" + ourAdditionalParameters);
    }
    return url;
// In dev mode:
//    http://127.0.0.1:8888/_ah/login?continue=%2F
// In production mode:
//    http://islay-test.appspot.com/_ah/login_redir?claimid=google.com/accounts/o8/id&continue=http://islay-test.appspot.com/
  }
}
