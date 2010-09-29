package org.dubh.islay.hub.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class NetworksView extends ViewImpl implements NetworksPresenter.MyView {
  interface Binder extends UiBinder<Widget, NetworksView> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  private final Widget widget;

  @UiField Button button;
  @UiField Label label;
  @UiField HasUserInformation header;
  
  public NetworksView() {
    widget = uiBinder.createAndBindUi(this);
  }
  
  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setButtonEnabled(boolean enabled) {
    button.setEnabled(enabled);
  }

  @Override
  public void showMessage(String message) {
    label.setVisible(true);
    label.setText(message);
  }

  @Override
  public HasClickHandlers button() {
    return button;
  }

  @Override
  public HasUserInformation userBar() {
    return header;
  }
}
