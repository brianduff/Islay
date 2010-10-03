package org.dubh.islay.hub.server.facebook;

/**
 * A named object in the facebook API. Can be any object that has
 * a name and and id.
 * 
 * @author brianduff
 */
public interface NamedObject extends FacebookObject {
  String getName();
}
