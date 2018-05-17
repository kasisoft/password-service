package com.kasisoft.cdi.services.password;

import static  com.kasisoft.cdi.services.password.internal.Messages.*;

import com.kasisoft.libs.common.constants.*;

import org.apache.commons.codec.binary.*;

import javax.annotation.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import lombok.extern.slf4j.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Service implementation which is used to deal with passwords.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Named @Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"digest", "saltLength"})
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class PasswordService {

  @Getter @Setter
  int       saltLength;
  
  @Getter @Setter
  String    digest;
  
  Digest    digester;
  
  public PasswordService() {
    saltLength  = 48;
    digest      = "SHA-512";
    init();
  }
  
  @PostConstruct
  public void init() {
    
    if( (digest == null) || (saltLength < 1) ) {
      String message = misconfiguration_error.format( String.valueOf( digest ), String.valueOf( saltLength ) );
      log.error( message );
      throw new IllegalStateException( message );
    }
    
    digester = Digest.valueByName( digest );
    if( digester == null ) {
      log.error( invalid_digest.format( digest ) );
      throw new IllegalStateException( invalid_digest.format( digest ) );
    }

  }
  
  /**
   * Creates a random salt value.
   *
   * @return   A random salt value. Not <code>null</code> and with the length of 64 characters.
   */
  public String createSalt() {
    byte[] bytes = new byte[ saltLength ];
    for( int i = 0; i < bytes.length; i++ ) {
      bytes[i] = (byte) (((long) (Math.random() * System.currentTimeMillis())) % 256);
    }
    return Base64.encodeBase64String( bytes );
  }

  /**
   * Calculates a hash for a given password.
   *
   * @param password   The password which hash should be calculated. Neither <code>null</code> nor empty.
   * @param salt       The salt for randomization. Neither <code>null</code> nor empty.
   *
   * @return   A base64 version of the hash. Neither <code>null</code>.
   */
  public String calculateHash( @NonNull String password, @NonNull String salt ) {

    byte[]  bytes   = salt.getBytes();

    // derive the hash calculation from the salt value itself
    boolean prepend = (bytes[0] % 2) == 1;
    int     count   = 100 * (Math.abs( bytes[1] ) + 1);

    String data     = String.format( "%s%s", prepend ? salt : password, prepend ? password : salt );
    byte[] asbytes  = data.getBytes();

    return Base64.encodeBase64String( digester.digest( count, asbytes ) );

  }

  /**
   * Verifies that a password is valid.
   *
   * @param hashed      The previously hashed password for the comparison. Neither <code>null</code> nor empty.
   * @param password    The password which has to be tested. Neither <code>null</code> nor empty.
   * @param salt        The salt which is used for password randomization. Neither <code>null</code> nor empty.
   *
   * @return   <code>true</code> <=> The password is valid.
   */
  public boolean isValidPassword( @NonNull String hashed, @NonNull String password, @NonNull String salt ) {
    return calculateHash( password, salt ).equals( hashed );
  }

} /* ENDCLASS */
