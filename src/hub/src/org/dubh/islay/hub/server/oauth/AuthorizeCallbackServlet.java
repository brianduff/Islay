package org.dubh.islay.hub.server.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dubh.islay.hub.client.service.NetworkAuthService;
import org.dubh.islay.hub.shared.Network;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The servlet that we direct Oauth providers to when they call us back with a token.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
@Singleton
public class AuthorizeCallbackServlet extends HttpServlet {
  private final NetworkAuthService networkAuthService;
  
  @Inject
  AuthorizeCallbackServlet(NetworkAuthService networkAuthService) {
    this.networkAuthService = networkAuthService;
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    Network network = Network.valueOf(req.getParameter("network"));
    String oauthVerifier = req.getParameter("oauth_verifier");
    String oauthToken = req.getParameter("oauth_token");
    String oauth2Code = req.getParameter("code");
    String devMode = req.getParameter("dev");
    
    if (oauth2Code != null) {
      oauthVerifier = oauth2Code;
      oauthToken = "";
    }
    
    String redirectUrl = "/Hub.html?s=1";
    if ("1".equals(devMode)) {
      redirectUrl += "&gwt.codesvr=127.0.0.1:9997";
    }
    redirectUrl += "#recentposts";
    // TODO(bduff) pass something through the URL to tell it to reveal settings by default.
    
    try {
      networkAuthService.getAccessToken(network, oauthToken, oauthVerifier);
      System.out.println("Successfully authorized to " + network);
    } catch (IOException e) {
      // TODO(bduff) how do we signal the error to the client?
      e.printStackTrace();
    } finally {
      resp.sendRedirect(redirectUrl);
    }
  }
}
