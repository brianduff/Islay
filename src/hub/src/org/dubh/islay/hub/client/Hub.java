package org.dubh.islay.hub.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hub implements EntryPoint {
  private final HubInjector injector = GWT.create(HubInjector.class);
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    RootPanel.get("content").add(injector.getMainPanel());
  }
}
