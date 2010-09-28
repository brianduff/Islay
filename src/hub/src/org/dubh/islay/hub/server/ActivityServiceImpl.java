package org.dubh.islay.hub.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;

import org.dubh.islay.hub.client.ActivityService;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.server.oauth.OAuthServiceFactory;
import org.dubh.islay.hub.shared.Activity;
import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.ObjectifyFactory;

@SuppressWarnings("serial")
@Singleton
public class ActivityServiceImpl extends RemoteServiceServlet implements ActivityService {
  private final OAuthServiceFactory oauthService;
  private final ObjectifyFactory of;
  private static final Logger log = Logger.getLogger(ActivityServiceImpl.class.getName());
  
  @Inject
  ActivityServiceImpl(OAuthServiceFactory oauthService, ObjectifyFactory of) {
    this.oauthService = oauthService;
    this.of = of;
  }
  
  @Override
  public List<Activity> getRecentActivities(UserAccount user, Network network) {
    OAuthConsumer consumer = oauthService.getConsumer(network);
    UserTokens tokens = of.begin().get(UserTokens.class, user.getInternalId());
    
    NetworkTokens association = tokens.getTokens(network);
    consumer.setTokenWithSecret(association.getAccessToken().getToken(), association.getAccessToken().getSecret());
    
    try {
      URL url = new URL("https://www.googleapis.com/buzz/v1/activities/userId/@self?userId=@me");
      
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      consumer.sign(conn);
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(10000);
      
      conn.connect();
      
      log.info(new String(ByteStreams.toByteArray(conn.getInputStream())));
      
      conn.disconnect();
      
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return Lists.newArrayList(
        new Activity().setText("One"),
        new Activity().setText("Two"),
        new Activity().setText("Three")
    );
  }

}