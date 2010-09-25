package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.gwtplatform.mvp.client.HasEventBus;

/**
 * An event that's fired to indicate that the user logged in.
 * 
 * @author bduff
 */
public class UserLoggedInEvent extends GwtEvent<UserLoggedInEvent.Handler> {
  public interface Handler extends EventHandler {
    void onUserLoggedIn(UserLoggedInEvent event);
  }
  private static final Type<Handler> TYPE = new Type<Handler>();
  
  private final UserAccount userAccount;
  
  public UserLoggedInEvent(UserAccount userAccount) {
    this.userAccount = userAccount;
  }
  
  public static void fire(HasEventBus source, UserAccount user) {
    source.fireEvent(new UserLoggedInEvent(user));
  }
  
  public static Type<Handler> getType() {
    return TYPE;
  }
  
  public UserAccount getUserAccount() {
    return userAccount;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onUserLoggedIn(this);
  }
}
