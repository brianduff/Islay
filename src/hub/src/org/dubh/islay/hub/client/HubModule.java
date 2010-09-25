package org.dubh.islay.hub.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Injection module for the client.
 * 
 * @author brianduff
 */
public class HubModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(MainPanel.class).in(Singleton.class);
  }
}
