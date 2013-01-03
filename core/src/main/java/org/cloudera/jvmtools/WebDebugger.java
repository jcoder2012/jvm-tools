// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 */
public class WebDebugger implements HttpHandler {
  public WebDebugger(Instrumentation inst) {
  }
  
  public void go() throws IOException {
    startWebPort();
  }

  private void startWebPort() throws IOException {
    // We're already using internal Sun/Oracle APIs, so may
    // as well use their web server.
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0 /* default backlog */);
    server.createContext("/", this);
    // do we need to set a thread pool?
    server.start();
  }

  @Override
  public void handle(HttpExchange ex) throws IOException {
    if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
      System.err.println(ex.getRequestURI());
      System.err.println(ex.getRequestURI().getPath());
      
      if (ex.getRequestURI().getPath().equals("/break")) {
        
        
      }
      String response = "This is the response";
      ex.sendResponseHeaders(200, response.length());
      OutputStream os = ex.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

}
