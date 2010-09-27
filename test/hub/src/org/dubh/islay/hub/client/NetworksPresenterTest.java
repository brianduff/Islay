package org.dubh.islay.hub.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * Unit tests for {@link NetworksPresenter}.
 * @author brianduff
 */
public class NetworksPresenterTest {
  private @Mock EventBus eventBus;
  private @Mock NetworksPresenter.MyView view;
  private @Mock NetworksPresenter.MyProxy proxy;
  private @Mock PlaceManager placeManager;
  private @Mock NetworkAuthServiceAsync authService;
  private @Mock UserAccountServiceAsync userService;
  private @Mock BrowserEnvironment env;
  
  private NetworksPresenter presenter;
  
  @Before
  public void setupMocks() {
    MockitoAnnotations.initMocks(this);
    // TODO(bduff) use a mocking module?
    presenter = new NetworksPresenter(eventBus, view, proxy, placeManager, authService, 
        userService, env);
  }
  
  @Test
  public void revealShouldRedirectToLoginPageIfNoUser() {
    // Presenter has no current user by default
    presenter.onReveal();
    verify(placeManager).revealDefaultPlace();
  }
  
  @Test
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void revealShouldRequestAccessTokenWhenHasOauthParameters() {
    simulateLogIn();
    
    when(env.getUrlParameter("oauth_verifier")).thenReturn("verifier");
    when(env.getUrlParameter("oauth_token")).thenReturn("token");
    
    presenter.onReveal();
    
    verify(view).setButtonEnabled(false);
    verify(view).showMessage("Requesting authorization...");
    
    // Verify that we call getAccessToken on the auth service. Capture the callback
    // so that we can assert additional stuff on it.
    ArgumentCaptor<AsyncCallback> callback = ArgumentCaptor.forClass(AsyncCallback.class);
    verify(authService).getAccessToken(eq(Network.BUZZ), eq("token"), eq("verifier"), callback.capture());
    
    // Pretend the callback just failed...
    callback.getValue().onFailure(new RuntimeException("Argh!"));
    // Then we should show an error message...
    verify(view).showMessage("Failed to get authorization to Buzz :(");
    
    // Now pretend the callback just succeeded...
    callback.getValue().onSuccess(null);
    // Then we should ask the user service to get the currently logged in user...
    callback = ArgumentCaptor.forClass(AsyncCallback.class);
    verify(userService).getLoggedInUser(callback.capture());
    
    // Pretend the user service failed...
    callback.getValue().onFailure(new RuntimeException("User service failed"));
    verify(view).showMessage("Failed to get current user");
    
    // And then pretend it succeeded...
    UserAccount accountWithAssociation = new UserAccount().setEmailAddress("me@somewhere.com");
    accountWithAssociation.getNetworkAssociation(Network.BUZZ)
        .setAccessToken("access_token")
        .setAccessTokenSecret("access_token_secret");
    callback.getValue().onSuccess(accountWithAssociation);
    verify(view).showMessage("Successfully authorized to the Buzz API!");
  }
  
  private void simulateLogIn() {
    presenter.onUserLoggedIn(new UserLoggedInEvent(
        new UserAccount().setEmailAddress("me@somewhere.com")));
  }
}
