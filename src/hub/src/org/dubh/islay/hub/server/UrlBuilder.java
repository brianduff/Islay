package org.dubh.islay.hub.server;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.google.appengine.repackaged.com.google.common.collect.Maps;

/**
 * Utility class for building URLs.
 * 
 * @author bduff
 */
public final class UrlBuilder {
  private final String baseUrl;
  private final Map<String, String> params = Maps.newLinkedHashMap();
  
  private UrlBuilder(String baseUrl) {
    this.baseUrl = baseUrl;
  }
  
  public static UrlBuilder on(String baseUrl) {
    return new UrlBuilder(baseUrl);
  }
  
  public UrlBuilder param(String key, String value) {
    params.put(key, value);
    return this;
  }
  
  public URL get() {
    StringBuilder urlString = new StringBuilder(baseUrl);
    
    if (!params.isEmpty()) {
      urlString.append('?');
      for (Map.Entry<String, String> entry : params.entrySet()) {
        urlString.append(entry.getKey());
        urlString.append("=");
        urlString.append(urlencode(entry.getValue()));
        urlString.append("&");
      }
      urlString.setLength(urlString.length() - 1);
    }
    
    try {
      return new URL(urlString.toString());
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    } finally {
      params.clear();
    }
  }
  
  private String urlencode(String value) {
    try {
      return URLEncoder.encode(value, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }
}