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


import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * Splash screen showed at startup.
 * This splash screen is showed at program start.
 * It shows the JCows logo and some additional information.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 184 $, $LastChangedDate: 2006-11-01 10:51:04 +0000 (Wed, 01 Nov 2006) $
 */
public class DialogSplash extends org.eclipse.swt.widgets.Dialog
{
  
  private static final Logger LOGGER = Logger.getLogger(DialogSplash.class);

  private Shell dialogShell;
  private Label labelVersion;
  private Label imageLabel;
  private Image titleImage;
  private ProgressBar progressBar;
  private Label labelStatus;

  /** 
   * Constructs a new instance of this class given its parent and 
   * a style value describing its behaviour and appearance.
   * 
   * @param parent a composite control which will be the parent of the new instance (cannot be null).
   * @param style the style of control to construct.
   */
  public DialogSplash(Shell parent, int style)
  {
    super(parent,style);
    
    dialogShell = new Shell(parent,SWT.APPLICATION_MODAL);
    dialogShell.setSize(600,300);
    
    Display display = dialogShell.getDisplay();
    RowLayout layout=new RowLayout(SWT.VERTICAL);
    dialogShell.setLayout(layout);
    
    titleImage = SWTResourceManager.getImage("resources/logo.png");
    dialogShell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    
    imageLabel = new Label(dialogShell, SWT.NONE);
    imageLabel.setImage(titleImage);
    
    labelVersion = new Label(dialogShell, SWT.NONE);
    labelVersion.setText("Version "+Properties.getConfig("version"));
    labelVersion.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

    labelStatus=new Label(dialogShell,SWT.NONE);
    labelStatus.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    
    progressBar=new ProgressBar(dialogShell,SWT.SMOOTH);
    progressBar.setBackground(SWTResourceManager.getColor(255,255,255));
    progressBar.setForeground(SWTResourceManager.getColor(254, 162, 57));
    RowData progressBarLData = new RowData();
    progressBarLData.width = 590;
    progressBarLData.height = 16;
    progressBar.setLayoutData(progressBarLData);
    
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
   * Returns the progress bar.
   * 
   * @return the progress bar.
   */
  public ProgressBar getProgressBar() {
    return progressBar;
  }
  
  /**
   * Sets the status text.
   * 
   * @param text the status text.
   */
  public void setStatusText(String text) {
    labelStatus.setText(text);
    Point size=labelStatus.computeSize(SWT.DEFAULT,SWT.DEFAULT);
    labelStatus.setSize(size);
    dialogShell.layout();
  }
  
  /**
   * Returns the status text.
   * 
   * @return the status text.
   */
  public String getStatusText() {
    return labelStatus.getText();
  }
  
}
