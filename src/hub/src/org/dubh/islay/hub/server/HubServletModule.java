package org.dubh.islay.hub.server;

import com.google.inject.servlet.ServletModule;

/**
 * Bindings for servlets in the Hub.
 * 
 * @author brianduff
 */
final class HubServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve("/hub/user").with(UserAccountServiceImpl.class);
  }
}
