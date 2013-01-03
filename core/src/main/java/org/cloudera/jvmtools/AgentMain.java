// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

import java.io.IOException;
import java.lang.instrument.Instrumentation; import java.lang.instrument.UnmodifiableClassException;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class AgentMain {
  
  private Instrumentation inst;

  public AgentMain(Instrumentation inst) {
    this.inst = inst;
  }

  public static void premain(String agentArgs, Instrumentation inst) throws Exception {
    System.err.println("Agent premain()...");
    new AgentMain(inst).dispatch(agentArgs);
  }
  
  public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
    System.err.println("Agent agentmain()...");
    new AgentMain(inst).dispatch(agentArgs);
  }

  private void dispatch(String agentArgs) throws Exception {
    System.err.println(agentArgs);
    if (agentArgs.equals("loaded_classes")) {
      printAllLoadedClasses();
    } else if (agentArgs.startsWith("break:")) {
      breakPoint(agentArgs.substring("break:".length()));
    } else if (agentArgs.startsWith("webdbg")) {
      webDebugger(agentArgs.substring("webdbg".length()));
    }
  }

  private void webDebugger(String x) throws IOException {
    new WebDebugger(inst).go();
  }

  private void breakPoint(String className) throws UnmodifiableClassException, ClassNotFoundException {
    new PseudoBreakpoint().setBreakpoint(inst, className);
  }

  /** Prints loaded classes, and whence. */
  private void printAllLoadedClasses() {
    Class[] loadedClasses = inst.getAllLoadedClasses();
    System.err.println("Loaded classes: " + loadedClasses.length);
    for (Class<?> c : inst.getAllLoadedClasses()) {
      System.err.print(c.getName());
      System.err.print(" ");
      ProtectionDomain protDomain = c.getProtectionDomain();
      boolean printed = false;
      if (protDomain != null) {
        CodeSource codeSource = protDomain.getCodeSource();
        if (codeSource != null) {
          System.out.println(codeSource.getLocation());
          printed = true;
        } 
      }
      if (!printed) {
        System.out.println("null");
      }
    }
  }
}
