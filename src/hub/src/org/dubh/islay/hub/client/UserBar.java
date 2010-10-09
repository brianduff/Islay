package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserBar extends Composite implements HasUserInformation {
  interface Binder extends UiBinder<Widget, UserBar> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField HorizontalPanel linkbar;
  @UiField Image avatar;
  @UiField Label username;
  @UiField Anchor settingsLink;
  @UiField Anchor logOutLink;
  @UiField VerticalPanel settings;
  
  private static final int EXPAND_HEIGHT = 500;
  private boolean expanded = false;
  private int collapsedHeight;
  private Animation animation;
  
  public UserBar() {
    initWidget(uiBinder.createAndBindUi(this));
    settings.setVisible(false);
    avatar.setWidth("20px");
    avatar.setHeight("20px");
    settingsLink.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (animation != null) {
          animation.cancel();
        }
        if (!expanded) {
          expanded = true;
          collapsedHeight = linkbar.getElement().getScrollHeight();
          animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
              int newHeight = (int) (collapsedHeight + ((EXPAND_HEIGHT-collapsedHeight) * progress));
              linkbar.getElement().getStyle().setHeight(newHeight, Unit.PX);
              settings.getElement().getStyle().setOpacity(progress);
            }            
          };
          
          animation.run(250);
          settings.setVisible(true);
        } else {
          expanded = false;
          animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
              int newHeight = (int) (collapsedHeight + ((EXPAND_HEIGHT-collapsedHeight) * (1.0 - progress)));
              linkbar.getElement().getStyle().setHeight(newHeight, Unit.PX);
              settings.getElement().getStyle().setOpacity(1.0 - progress);
            }
            
            @Override
            protected void onComplete() {
              super.onComplete();
              // Let the height layout naturally...
              linkbar.getElement().getStyle().clearHeight();
              settings.setVisible(false);
            }
          };
          animation.run(250);
        }
      }
    });
  }
  
  @Override
  public void setCurrentUser(UserAccount user) {
    username.setText(user.getEmailAddress());
    avatar.setUrl("https://secure.gravatar.com/avatar/" + user.getEmailMD5Sum() + "?s=140&d=identicon");
  }
  
  @Override
  public void setLogOutUrl(String url) {
    logOutLink.setHref(url);
  }
}
