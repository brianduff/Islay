package org.dubh.islay.hub.client;

import com.google.gwt.user.client.Window;

/**
 * Encapsulates information that we get from the browser environment (mostly
 * via static GWT APIs) so that we can mock it out in test code.
 * 
 * @author bduff
 */
public class BrowserEnvironment {
  
  /**
   * Returns the value of a URL parameter with the specified name.
   * 
   * @param parameterName
   * @return
   */
  public String getUrlParameter(String parameterName) {
    return Window.Location.getParameter(parameterName);
  }
}
