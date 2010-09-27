package org.dubh.islay.hub.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

/**
 * The main injector for the Hub server.
 * 
 * @author brianduff
 */
@GinModules(HubModule.class)
public interface HubInjector extends Ginjector {
  PlaceManager getPlaceManager();
  EventBus getEventBus();
  ProxyFailureHandler getProxyFailureHandler();
  
  Provider<LoginPresenter> getLoginPresenter();
  Provider<RegistrationPresenter> getRegistrationPresenter();
  Provider<NetworksPresenter> getNetworksPresenter();
  Provider<RecentPostsPresenter> getRecentPostsPresenter();
}
