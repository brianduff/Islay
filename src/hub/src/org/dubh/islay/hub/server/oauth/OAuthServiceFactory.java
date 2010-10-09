package org.dubh.islay.hub.server.oauth;

import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;

import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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
      ),
      Network.FACEBOOK, new DefaultOAuthConsumer("", ""),
      Network.LINKEDIN, new DefaultOAuthConsumer(
          "c7HD5uhAp6sflDVxxuvct8WdGdVtULS3V1RPgdR6znhcNKoURKS26ZXA-z_BPT-J",
          "2UHm65K1rHgm8YbLkA4CtqX9ozfWS2TmNVB5StIKe4QFRtoTgf4B3roigJq_FOjr"
      )
  );
  
  private final Map<Network, ? extends OAuthProvider> providers;
  
  @Inject
  OAuthServiceFactory(@Named("OAuthProviders") Map<Network, ? extends OAuthProvider> providers) {
    this.providers = providers;
  }
  
  /**
   * @param network a network to get the OAuthService for.
   * @return an OAuthService for that network.
   */
  public OAuthProvider getProvider(Network network) {
    return providers.get(network);
  }
  
  /**
   * @param network a network to get the OAuthConsumer for.
   * @return an OAuthConsumer for that network.
   */
  public OAuthConsumer getConsumer(Network network) {
    return CONSUMERS.get(network);
  }

}