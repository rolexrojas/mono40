package com.mono40.movil.recipient;

import java.util.List;

/**
 * @author hecvasro
 */
public interface RecipientStore {

  List<Object> creditCards();

  List<Object> loans();

  List<Object> contracts();

  List<Object> salaryAdvances();

  List<Object> extraCredits();

  List<Object> accountNumbers();

  List<Object> phoneNumbers();
}
