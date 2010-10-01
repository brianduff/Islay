package org.dubh.islay.hub.client;

import java.util.HashMap;
import java.util.Map;

import org.dubh.islay.hub.shared.Network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class NetworksView extends ViewImpl implements NetworksPresenter.MyView {
  interface Binder extends UiBinder<Widget, NetworksView> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  /**
   * Number of columns in the grid of networks.
   */
  private static final int NETWORK_COLS = 2;

  private final Widget widget;

  @UiField HasUserInformation header;
  @UiField Grid connections;
  @UiField Button showActivities;

  
  private final Map<Network, NetworkConnectionWidget> connectionWidgets =
    new HashMap<Network, NetworkConnectionWidget>();
  
  public NetworksView() {
    widget = uiBinder.createAndBindUi(this);
    
    for (Network network : Network.values()) {
      NetworkConnectionWidget connWidget = new NetworkConnectionWidget();
      connWidget.setNetwork(network, false);
      connectionWidgets.put(network, connWidget);
    }
    
    connections.resize((Network.values().length + 1) / NETWORK_COLS, NETWORK_COLS);
    
    int row = 0, col = 0;
    for (Network network : Network.values()) {
      connections.setWidget(row, col, connectionWidgets.get(network));
      col++;
      if (col == NETWORK_COLS) {
        col = 0;
        row++;
      }
    }
  }
  
  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setConnected(Network network, boolean isConnected) {
    connectionWidgets.get(network).setNetwork(network, isConnected);
  }
  
  @Override
  public void showStatus(Network network, String message) {
    connectionWidgets.get(network).setConnectionStatus(message);
  }

  @Override
  public HasClickHandlers getClickHandler(Network network) {
    return connectionWidgets.get(network);
  }

  @Override
  public HasClickHandlers getShowActivities() {
    return showActivities;
  }
  
  @Override
  public HasUserInformation userBar() {
    return header;
  }
}
