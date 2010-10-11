package org.dubh.islay.hub.client.util;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;

/**
 * An animation that slides up or down.
 * 
 * @author brianduff
 */
public class SlideDownAnimation extends Animation {
  private static final int TIME = 250;
  
  private final int expandedHeight;
  private final Element element;
  
  private boolean expanded = false;
  private int collapsedHeight;
  
  public SlideDownAnimation(int expandedHeight, Element element) {
    this.expandedHeight = expandedHeight;
    this.element = element;
  }
  
  /**
   * Toggles the state of this animation.
   */
  public void toggle() {
    // Cancel any pending animation.
    cancel();
    if (!expanded) {
      collapsedHeight = element.getScrollHeight();
    }
    expanded = !expanded;
    run(TIME);
  }
  
  /**
   * Explicitly sets whether the animation is expanded or collapsed.
   */
  public void setExpanded(boolean expanded) {
    if (expanded != this.expanded) {
      toggle();
    }
  }
  
  @Override
  protected void onUpdate(double progress) {
    int newHeight;
    if (expanded) {
      newHeight = (int) (collapsedHeight + ((expandedHeight - collapsedHeight) * progress));
    } else {
      newHeight = (int) (collapsedHeight + ((expandedHeight - collapsedHeight) * (1.0 - progress)));
    }
    element.getStyle().setHeight(newHeight, Unit.PX);
  }
  
  @Override
  protected void onComplete() {
    super.onComplete();
    if (!expanded) {
      element.getStyle().clearHeight();
    }
  }
}
