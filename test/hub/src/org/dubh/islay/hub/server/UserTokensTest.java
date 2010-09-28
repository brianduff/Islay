package org.dubh.islay.hub.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.dubh.islay.hub.server.NetworkTokens.TokenAndSecret;
import org.dubh.islay.hub.shared.Network;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Islandir
 */
public class UserTokensTest {
  private UserTokens accessTokens;
  
  @Before
  public void setUserAccount() {
    accessTokens = new UserTokens();
  }
  
  @Test
  public void testTokensNotNull() {
    assertNotNull(accessTokens.getTokens(Network.BUZZ));
  }
  
  @Test
  public void testNetworkCorrect() {
    NetworkTokens nAssoc = accessTokens.getTokens(Network.BUZZ);
    assertEquals(Network.BUZZ, nAssoc.getNetwork());
  }
  
  @Test
  public void testAssociationProperlySet() {
    accessTokens.getTokens(Network.BUZZ).setAccessToken(new TokenAndSecret("foo", "bar"));
    assertEquals("foo", accessTokens.getTokens(Network.BUZZ).getAccessToken().getToken());
    assertEquals("bar", accessTokens.getTokens(Network.BUZZ).getAccessToken().getSecret());
  }
}
