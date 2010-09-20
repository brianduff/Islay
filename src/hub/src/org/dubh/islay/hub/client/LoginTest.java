package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoginTest extends Composite {
  private static LoginTestUiBinder uiBinder = GWT
      .create(LoginTestUiBinder.class);

  private final UserServiceAsync userService = GWT.create(UserService.class);

  interface LoginTestUiBinder extends UiBinder<Widget, LoginTest> {
  }

  @UiField Label checking;
  @UiField Label loggedIn;
  @UiField Anchor logInLink;

  public LoginTest() {
    initWidget(uiBinder.createAndBindUi(this));
    checking.setVisible(true);
    loggedIn.setVisible(false);
    logInLink.setVisible(false);
    
    userService.getLoggedInUser(new AsyncCallback<User>() {
      @Override
      public void onSuccess(User result) {
        updateLoggedInUserInfo(result);
        checking.setVisible(false);
      }
      
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
        checking.setText("An error has occurred");
      }
    });
  }
  
  private void updateLoggedInUserInfo(User result) {
    if (result != null) {
      loggedIn.setText("Logged in as " + result.getUserId());
      loggedIn.setVisible(true);
    } else {
      userService.createLoginUrl(new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
          caught.printStackTrace();
          checking.setText("An error has occurred");
        }

        @Override
        public void onSuccess(String result) {
          showLoginLink(result);
        }
      });
    }
  }
  
  private void showLoginLink(String link) {
    checking.setVisible(false);
    logInLink.setHref(link);
    logInLink.setVisible(true);
  }
}