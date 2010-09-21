package org.dubh.islay.hub.shared;

import java.io.Serializable;

/**
 * Represents an openid provider.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
public class OpenIdProvider implements Serializable {
  private String imageUrl;
  private String name;
  private String loginUrl;
  private String providerId;
  
  public OpenIdProvider() {
  }
  
  public String getImageUrl() {
    return imageUrl;
  }

  public OpenIdProvider setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public String getName() {
    return name;
  }

  public OpenIdProvider setName(String name) {
    this.name = name;
    return this;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public OpenIdProvider setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
    return this;
  }
  
  public String getProviderId() {
    return providerId;
  }

  public OpenIdProvider setProviderId(String providerId) {
    this.providerId = providerId;
    return this;
  }
}
