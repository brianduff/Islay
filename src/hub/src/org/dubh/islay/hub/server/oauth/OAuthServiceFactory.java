package org.dubh.islay.hub.server.oauth;

import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.ImmutableMap;

/**
 * A factory that provides instances of {@link OAuthProvider} and {@link OAuthConsumer}
 * for various {@link Network}s.
 * 
 * @author brianduff
 */
public class OAuthServiceFactory {
  private static final Map<Network, ? extends OAuthConsumer> CONSUMERS = ImmutableMap.of(
      Network.BUZZ, new DefaultOAuthConsumer("anonymous", "anonymous"),
      Network.TWITTER, new DefaultOAuthConsumer(
          "lXBAwIgsQNvGTmpGNtEQ",
          "OuIjNR2TyRpfsmn7B11jNYK28eDJlcMLYZCRC2iF0U"
      )
  );
  private static final Map<Network, ? extends OAuthProvider> PROVIDERS = ImmutableMap.of(
      Network.BUZZ, new BuzzOAuthProvider(),
      Network.TWITTER, new DefaultOAuthProvider(
          "https://api.twitter.com/oauth/request_token",
          "https://api.twitter.com/oauth/access_token",
          "https://api.twitter.com/oauth/authorize"
      )
  );

  /**
   * @param network a network to get the OAuthService for.
   * @return an OAuthService for that network.
   */
  public OAuthProvider getProvider(Network network) {
    return PROVIDERS.get(network);
  }
  
  /**
   * @param network a network to get the OAuthConsumer for.
   * @return an OAuthConsumer for that network.
   */
  public OAuthConsumer getConsumer(Network network) {
    return CONSUMERS.get(network);
  }

}