package org.dubh.islay.hub.server;

import static org.dubh.islay.hub.shared.Path.USER;

import org.dubh.islay.hub.client.UserAccountService;
import org.dubh.islay.hub.shared.Path;

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
    bind(UserAccountService.class).to(UserAccountServiceImpl.class);
  }
}
