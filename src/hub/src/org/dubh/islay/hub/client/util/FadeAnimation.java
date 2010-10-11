package org.dubh.islay.hub.client.util;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.Widget;

/**
 * An animation that fades in or out.
 * 
 * @author brianduff
 */
public class FadeAnimation extends Animation {
  private final int time;
  private final Widget widget;
  
  private boolean visible;
  
  public FadeAnimation(int time, Widget element) {
    this.time = time;
    this.widget = element;
    visible = element.isVisible();
  }
  
  /**
   * Toggles the state of this animation.
   */
  public void toggle() {
    cancel();
    visible = !visible;
    if (visible) {
      widget.setVisible(true);
    }
    run(time);
  }
  
  public void setVisible(boolean visible) {
    if (visible != this.visible) {
      toggle();
    }
  }
  
  @Override
  protected void onUpdate(double progress) {
    widget.getElement().getStyle().setOpacity(visible ? progress : 1.0 - progress);
  }
  
  @Override
  protected void onComplete() {
    super.onComplete();
    if (!visible) {
      widget.setVisible(false);
    }
  }
  
}
