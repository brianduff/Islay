package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.NetworkAssociation;
import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class NetworksPresenter extends Presenter<NetworksPresenter.MyView, NetworksPresenter.MyProxy>
    implements UserLoggedInEvent.Handler {
  public static final String TOKEN = "networks";
  
  private final PlaceManager placeManager;
  
  private UserAccount currentUser;
  
  @Inject
  NetworksPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager) {
    super(eventBus, view, proxy);
    this.placeManager = placeManager;
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
    
    if (checkForSuccessfulAssociation()) {
      return;
    }
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
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<NetworksPresenter> {}

  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    currentUser = event.getUserAccount();
  }
}
