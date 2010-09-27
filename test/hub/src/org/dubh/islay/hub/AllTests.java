package org.dubh.islay.hub;

import org.dubh.islay.hub.client.NetworksPresenterTest;
import org.dubh.islay.hub.model.UserAccountTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all of our tests.
 * 
 * @author brianduff
 */
// TODO(bduff) use a suite builder instead of listing them all out here.
@Suite.SuiteClasses({
    NetworksPresenterTest.class,
    UserAccountTest.class
})
@RunWith(Suite.class)
public class AllTests {
}