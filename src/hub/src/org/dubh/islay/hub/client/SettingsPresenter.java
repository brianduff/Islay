package org.dubh.islay.hub.client;

import org.dubh.islay.hub.client.service.NetworkAuthServiceAsync;
import org.dubh.islay.hub.client.util.HasUrl;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> {
  
  private final BrowserEnvironment env;
  private final NetworkAuthServiceAsync authService;
  
  @Inject
  public SettingsPresenter(EventBus eventBus, MyView view,
      PlaceManager placeManager, BrowserEnvironment env, NetworkAuthServiceAsync authService) {
    super(eventBus, view);
    this.env = env;
    this.authService = authService;
    for (Network network : Network.values()) {
      getView().getClickHandler(network).addClickHandler(new ConnectClickHandler(network));
    }
  }

  public void setLoggedInUser(UserAccount user) {
    getView().email().setText(user.getEmailAddress());
    getView().logOutLink().setUrl(getLogOutUrl());
    getView().avatarImage().setUrl("https://secure.gravatar.com/avatar/" 
        + user.getEmailMD5Sum() + "?s=140&d=identicon");
    updateAssociations(user);
  }
  
  public void setExpanded(boolean expanded) {
    getView().setExpanded(expanded);
  }
  
  private String getLogOutUrl() {
    // We're supposed to ask appengine to give us a logout URL. However, that requires
    // an expensive roundtrip to the server, and the URL is completely predictable.
    // So we figure it out here on the client using some magic.
    if (env.isRunningInDevMode()) {
      return "http://" + env.getHost() + "/_ah/logout?continue=/?gwt.codesvr="
          + env.getUrlParameter("gwt.codesvr");
    } else {
      return "http://" + env.getHost() + "/_ah/logout_redir?continue=http://" + env.getHost() + "/";
    }
  }
  
  private void updateAssociations(UserAccount userAccount) {
    for (Network network : Network.values()) {
      boolean connected = userAccount.getAssociatedNetworks().contains(network);
      getView().setConnected(network, connected);
      getView().showStatus(network, connected ? "Connected" : "Not connected");
    }
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
    HasText email();
    HasUrl logOutLink();
    HasUrl avatarImage();
    void setConnected(Network network, boolean isConnected);
    void showStatus(Network network, String message);
    HasClickHandlers getClickHandler(Network network);
    void setExpanded(boolean expanded);
  }  
}
