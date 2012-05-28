// Copyright (c) 2012 Cloudera, Inc. All rights reserved.
package org.gridkit.util.monitoring;

import sun.jvm.hotspot.bugspot.BugSpotAgent;
import sun.jvm.hotspot.runtime.JavaThread;
import sun.jvm.hotspot.runtime.Threads;
import sun.jvm.hotspot.runtime.VM;

/**
 * See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7050524
 * for an "awesome" story of how this won't work for Ubuntu.  This
 * is also why "jstack -F" won't work on Ubuntu.
 */
public class ThreadNativeIds {
  public static void main(String[] args) throws Exception {
    String pid = args[0];
    Util.addToolsToClasspath();
    Util.addSaJdiToClasspath();
    
    BugSpotAgent agent = new BugSpotAgent();
    agent.attach(Integer.parseInt(pid));
    
    Threads threads = VM.getVM().getThreads();
    // C++ batman!
    for (JavaThread thread = threads.first(); thread != null; thread = thread.next()) {
      String name = thread.getThreadName();
      String nid = thread.getThreadProxy().toString();
      System.out.println(nid + " " + name);
    }
  }
}
