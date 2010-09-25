package org.dubh.islay.hub.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hub implements EntryPoint {
  private final HubInjector injector = GWT.create(HubInjector.class);
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    DelayedBindRegistry.bind(injector);
    injector.getPlaceManager().revealCurrentPlace();
//    RootPanel.get("content").add(injector.getMainPanel());
  }
}
