package org.dubh.islay.hub.server;

import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

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
    try {
      bind(Cache.class).toInstance(CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap()));
    } catch (CacheException e) {
      throw new IllegalStateException(e);
    }
  }
}
