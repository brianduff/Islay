package org.dubh.islay.hub.shared;

import java.io.Serializable;

/**
 * Temporary class to represent an activity. I expect this will eventually
 * move to the model package and be persistent. Just using this for now
 * temporarily to pass information back from the Buzz API for testing.
 * 
 * @author brianduff
 *
 */
@SuppressWarnings("serial")
public class Activity implements Serializable {
  private String text;
  
  public String getText() {
    return text;
  }
  
  public Activity setText(String text) {
    this.text = text;
    return this;
  }
}
