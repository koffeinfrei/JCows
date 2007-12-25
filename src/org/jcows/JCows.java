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
package org.jcows;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;
/**
 * Main class of the JCows application. This class allows to run the application
 * directly from the distributed Jar file without extending Java library and class
 * path. The main method starts a new Java process (new virtual machine) with the 
 * correct paths to include all necessary external libraries.
 * The new process creates a instance of the {@link JCowsApp JCowsApp} class.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class JCows
{
  private static final Logger LOGGER=Logger.getLogger(JCows.class);
  /*
   * Library path where the Java compiler classes are stored.
   */
  private static final String LIBRARY_TOOLS="lib"+File.separator+"tools.jar";
  /**
   * This method creates a new process that starts the JCows application.
   * 
   * @param args command-line arguments.
   * @throws Exception if new process creation failed.
   */
  public static void main(String[] args)
  {
    try {
      preInit();

      JCowsApp app=null;

      /*
       * Start JCows by creating a new instance of
       * JCowsApp instead of creating a new process.
       * This allows debugging in a development IDE
       * because no new process is created.
       */
      if(args.length>=1 && args[0].equals("-dev"))
        app=new JCowsApp(false);
      else {
        /*
         * Create a process builder to start JCows
         * with the correct class and library path.
         */
        ProcessBuilder builder=new ProcessBuilder();
        builder.redirectErrorStream(true);    
        /*
         * Start JCows main application.
         */
        System.out.println("Starting JCows...");
        int processStatus=Integer.MIN_VALUE;
        while(processStatus!=0 && processStatus!=-1) {
          builder.command(getJavaParams());
          Process process=builder.start();
          readProcessStreams(process);
          processStatus=process.waitFor();
        }      
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Returns a list of Java parameters. The list also contains
   * the path to the Java development kit.
   * 
   * @return list of Java parameters.
   * @throws JCowsException.
   */
  private static List<String> getJavaParams() throws JCowsException {
    /*
     * Save current class and library path and extend it.
     */
    List<String> params;
    String classpath;
    String librarypath;
    String currentclasspath=System.getProperty("java.class.path");
    String currentlibrarypath=System.getProperty("java.library.path");
    /*
     * Every init loads the config files new. This is needed if some values
     * have changed.
     */
    Properties.init();
    /*
     * Get JDK path if already saved before.
     */
    String currentjdkpath=Properties.getConfig(Properties.KEY_JDK_PATH);
    File file=null;

    if(currentjdkpath!=null && !currentjdkpath.equals(""))
      file=new File(currentjdkpath+File.separator+LIBRARY_TOOLS);

    if(currentclasspath==null || currentclasspath.equals(""))
      classpath=".";
    else
      classpath=currentclasspath+File.pathSeparator+".";

    if(file!=null && file.exists())
      classpath=classpath+File.pathSeparator+file.getAbsolutePath();

    if(currentlibrarypath==null || currentlibrarypath.equals(""))
      librarypath=".";
    else
      librarypath=currentlibrarypath+File.pathSeparator+".";

    /*
     * Add values to a parameter list.
     */
    params=new ArrayList<String>();
    params.add("java");
    params.add("-Djava.library.path="+librarypath);
    params.add("-cp");
    params.add(classpath);
    params.add("org.jcows.JCowsApp");
    return params;
  }
  /**
   * Reads the output and error stream a process and prints the on
   * the current output stream.
   * 
   * @param process process to read input and error stream from.
   * @throws IOException if an I/O error occurs.
   */
  private static void readProcessStreams(Process process) throws JCowsException {
    /*
     * Caution! InputStream is = process.getErrorStream();
     * has blocking behaviour on the new process. 
     */
    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    String line;
    try {
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }
    catch(IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"),e);
    }
  }
  /**
   * Initializes JCows environment. This method must be executed before a new
   * instance of Cows</code> is created.
   * 
   * @throws JCowsException.
   */
  private static void preInit() throws JCowsException {
    /*
     * Extract needed program files.
     */
    JCowsHelper.getFile("config.properties",false);
    JCowsHelper.getFile("messages.properties",false);
    /*
     * Initialize log and property settings.
     * PropertyConfigurator is responsible for the Log4j behaviour.
     */
    Properties.init();
    PropertyConfigurator.configureAndWatch("config.properties");
  }
}
