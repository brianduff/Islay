package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

/**
 * The main panel.
 * 
 * @author brianduff
 */
public class MainPanel extends SimplePanel {
  @Inject
  public MainPanel(UserAccountServiceAsync userService, final LoginView login,
      final RegistrationView registration, final NetworksView connect) {
    /**
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(UserAccount user) {
        if (user == null) {
          // Need to log in.
          setWidget(login);
        } else if (!user.isRegistered()) {
          registration.setUserAccount(user);
          setWidget(registration);
        } else {
          connect.setCurrentUser(user);
          setWidget(connect);
        }
      }
    });
    */
  }
}
