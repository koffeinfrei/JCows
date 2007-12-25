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
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.jcows.JCowsException;
import org.jcows.format.SyntaxHighlighter;
import org.jcows.model.core.GuiLogic;
import org.jcows.model.core.SoapLogic;
import org.jcows.model.vc.ParamListItem;
import org.jcows.model.vc.VCMapper;
import org.jcows.system.Properties;
import org.jcows.view.core.MainWindow;
import org.jcows.view.vc.IVC;

import com.cloudgarden.resource.SWTResourceManager;
/** 
 * The <code>MainWindowController</code> controls the main window of JCows.
 * It is responsible for all main actions like connecting to and interacting with a web service.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
 */
public class MainWindowController extends JCowsController {
  
  private static final Logger LOGGER = Logger.getLogger(MainWindowController.class);

  private static final String PREFIX_SSL_URL="https://";
  
  private VCMapper m_vcMapper;
  public MainWindow m_mainWindow;
  private List<ParamListItem> m_paramListRequest;
  private List<ParamListItem> m_paramListResponse;
  private List<IVC> m_visualComponents;
  
  /**
   * Constructs a new instance of this class.
   * 
   * @throws JCowsException
   */
  public MainWindowController() throws JCowsException {
    m_mainWindow = new MainWindow(m_shell, SWT.NONE);
    m_vcMapper = VCMapper.getInstance();    
    attachViewListenersMainWindow();
    m_shell.addShellListener(getMainWindowShellListener());
    postInit();
    /*
     * Set default application mode.
     */
    m_mainWindow.setViewMode(MainWindow.GUI_MODE);
    toggleViewMode();
  }
  
  /**
   * Attaches the listeners for the main window.
   */
  public void attachViewListenersMainWindow() {
    
    m_mainWindow.addMainWindowMenuItemOpenServiceListener(getMainWindowMenuItemOpenServiceListener());
    m_mainWindow.addMainWindowToolItemOpenServiceListener(getMainWindowMenuItemOpenServiceListener());
    m_mainWindow.addMainWindowMenuItemSaveServiceListener(getMainWindowMenuItemSaveServiceListener());
    m_mainWindow.addMainWindowToolItemSaveServiceListener(getMainWindowMenuItemSaveServiceListener());
    m_mainWindow.addMainWindowMenuItemCloseServiceListener(getMainWindowMenuItemCloseServiceListener());
    m_mainWindow.addMainWindowToolItemCloseServiceListener(getMainWindowMenuItemCloseServiceListener());
    m_mainWindow.addMainWindowMenuItemImportSOAPMessageListener(getMainWindowMenuItemImportSOAPMessageListener());
    m_mainWindow.addMainWindowMenuItemExportSOAPMessageListener(getMainWindowMenuItemExportSOAPMessageListener());
    m_mainWindow.addMainWindowMenuItemInsertXMLDocumentListener(getMainWindowMenuItemInsertXMLDocumentListener());
    m_mainWindow.addMainWindowMenuItemExitListener(getMainWindowMenuItemExitListener());

    m_mainWindow.addMainWindowMenuItemEditListener(getMainWindowMenuItemEditListener());
    m_mainWindow.addMainWindowMenuItemUndoListener(getMainWindowMenuItemUndoListener());
    m_mainWindow.addMainWindowMenuItemCutListener(getMainWindowMenuItemCutListener());
    m_mainWindow.addMainWindowToolItemCutListener(getMainWindowMenuItemCutListener());
    m_mainWindow.addMainWindowMenuItemCopyListener(getMainWindowMenuItemCopyListener());
    m_mainWindow.addMainWindowToolItemCopyListener(getMainWindowMenuItemCopyListener());
    m_mainWindow.addMainWindowMenuItemPasteListener(getMainWindowMenuItemPasteListener());
    m_mainWindow.addMainWindowToolItemPasteListener(getMainWindowMenuItemPasteListener());
    m_mainWindow.addMainWindowMenuItemDeleteListener(getMainWindowMenuItemDeleteListener());
    m_mainWindow.addMainWindowMenuItemSelectAllListener(getMainWindowMenuItemSelectAllListener());

    m_mainWindow.addMainWindowMenuItemEditorModeListener(getMainWindowMenuItemEditorModeListener());
    m_mainWindow.addMainWindowToolItemEditorModeListener(getMainWindowToolItemEditorModeListener());
    m_mainWindow.addMainWindowMenuItemGUIModeListener(getMainWindowMenuItemGUIModeListener());    
    m_mainWindow.addMainWindowToolItemGUIModeListener(getMainWindowToolItemGUIModeListener());
    
    m_mainWindow.addMainWindowMenuItemJDKSettingsListener(getMainWindowMenuItemJDKSettingsListener());
    m_mainWindow.addMainWindowToolItemJDKSettingsListener(getMainWindowToolItemJDKSettingsListener());
    m_mainWindow.addMainWindowMenuItemClearURLHistoryListener(getMainWindowMenuItemClearURLHistoryListener());
    
    m_mainWindow.addMainWindowMenuItemHelpListener(getMainWindowMenuItemHelpListener());
    m_mainWindow.addMainWindowMenuItemAboutListener(getMainWindowMenuItemAboutListener());

    m_mainWindow.addMainWindowSOAPToolItemSendRequestListener(getMainWindowToolItemSendRequestListener());
    
    m_mainWindow.addMainWindowChangeServiceListener(getMainWindowChangeServiceListener());
    m_mainWindow.addMainWindowChangePortListener(getMainWindowChangePortListener());
    m_mainWindow.addMainWindowChangeOperationListener(getMainWindowChangeOperationListener());

    //m_gui.addMainWindowAttachmentAddListener(getMainWindowAttachmentAddListener());
    //m_gui.addMainWindowAttachmentRemoveListener(getMainWindowAttachmentRemoveListener());
    
    m_mainWindow.addMainWindowConnectListener(getMainWindowConnectListener());
    
    m_mainWindow.addMainWindowSOAPMessageListener(getMainWindowSOAPMessageListener());
    m_mainWindow.addMainWindowCComboWSDLURLLListener(getMainWindowCComboWSDLURLListener());
    m_mainWindow.addMainWindowKeyListener(getMainWindowRootKeyListener());
  }
  
