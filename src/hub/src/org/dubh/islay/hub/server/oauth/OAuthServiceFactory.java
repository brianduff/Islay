package org.dubh.islay.hub.server.oauth;

import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;

import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.ImmutableMap;

/**
 * A factory that provides instances of {@link OAuthProvider} and {@link OAuthConsumer}
 * for various {@link Network}s.
 * 
 * @author brianduff
 */
class OAuthServiceFactory {
  private static final Map<Network, ? extends OAuthConsumer> CONSUMERS = ImmutableMap.of(
      Network.BUZZ, new DefaultOAuthConsumer("anonymous", "anonymous")
  );
  private static final Map<Network, ? extends OAuthProvider> PROVIDERS = ImmutableMap.of(
      Network.BUZZ, new BuzzOAuthProvider()
  );

  /**
   * @param network a network to get the OAuthService for.
   * @return an OAuthService for that network.
   */
  OAuthProvider getProvider(Network network) {
    return PROVIDERS.get(network);
  }
  
  /**
   * @param network a network to get the OAuthConsumer for.
   * @return an OAuthConsumer for that network.
   */
  OAuthConsumer getConsumer(Network network) {
    return CONSUMERS.get(network);
  }

}