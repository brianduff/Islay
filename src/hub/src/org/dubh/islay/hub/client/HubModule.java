package org.dubh.islay.hub.client;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultEventBus;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * Injection module for the client.
 * 
 * @author brianduff
 */
public class HubModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
    bind(PlaceManager.class).to(MyPlaceManager.class).in(Singleton.class);
    bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
    bind(RootPresenter.class).asEagerSingleton();
    bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);
//    bind(MainPanel.class).in(Singleton.class);
    
    // Presenters
    bindPresenter(LoginPresenter.class, LoginPresenter.MyView.class, LoginView.class, LoginPresenter.MyProxy.class);
    bindPresenter(RegistrationPresenter.class, RegistrationPresenter.MyView.class, RegistrationView.class, RegistrationPresenter.MyProxy.class);
    bindPresenter(RecentPostsPresenter.class, RecentPostsPresenter.MyView.class, RecentPostsView.class, RecentPostsPresenter.MyProxy.class);

    bindPresenterWidget(SettingsPresenter.class, SettingsPresenter.MyView.class, SettingsView.class);
  }
}
