package org.dubh.islay.hub.server.oauth;

import java.io.IOException;
import java.net.URL;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.dubh.islay.hub.server.HttpService;
import org.dubh.islay.hub.server.UrlBuilder;
import org.dubh.islay.hub.shared.Network;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * An OAuth provider for facebook.<b>
 * 
 * <p>Facebook actually uses OAuth 2, which turns out to be much simpler than OAuth 1, and 
 * doesn't require signed urls. We want to keep the code the same for each network
 * though (and the overall flow is more or less the same).
 *
 * @author brianduff
 */
@SuppressWarnings("serial")
public class FacebookOAuthProvider extends DefaultOAuthProvider {  
  private static final ImmutableSet<String> REQUIRED_PERMISSIONS = ImmutableSet.of(
      "publish_stream",
      "offline_access",
      "user_activities",
      "friends_activities"
  );
  
  private final String redirectUrlTemplate;
  private final String clientId;
  private final String clientSecret;
  private final HttpService http;
  
  @Inject
  public FacebookOAuthProvider(@Named("RedirectUrl") String redirectUrlTemplate,
      @Named("FacebookClientId") String clientId, 
      @Named("FacebookClientSecret") String clientSecret,
      HttpService http) {
    super("", "", "");
    this.redirectUrlTemplate = redirectUrlTemplate;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.http = http;
  }

  @Override
  public String retrieveRequestToken(OAuthConsumer consumer, String callbackUrl)
      throws OAuthMessageSignerException, OAuthNotAuthorizedException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    return UrlBuilder.on("https://graph.facebook.com/oauth/authorize")
      .param("client_id", clientId)
      .param("scope", Joiner.on(",").join(REQUIRED_PERMISSIONS))
      .param("redirect_uri", getRedirectUrl())
      .get().toString();
  }

  @Override
  public void retrieveAccessToken(OAuthConsumer consumer, String oauthVerifier)
      throws OAuthMessageSignerException, OAuthNotAuthorizedException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    try {
      URL url = UrlBuilder.on("https://graph.facebook.com/oauth/access_token")
          .param("client_id", clientId) 
          .param("client_secret", clientSecret) 
          .param("redirect_uri", getRedirectUrl())
          .param("code", oauthVerifier)
          .get();
      
      String response = http.get(url);
      
      if (response.startsWith("access_token=")) {
        consumer.setTokenWithSecret("", response.substring("access_token=".length()));
      } else {
        throw new OAuthCommunicationException("Unexpected response from facebook", response);
      }
    } catch (IOException e) {
      throw new OAuthCommunicationException(e);
    }
  }
  
  private String getRedirectUrl() {
    // There's a bug in facebook's oauth 2 implementation (found after quite
    // a bit of trial and error) where it will blow up if the redirect_uri
    // contains any colons or other urlencoded parts (e.g. |)
    // Unfortunately, in dev mode, we need the colons in
    // order for everything to work properly. 
    // We work around this by stripping the port off the gwt codesvr parameter for now,
    // and requiring us to
    // manually fix the URL after facebook redirects us back after a successful
    // authorization. This doesn't affect the site when it is deployed.
    // I filed the bug at:
    //    http://bugs.developers.facebook.net/show_bug.cgi?id=12817
    
    String callbackUrl = String.format(redirectUrlTemplate, Network.FACEBOOK);
    int lastColon = callbackUrl.lastIndexOf(':');
    if (lastColon != -1) {
      return callbackUrl.substring(0, lastColon);
    }
    return callbackUrl;
  }
}
