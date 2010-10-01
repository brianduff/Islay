package org.dubh.islay.hub.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class RegistrationView extends ViewImpl implements RegistrationPresenter.MyView {
  interface Binder extends UiBinder<Widget, RegistrationView> {}
  private static Binder uiBinder = GWT.create(Binder.class);  
  
  private final Widget widget;

  @UiField TextBox name;
  @UiField TextBox email;
  @UiField Button submit;
  @UiField Label message;
  @UiField HasUserInformation header;

  public RegistrationView() {
    widget = uiBinder.createAndBindUi(this);
    message.setVisible(false);
  }
  
  @Override
  public Widget asWidget() {
    return widget;
  }
  
  @Override
  public HasText name() {
    return name;
  }

  @Override
  public HasText email() {
    return email;
  }

  @Override
  public HasClickHandlers submitButton() {
    return submit;
  }

  @Override
  public void showMessage(String messageText) {
    message.setVisible(messageText != null);
    if (messageText != null) {
      message.setText(messageText);
    }
  }

  @Override
  public HasUserInformation userBar() {
    return header;
  }
}