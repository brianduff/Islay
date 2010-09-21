package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.shared.OpenIdProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class Login extends Composite {
  private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
  private final UserAccountServiceAsync userService = GWT.create(UserAccountService.class);

  interface LoginUiBinder extends UiBinder<Widget, Login> {
  }

  @UiField HTML providerLinks;
  
  public Login() {
    initWidget(uiBinder.createAndBindUi(this));
    userService.getOpenIdProviders(new AsyncCallback<List<OpenIdProvider>>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(List<OpenIdProvider> result) {
        StringBuilder html = new StringBuilder();
        for (OpenIdProvider provider : result) {
          html.append(getHtml(provider));
        }
        providerLinks.setHTML(html.toString());
      }
    });
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
}
