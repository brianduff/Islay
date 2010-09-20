package org.dubh.islay.hub.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
  private static final Map<String, String> openIdProviders = ImmutableMap.of(
      "Google", "google.com/accounts/o8/id",
      "Yahoo", "yahoo.com",
      "MySpace", "myspace.com",
      "AOL", "aol.com",
      "MyOpenId.com", "myopenid.com");

  private final UserService userService;
  
  @Inject
  LoginServlet(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    User user = userService.getCurrentUser(); // or req.getUserPrincipal()
    Set<String> attributes = Sets.newHashSet();

    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();

    if (user != null) {
      out.println("Hello <i>" + user.getNickname() + "</i>!");
      out.println("[<a href=\""
          + userService.createLogoutURL(req.getRequestURI())
          + "\">sign out</a>]");
    } else {
      out.println("Hello world! Sign in at: ");
      for (String providerName : openIdProviders.keySet()) {
        String providerUrl = openIdProviders.get(providerName);
        String loginUrl = userService.createLoginURL(req.getRequestURI(), null,
            providerUrl, attributes);
        out.println("[<a href=\"" + loginUrl + "\">" + providerName + "</a>] ");
      }
    }
  }
}
