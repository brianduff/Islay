package org.dubh.islay.hub.server.facebook;

import com.google.common.base.Objects;

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
  
  public User setId(String id) {
    this.id = id;
    return this;
  }
  
  @Override
  public String getName() {
    return name;
  }
  public User setName(String name) {
    this.name = name;
    return this;
  }
  
  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof User)) {
      return false;
    }
    User other = (User) obj;
    return Objects.equal(id, other.id) && Objects.equal(name, other.name); 
  }
}
