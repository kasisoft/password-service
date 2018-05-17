package com.kasisoft.spring.services.password;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.*;

import org.springframework.test.context.junit.jupiter.*;

import org.springframework.test.context.*;

import org.junit.jupiter.api.extension.*;

import org.junit.jupiter.api.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the PasswordService.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PasswordService.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordServiceTest {

  @Autowired
  PasswordService   service;
  
  @Test
  public void createSalt() {
    Set<String> salts = new HashSet<>();
    for( int i = 0; i < 1000; i++ ) {
      String salt = service.createSalt();
      assertThat( salt, is( notNullValue() ) );
      assertThat( salt.isEmpty(), is( false ) );
      assertThat( salts.contains( salt ), is( false ) );
      salts.add( salt );
    }
  }

  @Test // (groups="all", dependsOnMethods="createSalt")
  public void calculateHash() {

    String salt1 = service.createSalt();
    String salt2 = service.createSalt();
    assertThat( salt1, is( notNullValue() ) );
    assertThat( salt2, is( notNullValue() ) );
    assertThat( salt1.isEmpty(), is( false ) );
    assertThat( salt2.isEmpty(), is( false ) );
    assertThat( salt1.equals( salt2 ), is( false ) );

    String password1 = "Fred finds a frog";
    String password2 = "Bibo is weird";

    // 1. the same password with different salts should result in different hashes
    String hash1 = service.calculateHash( password1, salt1 );
    String hash2 = service.calculateHash( password1, salt2 );

    assertThat( hash1, is( notNullValue() ) );
    assertThat( hash2, is( notNullValue() ) );
    assertThat( hash1.equals( hash2 ), is( false ) );


    // 2. different password with the same salts should result in different hashes
    hash1 = service.calculateHash( password1, salt1 );
    hash2 = service.calculateHash( password2, salt1 );

    assertThat( hash1, is( notNullValue() ) );
    assertThat( hash2, is( notNullValue() ) );
    assertThat( hash1.equals( hash2 ), is( false ) );

  }

  @Test // (groups="all", dependsOnMethods="calculateHash")
  public void isValidPassword() {

    String salt = service.createSalt();
    assertThat( salt, is( notNullValue() ) );
    assertThat( salt.isEmpty(), is( false ) );

    String password = "Bibo is weird";

    String hash = service.calculateHash( password, salt );
    assertThat( hash, is( notNullValue() ) );
    assertThat( hash.isEmpty(), is( false ) );

    assertThat( service.isValidPassword( hash, password, salt ), is( true ) );
    assertThat( service.isValidPassword( hash, password.substring(1), salt ), is( false ) );
    assertThat( service.isValidPassword( hash, password, salt.substring(1) ), is( false ) );

  }

} /* ENDCLASS */
