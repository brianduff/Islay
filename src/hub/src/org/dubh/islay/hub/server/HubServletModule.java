package org.dubh.islay.hub.server;

import static org.dubh.islay.hub.shared.Path.ACTIVITY;
import static org.dubh.islay.hub.shared.Path.OAUTH_CALLBACK;
import static org.dubh.islay.hub.shared.Path.USER;

import java.util.Date;

import org.dubh.islay.hub.client.service.UserAccountService;
import org.dubh.islay.hub.server.oauth.AuthorizeCallbackServlet;
import org.dubh.islay.hub.shared.Path;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;

/**
 * Bindings for servlets in the Hub.
 * 
 * @author brianduff
 */
final class HubServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve(Path.of(USER)).with(UserAccountServiceImpl.class);
    serve(Path.of(ACTIVITY)).with(ActivityServiceImpl.class);
    serve(Path.of(OAUTH_CALLBACK)).with(AuthorizeCallbackServlet.class);
    bind(UserAccountService.class).to(UserAccountServiceImpl.class);
  }
  
  @Provides
  Date provideCurrentDate() {
    return new Date();
  }
}
