package org.dubh.islay.hub.server.oauth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.dubh.islay.hub.shared.Network;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharStreams;
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
  
  @Inject
  public FacebookOAuthProvider(@Named("RedirectUrl") String redirectUrlTemplate,
      @Named("FacebookClientId") String clientId, 
      @Named("FacebookClientSecret") String clientSecret) {
    super("", "", "");
    this.redirectUrlTemplate = redirectUrlTemplate;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  public String retrieveRequestToken(OAuthConsumer consumer, String callbackUrl)
      throws OAuthMessageSignerException, OAuthNotAuthorizedException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    return "https://graph.facebook.com/oauth/authorize"
        + "?client_id=" + clientId
        + "&scope=" + Joiner.on(",").join(REQUIRED_PERMISSIONS)
        + "&redirect_uri=" + urlEncode(callbackUrl);
  }

  @Override
  public void retrieveAccessToken(OAuthConsumer consumer, String oauthVerifier)
      throws OAuthMessageSignerException, OAuthNotAuthorizedException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    String callbackUrl = String.format(redirectUrlTemplate, Network.FACEBOOK);
    
    HttpURLConnection conn = null;
    try {
      URL url = new URL("https://graph.facebook.com/oauth/access_token"
          + "?client_id=" + clientId 
          + "&client_secret=" + clientSecret 
          + "&redirect_uri=" + urlEncode(callbackUrl)
          + "&type=client_cred"
          + "&code=" + urlEncode(oauthVerifier));
      
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(10000);
      
      String response = CharStreams.toString(new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));
      if (response.startsWith("access_token=")) {
        consumer.setTokenWithSecret("", response.substring("access_token=".length()));
      } else {
        throw new OAuthCommunicationException("Unexpected response from facebook", response);
      }
    } catch (IOException e) {
      throw new OAuthCommunicationException(e);
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
  }
  
  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
}
