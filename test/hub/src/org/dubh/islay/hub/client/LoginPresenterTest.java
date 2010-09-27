package org.dubh.islay.hub.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * Unit tests for {@link LoginPresenter}.
 * 
 * @author brianduff
 */
public class LoginPresenterTest {
  private @Mock EventBus eventBus;
  private @Mock LoginPresenter.MyView view;
  private @Mock LoginPresenter.MyProxy proxy;
  private @Mock UserAccountServiceAsync userService;
  private @Mock PlaceManager placeManager;
  private @Mock BrowserEnvironment env;
  private LoginPresenter presenter;
  
  @Before
  public void setupMocks() {
    MockitoAnnotations.initMocks(this);
    presenter = new LoginPresenter(eventBus, view, proxy, userService, placeManager, env);
  }
  
  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void showsLoginLinksWhenNoUserLoggedIn_DevMode() {
    presenter.onBind();
    
    ArgumentCaptor<AsyncCallback> callback = ArgumentCaptor.forClass(AsyncCallback.class);
    verify(userService).getLoggedInUser(callback.capture());
    
    when(env.isRunningInDevMode()).thenReturn(true);
    when(env.getHost()).thenReturn("localhost:8888");
    when(env.getUrlParameter("gwt.codesvr")).thenReturn("localhost:9997");
    
    callback.getValue().onSuccess(null);
    verify(view).showLoginProvider(OpenIdProvider.GOOGLE, 
        "http://localhost:8888/_ah/login?continue=/?gwt.codesvr=localhost:9997");
    verify(view).showLoginProvider(OpenIdProvider.FACEBOOK, 
        "http://localhost:8888/_ah/login?continue=/?gwt.codesvr=localhost:9997");
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void showsLoginLinksWhenNoUserLoggedIn_ProdMode() {
    presenter.onBind();
    
    ArgumentCaptor<AsyncCallback> callback = ArgumentCaptor.forClass(AsyncCallback.class);
    verify(userService).getLoggedInUser(callback.capture());
    
    when(env.isRunningInDevMode()).thenReturn(false);
    when(env.getHost()).thenReturn("something.appspot.com");
    
    callback.getValue().onSuccess(null);
    verify(view).showLoginProvider(OpenIdProvider.GOOGLE, 
        "http://something.appspot.com/_ah/login_redir?claimid=" + 
        OpenIdProvider.GOOGLE.getProviderId() + "&continue=http://something.appspot.com/");
    verify(view).showLoginProvider(OpenIdProvider.FACEBOOK, 
        "http://something.appspot.com/_ah/login_redir?claimid=" + 
        OpenIdProvider.FACEBOOK.getProviderId() + "&continue=http://something.appspot.com/");
  }

}
