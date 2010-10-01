package org.dubh.islay.hub.client;

import org.dubh.islay.hub.shared.Network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that displays information about connections to a particular network.
 * 
 * @author brianduff
 */
public class NetworkConnectionWidget extends Composite implements HasClickHandlers {
  interface Binder extends UiBinder<Widget, NetworkConnectionWidget> {}
  private static Binder uiBinder = GWT.create(Binder.class);
  
  @UiField Anchor serviceLink;
  @UiField Label connectionDescription;
  @UiField Style style;

  public NetworkConnectionWidget() {
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  interface Style extends CssResource {
    String connected();
    String notconnected();
  }
  
  /**
   * Sets the {@link Network} to display in this component, and whether we have
   * an authenticated connection to that network.
   * 
   * @param network a network to display.
   * @param isConnected if {@code true}, show this network as connected.
   */
  public void setNetwork(Network network, boolean isConnected) {
    serviceLink.setText(network.displayName());
    String imageUrl = isConnected ? network.iconUrl() : network.disabledIconUrl();
    serviceLink.getElement().getStyle().setBackgroundImage("url(" + imageUrl + ")");
    if (isConnected) {
      serviceLink.setTitle("Click to disconnect from " + network.displayName());
      connectionDescription.removeStyleName(style.notconnected());
      connectionDescription.addStyleName(style.connected());      
    } else {
      serviceLink.setTitle("Click to connect to " + network.displayName());
      connectionDescription.addStyleName(style.notconnected());
      connectionDescription.removeStyleName(style.connected());      
    }
  }
  
  /**
   * Sets the status of this connection.
   * 
   * @param status a status for this connection.
   */
  public void setConnectionStatus(String status) {
    connectionDescription.setText(status);
  }

  /**
   * Add a click handler which will be notified when the user clicks on the
   * hyperlink.
   */
  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return serviceLink.addClickHandler(handler);
  }  
}
