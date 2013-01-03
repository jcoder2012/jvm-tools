// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

public class Noop {

  public void foo() throws InterruptedException {
    Thread.sleep(50000);
  }
}
