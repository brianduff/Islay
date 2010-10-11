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
    return String.format(redirectUrlTemplate, Network.FACEBOOK);
  }
}
