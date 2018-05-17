# DISCONTINUED
# DISCONTINUED
# DISCONTINUED

# Purpose


This library allows to generate hashes for passwords.


# Infos

* [eMail: daniel.kasmeroglu@kasisoft.net](mailto:daniel.kasmeroglu@kasisoft.net)
* [GIT](https://kasisoft.com/bitbucket/projects/GRAV/repos/password-service)


# Development Setup

I assume that you're familiar with Maven. If not I suggest to visit the following page:

* https://maven.apache.org/


## Requirements

* Java 8


## Maven

### Releases

     <dependency>
         <groupId>com.kasisoft.cdi</groupId>
        <artifactId>password-service</artifactId>
        <version>0.1</version>
     </dependency>


### Snapshots

Snapshots can be used while accessing a dedicated maven repository. Your POM needs the following settings:

     <dependency>
       <groupId>com.kasisoft.cdi</groupId>
       <artifactId>password-service</artifactId>
       <version>0.1-SNAPSHOT</version>
     </dependency>
     
     <repositories>
         <repository>
             <id>libs-kasisoft</id>
             <url>https://kasisoft.com/artifactory/libs-kasisoft</url>
             <releases>
                 <enabled>true</enabled>
             </releases>
             <snapshots>
                 <enabled>true</enabled>
             </snapshots>
         </repository>
     </repositories>


# USAGE

The allowed digest values can be found here:

  http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
  
  
The service can be injected or instantiated:

    @Inject
    PasswordService     pwdService;
    
or

    PasswordService     pwdService = new PasswordService();
    pwdService.setSaltLength( 32 );
    // must be called after parameters have been changed
    pwdService.init();

Generate a salt and calculate the hash for a password:

    String salt   = pwdService.createSalt();
    String hashed = pwdService.calculateHash( "my password", salt );
    
Test wether a password is valid or not:

    boolean valid = pwdService.isValidPassword( hashed, "my password", salt );

  
# License

MIT License

Copyright (c) 2017 Daniel Kasmeroglu (Kasisoft)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
