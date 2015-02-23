package com.s5tech.backend.service;
/**
 * Thrown when a user attempts to login with invalid credentials.
 */
@SuppressWarnings("serial")
public class InvalidLoginException extends Exception {
  public InvalidLoginException( String msg ){
    super( msg );
  }
}
