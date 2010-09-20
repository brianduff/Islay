package org.dubh.islay.hub.server;

import org.dubh.islay.hub.client.GreetingService;
import org.dubh.islay.hub.model.User;
import org.dubh.islay.hub.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Singleton
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {
  
  private final ObjectifyFactory objectifyFactory;
  
  @Inject
  GreetingServiceImpl(ObjectifyFactory objectifyFactory) {
    this.objectifyFactory = objectifyFactory;
  }

  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid.
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
          "Name must be at least 4 characters long");
    }

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");

    // Escape data from the client to avoid cross-site script vulnerabilities.
    input = escapeHtml(input);
    userAgent = escapeHtml(userAgent);
    
    // for testing purposes, create a new entity in the appengine datastore
    // for this user.
    Objectify ofy = objectifyFactory.begin();
    ofy.put(new User().setUserId(input));

    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html
   *          the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;");
  }
}
