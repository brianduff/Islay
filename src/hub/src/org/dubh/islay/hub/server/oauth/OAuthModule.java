package org.dubh.islay.hub.server.oauth;

import static org.dubh.islay.hub.shared.Path.OAUTH;

import java.util.Map;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;

import org.dubh.islay.hub.client.service.NetworkAuthService;
import org.dubh.islay.hub.shared.Network;
import org.dubh.islay.hub.shared.Path;

import com.google.appengine.api.utils.SystemProperty;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;

/**
 * Module that configures oauth providers.
 * 
 * @author brianduff
 */
public class OAuthModule extends ServletModule {
//  private static final String FB_HUBZOG_CLIENT_ID = "158579430836939";
//  private static final String FB_HUBZOG_CLIENT_SECRET = "5a9ef7ac4b4cb7dd0676013cb6d61a79";
  
  private static final String FB_DEVMODE_CLIENT_ID = "138332356213546";
  private static final String FB_DEVMODE_CLIENT_SECRET = "cf595d313666ae6bce568170c646cc93";

  private static final String FB_ISLAY_TEST_CLIENT_ID = "156667651020294";
  private static final String FB_ISLAY_TEST_CLIENT_SECRET = "37b2be04346a9084a46eb895549748d0";
   
  @Override
  protected void configureServlets() {
    bind(NetworkAuthService.class).to(NetworkAuthServiceImpl.class);
    serve(Path.of(OAUTH)).with(NetworkAuthServiceImpl.class);
    // Feverish hackery. Assumes for Development that we're running in GWT hosted
    // mode. For prod assumes we're running on a specific app id (evilness!)
    bindConstant().annotatedWith(Names.named("RedirectUrl")).to(
        isDevMode() ?
            "http://127.0.0.1:8888/hub/oauth_cb?network=%s&dev=1" :
            "http://islay-test.appspot.com/hub/oauth_cb?network=%s"
    );
    
    // Facebook hands out client ids and secrets only for specific domains. Since we
    // run on a variety of different domains (e.g. islay-test.appspot.com/, 127.0.0.1, 
    // and hubzog.com), we have to pass a different key for each one.
    bindConstant().annotatedWith(Names.named("FacebookClientId")).to(
        isDevMode() ? FB_DEVMODE_CLIENT_ID : FB_ISLAY_TEST_CLIENT_ID
    );
    bindConstant().annotatedWith(Names.named("FacebookClientSecret")).to(
        isDevMode() ? FB_DEVMODE_CLIENT_SECRET : FB_ISLAY_TEST_CLIENT_SECRET
    );
  }
  
  private static boolean isDevMode() {
    return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
  }
  
  @Provides
  @Named("OAuthProviders")
  Map<Network, ? extends OAuthProvider> provideOAuthProviders(BuzzOAuthProvider buzzProvider, 
      FacebookOAuthProvider facebookProvider) {
    return ImmutableMap.of(
      Network.TWITTER, new DefaultOAuthProvider(
          "https://api.twitter.com/oauth/request_token",
          "https://api.twitter.com/oauth/access_token",
          "https://api.twitter.com/oauth/authorize"),
      Network.FACEBOOK, facebookProvider,
      Network.BUZZ, buzzProvider,
      Network.LINKEDIN, new DefaultOAuthProvider(
          "https://api.linkedin.com/uas/oauth/requestToken",
          "https://api.linkedin.com/uas/oauth/accessToken",
          "https://www.linkedin.com/uas/oauth/authorize")
    );
  }  
}
