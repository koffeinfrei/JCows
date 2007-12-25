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
import org.eclipse.swt.custom.StyledText;
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

import com.cloudgarden.resource.SWTResourceManager;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Error dialog shows exceptions.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class DialogError extends org.eclipse.swt.widgets.Dialog {
  
  //private static final Logger LOGGER = Logger.getLogger(DialogError.class);

  private Shell dialogShell;
  private Button buttonClose;
  private Composite compositeImage;
  private Label label4;
  private Composite compositeInfoText;
  private Label label3;
  private Label label2;
  private Label labelImage;
  private StyledText styledTextMessage;
  private Label label1;
  private Composite compositeBlank;
  private Composite compositeError;
  private Composite compositeButton;
  private Composite compositeMain;

  /** 
   * Constructs a new instance of this class given its parent and 
   * a style value describing its behaviour and appearance.
   * 
   * @param parent a composite control which will be the parent of the new instance (cannot be null).
   * @param style the style of control to construct.
   */
  public DialogError(Shell parent, int style) {
    super(parent, style);
    
    dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.CLOSE);
    
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
            label1.setText("Error Message");
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
            label4.setText("An error occured...");
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
            labelImage.setBackground(SWTResourceManager.getColor(255, 255, 255));
            labelImage.setImage(SWTResourceManager
                .getImage("resources/error.png"));
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
        compositeError = new Composite(compositeMain, SWT.NONE);
        GridLayout compositeErrorLayout = new GridLayout();
        compositeErrorLayout.numColumns = 2;
        GridData compositeErrorLData = new GridData();
        compositeErrorLData.verticalAlignment = GridData.FILL;
        compositeErrorLData.horizontalAlignment = GridData.FILL;
        compositeErrorLData.grabExcessHorizontalSpace = true;
        compositeErrorLData.grabExcessVerticalSpace = true;
        compositeError.setLayoutData(compositeErrorLData);
        compositeError.setLayout(compositeErrorLayout);
        compositeError.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
        {
          GridData styledTextMessageLData = new GridData();
          styledTextMessageLData.horizontalAlignment = GridData.FILL;
          styledTextMessageLData.grabExcessHorizontalSpace = true;
          styledTextMessageLData.verticalAlignment = GridData.FILL;
          styledTextMessageLData.grabExcessVerticalSpace = true;
          styledTextMessage = new StyledText(compositeError, SWT.V_SCROLL);
          styledTextMessage.setLayoutData(styledTextMessageLData);
          styledTextMessage.setWordWrap(true);
          styledTextMessage.setEditable(false);
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
          buttonClose = new Button(compositeButton, SWT.PUSH | SWT.CENTER);
          buttonClose.setText("&Close");
          buttonClose.setSize(60, 23);
        }
      }
    }
    
    dialogShell.layout();
    dialogShell.pack();
    dialogShell.setSize(500, 200);
    dialogShell.setText("JCows Error");
    dialogShell.setImage(SWTResourceManager.getImage("resources/error.png"));

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
   * Sets the dialog visible or invisible.
   * 
   * @param visible true, if visible, otherwise false.
   */
  public void setVisible(boolean visible) {
    dialogShell.setVisible(visible);
  }
  
  /**
   * Shows the error dialog with the text message of
   * the exception.
   * 
   * @param e the threw exception.
   */
  public void showError(Exception e)
  {
    String msg = e.getMessage();
    // set a default title if not defined
    if (!msg.contains(";")) msg ="Error;" + msg;
    String[] msgParts = msg.split(";", 2);
    Throwable cause = e.getCause();
    styledTextMessage.setText(msgParts[1] + "\n" 
        + ((cause == null)? "": cause.getMessage())); // set main message
    label4.setText(msgParts[0]); // set title
    dialogShell.setVisible(true);
  }
  
  /** 
   * Shows the error dialog with the exception message.
   * 
   * @param msg the error message.
   */
  public void showError(String msg) {
    dialogShell.setVisible(true);
  }
  
  /**
   * Attaches a listener for the Cancel button of the error dialog.
   * 
   * @param listener the listener which should be notified.
   */
  public void addDialogErrorCloseListener(SelectionAdapter listener) {
    buttonClose.addSelectionListener(listener);
  }
  
}
