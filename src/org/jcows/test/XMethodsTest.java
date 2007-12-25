/*
 * Copyright 2006 Project JCows.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jcows.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.tools.ant.BuildException;
import org.jcows.JCowsException;
import org.jcows.model.core.SoapLogic;
import org.jcows.system.Properties;

/** 
 * This is the main test class of the JCows application. It is used for testing a list
 * of WSDL URLs, whereas it tries each operation on the service. 
 * All successful and failed test runs
 * are logged and summarized at the end of the testing procedure.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 282 $, $LastChangedDate: 2006-11-20 08:50:06 +0000 (Mon, 20 Nov 2006) $
 */
public class XMethodsTest {
  private int success;
  private int error;
  private int exception;
  private int invalidWsdl;
  private int noWS;
  private int networkError;
  private int errCompile;
  private int errInvoke;
  private int errOther;
  private PrintWriter out;
  private PrintWriter err;
  private PrintWriter err_parse;
  private PrintWriter err_compile;
  private PrintWriter err_invoke;
  private PrintWriter err_other;
  private PrintWriter err_noWS;
  private PrintWriter exc;
  private static final String OUTPUT_DIR = "output";
  private static final String OUT_FILE = OUTPUT_DIR + "/jcowstest.out";
  private static final String ERR_FILE = OUTPUT_DIR + "/jcowstest.err";
  private static final String EXC_FILE = OUTPUT_DIR + "/jcowstest_exceptions.err";
  private static final String ERR_PARSE_FILE = OUTPUT_DIR + "/jcowstest_parse.err";
  private static final String ERR_COMPILE_FILE = OUTPUT_DIR + "/jcowstest_compile.err";
  private static final String ERR_INVOKE_FILE = OUTPUT_DIR + "/jcowstest_invoke.err";
  private static final String ERR_OTHER_FILE = OUTPUT_DIR + "/jcowstest_other.err";
  private static final String ERR_NOWS_FILE = OUTPUT_DIR + "/jcowstest_noWS.err";
  
  /**
   * Constructs a new instance.
   * 
   * @throws IOException
   */
  public XMethodsTest() throws IOException{
    // test if output dir exists
    File out_dir = new File(OUTPUT_DIR);
    if (!out_dir.exists()) out_dir.mkdirs();
      
    out = new PrintWriter(new FileWriter(OUT_FILE), true);
    err = new PrintWriter(new FileWriter(ERR_FILE), true);
    err_parse = new PrintWriter(new FileWriter(ERR_PARSE_FILE), true);
    err_compile = new PrintWriter(new FileWriter(ERR_COMPILE_FILE), true);
    err_invoke = new PrintWriter(new FileWriter(ERR_INVOKE_FILE), true);
    err_other = new PrintWriter(new FileWriter(ERR_OTHER_FILE), true);
    err_noWS = new PrintWriter(new FileWriter(ERR_NOWS_FILE), true);
    exc = new PrintWriter(new FileWriter(EXC_FILE), true);
    
    success = 0;
    error = 0;
    exception = 0;
    invalidWsdl = 0;
    networkError = 0;
    noWS = 0;
    errCompile = 0;
    errInvoke = 0;
    errOther = 0;
  }
  
