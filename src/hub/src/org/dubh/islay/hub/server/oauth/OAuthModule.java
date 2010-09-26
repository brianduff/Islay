package org.dubh.islay.hub.server.oauth;

import static org.dubh.islay.hub.shared.Path.OAUTH;

import org.dubh.islay.hub.client.NetworksPresenter;
import org.dubh.islay.hub.shared.Path;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;

/**
 * Module that configures oauth providers.
 * 
 * @author brianduff
 */
public class OAuthModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve(Path.of(OAUTH)).with(NetworkAuthServiceImpl.class);
    // Feverish hackery. Assumes for Development that we're running in GWT hosted
    // mode. For prod assumes we're running on a specific app id (evilness!)
    bindConstant().annotatedWith(Names.named("RedirectUrl")).to(
        SystemProperty.environment.value() == SystemProperty.Environment.Value.Development ?
            "http://127.0.0.1:8888/Hub.html?gwt.codesvr=127.0.0.1:9997#" + NetworksPresenter.TOKEN :
            "http://islay-test.appspot.com/Hub.html#" + NetworksPresenter.TOKEN
    );
  }
}
