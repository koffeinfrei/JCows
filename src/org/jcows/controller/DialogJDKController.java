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
package org.jcows.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.jcows.JCowsException;
import org.jcows.system.Properties;
import org.jcows.view.core.DialogJDK;
/**
 * The <code>DialogJDKController</code> controls the dialog where the path to
 * the Java Development Kit(JDK) can be entered. This path is necessary to find
 * the Jar file that contains the Java compiler. The Java Compiler is needed by AXIS
 * to compile the stub classes.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
 */
public class DialogJDKController extends JCowsController {
  
  private static final Logger LOGGER = Logger.getLogger(MainWindowController.class);
  
  public DialogJDK m_dialogJDK;

  /**
   * Constructs a new instance of this class.
   * 
   * @throws JCowsException
   */
  public DialogJDKController(){
    m_dialogJDK = new DialogJDK(m_shell,SWT.NONE);
    /*
     * Restore settings from config.properties.
     */
    String JDKPath=Properties.getConfig(Properties.KEY_JDK_PATH);
    if(JDKPath!=null)
      m_dialogJDK.setTextJDKPath(JDKPath);
    attachViewListenersDialogJDK();
  }
  
  /**
   * Attaches the listeners for the JDK dialog.
   */
  public void attachViewListenersDialogJDK() {
    m_dialogJDK.addDialogJDKOKListener(getDialogJDKOKListener());
    m_dialogJDK.addDialogJDKCancelListener(getDialogJDKCancelListener());
    m_dialogJDK.addDialogJDKBrowseListener(getDialogJDKBrowseListener());
    m_dialogJDK.addDialogJDKPathEnterListener(getDialogJDKPathEnterListener());
  }
  
  /** 
   * Listener for the OK button of the JDK dialog.
   * Saves the JDK path in the configuration file.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogJDKOKListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      String JDKPath=m_dialogJDK.getTextJDKPath();
      if(JDKPath.contains("\\"))
        JDKPath=JDKPath.replace("\\","\\\\");
      Properties.setConfig(Properties.KEY_JDK_PATH,JDKPath);
      /*
       * Special exit code to start a new process with correct
       * library and class path.
       */
      System.exit(-2);
    }
  }
  
  /** 
   * Listener for the Cancel button of the JDK dialog.
   * Closes the JDK dialog and exits the application because the path is necessary.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogJDKCancelListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_dialogJDK.setVisible(false);
    }
  }
  
  /** 
   * Listener for the Browse button of the JDK dialog.
   * Shows a file dialog to choose the JDK path.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogJDKBrowseListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      DirectoryDialog dialog = new DirectoryDialog(
          m_mainWindowController.m_mainWindow.getShell());
      String filename = dialog.open();
      if (filename != null){
        m_dialogJDK.setTextJDKPath(filename);
      }
    }
  }
  
  /** 
   * Key Listener for the JDK path text box of the JDK dialog.
   * Saves the JDK path in the configuration file.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogJDKPathEnterListener implements KeyListener {
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent evt) {
      LOGGER.debug(evt);
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent evt) {
      LOGGER.debug(evt);
      /* do nothing */
    }
  }
  
  /**
   * Returns a listener for the JDK path OK button of the JDK dialog.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getDialogJDKOKListener() {
    return new DialogJDKOKListener();
  }
  
  /**
   * Returns a listener for the JDK Cancel button of the JDK dialog.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getDialogJDKCancelListener() {
    return new DialogJDKCancelListener();
  }
  
  /**
   * Returns a listener for the URL dialog that intercepts
   * the ENTER key pressed in the URL text box.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getDialogJDKBrowseListener() {
    return new DialogJDKBrowseListener();
  }
  
  /**
   * Returns a key listener for the JDK dialog that intercepts
   * the ENTER key pressed in the JDK path text box.
   * 
   * @return a key listener.
   */  
  private KeyListener getDialogJDKPathEnterListener() {
    return new DialogJDKPathEnterListener();
  }

}
