package org.dubh.islay.hub.client;

import java.util.HashMap;
import java.util.Map;

import org.dubh.islay.hub.client.util.FadeAnimation;
import org.dubh.islay.hub.client.util.HasUrl;
import org.dubh.islay.hub.client.util.SlideDownAnimation;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class SettingsView extends ViewImpl implements SettingsPresenter.MyView {
  interface Binder extends UiBinder<Widget, SettingsView> {}
  private static Binder uiBinder = GWT.create(Binder.class);

  /**
   * Number of columns in the grid of networks.
   */
  private static final int NETWORK_COLS = 2;

  private final Map<Network, NetworkConnectionWidget> connectionWidgets =
    new HashMap<Network, NetworkConnectionWidget>();

  private final Widget widget;
  @UiField Grid connections;
  @UiField HorizontalPanel linkbar;
  @UiField Image avatar;
  @UiField Label username;
  @UiField Anchor settingsLink;
  @UiField Anchor logOutLink;
  @UiField HTMLPanel settings;
  
  private final HasUrl imageAdapter;
  private final HasUrl logOutLinkAdapter;
  private final SlideDownAnimation slideDown;
  private final FadeAnimation fade;
  
  public SettingsView() {
    widget = uiBinder.createAndBindUi(this);
    
    imageAdapter = new ImageAdapter(avatar);
    logOutLinkAdapter = new AnchorAdapter(logOutLink);
    
    initGrid();
    settings.setVisible(false);
    avatar.setWidth("20px");
    avatar.setHeight("20px");
    slideDown = new SlideDownAnimation(500, linkbar.getElement());
    fade = new FadeAnimation(250, settings);
    settingsLink.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        slideDown.toggle();
        fade.toggle();
      }
    });
  }
  
  private void initGrid() {    
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
  public HasText email() {
    return username;
  }

  @Override
  public HasUrl logOutLink() {
    return logOutLinkAdapter;
  }

  @Override
  public HasUrl avatarImage() {
    return imageAdapter;
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
  public void setExpanded(boolean expanded) {
    slideDown.setExpanded(expanded);
    fade.setVisible(expanded);
  }
  
  private static class ImageAdapter implements HasUrl {
    private final Image image;
    ImageAdapter(Image image) {
      this.image = image;
    }
    @Override
    public void setUrl(String url) {
      image.setUrl(url);
    }
  }
  
  private static class AnchorAdapter implements HasUrl {
    private final Anchor anchor;
    AnchorAdapter(Anchor anchor) {
      this.anchor = anchor;
    }
    @Override
    public void setUrl(String url) {
      anchor.setHref(url);
    }
  }

}
