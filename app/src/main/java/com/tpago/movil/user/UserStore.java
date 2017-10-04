package com.tpago.movil.user;

/**
 * @author hecvasro
 */
public interface UserStore {

  void set(User user);

  boolean isSet();

  User get();

  void clear();
}
