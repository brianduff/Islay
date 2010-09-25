package org.dubh.islay.hub.server;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;

/**
 * Binds implementations of appengine services.
 * 
 * @author brianduff
 */
public class AppEngineModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(UserService.class).toInstance(UserServiceFactory.getUserService());
  }
}
