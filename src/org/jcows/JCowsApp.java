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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.jcows.controller.JCowsController;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * The <code>JCowsApp</code> class creates a SWT window for the application.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class JCowsApp {

  private static final Logger LOGGER = Logger.getLogger(JCowsApp.class);
  
  /*
   * Disable splashscreen at the moment.
   */
  private final int SPLASHSCREEN_SLEEP_TIME=0;
  
  private JCowsController m_controller;
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param preload preload classes stored in the distributed Jar file.
   * @throws Exception
   */
  public JCowsApp(boolean preload) throws JCowsException {
    /**
     * Initializes the environment.
     */
    preInit();
    
    Display display=new Display();
    Shell shell = new Shell(display);
    /*
     * Set MVC (Modell-Controller-View) classes.
     */
    m_controller=new JCowsController(shell);

    if(preload)
      preloadClasses();
    /*
     * Center the SWT windows on the screen.
     */
    Point size = JCowsController.m_mainWindowController.m_mainWindow.getSize();
    shell.setText(Properties.getConfig("title"));
    shell.setImage(SWTResourceManager.getImage("resources/jcows.png"));
    shell.setLayout(new FillLayout());
    shell.layout();
    if(size.x == 0 && size.y == 0) {
      JCowsController.m_mainWindowController.m_mainWindow.pack();
      shell.pack();
    }
    else {
      Rectangle shellBounds = shell.computeTrim(0,0,size.x,size.y);
      shell.setSize(shellBounds.width,shellBounds.height);

      Monitor primary = display.getPrimaryMonitor();
      Rectangle bounds = primary.getBounds();
      Rectangle rect = shell.getBounds();
      int x = bounds.x + (bounds.width - rect.width) / 2;
      int y = bounds.y + (bounds.height - rect.height) / 2;
      shell.setLocation(x,y);
    }
    shell.open();
    
    postInit();
    /*
     * Do endless loop until main window is closed.
     */
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }
  /**
   * Starts the JCows application. 
   * 
   * @param args command-line arguments.
   */
  public static void main(String[] args) {
    System.out.println("CLASSPATH     : "+System.getProperty("java.class.path"));
    System.out.println("LIBRARY_PATH  : "+System.getProperty("java.library.path"));
    try {
      /*
       * If this main method is called, class preloading should
       * be enabled.
       */
      JCowsApp app=new JCowsApp(true);
    }
    catch(JCowsException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Initializes JCows environment. This method must be executed before a new
   * instance of <code>JCowsApp</code> is created.
   * 
   * @throws JCowsException.
   */
  private void preInit() throws JCowsException { 
    /*
     * Extract needed program files.
     */
    JCowsHelper.getFile("config.properties",false);
    JCowsHelper.getFile("messages.properties",false);
    /*
     * Init properties and log settings.
     * PropertyConfigurator is responsible for the Log4J behaviour.
     */
    Properties.init();
    PropertyConfigurator.configureAndWatch("config.properties");
    /*
     * Check operating system and extract needed
     * libraries to run SWT.
     */
    JCowsHelper.preprocessSWT();
    /*
     * Create output directory for compiled classes.
     */
    File out_dir = new File(Properties.getConfig("parser.outputDirName"));
    if(!out_dir.exists())
      out_dir.mkdir();
  }
  /**
   * Post-Initializes JCows environment. This method must be executed <strong>after</strong> a new
   * instance of <code>JCowsApp</code> is created.
   */
  private void postInit() {
    String librarytool="";
    String[] values=System.getProperty("java.class.path").split(File.pathSeparator);
    for(String value:values) {
      if(value.endsWith("tools.jar"))
        librarytool=value;
    }
    File file=new File(librarytool);
    if(!file.exists()) {
      JCowsController.m_dialogJDKController.m_dialogJDK.setVisible(true);
    }
  }
  /**
   * Pre-loads all classes if the application is started from the distributed
   * Jar file. Pre-loading increases the application performance because all
   * stored classes in the Jar file are initialized at start up.
   * 
   * @throws Exception
   */
  private void preloadClasses() throws JCowsException {
    ProgressBar progressbar=JCowsController.m_dialogSplash.getProgressBar();
    int position=0;
    /*
     * Pre-Load classes
     */
    String classJarName=this.getClass().getName();
    /*
     * Replace package separator with application independent
     * path separator and add Java class postfix.
     */
    classJarName=classJarName.replace(".","/")+".class";
    
    URL classURL=Thread.currentThread().getContextClassLoader().getResource(classJarName);
    /*
     * Only for test purpose.
     */
    //classURL=new URL("file:/D:/Diplomarbeit/Java/jcows/dist/win32/jcows_0.2_win32.jar!/org/jcows/JCowsApp.class");
    if(classURL==null || !classURL.getFile().contains("!/"))
      return;
    
    String jarURL=classURL.getFile().replaceAll("!/.*","");
    
    LOGGER.debug("Pre-loading classes from "+jarURL+"...");
    
    URL anyURL=null;
    try {
      anyURL=new URL(jarURL);
    }
    catch(MalformedURLException e){
      throw new JCowsException(Properties.getMessage("error.MalformedURLException"),e);
    }
    
    List<String> classes=JCowsHelper.preloadClassList(anyURL);
    
    /*
     * Reset progress bar.
     */
    progressbar.setMaximum(classes.size());
    JCowsController.m_dialogSplash.setVisible(true);
    for(String clazz:classes) {
      try {
        Class.forName(clazz);
        progressbar.setSelection(++position);
        JCowsController.m_dialogSplash.setStatusText("Pre-Loading Class "+clazz);
        try {
          /*
           * Do a small nap. Otherwise, splash screen would disappear
           * to fast.
           */
          Thread.sleep(SPLASHSCREEN_SLEEP_TIME);
        }
        catch(InterruptedException e) {
          throw new JCowsException(Properties.getMessage("error.InterruptedException"),e);
        }
      }
      catch(ClassNotFoundException e) {
        throw new JCowsException(Properties.getMessage("error.ClassNotFoundException"),e);
      }
    }
    JCowsController.m_dialogSplash.setVisible(false); 
  }
  
} 
