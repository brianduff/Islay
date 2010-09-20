package org.dubh.islay.hub.model;

import java.io.Serializable;

import javax.persistence.Id;

/**
 * Information about a user of the hub.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
public class User implements Serializable {
  /**
   * The numeric internal identifier of this user. This is automatically
   * generated by AppEngine for persisted entities.
   */
  private @Id Long internalId;
  
  /**
   * The user visible identifier of this user. An openid or an email address.
   */
  private String userId;
  
  public Long getInternalId() {
    return internalId;
  }

  public User setInternalId(Long internalId) {
    this.internalId = internalId;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public User setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((internalId == null) ? 0 : internalId.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (internalId == null) {
      if (other.internalId != null)
        return false;
    } else if (!internalId.equals(other.internalId))
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    } else if (!userId.equals(other.userId))
      return false;
    return true;
  }

}