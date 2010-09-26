package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;



/**
 * 
 * @author Islandir
 *
 * @param <V>
 * @param <Proxy_>
 */
public abstract class AuthenticatedUserPresenter<V extends View, Proxy_ extends Proxy<?>> extends Presenter<V, Proxy_> 
  implements UserLoggedInEvent.Handler {
  
  private UserAccount currentUser;
  private final PlaceManager placeManager;

  protected AuthenticatedUserPresenter(EventBus eventBus, V view, Proxy_ proxy,
      PlaceManager placeManager) {
    super(eventBus, view, proxy);
    this.placeManager = placeManager;
  }


  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    currentUser = event.getUserAccount();
  }

  protected final UserAccount getCurrentUser() {
    return currentUser;
  }
  
  protected final void revealPlace(String placeToken) {
    placeManager.revealPlace(new PlaceRequest(placeToken));
  }
  
  @Override
  protected final void onBind() {
    super.onBind();
    postBind();
  }

  @Override
  protected final void onReveal() {
    super.onReveal();

    if (currentUser == null) {
      // Perhaps the user came straight here, bypassing the login page. Punt them
      // back to the login page.
      placeManager.revealDefaultPlace();
      return;
    }
    
    postReveal();
  }
  
  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }


  protected void postBind() {
    
  }
  
  protected void postReveal() {
    
  }
}
