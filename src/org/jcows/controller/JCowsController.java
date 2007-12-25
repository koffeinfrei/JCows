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

import java.io.File;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Shell;
import org.jcows.JCowsException;
import org.jcows.model.core.IGuiLogic;
import org.jcows.model.core.ISoapLogic;
import org.jcows.view.core.DialogSplash;

/** 
 * The <code>JCowsController</code> is the main controller of the application. 
 * It implements the MVC-Pattern (Model-View-Controller).<br/> 
 * In fact it is not a real controller, as it does not connect a model to a view.
 * It merely initializes all controllers that are needed for the application, 
 * i.e. initializes for each GUI Component its corresponding controller.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
 */
public class JCowsController {
  //private static final Logger LOGGER = Logger.getLogger(JCowsController.class);

  protected static Shell m_shell;
  
  /*
   * All subcontrollers
   */
  public static MainWindowController m_mainWindowController;
  public static DialogJDKController m_dialogJDKController;
  public static DialogSSLController m_dialogSSLController;
  public static DialogErrorController m_dialogErrorController;
  public static DialogAboutController m_dialogAboutController;
  
  /*
   * Dialogs without own controller
   */
  public static DialogSplash m_dialogSplash;
  public static Clipboard m_clipboard;
  
  /*
   * Other common fields
   */
  protected static JCowsController _instance;
  protected static ISoapLogic m_soapLogic;
  protected static IGuiLogic m_guiLogic;
  protected static Vector<File> m_attachments;
  protected static String m_current_URL;
  
  /**
   * Constructs a new instance of this class.<br/>
   * This constructor is called from the subclassing controller classes.
   */
  public JCowsController(){}
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param shell The {@link org.eclipse.swt.widgets.Shell Shell} of the parent window.
   * @throws JCowsException
   */
  public JCowsController(Shell shell) throws JCowsException {
    m_shell = shell;
    
    /* only invoke if not called from childclass */
    if (_instance == null){
      _instance = this;
      m_mainWindowController = new MainWindowController();
      m_dialogJDKController = new DialogJDKController();
      m_dialogSSLController = new DialogSSLController();
      m_dialogErrorController = new DialogErrorController();
      m_dialogAboutController = new DialogAboutController();
      
      m_dialogSplash = new DialogSplash(m_shell,SWT.NONE);
      m_clipboard=new Clipboard(m_mainWindowController.m_mainWindow.getDisplay());
      
      m_attachments=new Vector<File>();
    }
  }
}
