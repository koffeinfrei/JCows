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
package org.jcows.view.core;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * About dialog. This dialog shows application information (version number, release date etc.)
 * about the application.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 184 $, $LastChangedDate: 2006-11-01 10:51:04 +0000 (Wed, 01 Nov 2006) $
 */
public class DialogAbout extends org.eclipse.swt.widgets.Dialog
{
  
  //private static final Logger LOGGER = Logger.getLogger(DialogAbout.class);

  private Shell dialogShell;
  private Label labelAbout;
  private Label imageLabel;
  private Image titleImage;

  /** 
   * Constructs a new instance of this class given its parent and 
   * a style value describing its behaviour and appearance.
   * 
   * @param parent a composite control which will be the parent of the new instance (cannot be null).
   * @param style the style of control to construct.
   */
  public DialogAbout(Shell parent, int style)
  {
    super(parent,style);
    
    dialogShell = new Shell(parent,SWT.APPLICATION_MODAL);
    dialogShell.setSize(600,300);
    
    Display display = dialogShell.getDisplay();
    RowLayout layout=new RowLayout(SWT.VERTICAL);
    layout.justify=true;
    dialogShell.setLayout(layout);
    
    titleImage = SWTResourceManager.getImage("resources/logo.png");
    dialogShell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    
    imageLabel = new Label(dialogShell, SWT.NONE);
    imageLabel.setImage(titleImage);
    
    labelAbout=new Label(dialogShell,SWT.NONE);
    labelAbout.setText(Properties.getConfig("about"));
    labelAbout.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    
    Monitor primary = display.getPrimaryMonitor();
    Rectangle bounds = primary.getBounds();
    Rectangle rect = dialogShell.getBounds();
    int x = bounds.x + (bounds.width - rect.width) / 2;
    int y = bounds.y + (bounds.height - rect.height) / 2;
    
    dialogShell.setLocation(x,y);
  }
  
  /** 
   * Sets the dialog visible or invisible.
   * 
   * @param visible true, if visible, otherwise false.
   */
  public void setVisible(boolean visible) {
    dialogShell.setVisible(visible);
  }
  
  /**
   * Attaches a mouse listener for the Close button of the about dialog.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogAboutCloseListener(MouseListener listener) {
    dialogShell.addMouseListener(listener);
    imageLabel.addMouseListener(listener);
  }

}
