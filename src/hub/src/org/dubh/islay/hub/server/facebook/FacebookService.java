package org.dubh.islay.hub.server.facebook;

import java.io.IOException;
import java.util.List;

import org.dubh.islay.hub.server.HttpService;
import org.dubh.islay.hub.server.JsonObjectFactory;
import org.dubh.islay.hub.server.UrlBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.common.collect.ImmutableList;
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
      return json.create(User.class, parseJson(http.get(url("me").get())));
    } catch (IOException e) {
      throw new FacebookServiceException(e);
    }
  }
  
  public ImmutableList<? extends NamedObject> getFriends(String userId) {
    return getList(User.class, userId + "/friends");
  }
  
  public ImmutableList<Post> getPosts(String userId) {
    // TODO(bduff) limit / paging. By default we get 25 at a time. Can pass limit=n to get more.
    return getList(Post.class, userId + "/posts");
  }
  
  @SuppressWarnings("unchecked")
  private <T> ImmutableList<T> getList(Class<T> itemClazz, String relativePath) {
    try {
      String jsonString = http.get(url(relativePath).get());
      List<?> data = (List<?>) parseJson(jsonString).get("data");      
      ImmutableList.Builder<Object> result = ImmutableList.builder();
      for (Object item : data) {
        result.add(json.create(itemClazz, (JSONObject) item));
      }
      return (ImmutableList<T>) result.build();
    } catch (IOException e) {
      throw new FacebookServiceException(e);
    }
    
  }
  
  private JSONObject parseJson(String text) {
    return (JSONObject) JSONValue.parse(text);
  }
  
  private UrlBuilder url(String relativePath) {
    return UrlBuilder.on(GRAPH_URL + relativePath).param("access_token", accessToken);
  }
}
