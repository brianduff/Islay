package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.NetworkAssociation;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class NetworksPresenter extends Presenter<NetworksPresenter.MyView, NetworksPresenter.MyProxy>
    implements UserLoggedInEvent.Handler {
  public static final String TOKEN = "networks";
  
  private final PlaceManager placeManager;
  private final NetworkAuthServiceAsync authService;
  private final UserAccountServiceAsync userService;

  private UserAccount currentUser;
  private String oauthVerifier;
  private String oauthToken;
  
  @Inject
  NetworksPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
      NetworkAuthServiceAsync authService, UserAccountServiceAsync userService) {
    super(eventBus, view, proxy);
    this.placeManager = placeManager;
    this.authService = authService;
    this.userService = userService;
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    oauthVerifier = request.getParameter("oauth_verifier", null);
    oauthToken = request.getParameter("oauth_token", null);
  }
  
  @Override
  protected void onBind() {
    super.onBind();
    if (currentUser == null) {
      // Perhaps the user came straight here, bypassing the login page. Punt them
      // back to the login page.
      placeManager.revealDefaultPlace();
      return;
    }
    
    getView().button().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        authService.getRequestTokenUrl(Network.BUZZ, new AsyncCallback<String>() {
          @Override
          public void onFailure(Throwable caught) {
            caught.printStackTrace();
            getView().showMessage("An error occurred.");
          }

          @Override
          public void onSuccess(String result) {
            // Redirect to the authorization page for this service. It should eventually
            // call us back.
            Window.Location.assign(result);
          }
        });
      }
    });
    
    if (checkForSuccessfulAssociation()) {
      return;
    }
    
    if (oauthVerifier != null && oauthToken != null) {
      getView().setButtonEnabled(false);
      getView().showMessage("Requesting authorization...");
      authService.getAccessToken(Network.BUZZ, oauthToken, oauthVerifier, new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable caught) {
          getView().showMessage("Failed to get authorization to Buzz :(");
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
  
  private void updateUser() {
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        getView().showMessage("Failed to get current user");
      }

      @Override
      public void onSuccess(UserAccount result) {
        UserLoggedInEvent.fire(NetworksPresenter.this, currentUser);
        checkForSuccessfulAssociation();
      }
    });
  }
  
  private boolean checkForSuccessfulAssociation() {
    NetworkAssociation assoc = currentUser.getNetworkAssociation(Network.BUZZ);
    if (assoc != null && assoc.isAccessTokenGranted()) {
      getView().setButtonEnabled(false);
      getView().showMessage("Successfully authorized to the Buzz API!");
      return true;
    }
    return false;
  }
  
  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }
  
  public interface MyView extends View {
    void setButtonEnabled(boolean enabled);
    void showMessage(String message);
    HasClickHandlers button();
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<NetworksPresenter> {}

  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    currentUser = event.getUserAccount();
  }
}
