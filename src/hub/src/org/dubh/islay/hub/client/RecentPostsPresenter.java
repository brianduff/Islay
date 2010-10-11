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
  
  public static final Object TYPE_RevealSettingsBar = new Object();
  
  private final ActivityServiceAsync activityService;
  private final SettingsPresenter settingsPresenter;
  private final BrowserEnvironment env;
  
  @Inject
  public RecentPostsPresenter(EventBus eventBus, MyView view, MyProxy proxy,
      PlaceManager placeManager, ActivityServiceAsync activityService, BrowserEnvironment env,
      SettingsPresenter settingsPresenter) {
    super(eventBus, view, proxy, placeManager);
    this.activityService = activityService;
    this.settingsPresenter = settingsPresenter;
    this.env = env;
  }
  
  @Override
  protected void postReveal() {
    setInSlot(TYPE_RevealSettingsBar, settingsPresenter);
    settingsPresenter.setLoggedInUser(getCurrentUser());
    
    if (getCurrentUser().getAssociatedNetworks().isEmpty()) {
      getView().setStatus("You must connect to a network first. Click on settings --->");
    } else {
      activityService.getRecentActivities(getCurrentUser(), Network.BUZZ, new AsyncCallback<List<Activity>>() {
        @Override
        public void onFailure(Throwable caught) {
          caught.printStackTrace();
        }
  
        @Override
        public void onSuccess(List<Activity> result) {
          getView().showActivities(result);
          getView().showRawActivities("This is where the raw string should go");
        }      
      });
    }
    
    if ("1".equals(env.getUrlParameter("s"))) {
      settingsPresenter.setExpanded(true);
    }
  }

  @ProxyEvent
  @Override
  public void onUserLoggedIn(UserLoggedInEvent event) {
    super.onUserLoggedIn(event);
    settingsPresenter.setLoggedInUser(event.getUserAccount());
  }
  
  public interface MyView extends View {
    void showActivities(List<Activity> activities);
    void showRawActivities(String raw);
    void setStatus(String status);
  }
  
  @ProxyStandard
  @NameToken(TOKEN)
  public interface MyProxy extends ProxyPlace<RecentPostsPresenter> {}

}
