package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.NetworkAssociation;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Connect extends Composite {

  private static ConnectUiBinder uiBinder = GWT.create(ConnectUiBinder.class);

  interface ConnectUiBinder extends UiBinder<Widget, Connect> {
  }

  @UiField Button button;
  @UiField Label label;
  
  private final NetworkAuthServiceAsync authService;
  private final UserAccountServiceAsync userService;
  
  private UserAccount currentUser;

  @Inject
  public Connect(NetworkAuthServiceAsync authService, UserAccountServiceAsync userService) {
    this.authService = authService;
    this.userService = userService;
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  private boolean checkForSuccessfulAssociation() {
    NetworkAssociation assoc = currentUser.getNetworkAssociation(Network.BUZZ);
    if (assoc != null && assoc.isAccessTokenGranted()) {
      button.setEnabled(false);
      label.setText("Successfully authorized to the Buzz API!");
      return true;
    }
    return false;
  }
  
  private void updateUser() {
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        label.setText("Failed to get current user");
      }

      @Override
      public void onSuccess(UserAccount result) {
        currentUser = result;
        checkForSuccessfulAssociation();
      }
    });
  }

  void setCurrentUser(UserAccount user) {
    this.currentUser = user;
    load();
  }
  
  private void load() {
    if (checkForSuccessfulAssociation()) {
      return;
    }
    
    // Did we get an oauth_verifier and oauth_token in the URL? If so, then
    // we're being called back by some OAuth service. We immediately initiate a call
    // to obtain an access token.
    String oauthVerifier = Window.Location.getParameter("oauth_verifier");
    String oauthToken = Window.Location.getParameter("oauth_token");
    
    if (oauthVerifier != null && oauthToken != null) {
      button.setEnabled(false);
      label.setText("Requesting authorization...");
      authService.getAccessToken(Network.BUZZ, oauthToken, oauthVerifier, new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable caught) {
          label.setText("Failed to get authorization to Buzz :(");
        }

        @Override
        public void onSuccess(Void result) {
          // Cool.. call back the user service to refresh the current user, and 
          // make sure it worked.
          // TODO(bduff) we should use EventBus for this kind of thing. 
          updateUser();
        }
      });
    }
  }
  
  @UiHandler("button")
  void onClick(ClickEvent e) {
    authService.getRequestTokenUrl(Network.BUZZ, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
        label.setText("An error occurred.");
      }

      @Override
      public void onSuccess(String result) {
        // Redirect to the authorization page for this service. It should eventually
        // call us back.
        Window.Location.assign(result);
      }
    });
  }

}
