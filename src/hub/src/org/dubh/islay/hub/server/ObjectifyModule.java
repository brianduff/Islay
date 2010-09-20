package org.dubh.islay.hub.server;

import org.dubh.islay.hub.model.User;

import com.google.inject.AbstractModule;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * A guice module that provides access to Objectify datastore persistence.
 * 
 * @author brianduff
 */
public class ObjectifyModule extends AbstractModule {
  @Override
  protected void configure() {
    ObjectifyService.register(User.class);
    bind(ObjectifyFactory.class).toInstance(ObjectifyService.factory());
  }  
}
