package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.shared.Activity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * Simple temporary view to show posts by the current user in Buzz. Just
 * a proof of concept to show that we can get stuff from the API.
 * @author brianduff
 */
public class RecentPostsView extends ViewImpl implements RecentPostsPresenter.MyView {
  interface Binder extends UiBinder<Widget, RecentPostsView> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  private final Widget widget;
  
  @UiField VerticalPanel activitiesList;

  public RecentPostsView() {
    widget = uiBinder.createAndBindUi(this);
  }
  
  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void showActivities(List<Activity> activities) {
    activitiesList.clear();
    for (Activity activity : activities) {
      activitiesList.add(new Label(activity.getText()));
    }
  }
}
