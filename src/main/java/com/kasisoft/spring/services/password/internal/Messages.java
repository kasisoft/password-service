package com.kasisoft.spring.services.password.internal;

import com.kasisoft.libs.common.i18n.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages {

  @I18N("No message digester named '%s' found. See http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest")
  public static I18NFormatter     invalid_digest;
  
  @I18N("The password generation isn't configured properly. Digest: '%s', Salt length: '%s'.")
  public static I18NFormatter     misconfiguration_error;
  
  static {
    I18NSupport.initialize( Messages.class );
  }
  
} /* ENDCLASS */
