package org.dubh.islay.hub.model;

import org.dubh.islay.hub.shared.Network;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * 
 * @author Islandir
 *
 */
public class UserAccountTest {
  private UserAccount userAccount;
  
  @Before
  public void setUserAccount() {
    userAccount = new UserAccount();
  }
  
  @Test
  public void testNetworkAssociationNotNull() {
    Assert.assertNotNull(userAccount.getNetworkAssociation(Network.BUZZ));
  }
  
  @Test
  public void testNetworkCorrect() {
    NetworkAssociation nAssoc = userAccount.getNetworkAssociation(Network.BUZZ);
    Assert.assertEquals(Network.BUZZ, nAssoc.getNetwork());
  }
  
  @Test
  public void testNoAssociationOnCreation() {
    Assert.assertTrue(userAccount.getAssociatedNetworks().isEmpty());
  }

  @Test
  public void testAssociationProperlySet() {
    userAccount.getNetworkAssociation(Network.BUZZ);
    Assert.assertEquals(1, userAccount.getAssociatedNetworks().size());
    Assert.assertTrue(userAccount.getAssociatedNetworks().contains(Network.BUZZ));
  }
}
