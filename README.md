jvm-tools
=========

Small tools for JVM monitoring, profiling and tuning.

Build
-----

    $ mvn install

Development
-----------

If developing in Eclipse, you'll need to add the
'tools' jar manually to your build path.
(TODO: make mvn eclipse:eclipse do it magically?)

Usage
-----

    $java -jar jtool/target/jtool-0.1-SNAPSHOT.jar 
    Usage: 
      jmx <pid>
      top <pid>
      gc <pid>
      ps
      stack <pid>
      threadnids <pid>


top -- This tool will monitor java application threads and dump CPU usage per thread periodically.

gc -- Generate log of GC events on remote JVM

