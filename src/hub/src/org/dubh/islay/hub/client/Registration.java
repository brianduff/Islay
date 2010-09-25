package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Registration extends Composite {
  private static RegistrationUiBinder uiBinder = GWT
      .create(RegistrationUiBinder.class);  
  interface RegistrationUiBinder extends UiBinder<Widget, Registration> {
  }
  private final UserAccountServiceAsync userService;
  
  private UserAccount userAccount;

  @UiField TextBox name;
  @UiField TextBox email;
  @UiField Button submit;
  @UiField Label message;
  
  @Inject
  public Registration(UserAccountServiceAsync userService) {
    this.userService = userService;
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  public void setUserAccount(UserAccount userAccount) {
    this.userAccount = userAccount;
    message.setVisible(false);
    email.setText(userAccount.getEmailAddress());    
  }

  @UiHandler("submit")
  void onClick(ClickEvent e) {
    if (name.getText().trim().length() == 0) {
      showMessage("Please enter your name");
      return;
    }
    if (email.getText().trim().length() == 0) {
      showMessage("Please enter your email address");
      return;
    }
    submit.setEnabled(false);
    
    userAccount.setEmailAddress(email.getText().trim());
    userAccount.setName(name.getText().trim());
    userAccount.setRegistered(true);
    
    userService.save(userAccount, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        showMessage("An error occurred. Please try again.");
        submit.setEnabled(true);
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(Void result) {
        // TODO(bduff) redirect to normal page.
        submit.setEnabled(true);
        showMessage("Saved");
      }      
    });
  }
  
  private void showMessage(String text) {
    message.setVisible(true);
    message.setText(text);
  }

}