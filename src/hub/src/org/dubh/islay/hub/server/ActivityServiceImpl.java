package org.dubh.islay.hub.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;

import org.dubh.islay.hub.client.ActivityService;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.server.NetworkTokens.TokenAndSecret;
import org.dubh.islay.hub.server.facebook.FacebookService;
import org.dubh.islay.hub.server.facebook.FacebookServiceFactory;
import org.dubh.islay.hub.server.facebook.NamedObject;
import org.dubh.islay.hub.server.facebook.User;
import org.dubh.islay.hub.server.oauth.OAuthServiceFactory;
import org.dubh.islay.hub.shared.Activity;
import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.ObjectifyFactory;

@SuppressWarnings("serial")
@Singleton
public class ActivityServiceImpl extends RemoteServiceServlet implements ActivityService {
  private final OAuthServiceFactory oauthService;
  private final ObjectifyFactory of;
  private final FacebookServiceFactory fb;
  private final HttpService http;
  private static final Logger log = Logger.getLogger(ActivityServiceImpl.class.getName());
  
  @Inject
  ActivityServiceImpl(OAuthServiceFactory oauthService, ObjectifyFactory of, FacebookServiceFactory fb, HttpService http) {
    this.oauthService = oauthService;
    this.of = of;
    this.fb = fb;
    this.http = http;
  }
  
  @Override
  public List<Activity> getRecentActivities(UserAccount user, Network network) {
    OAuthConsumer consumer = oauthService.getConsumer(network);
    UserTokens tokens = of.begin().get(UserTokens.class, user.getInternalId());
    
    NetworkTokens association = tokens.getTokens(network);
    consumer.setTokenWithSecret(association.getAccessToken().getToken(), association.getAccessToken().getSecret());
    
    try {
      tryToGetBuzzStuff(consumer);
      tryToGetFacebookStuff(user);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return Lists.newArrayList(
        new Activity().setText("One"),
        new Activity().setText("Two"),
        new Activity().setText("Three")
    );
  }

  private void tryToGetBuzzStuff(OAuthConsumer consumer) throws IOException {
    URL url = UrlBuilder.on("https://www.googleapis.com/buzz/v1/activities/userId/@self")
        .param("userId", "@me").get();
    
    log.info(http.get(url, oauthSigner(consumer)));
  }
  
  private void tryToGetFacebookStuff(UserAccount user) {
    if (user.getAssociatedNetworks().contains(Network.FACEBOOK)) {
      TokenAndSecret accessToken = of.begin().get(UserTokens.class, user.getInternalId()).getTokens(Network.FACEBOOK).getAccessToken();
      FacebookService facebook = fb.create(accessToken.getSecret());
      User me = facebook.getMe();
      log.info("You are facebook user " + me);
      log.info("Your friends are:");
      StringBuilder s = new StringBuilder();
      for (NamedObject friend : facebook.getFriends(me.getId())) {
        s.append(friend.getName()).append("\n");
      }
      log.info(s.toString());
      
      log.info("Recent posts:");
      log.info(facebook.getPosts("me").toString());
    }
  }
  
  private HttpService.Signer oauthSigner(final OAuthConsumer consumer) {
    return new HttpService.Signer() {
      @Override
      public void sign(HttpURLConnection conn) throws IOException {
        try {
          consumer.sign(conn);
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
    };
  }

}
