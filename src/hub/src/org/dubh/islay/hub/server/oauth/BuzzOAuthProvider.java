package org.dubh.islay.hub.server.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * A custom implementation of {@link OAuthProvider} for buzz that makes sure the auth url
 * includes the scope and domain url parameters.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
class BuzzOAuthProvider extends DefaultOAuthProvider {
  /**
   * Scope indicating read / write access to Buzz.
   */
  static final String SCOPE = "https://www.googleapis.com/auth/buzz"; 
  
  /**
   * Domain of this app.
   */
  private static final String DOMAIN = "hubzog.com";
  
  BuzzOAuthProvider() {
    super("https://www.google.com/accounts/OAuthGetRequestToken?scope="
              + urlEncode("https://www.googleapis.com/auth/buzz"),
          "https://www.google.com/accounts/OAuthGetAccessToken",
          "https://www.google.com/buzz/api/auth/OAuthAuthorizeToken");
  }

  @Override
  public String retrieveRequestToken(OAuthConsumer consumer, String callbackUrl)
      throws OAuthMessageSignerException, OAuthNotAuthorizedException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    String baseUrl = super.retrieveRequestToken(consumer, callbackUrl);
    
    return baseUrl + "&scope=" + urlEncode(SCOPE) + "&domain=" + urlEncode(DOMAIN);
  }  
  
  private static String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (UnsupportedEncodingException e) {
      // Something is very wrong with the JDK.
      throw new IllegalStateException(e);
    }
  }
}