  /**
   * Listener for the {@link org.eclipse.swt.widgets.Shell} object of the main window.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowShellListener implements ShellListener {
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
     */
    public void shellActivated(ShellEvent evt) {
      LOGGER.debug(evt);
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#shellClosed(org.eclipse.swt.events.ShellEvent)
     */
    public void shellClosed(ShellEvent evt) {
      LOGGER.debug(evt);
      /*
       * Calls application exit method.
       */
      exit(0);
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#shellDeactivated(org.eclipse.swt.events.ShellEvent)
     */
    public void shellDeactivated(ShellEvent evt) {
      LOGGER.debug(evt);
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#shellDeiconified(org.eclipse.swt.events.ShellEvent)
     */
    public void shellDeiconified(ShellEvent evt) {
      LOGGER.debug(evt);
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#shellIconified(org.eclipse.swt.events.ShellEvent)
     */
    public void shellIconified(ShellEvent evt) {
      LOGGER.debug(evt);
    }
  }
  
  /** 
   * Listener for the file menu open service item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemOpenServiceListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {        
        FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.OPEN);
        dialog.setFilterNames (new String [] {"JCows File (*.jcows)", "All Files (*.*)"});
        dialog.setText("Open Web Service");
        dialog.setFilterExtensions (new String [] {"*.jcows", "*.*"});
        String filename = dialog.open();
        if (filename != null){
          m_soapLogic = new SoapLogic(filename, m_dialogSSLController);
          m_guiLogic = new GuiLogic(filename, m_dialogSSLController);
          
          // set text in URL bar
          m_current_URL = m_soapLogic.getWsdlUrl();
          m_mainWindow.getCComboWSDLURL().setText(m_current_URL);
          if(m_current_URL.startsWith(PREFIX_SSL_URL))
            m_mainWindow.setSSLMode(true, m_guiLogic.getSSLCertificateIssuer());
          else
            m_mainWindow.setSSLMode(false, null);
          
          setGUIRequest();
          setSoapRequest();
          setServices();
          setPorts();
          setOperations();
          
          // enable the send button
          m_mainWindow.getToolItemSendRequest().setEnabled(true); 
        }
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
    }    
  }
  
  /** 
   * Listener for the file menu open WSDL item.
   * Opens a new URL dialog and sets the current URL string in the text
   * box of the URL dialog.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemOpenWSDLListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
    }    
  }
  
  /** 
   * Listener for the file menu save service item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemSaveServiceListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {
        if (m_soapLogic == null)
          throw new JCowsException(
            Properties.getMessage("error.wsdlNotLoaded"));
        
        FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.SAVE);
        dialog.setFilterNames (new String [] {"JCows File (*.jcows)", "All Files (*.*)"});
        dialog.setText("Save Web Service");
        dialog.setFileName(m_soapLogic.getWsdlLocalName() 
            + Properties.getConfig("parser.JCowsFileEnding"));
        dialog.setFilterExtensions (new String [] {"*.jcows", "*.*"});
        String filename = dialog.open();
        if (filename != null){
          m_soapLogic.saveWS(filename);
          setSoapRequest();
        }
      }
      catch (JCowsException e) {
        DialogErrorController.m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
    }    
  }
  
  /** 
   * Listener for the file menu close service item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemCloseServiceListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {
        if (m_soapLogic == null)
          throw new JCowsException(
            Properties.getMessage("error.wsdlNotLoaded"));
        m_soapLogic = null;
        resetUI();
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
      
      // disable the send button
      m_mainWindow.getToolItemSendRequest().setEnabled(false);
    }    
  }
  
  /** 
   * Listener for the file menu import SOAP message item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemImportSOAPMessageListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {
        if (m_soapLogic == null)
          throw new JCowsException(
            Properties.getMessage("error.wsdlNotLoaded"));
        
        FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.OPEN);
        dialog.setFilterNames (new String [] {"All Files (*.*)"});
        dialog.setText("Import SOAP Message");
        dialog.setFilterExtensions (new String [] {"*.*"});
        String filename = dialog.open();
        if (filename != null){
          m_soapLogic.importSoapMessage(filename);
          setSoapRequest();
        }
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
    }    
  }
  
  /**
   * Listener for the file menu export SOAP message item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemExportSOAPMessageListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {
        if (m_soapLogic == null)
          throw new JCowsException(
            Properties.getMessage("error.wsdlNotLoaded"));
        
        FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.SAVE);
        dialog.setFilterNames (new String [] {"XML File (*.xml)", "All Files (*.*)"});
        dialog.setText("Export SOAP Message");
        dialog.setFileName(m_soapLogic.getWsdlLocalName() + ".xml");
        dialog.setFilterExtensions (new String [] {"*.xml", "*.*"});
        String filename = dialog.open();
        if (filename != null){
          m_soapLogic.exportSoapMessage(filename, m_mainWindow.getActiveStyledText().getText());
        }
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
    }    
  }
  
  /**
   * Listener for the file import XML document item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemInsertXMLDocumentListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      try {
        if (m_soapLogic == null)
          throw new JCowsException(
            Properties.getMessage("error.wsdlNotLoaded"));
        
        FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.SAVE);
        dialog.setFilterNames (new String [] {"XML File (*.xml)", "All Files (*.*)"});
        dialog.setText("Insert XML Document");
        dialog.setFileName(m_soapLogic.getWsdlLocalName() + ".xml");
        dialog.setFilterExtensions (new String [] {"*.xml", "*.*"});
        String filename = dialog.open();
        if (filename != null){
          String message = m_soapLogic.insertXmlDocument(filename);
          updateRequestSoapMessage(message);
        }
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
    }    
  }
  
  /**
   * Listener for the file menu exit item.
   * Saves program history and exits the JCows application.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemExitListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      exit(0);
    }    
  }
  
  /**
   * Listener for the edit menu item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemEditListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
    }    
  }
  
  /**
   * Listener for the edit menu undo item. This function is not implemented yet.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemUndoListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
    }
  }
  
  /**
   * Listener for the edit menu cut item.
   * Cuts the selected text in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemCutListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      String textData=null;
      
      if(m_mainWindow.getActiveStyledText().isFocusControl()) {
        StyledText styledText=m_mainWindow.getActiveStyledText();
        textData=styledText.getSelectionText();
        styledText.cut();
      }
      else if(m_mainWindow.getCComboWSDLURL().isFocusControl()) {
        Point selection=m_mainWindow.getCComboWSDLURL().getSelection();
        textData=m_mainWindow.getCComboWSDLURL().getText().substring(selection.x,selection.y);
        String textCut=m_mainWindow.getCComboWSDLURL().getText().substring(0,selection.x);
        textCut+=m_mainWindow.getCComboWSDLURL().getText().substring(selection.y,m_mainWindow.getCComboWSDLURL().getText().length());
        m_mainWindow.getCComboWSDLURL().setText(textCut);
      }
      

        
      if(textData.length() > 0) {
        TextTransfer textTransfer = TextTransfer.getInstance();
        m_clipboard.setContents(new Object[]{textData}, new Transfer[]{textTransfer});
      
      }
    }    
  }
  
  /**
   * Listener for the edit menu copy item.
   * Copies (to the clipboard) the selected text in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemCopyListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      String textData=null;

      if(m_mainWindow.getActiveStyledText().isFocusControl()) {
        StyledText styledText=m_mainWindow.getActiveStyledText();
        textData=styledText.getSelectionText();
      }
      else if(m_mainWindow.getCComboWSDLURL().isFocusControl()) {
        Point selection=m_mainWindow.getCComboWSDLURL().getSelection();
        textData=m_mainWindow.getCComboWSDLURL().getText().substring(selection.x,selection.y);
      }
      if(textData!=null && textData.length() > 0) {
        TextTransfer textTransfer = TextTransfer.getInstance();
        m_clipboard.setContents(new Object[]{textData}, new Transfer[]{textTransfer});
      } 
    }    
  }
  
  /**
   * Listener for the edit menu paste item.
   * Pastes the text from the clipboard to the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemPasteListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);

      if(m_mainWindow.getActiveStyledText().isFocusControl()) {
        StyledText styledText=m_mainWindow.getActiveStyledText();
        TextTransfer transfer = TextTransfer.getInstance();
        String data = (String)m_clipboard.getContents(transfer);
        if (data != null)
          styledText.insert(data);
      }
      else if(m_mainWindow.getCComboWSDLURL().isFocusControl()) {
        TextTransfer transfer = TextTransfer.getInstance();
        String data = (String)m_clipboard.getContents(transfer);
        if (data != null) {
          Point position=m_mainWindow.getCComboWSDLURL().getSelection();
          String currentText=m_mainWindow.getCComboWSDLURL().getText();
          String textBefore=currentText.substring(0,position.x);
          String textAfter=currentText.substring(position.x,currentText.length());
          if(position.x!=position.y) {
            currentText=data;
            m_mainWindow.getCComboWSDLURL().setText(currentText);
            m_mainWindow.getCComboWSDLURL().setSelection(new Point(currentText.length(),currentText.length()));            
          }
          else {
            currentText=textBefore+data+textAfter;
            m_mainWindow.getCComboWSDLURL().setText(currentText);
            m_mainWindow.getCComboWSDLURL().setSelection(new Point(position.x,position.x));
          }

        }
      }
      
      
    }    
  }
  
  /**
   * Listener for the edit menu delete item.
   * Deletes the selected text in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemDeleteListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
    }    
  }
  
  /**
   * Listener for the edit menu select all item.
   * Selects the whole text in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemSelectAllListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);

      /*
       * Tests, on which text field the focus is set.
       */
      if(m_mainWindow.getActiveStyledText().isFocusControl()) {
        m_mainWindow.getActiveStyledText().selectAll();
      }
      else if(m_mainWindow.getCComboWSDLURL().isFocusControl()) {
        String textdata=m_mainWindow.getCComboWSDLURL().getText();
        m_mainWindow.getCComboWSDLURL().setSelection(new Point(0,textdata.length()));
      }
    }    
  }
  
  /**
   * Listener for the view menu editor mode item.
   * Changes the view to the XML editor mode.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemEditorModeListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      m_mainWindow.setViewMode(MainWindow.EDITOR_MODE);
      
      toggleViewMode();
    }    
  }
  
  /**
   * Listener for the view menu GUI mode item.
   * Changes the view to GUI mode.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemGUIModeListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      m_mainWindow.setViewMode(MainWindow.GUI_MODE);
      
      toggleViewMode();
    }    
  }
  
  private void toggleViewMode(){
    boolean isGUIMode = m_mainWindow.getViewMode() == MainWindow.GUI_MODE;
    
    /* Toggle tabs and editor/gui itself */
    GridData gridData = (GridData)m_mainWindow.getTabFolderEditorMode()
      .getLayoutData();
    gridData.exclude = isGUIMode;
    m_mainWindow.getTabFolderEditorMode().setVisible(!isGUIMode);
    
    gridData = (GridData)m_mainWindow.getTabFolderGUIMode().getLayoutData();
    gridData.exclude = !isGUIMode;
    m_mainWindow.getTabFolderGUIMode().setVisible(isGUIMode);
    
    m_mainWindow.getCompositeCenter().layout();
    
    /* Toggle corresponding button */
    m_mainWindow.getToolItemEditorMode().setSelection(!isGUIMode);
    m_mainWindow.getToolItemGUIMode().setSelection(isGUIMode);
    
    /* Editor Mode specific */
    m_mainWindow.getMenuItemImportSOAPMessage().setEnabled(!isGUIMode);
    m_mainWindow.getMenuItemExportSOAPMessage().setEnabled(!isGUIMode);
    m_mainWindow.getMenuItemInsertXMLDocument().setEnabled(!isGUIMode);
    
    /* Toggle menu item */
    m_mainWindow.getMenuItemGUIMode().setSelection(isGUIMode);
    m_mainWindow.getMenuItemEditorMode().setSelection(!isGUIMode);
  }
  
