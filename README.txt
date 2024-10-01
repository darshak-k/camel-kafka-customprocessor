*********************************************************************************************
* Smile CDR Camel Demo Project
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

This project is an example project demonstrating how to create a Smile CDR
Camel JAR. It is designed so that you can use it as a guide if you like,
but you can also simply use it as a starter for your project and begin
building directly in this project.

*********************************************************************************************
* Contents
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

This project is laid out as a standard Maven project. All source files are
found in the "src/main/java" directory, and the sources contain the following
classes:

com.example.camel.TestCustomProcessor  - Custom Apache Camel Processor
com.example.camel.TestCustomAppCtx     - Custom Spring Context

*********************************************************************************************
* Deploying
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

This section contains instructions on how to deploy this project to a
Smile CDR installation as a Camel module. Note that these instructions
assume that your installation is in /opt/smilecdr. Adjust as necessary.

1. Build your Camel project using Maven

      mvn install

2. When the build completes successfully, you will have a JAR file in
   your Maven target/ directory. With Smile CDR stopped, Copy this file
   into your Smile CDR customerlib/ directory.

      cp target/cdr-camel-demoproject-1.0.jar /opt/smilecdr/customerlib/

3. Start Smile CDR

      cd /opt/smilecdr
      bin/smilecdr start

4. In the Smile CDR Web Admin Console, enter the module config section and
   create a new module of type "Camel" with the configuration below:

        - Module ID: A sensible identifier
        - Camel Routes (Text): YAML routes using text, OR
        - Camel Routes (File): Path to a .yaml file that contains YAML routes
        - Spring Context Config Class: The fully qualified class name of the
          Spring Configuration class:     com.example.camel.TestCustomAppCtx

        A Sample Route is shown below:

             - from:
                  uri: "timer:test"
                  steps:
                    - to:
                        uri: "bean:testCustomProcessor"

             Description: Uses the Timer component to create a Message every
             second with an empty body. This Message is then passed to the
             TestCustomProcessor which calls the process(Exchange) method.
             This method simply logs the incoming Message body and headers,
             modifies them, and then logs them again.

5. Click "Save", and then start the module.
