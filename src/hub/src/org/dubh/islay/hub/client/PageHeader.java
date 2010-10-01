package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * The standard page header component that appears on all pages.
 * 
 * @author brianduff
 */
public class PageHeader extends Composite implements HasUserInformation {
  interface Binder extends UiBinder<Widget, PageHeader> { }
  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField UserBar userbar;

  public PageHeader() {
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  public UserBar getUserBar() {
    return userbar;
  }

  @Override
  public void setCurrentUser(UserAccount user) {
    userbar.setCurrentUser(user);
  }

  @Override
  public void setLogOutUrl(String url) {
    userbar.setLogOutUrl(url);
  }
}
