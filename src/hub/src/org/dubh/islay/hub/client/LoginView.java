package org.dubh.islay.hub.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
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
        .append(provider.getLoginUrl(Window.Location.getHost(), getAdditionalRedirectParameters()))
        .append("\"><img border=\"0\" alt=\"")
        .append(provider.getName())
        .append("\" src=\"")
        .append(provider.getImageUrl())
        .append("\" /></a>")
        .toString();
  }
  
  private String getAdditionalRedirectParameters() {
    // Checks to see if gwt.codesrv is present and provides it to the
    // redirection url so that we continue to function properly in hosted
    // (dev) mode.
    String codeSvr = Window.Location.getParameter("gwt.codesvr");
    if (codeSvr != null) {
      return "gwt.codesvr=" + codeSvr; 
    }
    return null;
  }

  @Override
  public Widget asWidget() {
    return widget;
  }
}
