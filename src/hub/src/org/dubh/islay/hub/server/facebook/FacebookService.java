package org.dubh.islay.hub.server.facebook;

import java.io.IOException;

import org.dubh.islay.hub.server.HttpService;
import org.dubh.islay.hub.server.JsonObjectFactory;
import org.dubh.islay.hub.server.UrlBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Provides access to facebook's api.
 * 
 * @author bduff
 */
public class FacebookService {
  private static final String GRAPH_URL = "https://graph.facebook.com/";
  private final String accessToken;
  private final HttpService http;
  private final JsonObjectFactory json;
  
  @Inject
  FacebookService(@Assisted String accessToken, HttpService http, JsonObjectFactory jsonFactory) {
    this.accessToken = accessToken;
    this.http = http;
    this.json = jsonFactory;
  }
  
  /**
   * Returns the current user.
   * 
   * @return
   */
  public User getMe() throws FacebookServiceException {
    try {
      return json.create(User.class, http.get(url("me").get()));
    } catch (IOException e) {
      throw new FacebookServiceException(e);
    }
  }
  
  private UrlBuilder url(String relativePath) {
    return UrlBuilder.on(GRAPH_URL + relativePath).param("access_token", accessToken);
  }
}
