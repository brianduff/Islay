package org.dubh.islay.hub.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * The main injector for the Hub server.
 * 
 * @author brianduff
 */
@GinModules(HubModule.class)
public interface HubInjector extends Ginjector {
  /**
   * @return the main panel.
   */
  MainPanel getMainPanel();
}
