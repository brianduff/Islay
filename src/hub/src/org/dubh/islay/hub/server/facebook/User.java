package org.dubh.islay.hub.server.facebook;

/**
 * Represents a person in the facebook API.
 * 
 * @author bduff
 */
public class User implements NamedObject {
  private String id;
  private String name;
  
  @Override
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @Override
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + "]";
  }
}
