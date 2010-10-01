package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

/**
 * 
 * @author Islandir
 */
public class NetworksPresenter extends AuthenticatedUserPresenter<NetworksPresenter.MyView, NetworksPresenter.MyProxy> {
  public static final String TOKEN = "networks";
  private final NetworkAuthServiceAsync authService;
  private final BrowserEnvironment env;
  
  @Inject
  NetworksPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
      NetworkAuthServiceAsync authService, BrowserEnvironment env) {
    super(eventBus, view, proxy, placeManager, env);
    this.authService = authService;
    this.env = env;
  }
  
  @Override
  protected void postBind() {
    for (Network network : Network.values()) {
      getView().getClickHandler(network).addClickHandler(new ConnectClickHandler(network));
    }
    
    getView().getShowActivities().addClickHandler(
      new ClickHandler()
      {
        @Override
        public void onClick(ClickEvent event) {
          revealPlace(RecentPostsPresenter.TOKEN);
        }        
      });
  }
  
  @Override
  protected void postReveal() {
    updateAssociations(getCurrentUser());
    
    String networkParam = env.getUrlParameter("network");
    if (networkParam == null) {
      return;
    }
    // If we got a network parameter, it's possible we're being called back by oauth
    // as a result of a successful (or failed) authorization attempt.
    final Network network = Network.valueOf(networkParam);
    if (getCurrentUser().getAssociatedNetworks().contains(network)) {
      // We already successfully authorized.
      return;
    }
    
    String oauthVerifier = env.getUrlParameter("oauth_verifier");
    String oauthToken = env.getUrlParameter("oauth_token");
    String oauth2Code = env.getUrlParameter("code");
    
    if (oauth2Code != null) {
      oauthVerifier = oauth2Code;
      oauthToken = "";
    }
    
    if (oauthVerifier != null && oauthToken != null) {
      getView().showStatus(network, "Requesting authorization...");
      authService.getAccessToken(network, oauthToken, oauthVerifier, new AsyncCallback<UserAccount>() {
        @Override
        public void onFailure(Throwable caught) {
          getView().showStatus(network, "Failed to get authorization");
        }

        @Override
        public void onSuccess(UserAccount result) {
          updateAssociations(result);
        }
      });
    }
  }
  
  private void updateAssociations(UserAccount userAccount) {
    for (Network network : Network.values()) {
      boolean connected = userAccount.getAssociatedNetworks().contains(network);
      getView().setConnected(network, connected);
      getView().showStatus(network, connected ? "Connected" : "Not connected");
    }
  }
  
  @Override
  protected HasUserInformation getUserBar() {
    return getView().userBar();
  }
  
  private class ConnectClickHandler implements ClickHandler {
    private final Network network;
    
    ConnectClickHandler(Network network) {
      this.network = network;
    }
    
    @Override
    public void onClick(ClickEvent e) {
      getView().showStatus(network, "Requesting access...");
      authService.getRequestTokenUrl(network, new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
          caught.printStackTrace();
          getView().showStatus(network, "Unable to connect");
        }

        @Override
        public void onSuccess(String result) {
          // Redirect to the authorization page for the OAuth service. It should eventually
          // call us back.
          env.redirectToExternalUrl(result);
        }
      });
    }
  }
  
  public interface MyView extends View {
    void setConnected(Network network, boolean isConnected);
    void showStatus(Network network, String message);
    HasClickHandlers getClickHandler(Network network);
    HasClickHandlers getShowActivities();
    HasUserInformation userBar();
  }

  @ProxyEvent
  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    super.onUserLoggedIn(event);
  }

  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<NetworksPresenter> {}

}
