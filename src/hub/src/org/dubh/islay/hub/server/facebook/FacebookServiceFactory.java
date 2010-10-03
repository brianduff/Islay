package org.dubh.islay.hub.server.facebook;


public interface FacebookServiceFactory {
  FacebookService create(String accessToken);
}
