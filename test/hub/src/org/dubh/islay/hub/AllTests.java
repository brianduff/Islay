package org.dubh.islay.hub;

import org.dubh.islay.hub.client.LoginPresenterTest;
import org.dubh.islay.hub.server.UserAccountServiceImplTest;
import org.dubh.islay.hub.server.UserTokensTest;
import org.dubh.islay.hub.server.facebook.FacebookServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all of our tests.
 * 
 * @author brianduff
 */
// TODO(bduff) use a suite builder instead of listing them all out here.
@Suite.SuiteClasses({
    LoginPresenterTest.class,
    UserAccountServiceImplTest.class,
    UserTokensTest.class,
    FacebookServiceTest.class
})
@RunWith(Suite.class)
public class AllTests {
}
