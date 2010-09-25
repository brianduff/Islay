package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class RegistrationPresenter extends Presenter<RegistrationPresenter.MyView,
    RegistrationPresenter.MyProxy> implements UserLoggedInEvent.Handler {
  public static final String TOKEN = "registration";
  
  private final PlaceManager placeManager;
  private UserAccount currentUser;
  private UserAccountServiceAsync userService;
  
  @Inject
  public RegistrationPresenter(EventBus eventBus, MyView view, MyProxy proxy,
      PlaceManager placeManager, UserAccountServiceAsync userService) {
    super(eventBus, view, proxy);
    this.placeManager = placeManager;
    this.userService = userService;
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
    
    getView().email().setText(currentUser.getEmailAddress());
    getView().submitButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        String name = getView().name().getText().trim();
        String email = getView().email().getText().trim();
        if (name.length() == 0) {
          getView().showMessage("Please enter your name");
          return;
        }
        if (email.length() == 0) {
          getView().showMessage("Please enter your email address");
          return;
        }
        
        currentUser.setEmailAddress(email);
        currentUser.setName(name);
        currentUser.setRegistered(true);
        
        userService.save(currentUser, new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable caught) {
            getView().showMessage("An error occurred. Please try again.");
            caught.printStackTrace();
          }

          @Override
          public void onSuccess(Void result) {
            placeManager.revealPlace(new PlaceRequest("connections"));
          }      
        });
      }
    });
  }

  @ProxyEvent
  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    currentUser = event.getUserAccount();
  }

  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }
  
  public interface MyView extends View {
    HasText name();
    HasText email();
    HasClickHandlers submitButton();
    void showMessage(String messageText);
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<RegistrationPresenter> {}
}
