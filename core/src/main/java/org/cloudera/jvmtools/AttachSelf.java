// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

import java.net.URLDecoder;

import org.gridkit.util.monitoring.Util;

import com.sun.tools.attach.VirtualMachine;

/** 
 * Attaches the jar represented by the jar whence this class came from as an agent to
 * the given pid.
 */
public class AttachSelf {
  public static void main(String[] args) throws Exception {
    String pid = args[0];
    Util.addToolsToClasspath();
    VirtualMachine jvm = Util.attachToPid(pid);
    
    // Gets the current path
    String path = AttachSelf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    
    String agentArg = args.length <= 1 ? null : args[1];
    jvm.loadAgent(decodedPath, agentArg);
  }
}
