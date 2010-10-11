package org.dubh.islay.hub.client;

import org.dubh.islay.hub.client.util.FadeAnimation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A very simple UI control that shows a status message. The message
 * will disappear automatically after a set period of time.
 * 
 * @author brianduff
 */
public class StatusMessage extends Composite {
  private static final int DISPLAY_DURATION = 30000;  // 30s
  
  interface Binder extends UiBinder<Widget, StatusMessage> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField InlineLabel statusMessage;
  private final FadeAnimation fadeAnimation;
  private final Timer timer = new Timer() {
    @Override
    public void run() {
      fadeAnimation.setVisible(false);
    }
  };
  
  public StatusMessage() {
    initWidget(uiBinder.createAndBindUi(this));
    statusMessage.setVisible(false);
    fadeAnimation = new FadeAnimation(500, statusMessage);
  }
  
  public void showMessage(String message) {
    timer.cancel();
    statusMessage.setText(message);
    if (!statusMessage.isVisible()) {
      fadeAnimation.setVisible(true);
    }
    // Schedule the label to disappear after DISPLAY_DURATION has elapsed.
    timer.schedule(DISPLAY_DURATION);
  }
  
  public void clearMessage() {
    timer.cancel();
    fadeAnimation.setVisible(false);
    statusMessage.setText("");
  }
}
