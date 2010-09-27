package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

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

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> {
  public static final String TOKEN = "login";
  
  private final PlaceManager placeManager;
  
  @Inject
  public LoginPresenter(EventBus eventBus, MyView view, MyProxy proxy, 
      final UserAccountServiceAsync userService, final PlaceManager placeManager) {
    super(eventBus, view, proxy);
    
    this.placeManager = placeManager;
  
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(UserAccount user) {
        if (user == null) {
          showLoginLinks();
        } else {
          UserLoggedInEvent.fire(LoginPresenter.this, user);
          gotoRegistrationPage();
        }
      }      
    });
  }

  private void gotoRegistrationPage() {
    placeManager.revealPlace(new PlaceRequest(RegistrationPresenter.TOKEN));
  }
  
  private void showLoginLinks() {
    for (OpenIdProvider provider : OpenIdProvider.values()) {
      getView().showLoginProvider(provider);
    }
  }

  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }
  
  public interface MyView extends View {
    void showLoginProvider(OpenIdProvider provider);
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<LoginPresenter> {}
}
