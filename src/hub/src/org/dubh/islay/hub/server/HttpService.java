package org.dubh.islay.hub.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.common.base.Charsets;
import com.google.appengine.repackaged.com.google.common.io.CharStreams;

/**
 * A service that makes it easier to simple HTTP requests.
 * 
 * @author bduff
 */
public class HttpService {
  private static final Logger log = Logger.getLogger(HttpService.class.getName());
  /**
   * Gets the specified URL.
   * 
   * @param url
   * @return
   * @throws IOException
   */
  public String get(URL url, Signer signer) throws IOException {
    HttpURLConnection conn = null;
    try {
      conn = (HttpURLConnection) url.openConnection();
      signer.sign(conn);

      conn.setReadTimeout(10000);
      conn.setConnectTimeout(10000);
     
      conn.connect();
      
      String result = CharStreams.toString(new BufferedReader(
          new InputStreamReader(conn.getInputStream(), Charsets.UTF_8)));
      
      log.info(url + "\n" + result);
      
      return result;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
  }
  
  public String get(URL url) throws IOException {
    return get(url, NullSigner.INSTANCE);
  }
  
  /**
   * Something that can sign a connection.
   * @author brianduff
   */
  public interface Signer {
    void sign(HttpURLConnection conn) throws IOException;
  }
  
  private enum NullSigner implements Signer {
    INSTANCE {
      @Override
      public void sign(HttpURLConnection conn) {}
    }
  }
}
