package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

/**
 * The main panel.
 * 
 * @author brianduff
 */
public class MainPanel extends SimplePanel {
  @Inject
  public MainPanel(UserAccountServiceAsync userService, final Login login,
      final Registration registration) {
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(UserAccount result) {
        if (result == null) {
          // Need to log in.
          setWidget(login);
        } else if (!result.isRegistered()) {
          registration.setUserAccount(result);
          setWidget(registration);
        } else {
          setWidget(new HTML("<b>normal page</b>"));
        }
      }
    });
  }
}