  /**
   * Listener for the settings menu JDK item.
   * Shows a window that allows to set the Java Development Kit (JDK) path.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemJDKSettingsListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_dialogJDKController.m_dialogJDK.setVisible(true);
    }    
  }
  
  /**
   * Listener for the settings menu clear URL history item.
   * Clears all cached URLs in the WSDL URL address combo box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemClearURLHistoryListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      Properties.setHistory("URLHistory","");
    }    
  }
  
  /**
   * Listener for the help menu help item.
   * Opens the online help in a browser window.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuItemHelpListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      
      Shell shell = new Shell(m_mainWindow.getDisplay());
      shell.setLayout(new FillLayout());
      
      Browser browser = null;
      try {
        browser = new Browser(shell, SWT.NONE);
      } catch (SWTError e) {
        m_dialogErrorController.m_dialogError.showError(new RuntimeException(e.getMessage()));
      }
      if (browser != null) {
        /* The Browser widget can be used */
        browser.setUrl(Properties.getConfig("helpURL"));
        shell.open();
      }
      
    }    
  }
  
  /**
   * Listener for the help menu about item.
   * Opens a About dialog which shows additional information about JCows.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowMenuItemAboutListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_dialogAboutController.m_dialogAbout.setVisible(true);
    }    
  }
  
  /**
   * Listener for the URL go button.
   * Connects to the Web Service.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowConnectListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      connect();
    }
  }
  
  /**
   * Listener for the send SOAP message button.
   * Sends the SOAP message to the Web Service.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowToolItemSendRequestListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      sendRequest();
    }    
  }
  
  /**
   * Listener for the Web Service service combo box.
   * Changes the service of a Web Service. A new default SOAP message will showed
   * in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowChangeServiceListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      try {
        m_soapLogic.setCurrentService(m_mainWindow.getComboService().getText());
        m_guiLogic.setCurrentService(m_mainWindow.getComboService().getText());
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
      refreshRequest();
    }    
  }
  
  /**
   * Listener for the Web Service port combo box.
   * Changes the port of a Web Service. A new default SOAP message will showed
   * in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowChangePortListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      try {
        m_soapLogic.setCurrentPort(m_mainWindow.getComboPort().getText());
        m_guiLogic.setCurrentPort(m_mainWindow.getComboPort().getText());
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
      refreshRequest();
    }
  }
  
  /**
   * Listener for the Web Service operation combo box.
   * Changes the operation of a Web Service. A new default SOAP message will showed
   * in the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowChangeOperationListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      try {
        m_soapLogic.setCurrentOperation(m_mainWindow.getComboMethod().getText());
        m_guiLogic.setCurrentOperation(m_mainWindow.getComboMethod().getText());
        
      }
      catch (JCowsException e) {
        m_dialogErrorController.m_dialogError.showError(e);
        return;
      }
      refreshRequest();
    }    
  }
  
  /**
   * Listener for the add attachment button.
   * Attaches a file that will sent with the SOAP message.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowAttachmentAddListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      FileDialog dialog = new FileDialog(m_mainWindow.getShell(), SWT.OPEN);
      dialog.setFilterNames (new String [] {"All Files (*.*)"});
      dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
      dialog.open();

      if(!dialog.getFileName().equals("")) {
        TableItem tableItem = new TableItem(m_mainWindow.getTableAttachment(),SWT.NONE);
        tableItem.setText(dialog.getFileName());
        // set the full path
        tableItem.setData(
            dialog.getFilterPath() 
            + System.getProperty("file.separator") 
            + dialog.getFileName());
        tableItem.setImage(SWTResourceManager.getImage("resources/attachment.png"));
        m_attachments.add(new File(dialog.getFileName()));
      }
        
    }    
  }
  
  /**
   * Listener for the remove attachment button.
   * Removes the selected file in the attachment table.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class MainWindowAttachmentRemoveListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      if(m_mainWindow.getTableAttachment().getSelectionIndex()>=0)
        m_mainWindow.getTableAttachment().remove(m_mainWindow.getTableAttachment().getSelectionIndex());
    }
  }
  
  /**
   * Listener for the SOAP message text box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */   
  private class MainWindowSOAPMessageListener implements Listener {
    public void handleEvent (Event evt) {
      LOGGER.debug(evt + " keycode:" + evt.keyCode);
      
      // TODO: add navigation features (e.g. shift+cursor -> select text)
      
      /* check if alphanumeric key or delete or backspace */
      if (evt.character != '\0' 
          || evt.keyCode == SWT.DEL
          || evt.keyCode == SWT.BS)
        updateRequestSoapMessage();
    }    
  }
  
  /**
   * Listener for the URL combo box.
   * Executes the {@link org.jcows.controller.MainWindowController#connect()} method if the enter key was pressed.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowCComboWSDLURLLListener implements Listener {
    public void handleEvent (Event evt) {
      LOGGER.debug(evt);
      /*
       * For both enter keys.
       */
      if(evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)
        connect();
    }    
  }
  
  /** 
   * Listener for the file menu item.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowMenuFileListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
    }    
  }
  
  /** 
   * Key listener for the main window.
   * Executes the {@link org.jcows.controller.MainWindowController#connect()} method if the enter key was pressed.
   * This is useful at program start when the focus is set on the main window and
   * not on the URL combo box.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class MainWindowKeyListener implements Listener {
    public void handleEvent (Event evt) {
      /*
       * For both enter keys.
       */
      if(evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)
        connect();
    }
  }
  
  /**
   * Post-Initializes the main window controller. This method must be executed
   * <strong>after</strong> a new instance of <string>MainWindowController</strong> is created.
   */
  private void postInit() {
    String urlHistory=Properties.getHistory(Properties.KEY_HISTORY_URL);
    if(urlHistory!=null && !urlHistory.equals("")) {
      String[] items=urlHistory.split(";");
      m_mainWindow.getCComboWSDLURL().setItems(items);
    }
  }
  
  /**
   * Returns the {@link org.eclipse.swt.widgets.Shell} object of the main window.
   * 
   * @return the {@link org.eclipse.swt.widgets.Shell} object.
   */
  private ShellListener getMainWindowShellListener() {
    return new MainWindowShellListener();
  }

  /**
   * Returns a listener for the
   * file menu item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuFileListener() {
    return new MainWindowMenuFileListener();
  }
  
  /**
   * Returns a listener for the file menu open service item.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getMainWindowMenuItemOpenServiceListener() {
    return new MainWindowMenuItemOpenServiceListener();
  }
  
  /**
   * Returns a listener for the file menu open WSDL item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemOpenWSDLListener() {
    return new MainWindowMenuItemOpenWSDLListener();
  }
  
  /**
   * Returns a listener for the file menu save service item.
   * 
   * @return a listener.
   */      
  private SelectionAdapter getMainWindowMenuItemSaveServiceListener() {
    return new MainWindowMenuItemSaveServiceListener();
  }
  
  /**
   * Returns a listener for the file menu close service item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemCloseServiceListener() {
    return new MainWindowMenuItemCloseServiceListener();
  }
  
  /**
   * Returns a listener for the file menu import SOAP message item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemImportSOAPMessageListener() {
    return new MainWindowMenuItemImportSOAPMessageListener();
  }
  
  /**
   * Returns a listener for the file menu export SOAP message item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemExportSOAPMessageListener() {
    return new MainWindowMenuItemExportSOAPMessageListener();
  }
  
  /**
   * Returns a listener for the file menu import XML document item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemInsertXMLDocumentListener() {
    return new MainWindowMenuItemInsertXMLDocumentListener();
  }
  
  /**
   * Returns a listener for the file menu exit item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemExitListener() {
    return new MainWindowMenuItemExitListener();
  }
  
  /**
   * Returns a listener for the edit menu.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemEditListener() {
    return new MainWindowMenuItemEditListener();
  }
  
  /**
   * Returns a listener for the edit menu undo item. 
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemUndoListener() {
    return new MainWindowMenuItemUndoListener();
  }
  
  /**
   * Returns a listener for the edit menu cut item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemCutListener() {
    return new MainWindowMenuItemCutListener();
  }
  
  /**
   * Returns a listener for the edit menu copy item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemCopyListener() {
    return new MainWindowMenuItemCopyListener();
  }
  
  /**
   * Returns a listener for the edit menu paste item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemPasteListener() {
    return new MainWindowMenuItemPasteListener();
  }
  
  /**
   * Returns a listener for the edit menu delete item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemDeleteListener() {
    return new MainWindowMenuItemDeleteListener();
  }
  
  /**
   * Returns a listener for the edit menu select all item.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getMainWindowMenuItemSelectAllListener() {
    return new MainWindowMenuItemSelectAllListener();
  }
  
  /**
   * Returns a listener for the view menu editor mode item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemEditorModeListener() {
    return new MainWindowMenuItemEditorModeListener();
  }
  
  /**
   * Returns a listener for the editor mode tool item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowToolItemEditorModeListener() {
    return new MainWindowMenuItemEditorModeListener();
  }
  
  /**
   * Returns a listener for the view menu GUI mode item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemGUIModeListener() {
    return new MainWindowMenuItemGUIModeListener();
  }
  
  /**
   * Returns a listener for the GUI mode tool item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowToolItemGUIModeListener() {
    return new MainWindowMenuItemGUIModeListener();
  }
  
  /**
   * Returns a listener for the settings menu JDK settings item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemJDKSettingsListener() {
    return new MainWindowMenuItemJDKSettingsListener();
  }
  
  /**
   * Returns a listener for the settings menu clear URL history item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemClearURLHistoryListener() {
    return new MainWindowMenuItemClearURLHistoryListener();
  }
  
  /**
   * Returns a listener for the JDK settings tool item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowToolItemJDKSettingsListener() {
    return new MainWindowMenuItemJDKSettingsListener();
  }
  
  /**
   * Returns a listener for the help menu help item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemHelpListener() {
    return new MainWindowMenuItemHelpListener();
  }
  
  /**
   * Returns a listener for the help menu about item.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowMenuItemAboutListener() {
    return new MainWindowMenuItemAboutListener();
  }
  
  /**
   * Returns a listener for the connect button.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowConnectListener() {
    return new MainWindowConnectListener();
  }
  
  /**
   * Returns a listener for the send SOAP message button.
   * 
   * @return a listener.
   */    
  public SelectionAdapter getMainWindowToolItemSendRequestListener() {
    return new MainWindowToolItemSendRequestListener();
  }
  
  /**
   * Returns a listener for the Web Service service combo box.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowChangeServiceListener() {
    return new MainWindowChangeServiceListener();
  }
  
  /**
   * Returns a listener for the Web Service port combo box.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowChangePortListener() {
    return new MainWindowChangePortListener();
  }
  
  /**
   * Returns a listener for the Web Service operation combo box.
   * 
   * @return a listener.
   */ 
  private SelectionAdapter getMainWindowChangeOperationListener() {
    return new MainWindowChangeOperationListener();
  }
  
  /**
   * Returns a listener for the add attachment button.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowAttachmentAddListener() {
    return new MainWindowAttachmentAddListener();
  }
  
  /**
   * Returns a listener for the remove attachment button.
   * 
   * @return a listener.
   */
  private SelectionAdapter getMainWindowAttachmentRemoveListener() {
    return new MainWindowAttachmentRemoveListener();
  }
  
  /**
   * Returns a listener for the main window.
   * 
   * @return a listener.
   */
  private Listener getMainWindowRootKeyListener() {
    return new MainWindowKeyListener();
  }
  
  /**
   * Returns a for the SOAP message text box.
   * 
   * @return a listener.
   */  
  private Listener getMainWindowSOAPMessageListener() {
    return new MainWindowSOAPMessageListener();
  }
  
  /**
   * Returns a listener for the WSDL URL combo box.
   * 
   * @return a listener.
   */
  private Listener getMainWindowCComboWSDLURLListener() {
    return new MainWindowCComboWSDLURLLListener();
  }
  
  /**
   * Updates the displayed SOAP message.
   */
  private void updateRequestSoapMessage(){
    updateRequestSoapMessage(null);
  }
  
  /**
   * Connects to the web service and displays the SOAP message or the
   * GUI representation of the web service.
   *
   */
  private void connect() {
    
    ProgressBar bar = m_mainWindow.getProgressBar();
    //bar.setBounds(10, 10, 200, 32);
    //for (int i=0; i<=10; i++) {
    //  try {Thread.sleep (100);} catch (Throwable th) {}
    // bar.setSelection (i);
    //}
    bar.setSelection(0);
    bar.setSelection(bar.getMaximum() / 3);
    
    m_current_URL=m_mainWindow.getCComboWSDLURL().getText();

    try {
      m_soapLogic = new SoapLogic(m_current_URL, m_dialogSSLController);
      m_guiLogic = new GuiLogic(m_current_URL, m_dialogSSLController);//TODO dispatch? (soap/gui)
    }
    catch(JCowsException e){
      bar.setSelection(bar.getMinimum());
      m_dialogErrorController.m_dialogError.showError(e);
      return;
    }
    
    if(m_current_URL.startsWith(PREFIX_SSL_URL))
      m_mainWindow.setSSLMode(true, m_guiLogic.getSSLCertificateIssuer());
    else
      m_mainWindow.setSSLMode(false, null);

    boolean found=false;
    String[] items=m_mainWindow.getCComboWSDLURL().getItems();
    for(String item:items) {
      if(m_mainWindow.getCComboWSDLURL().getText().equals(item))
        found=true;
    }
    if(found==false) {
      m_mainWindow.getCComboWSDLURL().add(m_mainWindow.getCComboWSDLURL().getText(),0);
    }
    
    bar.setSelection(bar.getMaximum() / 3 * 2);
    
    setSoapRequest();
    setGUIRequest();
    setServices();
    setPorts();
    setOperations();

    bar.setSelection(bar.getMinimum());
    
    m_mainWindow.getToolItemSendRequest().setEnabled(true);    
  }
  
  /**
   * Updates the displayed SOAP message.<br/>
   * If the message is null the message is not replaced.
   * @param message the message string.
   */
  private void updateRequestSoapMessage(String message){
    StyledText requestST = m_mainWindow.getStyledTextSOAPRequest();//m_gui.getActiveStyledText();

    if (message == null)
      message = requestST.getText();
    
    /* TODO: make this nicer to keep the view section
     * not reposition every part of it. The cursor position
     * doesnt work in all cases either (e.g. on indentation)
     */ 
    
    // save caret position for later resetting
    int cursorPos = requestST.getCaretOffset();
    // save text size
    int textSize = requestST.getCharCount();
    //int scrollPos = requestST.getVerticalBar().getSelection();
    int topIndex = requestST.getTopIndex();
    
    // update text with syntax highlighting
    SyntaxHighlighter syntax = 
      new SyntaxHighlighter(message);
    requestST.setText(syntax.getText());
    for (StyleRange style : syntax.getStyleRanges())
      requestST.setStyleRange(style);
    // set caret to former position
    int additionalPos = (int)Math.round((requestST.getCharCount() - textSize) / 2.0);
    requestST.setCaretOffset(cursorPos + additionalPos);
    
    //requestST.getVerticalBar().setSelection(scrollPos);
    requestST.setTopIndex(topIndex);
    
    
    //if (m_gui.getTabFolderMessages().getSelectionIndex() != 0)
    //  m_gui.getTabFolderMessages().setSelection(0);
  }

  /**
   * Sets the raw SOAP message in the gui, adds syntax highlighting to it 
   * and sets the focus to the appropriate tab
   */
  private void setSoapRequest() {
    SyntaxHighlighter syntax = 
      new SyntaxHighlighter(m_soapLogic.getSoapMessage());
    
    m_mainWindow.getStyledTextSOAPRequest().setText(syntax.getText());
    for (StyleRange style : syntax.getStyleRanges())
      m_mainWindow.getStyledTextSOAPRequest().setStyleRange(style);
    //m_gui.getStyledTextSOAPRequest().setStyleRanges(syntax.getStyleRanges());
    m_mainWindow.getTabFolderEditorMode().setSelection(0);
    
    //m_mainWindow.getStyledTextSOAPResponse().setText("");
    resetResponse();
  }
  
  /**
   * Sets the request GUI component according to the current operation
   */
  private void setGUIRequest() {
    /*
     * Gets all elements in the request composite and disposes them.
     */
    Control[] children=m_mainWindow.getCompositeGUIRequest().getChildren();
    for(Control child:children)
      child.dispose();
    
    try {
      m_paramListRequest=m_guiLogic.getParamList();
      m_visualComponents=m_vcMapper.mapVCRequest(m_mainWindow.getCompositeGUIRequest(),m_paramListRequest);
    }
    catch(JCowsException e) {
      m_dialogErrorController.m_dialogError.showError(e);
      return;      
    }
    
    m_mainWindow.scrolledCompositeGUIRequestUpdate();
    m_mainWindow.getTabFolderGUIMode().setSelection(0);
    
    resetResponse();
  }

  /**
   * Sets the response SOAP message in the gui, adds syntax highlighting to it 
   * and sets the focus to the appropriate tab
   */
  private void setSoapResponse(){
    SyntaxHighlighter syntax = 
      new SyntaxHighlighter(m_soapLogic.getSoapResponse());
    
    m_mainWindow.getStyledTextSOAPResponse().setText(syntax.getText());
    for (StyleRange style : syntax.getStyleRanges())
      m_mainWindow.getStyledTextSOAPResponse().setStyleRange(style);
    //m_gui.getStyledTextSOAPResponse().setStyleRanges(syntax.getStyleRanges());
    
    m_mainWindow.getTabFolderEditorMode().setSelection(1);
  }
  
  /**
   * Sets the response GUI component to the response from the web service
   */
  private void setGUIResponse() {
    /*
     * Gets all elements in the response composite and disposes them.
     */    
    Control[] children=m_mainWindow.getCompositeGUIResponse().getChildren();
    for(Control child:children)
      child.dispose();
    
    try {
      m_vcMapper.mapVCResponse(m_mainWindow.getCompositeGUIResponse(),m_paramListResponse);
    }
    catch(JCowsException e)
    {
      m_dialogErrorController.m_dialogError.showError(e);
      return;
    }
    m_mainWindow.scrolledCompositeGUIResponseUpdate();
    
    m_mainWindow.getTabFolderGUIMode().setSelection(1);
  }
  
  /**
   * Sets all available services in the combo box in the gui.
   */
  private void setServices(){
    Combo combo = m_mainWindow.getComboService();
    combo.removeAll();
    for (String service : m_soapLogic.getServices())
      combo.add(service);
    if(combo.getItemCount()>0) {
      combo.select(0);
      combo.setEnabled(true);
    }
  }
  
  /**
   * Sets all available ports in the combo box in the gui.
   */
  private void setPorts(){
    Combo combo = m_mainWindow.getComboPort();
    combo.removeAll();
    for (String port : m_soapLogic.getPorts())
      combo.add(port);
    if(combo.getItemCount()>0) {
      combo.select(0);
      combo.setEnabled(true);
    }
  }
  
  /**
   * Sets all available operations in the combo box in the gui.
   */
  private void setOperations(){
    Combo combo = m_mainWindow.getComboMethod();
    combo.removeAll();
    for (String operation : m_soapLogic.getOperations())
      combo.add(operation);
    if(combo.getItemCount()>0) {
      combo.select(0);
      combo.setEnabled(true);
    }
  }
  
  /**
   * Resets the User Interface (clears all components).
   */
  private void resetUI(){
      m_mainWindow.getComboService().removeAll();
      m_mainWindow.getComboPort().removeAll();
      m_mainWindow.getComboMethod().removeAll();
      
      m_mainWindow.getComboService().setEnabled(false);
      m_mainWindow.getComboPort().setEnabled(false);
      m_mainWindow.getComboMethod().setEnabled(false);
      
      //m_gui.getTableAttachment().removeAll();
      m_mainWindow.getToolItemSendRequest().setEnabled(false);
      
      m_mainWindow.getCComboWSDLURL().setText("");
      
      resetRequest();
      resetResponse();
  }
  
  /**
   * This method sends a request to the web service. Depending on the mode that
   * is used it either invokes the invoke method of the gui or the soap logic.
   * If the invocation was successful the response is set. 
   */
  public void sendRequest() {
    //SOAPEnvelope response = null;
    try {
      /* EDITOR MODE (SOAP) */
      if(m_mainWindow.getViewMode()==MainWindow.EDITOR_MODE) {
        // no web service loaded yet
        if (m_soapLogic == null)
          throw new JCowsException(
              Properties.getMessage("error.wsdlNotLoaded"));
        
        m_soapLogic.invoke(m_mainWindow.getStyledTextSOAPRequest().getText(), getAttachmentNames());
        setSoapResponse();
      }
      /* GUI MODE */
      else if(m_mainWindow.getViewMode()==MainWindow.GUI_MODE) {
        // no web service loaded yet
        if (m_guiLogic == null)
          throw new JCowsException(
              Properties.getMessage("error.wsdlNotLoaded"));

        for(IVC ivc:m_visualComponents) {
          if(ivc.validate()==false)
            throw new JCowsException(Properties.getMessage("error.validatorError"));
        }
        
        m_paramListResponse=m_guiLogic.invoke(m_paramListRequest,getAttachmentNames());
        setGUIResponse();
      }
    }
    catch (JCowsException e) {
      m_dialogErrorController.m_dialogError.showError(e);
      return;
    } 
  }
  
  /**
   * Resets the request part of the user interface
   * (the GUI as well as the editor component). 
   */
  private void resetRequest(){
    // soap
    m_mainWindow.getStyledTextSOAPRequest().setText("");
    // gui
    Control[] children=m_mainWindow.getCompositeGUIRequest().getChildren();
    for(Control child:children)
      child.dispose();
  }
  
  /**
   * Resets the response part of the user interface
   * (the GUI as well as the editor component). 
   */
  private void resetResponse(){
    // soap
    m_mainWindow.getStyledTextSOAPResponse().setText("");
    // GUI
    Control[] children=m_mainWindow.getCompositeGUIResponse().getChildren();
    for(Control child:children)
      child.dispose();
  }
  
  /**
   * Sets the request component (editor and gui) to the current request.
   */
  private void refreshRequest() {
    setSoapRequest();
    setGUIRequest();
  }
  
  /**
   * Sets the response component (editor and gui) to the current request.
   */
  private void refreshResponse() {
    setSoapResponse();
    setGUIResponse();
  }
  
  /**
   * Saved application information (history) and terminates the currently
   * running JCows application. The argument serves as a status code; by convention,
   * a nonzero status code indicates abnormal termination. 
   * 
   * @param status exit status.
   * @see java.lang.System#exit(int)
   */
  private void exit(int status) {

    String urls="";
    String[] items=m_mainWindow.getCComboWSDLURL().getItems();
    for(String item:items) {
      /*
       * Replace any space character (space \n \r or \t).
       */
      urls+=item.replace("\\s","")+";";
    }
    Properties.setHistory("URLHistory",urls);

    LOGGER.info("JCows exited normally...");
    
    System.exit(status);
  }

  /**
   * Gets an array containing the names of the attachments
   * that were chosen.
   * 
   * @return a String array containing the attachment names.
   */
  private String[] getAttachmentNames(){
    // TODO: temp. (attachment support disabled for now)
    return null;
    /*
    TableItem[] items = m_gui.getTableAttachment().getItems();
    if (items.length < 1) 
      return null;
    
    String[] names = new String[items.length];
    for (int i = 0; i < names.length; ++i)
      names[i] = (String)items[i].getData();
    
    return names;
    */
  }
}
