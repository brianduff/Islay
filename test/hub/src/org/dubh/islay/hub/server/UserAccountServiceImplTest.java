package org.dubh.islay.hub.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.dubh.islay.hub.model.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.util.Providers;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Query;

/**
 * Unit tests for {@link UserAccountServiceImpl}.
 * 
 * @author brianduff
 */
public class UserAccountServiceImplTest {
  private static final String FED_ID = "federatedIdentity";
  private static final String EMAIL = "test@example.com";
  private @Mock UserService userService;
  private @Mock ObjectifyFactory of;
  private @Mock Objectify objectify;
  private @Mock Query<UserAccount> query;
  private final User user = new User(EMAIL, "", "", FED_ID);
  private UserAccountServiceImpl accountService;
  private final Date now = new Date();
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    accountService = new UserAccountServiceImpl(userService, of, Providers.of(now));
    when(of.begin()).thenReturn(objectify);
    when(objectify.query(UserAccount.class)).thenReturn(query);
    when(query.filter("userId", FED_ID)).thenReturn(query);
  }
  
  @Test
  public void getLoggedInUser_ReturnsNullWhenNobodyLoggedIn() {
    assertNull(accountService.getLoggedInUser());
  }
  
  @Test
  public void getLoggedInUser_NewUser() {
    when(userService.getCurrentUser()).thenReturn(user);

    accountService.getLoggedInUser();

    verify(objectify).put(createUserAccount());
  }

  @Test
  public void getLoggedInUser_ReturnsExistingUser() {
    when(userService.getCurrentUser()).thenReturn(user);
    UserAccount userAccount = createUserAccount();
    when(query.get()).thenReturn(userAccount);
    
    assertEquals(userAccount, accountService.getLoggedInUser());
    
    verify(objectify, never()).put(userAccount);
  }

  @Test
  public void getLoggedInUser_UpdatesMD5SumIfNotSet() {
    when(userService.getCurrentUser()).thenReturn(user);
    UserAccount userAccount = createUserAccount().setEmailMD5Sum(null);
    when(query.get()).thenReturn(userAccount);
    
    UserAccount accountWithMD5 = createUserAccount();
    assertEquals(accountWithMD5, accountService.getLoggedInUser());
    
    verify(objectify).put(accountWithMD5);
  }
  
  @Test
  public void save() {
    UserAccount account = createUserAccount();
    account.setEmailAddress("another@example.com");
    
    accountService.save(account);
    
    UserAccount expected = createUserAccount()
        .setEmailAddress("another@example.com")
        .setEmailMD5Sum("86a7c65390f83bdee8c4455af823b7cb");
    verify(objectify).put(expected);
  }

  
  private UserAccount createUserAccount() {
    return new UserAccount().setUserId(FED_ID)
        .setEmailAddress(EMAIL)
        .setEmailMD5Sum("55502f40dc8b7c769880b10874abc9d0")
        .setJoinDate(now);
  }
}
