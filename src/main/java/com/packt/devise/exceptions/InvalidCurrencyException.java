package com.packt.devise.exceptions;

public class InvalidCurrencyException extends RuntimeException {
  public InvalidCurrencyException(String currencyCode) {
    super("Devise non valide : " + currencyCode);
  }
}
