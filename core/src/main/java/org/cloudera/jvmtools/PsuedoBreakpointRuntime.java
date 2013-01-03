// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

public class PsuedoBreakpointRuntime {
  public static void handle() {
    System.err.println("foo");
  }
  
  public static void dox() {
    handle();
  }
}