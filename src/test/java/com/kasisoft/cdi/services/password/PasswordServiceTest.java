package com.kasisoft.cdi.services.password;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.cdi.weldex.*;

import org.testng.annotations.*;

import javax.annotation.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the PasswordService.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ManagedBean
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordServiceTest {

  PasswordService   service;
  
  @BeforeSuite
  public void setUp() {
    service = CdiContext.component( PasswordService.class );
  }
  
  @Test(groups="all")
  public void createSalt() {
    Set<String> salts = new HashSet<>();
    for( int i = 0; i < 1000; i++ ) {
      String salt = service.createSalt();
      assertThat( salt, is( notNullValue() ) );
      assertFalse( salt.isEmpty() );
      assertFalse( salts.contains( salt ) );
      salts.add( salt );
    }
  }

  @Test(groups="all", dependsOnMethods="createSalt")
  public void calculateHash() {

    String salt1 = service.createSalt();
    String salt2 = service.createSalt();
    assertThat( salt1, is( notNullValue() ) );
    assertThat( salt2, is( notNullValue() ) );
    assertFalse( salt1.isEmpty() );
    assertFalse( salt2.isEmpty() );
    assertFalse( salt1.equals( salt2 ) );

    String password1 = "Fred finds a frog";
    String password2 = "Bibo is weird";

    // 1. the same password with different salts should result in different hashes
    String hash1 = service.calculateHash( password1, salt1 );
    String hash2 = service.calculateHash( password1, salt2 );

    assertThat( hash1, is( notNullValue() ) );
    assertThat( hash2, is( notNullValue() ) );
    assertFalse( hash1.equals( hash2 ) );


    // 2. different password with the same salts should result in different hashes
    hash1 = service.calculateHash( password1, salt1 );
    hash2 = service.calculateHash( password2, salt1 );

    assertThat( hash1, is( notNullValue() ) );
    assertThat( hash2, is( notNullValue() ) );
    assertFalse( hash1.equals( hash2 ) );

  }

  @Test(groups="all", dependsOnMethods="calculateHash")
  public void isValidPassword() {

    String salt = service.createSalt();
    assertThat( salt, is( notNullValue() ) );
    assertFalse( salt.isEmpty() );

    String password = "Bibo is weird";

    String hash = service.calculateHash( password, salt );
    assertThat( hash, is( notNullValue() ) );
    assertFalse( hash.isEmpty() );

    assertTrue( service.isValidPassword( hash, password, salt ) );
    assertFalse( service.isValidPassword( hash, password.substring(1), salt ) );
    assertFalse( service.isValidPassword( hash, password, salt.substring(1) ) );

  }

} /* ENDCLASS */
