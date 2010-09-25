package org.dubh.islay.hub.client;

import org.dubh.islay.hub.shared.OpenIdProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class LoginView extends ViewImpl implements LoginPresenter.MyView {
  interface Binder extends UiBinder<Widget, LoginView> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  private final Widget widget;
  
  @UiField HTML providerLinks;

  public LoginView() {
    widget = uiBinder.createAndBindUi(this);
  }
  
  @Override
  public void showLoginProvider(OpenIdProvider provider) {
    providerLinks.setHTML(providerLinks.getHTML() + getHtml(provider));
  }
  
  private String getHtml(OpenIdProvider provider) {
    return new StringBuilder()
        .append("<a href=\"")
        .append(provider.getLoginUrl())
        .append("\"><img border=\"0\" alt=\"")
        .append(provider.getName())
        .append("\" src=\"")
        .append(provider.getImageUrl())
        .append("\" /></a>")
        .toString();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }
}
