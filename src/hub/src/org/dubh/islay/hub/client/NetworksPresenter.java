package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.NetworkAssociation;
import org.dubh.islay.hub.shared.Network;

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
 *
 */
public class NetworksPresenter extends AuthenticatedUserPresenter<NetworksPresenter.MyView, NetworksPresenter.MyProxy> {
  public static final String TOKEN = "networks";
  
  
  @Inject
  NetworksPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager) {
    super(eventBus, view, proxy, placeManager);
  }
  
  @Override
  protected void onReveal() {
    super.onReveal();
  }

  @Override
  protected void postReveal() {
    checkForSuccessfulAssociation();
  }
  
  private boolean checkForSuccessfulAssociation() {
    NetworkAssociation assoc = getCurrentUser().getNetworkAssociation(Network.BUZZ);
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
