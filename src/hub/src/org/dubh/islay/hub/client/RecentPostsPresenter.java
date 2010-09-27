package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.client.RecentPostsPresenter.MyProxy;
import org.dubh.islay.hub.client.RecentPostsPresenter.MyView;
import org.dubh.islay.hub.shared.Activity;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class RecentPostsPresenter extends AuthenticatedUserPresenter<MyView, MyProxy> {
  public static final String TOKEN = "recentposts";
  
  private final ActivityServiceAsync activityService;
  
  @Inject
  public RecentPostsPresenter(EventBus eventBus, MyView view, MyProxy proxy,
      PlaceManager placeManager, ActivityServiceAsync activityService) {
    super(eventBus, view, proxy, placeManager);
    this.activityService = activityService;
  }
  
  @Override
  protected void postReveal() {
    activityService.getRecentActivities(getCurrentUser(), Network.BUZZ, new AsyncCallback<List<Activity>>() {
      @Override
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(List<Activity> result) {
        getView().showActivities(result);
      }      
    });
  }
  
  @ProxyEvent
  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    super.onUserLoggedIn(event);
  }
  
  public interface MyView extends View {
    void showActivities(List<Activity> activities);
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<RecentPostsPresenter> {}
}