  /**
   * This is the main method that starts the JCows test.
   * 
   * @param args Not used.
   */
  public static void main(String[] args) {    
    try {
      PropertyConfigurator.configureAndWatch("config.properties");
      Properties.init();
    }
    catch (JCowsException e1) {
      System.err.println("ERROR: Could not initialize JCows Properties");
    }
    
    XMethodsTest test = null;
    
    try {
      test = new XMethodsTest();
      XMethods xmethods = new XMethods();
      //xmethods.enableServerTimeout(false);
      String[] wsdlUrls = xmethods.getWsdlUrlsFromFile();
//      int limit = 3;
      long startTime = System.currentTimeMillis();
      for (String wsdlUrl : wsdlUrls){
          test.out.println("------------ " + wsdlUrl);
          test.invoke(wsdlUrl);
//          if (limit < (test.success + test.error)) break;
      }
      
      // compute and format execution time
      long millis = System.currentTimeMillis() - startTime;
      int millisPerMinute = 1000 * 60;
      int millisPerHour = millisPerMinute * 60;
      int hours = (int)(millis / millisPerHour);
      int minutes = (int)((millis - hours*millisPerHour) / millisPerMinute);
      int seconds = (int)((millis - hours*millisPerHour - minutes*millisPerMinute) / 1000);
      String durationStr = 
        ((hours < 10)? "0": "") + hours + "h " + 
        ((minutes < 10)? "0": "") + minutes + "m " + 
        ((seconds < 10)? "0": "") + seconds + "s";
      
      /* format the result output */
      String resultOutput = 
          "\n---------------------------------------------------------"
        + "\nSuccessfuly finished test"
        + "\n\nTested " + (test.success + test.error) + " Web Services"
        + "\nDuration: " +  durationStr
        + "\n\nSuccessful:            " + test.success 
        + "\nFailed:                " + test.error
        + "\n-----------------------"
        + "\nInvalid Web Services:  " + test.noWS
        + "\nInvalid Wsdl:          " + test.invalidWsdl
        + "\nNetwork/IO Errors:     " + test.networkError
        + "\nCompile Errors:        " + test.errCompile
        + "\nInvocation Errors:     " + test.errInvoke
        + "\nOther Errors:          " + test.errOther
        + "\nExceptions:            " + test.exception
        + "\n\n" 
        + (100.0 / (test.success + test.error) * test.success) + " % Successful"
        + "\n" 
        + (100.0 / (test.success + test.error - test.networkError) * test.success) 
        + " % Successful (Excluding network errors)"
        + "\n" 
        + (100.0 / (test.success + test.error - test.networkError - test.noWS) 
            * test.success) 
          + " % Successful (Excluding network errors / invalid web services)"
        + "\n"
        + (100.0 / 
          (test.success + test.error - test.invalidWsdl - test.networkError - test.noWS) 
          * test.success) 
        + " % Successful (Excluding network errors / invalid web services / invalid wsdl's)"
        + "\n---------------------------------------------------------";
        
        test.out.println(resultOutput);
        System.out.println(resultOutput);
    }
    catch (FileNotFoundException e) {
      System.err.println("ERROR: File could not be found " + e.getMessage());
    }
    catch (IOException e) {
      System.err.println("ERROR: IOException: " + e.getMessage());
    }
    catch (Exception e){
      System.err.println("ERROR: Exception: " + e.getMessage());
    }
    finally{
      if (test.out != null) test.out.close();
      if (test.err != null) test.err.close();
      if (test.exc != null) test.exc.close();
      if (test.err_parse != null) test.err_parse.close();
      if (test.err_compile != null) test.err_compile.close();
      if (test.err_invoke != null) test.err_invoke.close();
      if (test.err_other != null) test.err_other.close();
      if (test.err_noWS != null) test.err_noWS.close();
    }
  }
  
  /**
   * Invokes the JCows application with the specified 
   * WSDL URL and tests if all services, ports and
   * operations can be invoked successfully.<br/> 
   * It increments accordingly the counters of the successful and
   * failed runs. In case of an error it classifies the error and
   * writes the error information to the corresponding log file.
   * 
   * @param wsdl the WSDL URL.
   */
  private void invoke(String wsdl){
    System.out.println("Free Memory --> " + Runtime.getRuntime().freeMemory());
    try {
      SoapLogic soapLogic = new SoapLogic(wsdl);
      String[] services = soapLogic.getServices();
      for (String service: services){
        out.println(service);
        soapLogic.setCurrentService(service);
        String[] ports = soapLogic.getPorts();
        for (String port: ports){
          out.println("  " + port);
          soapLogic.setCurrentPort(port);
          String[] operations = soapLogic.getOperations();
          for (String operation: operations){
            out.println("    " + operation);
            soapLogic.setCurrentOperation(operation);
          }
        }
      }
      
      ++success;
    }
    catch (JCowsException e) {
      ++error;
      out.println("***********\n" + e.getMessage());
      
      // extract the ones that failed due to invalid wsdl
      String msg = e.getMessage();    
      
      /* dispatch errors */
      
      // network/io error
      if (e.getNestedException() instanceof SocketTimeoutException
          || e.getNestedException() instanceof IOException){ 
        ++networkError;
      }
      // not a WSDL file
      else if (msg.startsWith(Properties.getMessage("error.notAWSDLDocument"))){
        err_noWS.println(wsdl);
        ++noWS;
      }
      // invalid WSDL (contains errors/not well-formed)
      else if (msg.startsWith(Properties.getMessage("error.wsdl2javaParse"))){ 
        err_parse.println(wsdl + "\n" + msg + "\n\n");
        ++invalidWsdl;
      }
      // Java compiler error
      else if (e.getNestedException() instanceof BuildException){
        err_compile.println(wsdl + "\n" + msg + "\n\n");
        ++errCompile;
      }
      // invocation error
      else if (e.getNestedException() instanceof InvocationTargetException){
        err_invoke.println(wsdl + "\n" + msg + "\n\n");
        ++errInvoke;
      }
      else{
        err_other.println(wsdl + "\n" + msg + "\n\n");
        ++errOther;
      }
      
      // log error
      err.println(wsdl + "\n" + msg + "\n\n");
      
      return;
    }
    // Those are exception that must be fixed in future releases 
    catch (Exception e){
      ++error;
      ++exception;
      exc.println(wsdl + "\n" + e.getMessage() + "\n");
      return;
    }
  }

}
