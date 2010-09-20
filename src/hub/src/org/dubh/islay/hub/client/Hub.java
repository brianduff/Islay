package org.dubh.islay.hub.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hub implements EntryPoint {
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    RootPanel.get("content").add(new LoginTest());
  }
}
