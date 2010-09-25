package org.dubh.islay.hub.server.oauth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthException;

import org.dubh.islay.hub.client.NetworkAuthService;
import org.dubh.islay.hub.client.UserAccountService;
import org.dubh.islay.hub.model.NetworkAssociation;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.googlecode.objectify.ObjectifyFactory;

@Singleton
@SuppressWarnings("serial")
public class NetworkAuthServiceImpl extends RemoteServiceServlet implements NetworkAuthService {
  private static final Logger log = Logger.getLogger(NetworkAuthServiceImpl.class.getName());
  private final OAuthServiceFactory serviceFactory;
  private final String callbackUrl;
  private final UserAccountService userAccountService;
  private final ObjectifyFactory of;
  
  @Inject
  NetworkAuthServiceImpl(OAuthServiceFactory serviceFactory, @Named("RedirectUrl") String callbackUrl,
      UserAccountService userAccountService, ObjectifyFactory of) {
    this.serviceFactory = serviceFactory;
    this.callbackUrl = callbackUrl;
    this.userAccountService = userAccountService;
    this.of = of;
  }

  @Override
  public String getRequestTokenUrl(Network network) throws IOException {
    try {
      OAuthConsumer consumer = consumer(network);
      String url = provider(network).retrieveRequestToken(consumer(network), callbackUrl);
      saveRequestToken(network, consumer.getToken(), consumer.getTokenSecret());
      return url;
    } catch (OAuthException e) {
      log.log(Level.SEVERE, "Failed to get request token", e);
      throw new IOException(e.getMessage());
    }
  }
  
  @Override
  public void getAccessToken(Network network, String requestToken, String verifyToken) throws IOException {
    NetworkAssociation association = networkAssociation(userAccountService.getLoggedInUser(), network);
    OAuthConsumer consumer = consumer(network);
    consumer.setTokenWithSecret(association.getRequestToken(), association.getRequestTokenSecret());

    try {
      provider(network).retrieveAccessToken(consumer, verifyToken.trim());
      saveAccessToken(network, consumer.getToken(), consumer.getTokenSecret());
    } catch (OAuthException e) {
      log.log(Level.SEVERE, "Failed to get access token", e);
      throw new IOException(e.getMessage());
    }
  }
  
  private void saveRequestToken(Network network, String requestToken, String requestTokenSecret) {
    UserAccount currentUser = userAccountService.getLoggedInUser();
    networkAssociation(currentUser, network).setRequestToken(requestToken)
        .setRequestTokenSecret(requestTokenSecret);
    of.begin().put(currentUser);    
  }
    
  private void saveAccessToken(Network network, String accessToken, String accessTokenSecret) {
    UserAccount currentUser = userAccountService.getLoggedInUser();
    networkAssociation(currentUser, network).setAccessToken(accessToken)
        .setAccessTokenSecret(accessTokenSecret);
    of.begin().put(currentUser);    
  }
  
  private NetworkAssociation networkAssociation(UserAccount user, Network network) {
    if (user == null) {
      throw new IllegalStateException("No longer logged in");
    }
    return user.getNetworkAssociation(network);
  }
  
  private OAuthConsumer consumer(Network network) {
    return serviceFactory.getConsumer(network);
  }
  
  private OAuthProvider provider(Network network) {
    return serviceFactory.getProvider(network);
  }
}
