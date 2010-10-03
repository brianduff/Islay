package org.dubh.islay.hub.server.facebook;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.dubh.islay.hub.server.HttpService;
import org.dubh.islay.hub.server.JsonObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;

public class FacebookServiceTest {
  private static final String ACCESS_TOKEN = "at";
  private final JsonObjectFactory jsonFactory = JsonObjectFactory.builder()
      .withIsoDateConverter()
      .withRecursiveConverter(FacebookObject.class)
      .build();
  private @Mock HttpService http;
  private FacebookService service;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new FacebookService(ACCESS_TOKEN, http, jsonFactory);
  }
  
  @Test
  public void getMeReturnsValidUser() throws Exception {
    when(http.get(url("me"))).thenReturn(json("{ 'id': '1', 'name': 'Brian Duff' }"));
    assertEquals(new User().setName("Brian Duff").setId("1"), 
        service.getMe());
  }
  
  @Test
  public void getFriends() throws Exception {
    when(http.get(url("me/friends"))).thenReturn(json(
        "{ 'data': [ { 'name': 'First Person', 'id': '2' }, { 'name': 'Second Person', 'id': '3' } ] }"));
    assertEquals(ImmutableList.of(
        new User().setId("2").setName("First Person"),
        new User().setId("3").setName("Second Person")
    ), service.getFriends("me"));
  }
  
  @Test
  public void getPosts() throws Exception {
    when(http.get(url("me/posts"))).thenReturn(json(
        "{ 'data': [ { 'id': '25', 'from': { 'name': 'Brian Duff', 'id': '76' } } ] }"));
    assertEquals(ImmutableList.of(
        new Post().setId("25").setFrom(new User().setId("76").setName("Brian Duff"))
    ), service.getPosts("me"));
  }
  
  /**
   * Utility to let us code single quotes instead of doubles to make json
   * strings more readable in the test code.
   */
  private String json(String json) {
    return json.replace('\'', '\"');
  }
  
  private URL url(String relativePath) {
    try {
      return new URL("https://graph.facebook.com/" + relativePath + "?access_token=at");
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }
}
