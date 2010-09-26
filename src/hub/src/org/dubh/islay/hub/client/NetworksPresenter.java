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
  private final UserAccountServiceAsync userService;
  private final BrowserEnvironment env;
  
  @Inject
  NetworksPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
      NetworkAuthServiceAsync authService, UserAccountServiceAsync userService,
      BrowserEnvironment env) {
    super(eventBus, view, proxy, placeManager);
    this.authService = authService;
    this.userService = userService;
    this.env = env;
  }
  
  @Override
  protected void postBind() {    
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

  }
  
  private void updateUser() {
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        getView().showMessage("Failed to get current user");
      }

      @Override
      public void onSuccess(UserAccount result) {
        checkForSuccessfulAssociation(result);
      }
    });
  }

  @Override
  protected void postReveal() {
    if (checkForSuccessfulAssociation(getCurrentUser())) {
      return;
    }
    
    String oauthVerifier = env.getUrlParameter("oauth_verifier");
    String oauthToken = env.getUrlParameter("oauth_token");
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
          updateUser();
        }
      });
    }
  }
  
  private boolean checkForSuccessfulAssociation(UserAccount userAccount) {
    NetworkAssociation assoc = userAccount.getNetworkAssociation(Network.BUZZ);
    if (assoc != null && assoc.isAccessTokenGranted()) {
      getView().setButtonEnabled(false);
      getView().showMessage("Successfully authorized to the Buzz API!");
      return true;
    }
    return false;
  }
  
  public interface MyView extends View {
    void setButtonEnabled(boolean enabled);
    void showMessage(String message);
    HasClickHandlers button();
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
