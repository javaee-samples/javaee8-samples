# Java EE 8 Samples #

This workspace consists of Java EE 8 Samples and unit tests. They are categorized in different directories, one for each Technology/JSR.

Some samples/tests have documentation, otherwise read the code. 

## How to run? ##

Samples are tested on Payara, GlassFish and Tomcat using Arquillian. Arquillian uses container profiles to start up and deploy tests to individual containers. 

Only one container profile can be active at a given time, otherwise there will be dependency conflicts.

These are the available container profiles:

* Payara and GlassFish
  * ``payara-ci-managed``
    
      This profile will install a Payara server and start up the server per sample.
      Useful for CI servers. The Payara version that's used can be set via the ``payara.version`` property.
      This is the default profile and does not have to be specified explicitly.

   * ``payara-micro-managed``
    
      This profile will install Payara Micro and start up the jar per sample.
      Useful for CI servers. The Payara Micro version that's used can be set via the ``payara.micro.version`` property.

  * ``payara-remote``
    
      This profile requires you to start up a Payara server outside of the build. Each sample will then
      reuse this instance to run the tests.
      Useful for development to avoid the server start up cost per sample.
      
      This profile supports for some tests to set the location where Payara is installed via the ``glassfishRemote_gfHome``
      system property. E.g.
    
      ``-DglassfishRemote_gfHome=/opt/payara173``
      
      This is used for sending asadmin commands to create container resources, such as users in an identity store.

  * ``glassfish-embedded``
    
      This profile uses the GlassFish embedded server and runs in the same JVM as the TestClass.
      Useful for development, but has the downside of server startup per sample.

  * ``glassfish-remote``
    
      This profile requires you to start up a GlassFish server outside of the build. Each sample will then
      reuse this instance to run the tests.
      Useful for development to avoid the server start up cost per sample.
      
      This profile supports for some tests to set the location where GlassFish is installed via the ``glassfishRemote_gfHome``
      system property. E.g.
    
      ``-DglassfishRemote_gfHome=/opt/glassfish50``
      
      This is used for sending asadmin commands to create container resources, such as users in an identity store.

* Tomcat
    
  * ``tomcat-remote``

      This profile requires you to start up a plain Tomcat 9 server outside of the build. Each sample will then
      reuse this instance to run the tests.
    
      Tomcat supports samples that make use of Servlet, JSP, Expression Language (EL), WebSocket and JASPIC.
    
      This profile requires you to enable JMX in Tomcat. This can be done by adding the following to ``[tomcat home]/bin/catalina.sh``:
    
      ```
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=8089 -Dcom.sun.management.jmxremote=true "
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false "
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
      JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=localhost "
      ```
    
      This profile also requires you to set a username (``tomcat``) and password (``manager``) for the management application in 
      ``tomcat-users.xml``. See the file ``test-utils/src/main/resources/tomcat-users.xml`` in this repository for a full example.
    
      Be aware that this should *only* be done for a Tomcat instance that's used exclusively for testing, as the above will make
      the Tomcat installation **totally insecure!**
    
  * ``tomcat-ci-managed``

      This profile will install a Tomcat server and start up the server per sample.
      Useful for CI servers. The Tomcat version that's used can be set via the ``tomcat.version`` property.
      
   
    
The containers that download and install a server (the \*-ci-managed profiles) allow you to override the version used, e.g.:

* `-Dpayara.version=5.0.0.174`

    This will change the version from the current one (e.g 5.0.0.172) to 5.0.0.173 for Payara testing purposes.

* `-Dglassfish.version=5.0`

    This will change the version from the current one (e.g 5.1.1) to 5.0 for GlassFish testing purposes.



**To run them in the console do**:

1. In the terminal, ``mvn test -fae`` at the top-level directory to start the tests for the default profile.

When developing and runing them from IDE, remember to activate the profile before running the test.

To learn more about Arquillian please refer to the [Arquillian Guides](http://arquillian.org/guides/)

**To run only a subset of the tests do at the top-level directory**:

1. Install top level dependencies: ``mvn clean install -pl "test-utils" -am``
1. cd into desired module, e.g.: ``cd cdi``
1. Run tests against desired server, e.g.: ``mvn clean test -P glassfish-ci-managed``


## How to contribute ##

With your help we can improve this set of samples, learn from each other and grow the community full of passionate people who care about the technology, innovation and code quality. Every contribution matters!

There is just a bunch of things you should keep in mind before sending a pull request, so we can easily get all the new things incorporated into the master branch.

Standard tests are jUnit based. Test classes naming must comply with surefire naming standards `**/*Test.java`, `**/*Test*.java` or `**/*TestCase.java`.

For the sake of clarity and consistency, and to minimize the upfront complexity, we prefer standard jUnit tests using Java, with as additional helpers HtmlUnit, Hamcrest and of course Arquillian. Please don't use alternatives for these technologies. If any new dependency has to be introduced into this project it should provide something that's not covered by these existing dependencies.


### Some coding principles ###

* When creating new source file do not put (or copy) any license header, as we use top-level license (MIT) for each and every file in this repository.
* Please follow JBoss Community code formatting profile as defined in the [jboss/ide-config](https://github.com/jboss/ide-config#readme) repository. The details are explained there, as well as configurations for Eclipse, IntelliJ and NetBeans.


### Small Git tips ###

* Make sure your [fork](https://help.github.com/articles/fork-a-repo) is always up-to-date. Simply run ``git pull upstream master`` and you are ready to hack.
* When developing new features please create a feature branch so that we incorporate your changes smoothly. It's also convenient for you as you could work on few things in parallel ;) In order to create a feature branch and switch to it in one swoop you can use ``git checkout -b my_new_cool_feature``

That's it! Welcome in the community!

## CI Job ##

CI jobs are executed by [Travis](https://travis-ci.org/javaee-samples/javaee8-samples). Note that by the very nature of the samples provided here it's perfectly normal that not all tests pass. This normally would indicate a bug in the server on which the samples are executed. If you think it's really the test that's faulty, then please submit an issue or provide a PR with a fix.


## Run each sample in Docker

* Install Docker client from http://boot2docker.io
* Build the sample that you want to run as
  
  ``mvn clean package -DskipTests``

  For example: (note the exact module doens't exist yet, wip here)

  ``mvn -f jaxrs/jaxrs-client/pom.xml clean package -DskipTests``

* Change the second line in ``Dockerfile`` to specify the location of the generated WAR file
* Run boot2docker and give the command

  ``docker build -it -p 80:8080 javaee8-sample``

* In a different shell, find out the IP address of the running container as:

  ``boot2docker ip``

* Access the sample as http://IP_ADDRESS:80/jaxrs-client/webresources/persons. The exact URL would differ based upon the sample.

