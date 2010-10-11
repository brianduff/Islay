package org.dubh.islay.hub.client;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * A place manager that handles setting the default place in our application
 * to the login page.
 * 
 * @author bduff
 */
public class MyPlaceManager extends PlaceManagerImpl {
  @Override
  public void revealErrorPlace(String invalidHistoryToken) {
    // TODO Auto-generated method stub
    System.out.println(invalidHistoryToken);
    super.revealErrorPlace(invalidHistoryToken);
  }

  @Inject
  MyPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter) {
    super(eventBus, tokenFormatter);
  }

  @Override
  public void revealDefaultPlace() {
    revealPlace(new PlaceRequest(LoginPresenter.TOKEN));
  }
}
