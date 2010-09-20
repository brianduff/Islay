package org.dubh.islay.hub.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Central Guice configuration. Installs the {@link ServletModule} which allows us to 
 * create servlet implementations that have services injected into them.
 * 
 * @author brianduff
 */
public class HubGuiceConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new HubServletModule(),
        new ObjectifyModule(),
        new AppEngineModule());
  }
}
