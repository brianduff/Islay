package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hub implements EntryPoint {
  private final UserAccountServiceAsync userService = GWT.create(UserAccountService.class);
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    System.out.println("Module LOAD");
    userService.getLoggedInUser(new AsyncCallback<UserAccount>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(UserAccount result) {
        if (result == null) {
          // Need to log in.
          RootPanel.get("content").add(new Login());
        } else if (!result.isRegistered()) {
          RootPanel.get("content").add(new Registration(result));
        } else {
          RootPanel.get("content").add(new HTML("<b>normal page</b>"));
        }
      }
    });
  }
}
