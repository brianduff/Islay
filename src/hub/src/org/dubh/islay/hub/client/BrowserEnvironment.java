package org.dubh.islay.hub.client;

import com.google.gwt.http.client.URL;
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
  
  /**
   * Redirects the browser to an external URL.
   * 
   * @param url
   */
  public void redirectToExternalUrl(String url) {
    Window.Location.assign(url);
  }
  
  /**
   * Returns true if we're running in dev mode. We determine this
   * by looking for the gwt.codesrv url parameter (this is kind of a hack).
   * @return
   */
  public boolean isRunningInDevMode() {
    return Window.Location.getParameter("gwt.codesvr") != null;
  }
  
  /**
   * Encodes some text to make it suitable for passing as part of a URL.
   * @param text
   * @return
   */
  public String urlEncode(String text) {
    return URL.encode(text);
  }
  
  public String getHost() {
    return Window.Location.getHost();
  }
}
