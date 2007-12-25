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
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * Dialog to enter the Java Development Kit (JDK) path.
 * This dialog is to enter a path to find the JDK related
 * libraries.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 184 $, $LastChangedDate: 2006-11-01 11:51:04 +0100 (Mi, 01 Nov 2006) $
 *
 */
public class DialogJDK extends org.eclipse.swt.widgets.Dialog {
  
  //private static final Logger LOGGER = Logger.getLogger(DialogJDK.class);

  private Shell dialogShell;
  private Button buttonCancel;
  private Composite compositeImage;
  private Label label4;
  private Composite compositeInfoText;
  private Label label3;
  private Label label2;
  private Label labelImage;
  private Button buttonBrowse;
  private Label label5;
  private Label label1;
  private Composite compositeBlank;
  private Text textJDKPath;
  private Label labelJDKPath;
  private Button buttonOK;
  private Composite compositeJDK;
  private Composite compositeButton;
  private Composite compositeMain;
  
  private String m_textJDKPath="";
  /**
   * Constructs a new instance of this class given its parent and 
   * a style value describing its behaviour and appearance.
   * 
   * @param parent a composite control which will be the parent of the new instance (cannot be null).
   * @param style the style of control to construct.
   */
  public DialogJDK(Shell parent, int style) {
    super(parent, style);

    dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

    {
      //Register as a resource user - SWTResourceManager will
      //handle the obtaining and disposing of resources
      SWTResourceManager.registerResourceUser(dialogShell);
    }

    GridLayout dialogShellLayout = new GridLayout();
    dialogShell.setLayout(dialogShellLayout);
    dialogShellLayout.horizontalSpacing = 0;
    dialogShellLayout.marginHeight = 0;
    dialogShellLayout.marginWidth = 0;
    dialogShellLayout.verticalSpacing = 0;
    {
      compositeMain = new Composite(dialogShell, SWT.NONE);
      GridLayout compositeMainLayout = new GridLayout();
      compositeMainLayout.horizontalSpacing = 0;
      compositeMainLayout.marginHeight = 0;
      compositeMainLayout.marginWidth = 0;
      compositeMainLayout.verticalSpacing = 0;
      GridData compositeMainLData = new GridData();
      compositeMainLData.horizontalAlignment = GridData.FILL;
      compositeMainLData.verticalAlignment = GridData.FILL;
      compositeMainLData.grabExcessVerticalSpace = true;
      compositeMainLData.grabExcessHorizontalSpace = true;
      compositeMain.setLayoutData(compositeMainLData);
      compositeMain.setLayout(compositeMainLayout);
      compositeMain.setSize(500, 160);
      {
        compositeBlank = new Composite(compositeMain, SWT.NONE);
        GridLayout compositeBlankLayout = new GridLayout();
        compositeBlankLayout.makeColumnsEqualWidth = true;
        compositeBlankLayout.numColumns = 2;
        compositeBlank.setLayout(compositeBlankLayout);
        compositeBlank.setBackground(SWTResourceManager.getColor(255, 255, 255));
        GridData compositeBlankLData = new GridData();
        compositeBlankLData.horizontalAlignment = GridData.FILL;
        compositeBlankLData.heightHint = 70;
        compositeBlankLData.grabExcessHorizontalSpace = true;
        compositeBlank.setLayoutData(compositeBlankLData);
        compositeBlank.setSize(500, 70);
        {
          compositeInfoText = new Composite(compositeBlank, SWT.NONE);
          GridLayout compositeInfoTextLayout = new GridLayout();
          compositeInfoTextLayout.makeColumnsEqualWidth = true;
          compositeInfoText.setLayout(compositeInfoTextLayout);
          compositeInfoText.setBackground(SWTResourceManager.getColor(255, 255, 255));
          {
            label1 = new Label(compositeInfoText, SWT.NONE);
            label1.setText("Please enter the Java SDK path");
            label1.setFont(SWTResourceManager.getFont(
              "Tahoma",
              10,
              1,
              false,
              false));
            label1.setBackground(SWTResourceManager.getColor(255, 255, 255));
          }
          {
            label4 = new Label(compositeInfoText, SWT.NONE);
            label4.setText("Sets the Java SDK path...");
            label4.setBackground(SWTResourceManager.getColor(255, 255, 255));
          }
        }
        {
          compositeImage = new Composite(compositeBlank, SWT.NONE);
          GridLayout compositeImageLayout = new GridLayout();
          compositeImageLayout.makeColumnsEqualWidth = true;
          compositeImage.setLayout(compositeImageLayout);
          GridData compositeImageLData = new GridData();
          compositeImageLData.horizontalAlignment = GridData.END;
          compositeImageLData.grabExcessHorizontalSpace = true;
          compositeImage.setLayoutData(compositeImageLData);
          compositeImage.setBackground(SWTResourceManager.getColor(255, 255, 255));
          {
            GridData labelImageLData = new GridData();
            labelImage = new Label(compositeImage, SWT.NONE);
            labelImage.setLayoutData(labelImageLData);
            labelImage.setImage(SWTResourceManager
              .getImage("resources/jdk.png"));
            labelImage.setBackground(SWTResourceManager.getColor(
              255,
              255,
              255));
          }
        }
      }
      {
        GridData label2LData = new GridData();
        label2LData.horizontalAlignment = GridData.FILL;
        label2 = new Label(compositeMain, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setLayoutData(label2LData);
      }
      {
        compositeJDK = new Composite(compositeMain, SWT.NONE);
        GridLayout compositeURLLayout = new GridLayout();
        compositeURLLayout.numColumns = 2;
        GridData compositeURLLData = new GridData();
        compositeURLLData.verticalAlignment = GridData.FILL;
        compositeURLLData.horizontalAlignment = GridData.FILL;
        compositeURLLData.grabExcessHorizontalSpace = true;
        compositeURLLData.grabExcessVerticalSpace = true;
        compositeJDK.setLayoutData(compositeURLLData);
        compositeJDK.setLayout(compositeURLLayout);
        {
          labelJDKPath = new Label(compositeJDK, SWT.NONE);
          GridData labelURLLData = new GridData();
          labelURLLData.horizontalAlignment = GridData.CENTER;
          labelJDKPath.setText("Path:");
        }
        {
          textJDKPath = new Text(compositeJDK, SWT.BORDER);
          GridData textURLLData = new GridData();
          textURLLData.horizontalAlignment = GridData.FILL;
          textURLLData.grabExcessHorizontalSpace = true;
          textJDKPath.setLayoutData(textURLLData);
          textJDKPath.setText(m_textJDKPath);
        }
        {
          label5 = new Label(compositeJDK, SWT.NONE);
        }
		{
			buttonBrowse = new Button(compositeJDK, SWT.PUSH | SWT.CENTER);
			buttonBrowse.setText("Browse...");
		}
      }
      {
        GridData label3LData = new GridData();
        label3LData.horizontalAlignment = GridData.FILL;
        label3 = new Label(compositeMain, SWT.SEPARATOR | SWT.HORIZONTAL);
        label3.setLayoutData(label3LData);
      }
      {
        compositeButton = new Composite(compositeMain, SWT.NONE);
        RowLayout compositeButtonLayout = new RowLayout(
          org.eclipse.swt.SWT.HORIZONTAL);
        GridData compositeButtonLData = new GridData();
        compositeButtonLData.horizontalAlignment = GridData.END;
        compositeButton.setLayoutData(compositeButtonLData);
        compositeButton.setLayout(compositeButtonLayout);
        {
          buttonCancel = new Button(compositeButton, SWT.PUSH | SWT.CENTER);
          buttonCancel.setText("&Cancel");
          buttonCancel.setSize(60, 23);
        }
        {
          buttonOK = new Button(compositeButton, SWT.PUSH | SWT.CENTER);
          buttonOK.setText("&OK");
          buttonOK.setSize(60, 23);
        }
      }
    }
    
    dialogShell.layout();
    dialogShell.pack();
    dialogShell.setSize(500, 200);
    dialogShell.setText("Java SDK Path");

    /*
     * Center the dialog on the screen.
     */
    Monitor primary = dialogShell.getMonitor();
    Rectangle bounds = primary.getBounds();
    Rectangle rect = dialogShell.getBounds();
    int x = bounds.x + (bounds.width - rect.width) / 2;
    int y = bounds.y + (bounds.height - rect.height) / 2;
    dialogShell.setLocation(x,y);
    
    /*
     * Add an internal listener that avoids closing the
     * window AND disposing the resources by pressing the x
     * on the upper right corner in the dialog.
     */
    dialogShell.addListener(SWT.Close, new Listener() {
      public void handleEvent(Event event) {
        event.doit=false;
        dialogShell.setVisible(false);
      }
    });
  }
  
  /**
   * Attaches a listener for the OK button of the JDK dialog.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogJDKOKListener(SelectionAdapter listener) {
	    buttonOK.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the Cancel button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogJDKCancelListener(SelectionAdapter listener) {
    buttonCancel.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the Browse button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogJDKBrowseListener(SelectionAdapter listener) {
    buttonBrowse.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the JDK dialog that intercepts
   * the ENTER key pressed in the JDK path text box.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogJDKPathEnterListener(KeyListener listener) {
    textJDKPath.addKeyListener(listener);
  }
  
  /** 
   * Returns the JDK path that was entered.
   * 
   * @return the JDK path
   */
  public String getTextJDKPath() {
    m_textJDKPath=textJDKPath.getText();
    return m_textJDKPath;
  }
  
  /** 
   * Sets the JDK path.
   * 
   * @param path JDK path.
   */
  public void setTextJDKPath(String path) {
    m_textJDKPath=path;
    textJDKPath.setText(path);
  }
  
  /** 
   * Sets the dialog visible or invisible.
   * 
   * @param visible true, if visible, otherwise false.
   */
  public void setVisible(boolean visible) {
    dialogShell.setVisible(visible);
    textJDKPath.setFocus();
    textJDKPath.selectAll();
  }
  public boolean isVisible() {
	  return dialogShell.isVisible();
  }
}

