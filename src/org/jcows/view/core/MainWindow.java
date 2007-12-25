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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jcows.JCowsException;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;

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
 * A SWT GUI for JCows.
 * Contains all relevant widgets (windows, buttons etc.) to control
 * the JCows logic.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
public class MainWindow extends org.eclipse.swt.widgets.Composite {

  private static final Logger LOGGER = Logger.getLogger(MainWindow.class);

  public static int EDITOR_MODE=0;
  public static int GUI_MODE=1;

  private Menu menu1;
  private Composite compositeChoose;
  private MenuItem menuItemAbout;
  private MenuItem menuItemOnlineHelp;
  private Menu menuHelp;
  private MenuItem menuItemHelp;
  private MenuItem menuItemExit;
  private MenuItem menuItemCloseService;
  private MenuItem menuItemSaveService;
  private MenuItem menuItemOpenService;
  private Composite compositeCenter;
  private Composite compositeSouth;
  private MenuItem menuItemClearURLHistory;
  private Label labelSSLIcon;
  private MenuItem menuItemSeperator9;
  private MenuItem menuItemUndo;
  private CCombo cComboWSDLURL;
  private Button button1;
  private ScrolledComposite scrolledCompositeGUIResponse;
  private ScrolledComposite scrolledCompositeGUIRequest;
  private Composite compositeGUIResponse;
  private Composite compositeGUIRequest;
  private TabItem tabItem1;
  private TabFolder tabFolderGUIMode;
  private Composite compositeToolBarRow2;
  private Composite compositeToolBarRow1;
  private TabItem tabItemGUIResponse;
  private TabItem tabItemGUIRequest;
  private ToolItem toolItemSeperator5;
  private ToolItem toolItemSeperator4;
  private ToolItem toolItemSeperator3;
  private ToolItem toolItemSeperator2;
  private ToolItem toolItemSeperator1;
  private MenuItem menuItemGUIMode;
  private MenuItem menuItemEditorMode;
  private Menu menuView;
  private Composite compositeToolBar;
  private MenuItem menuItemView;
  private ToolItem toolItemGUIMode;
  private ToolItem toolItemEditorMode;
  private ToolItem toolItemSendRequest;
  private ToolBar toolBarView;
  private ToolBar toolBarWebService;
  private ToolItem toolItem1;
  private ToolBar toolBar1;
  private ToolItem toolItemJDKSettings;
  private ToolItem toolItemCut;
  private ToolItem toolItemPaste;
  private ToolItem toolItemCopy;
  private ToolItem toolItemCloseService;
  private ToolItem toolItemSaveService;
  private ToolItem toolItemOpenService;
  private ToolBar toolBarFile;
  private ToolBar toolBarSettings;
  private ToolBar toolBarEdit;
  private MenuItem menuItemJDKSettings;
  private Menu menuSettings;
  private MenuItem menuItemSettings;
  private ProgressBar progressBar;
  private Composite compositeWebService;
  private Text textStatus;
  private Composite compositeStatus;
  private Composite compositeNorth;
  private Button buttonConnect;
  private Text textURL;
  private Label labelURL;
  private Label labelSeperator4;
  private Composite compositeURLBar;
  private MenuItem menuItem1;
  private MenuItem menuItemInsertXMLDocument;
  private TabFolder tabFolderEditorMode;
  private StyledText styledTextSOAPResponse;
  private TabItem tabItemEditorResponse;
  private TabItem tabItemEditorRequest;
  private MenuItem menuItemSeperator3;
  private MenuItem menuItemSelectAll;
  private MenuItem menuItemPaste;
  private Label labelSeperator3;
  private Label labelSeperator2;
  private Label labelSeperator1;
  private Label labelIconMethod;
  private Label labelIconPort;
  private Label labelIconService;
  private Composite compositeButtonAttachment;
  private Button buttonRemoveAttachement;
  private Button buttonAddAttachment;
  private MenuItem menuItemDelete;
  private MenuItem menuItemCopy;
  private MenuItem menuItemCut;
  private MenuItem menuItemSeperator2;
  private MenuItem menuItemSeperator1;
  private MenuItem menuItemOpenWSDL;
  private MenuItem menuItemExportSOAPMessage;
  private MenuItem menuItemImportSOAPMessage;
  private Menu menuEdit;
  private MenuItem menuItemEdit;
  private TableItem tableItem2;
  private TableItem tableItem1;
  private Table tableAttachment;
  private Combo comboMethod;
  private Label labelOperation;
  private Combo comboPort;
  private Label labelPort;
  private Combo comboService;
  private Label labelService;
  private Group groupAttachment;
  private StyledText styledTextSOAPRequest;
  private Menu menuFile;
  private MenuItem menuItemFile;

  private String m_textURL="";
  private int m_mode;

  {
    //Register as a resource user - SWTResourceManager will
    //handle the obtaining and disposing of resources
    SWTResourceManager.registerResourceUser(this);
  }
  /** 
   * Constructs a new instance of this class given its parent and 
   * a style value describing its behaviour and appearance.
   * 
   * @param parent a shell which will be the parent of the new instance.
   * @param style the style of control to construct.
   */
  public MainWindow(Composite parent, int style) throws JCowsException {
    super(parent, style);
    initGUI();
  }

  /**
   * Initializes the GUI components.
   *
   */
  private void initGUI() throws JCowsException {
    try {
      this.setSize(600, 600);
      GridLayout thisLayout = new GridLayout();
      thisLayout.marginHeight = 0;
      thisLayout.makeColumnsEqualWidth = true;
      thisLayout.marginWidth = 0;
      thisLayout.verticalSpacing = 0;
      thisLayout.horizontalSpacing = 0;
      this.setLayout(thisLayout);
      {
        compositeToolBar = new Composite(this, SWT.NONE);
        RowLayout compositeToolBarLayout = new RowLayout(
            org.eclipse.swt.SWT.VERTICAL);
        GridData compositeToolBarLData = new GridData();
        compositeToolBarLData.grabExcessHorizontalSpace = true;
        compositeToolBarLData.horizontalAlignment = GridData.FILL;
        compositeToolBar.setLayoutData(compositeToolBarLData);
        compositeToolBar.setLayout(compositeToolBarLayout);
        {
          compositeToolBarRow1 = new Composite(compositeToolBar, SWT.NONE);
          RowLayout compositeToolBarRow1Layout = new RowLayout(
              org.eclipse.swt.SWT.HORIZONTAL);
          compositeToolBarRow1.setLayout(compositeToolBarRow1Layout);
          {
            toolBarFile = new ToolBar(compositeToolBarRow1, SWT.FLAT);
            {
              toolItemOpenService = new ToolItem(toolBarFile, SWT.NONE);
              toolItemOpenService.setImage(SWTResourceManager
                  .getImage("resources/open.png"));
              toolItemOpenService.setToolTipText("Open Service");
            }
            {
              toolItemSaveService = new ToolItem(toolBarFile, SWT.NONE);
              toolItemSaveService.setImage(SWTResourceManager
                  .getImage("resources/save.png"));
              toolItemSaveService.setToolTipText("Save Service");
            }
            {
              toolItemCloseService = new ToolItem(toolBarFile, SWT.NONE);
              toolItemCloseService.setImage(SWTResourceManager
                  .getImage("resources/close.png"));
              toolItemCloseService.setToolTipText("Close Service");
            }
            {
              toolItemSeperator1 = new ToolItem(toolBarFile, SWT.SEPARATOR);
            }
            toolBarFile.pack();
            Point size = toolBarFile.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          }
          {
            toolBarEdit = new ToolBar(compositeToolBarRow1, SWT.FLAT);
            {
              toolItemCopy = new ToolItem(toolBarEdit, SWT.NONE);
              toolItemCopy.setImage(SWTResourceManager
                  .getImage("resources/copy.png"));
            }
            {
              toolItemPaste = new ToolItem(toolBarEdit, SWT.NONE);
              toolItemPaste.setImage(SWTResourceManager
                  .getImage("resources/paste.png"));
            }
            {
              toolItemCut = new ToolItem(toolBarEdit, SWT.NONE);
              toolItemCut.setImage(SWTResourceManager
                  .getImage("resources/cut.png"));
            }
            {
              toolItemSeperator3 = new ToolItem(toolBarEdit, SWT.SEPARATOR);
            }
            toolBarEdit.pack();
            Point size = toolBarEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          }
          {
            toolBarSettings = new ToolBar(compositeToolBarRow1, SWT.FLAT);
            {
              toolItemJDKSettings = new ToolItem(toolBarSettings, SWT.NONE);
              toolItemJDKSettings.setImage(SWTResourceManager
                  .getImage("resources/jdk_icon.png"));
            }
            {
              toolItemSeperator5 = new ToolItem(toolBarSettings, SWT.SEPARATOR);
            }
            toolBarSettings.pack();
            Point size = toolBarSettings.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          }
        }
        {
          compositeToolBarRow2 = new Composite(compositeToolBar, SWT.NONE);
          RowLayout compositeToolBarRow2Layout = new RowLayout(
              org.eclipse.swt.SWT.HORIZONTAL);
          compositeToolBarRow2.setLayout(compositeToolBarRow2Layout);
          {
            toolBarWebService = new ToolBar(compositeToolBarRow2, SWT.FLAT);
            {
              toolItemSendRequest = new ToolItem(toolBarWebService, SWT.NONE);
              toolItemSendRequest.setImage(SWTResourceManager
                  .getImage("resources/send_enabled.png"));
              toolItemSendRequest.setDisabledImage(SWTResourceManager
                  .getImage("resources/send_disabled.png"));
              toolItemSendRequest.setToolTipText("Send Request");
              toolItemSendRequest.setEnabled(false);

            }
            {
              toolItemSeperator4 = new ToolItem(
                  toolBarWebService,
                  SWT.SEPARATOR);
            }
            toolBarWebService.pack();
            Point size = toolBarWebService
            .computeSize(SWT.DEFAULT, SWT.DEFAULT);
          }
          {
            toolBarView = new ToolBar(compositeToolBarRow2, SWT.FLAT);
            {
              toolItemEditorMode = new ToolItem(toolBarView, SWT.CHECK);
              toolItemEditorMode.setImage(SWTResourceManager
                  .getImage("resources/editor.png"));
              toolItemEditorMode.setToolTipText("Editor Mode");
              toolItemEditorMode.setSelection(true);
            }
            {
              toolItemGUIMode = new ToolItem(toolBarView, SWT.CHECK);
              toolItemGUIMode.setImage(SWTResourceManager
                  .getImage("resources/gui.png"));
              toolItemGUIMode.setToolTipText("GUI Mode");
            }
            {
              toolItemSeperator2 = new ToolItem(toolBarView, SWT.SEPARATOR);
            }
            toolBarView.pack();
            Point size = toolBarView.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          }
        }
      }
      {
        GridData labelSeperatorLData = new GridData();
        labelSeperatorLData.horizontalAlignment = GridData.FILL;
        labelSeperatorLData.grabExcessHorizontalSpace = true;
        labelSeperator1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        labelSeperator1.setLayoutData(labelSeperatorLData);
      }
      {
        compositeNorth = new Composite(this, SWT.NONE);
        GridLayout compositeNorthLayout = new GridLayout();
        compositeNorthLayout.makeColumnsEqualWidth = true;
        compositeNorthLayout.marginWidth = 0;
        compositeNorthLayout.verticalSpacing = 0;
        compositeNorthLayout.marginHeight = 0;
        compositeNorthLayout.horizontalSpacing = 0;
        GridData compositeNorthLData = new GridData();
        compositeNorthLData.horizontalAlignment = GridData.FILL;
        compositeNorthLData.grabExcessHorizontalSpace = true;
        compositeNorth.setLayoutData(compositeNorthLData);
        compositeNorth.setLayout(compositeNorthLayout);
        {
          compositeURLBar = new Composite(compositeNorth, SWT.NONE);
          GridLayout compositeURLBarLayout = new GridLayout();
          compositeURLBarLayout.numColumns = 3;
          GridData compositeURLBarLData = new GridData();
          compositeURLBarLData.horizontalAlignment = GridData.FILL;
          compositeURLBar.setLayoutData(compositeURLBarLData);
          compositeURLBar.setLayout(compositeURLBarLayout);
          {
            labelURL = new Label(compositeURLBar, SWT.NONE);
            labelURL.setText("WSDL URL:");
          }
          {
            GridData cComboWSDLURLLData = new GridData();
            cComboWSDLURLLData.horizontalAlignment = GridData.FILL;
            cComboWSDLURLLData.grabExcessHorizontalSpace = true;
            cComboWSDLURL = new CCombo(compositeURLBar, SWT.NONE);
            cComboWSDLURL.setLayoutData(cComboWSDLURLLData);
            cComboWSDLURL.setText(Properties.getConfig("defaultWSDLURL"));
          }
          {
            buttonConnect = new Button(compositeURLBar, SWT.ARROW
                | SWT.FLAT
                | SWT.RIGHT);
            buttonConnect.setToolTipText("Connect to Web Service");
          }
        }
        {
          GridData labelSeperator4LData = new GridData();
          labelSeperator4LData.horizontalAlignment = GridData.FILL;
          labelSeperator4LData.grabExcessHorizontalSpace = true;
          labelSeperator4 = new Label(compositeNorth, SWT.SEPARATOR
              | SWT.HORIZONTAL);
          labelSeperator4.setLayoutData(labelSeperator4LData);
        }
        {
          GridData compositeChooseLData = new GridData();
          compositeChooseLData.horizontalAlignment = GridData.FILL;
          compositeChooseLData.grabExcessHorizontalSpace = true;
          compositeChoose = new Composite(compositeNorth, SWT.NONE);
          GridLayout compositeChooseLayout = new GridLayout();
          compositeChoose.setLayoutData(compositeChooseLData);
          compositeChooseLayout.numColumns = 2;
          compositeChooseLayout.makeColumnsEqualWidth = true;
          compositeChooseLayout.marginWidth = 0;
          compositeChooseLayout.marginHeight = 0;
          compositeChooseLayout.horizontalSpacing = 0;
          compositeChooseLayout.verticalSpacing = 0;
          compositeChoose.setLayout(compositeChooseLayout);
          {
            compositeWebService = new Composite(compositeChoose, SWT.NONE);
            GridLayout compositeWebServiceLayout = new GridLayout();
            compositeWebServiceLayout.numColumns = 3;
            compositeWebServiceLayout.verticalSpacing = 0;
            GridData compositeWebServiceLData = new GridData();
            compositeWebServiceLData.grabExcessHorizontalSpace = true;
            compositeWebService.setLayoutData(compositeWebServiceLData);
            compositeWebService.setLayout(compositeWebServiceLayout);
            {
              labelIconService = new Label(compositeWebService, SWT.NONE);
              labelIconService.setImage(SWTResourceManager
                  .getImage("resources/service.png"));
            }
            {
              labelService = new Label(compositeWebService, SWT.NONE);
              labelService.setText("Services");

            }
            {
              GridData comboServiceLData = new GridData();
              comboServiceLData.widthHint = 180;
              comboServiceLData.heightHint = 21;
              comboService = new Combo(compositeWebService, SWT.READ_ONLY);
              comboService.setLayoutData(comboServiceLData);
              comboService.setEnabled(false);
            }
            {
              labelIconPort = new Label(compositeWebService, SWT.NONE);
              labelIconPort.setImage(SWTResourceManager
                  .getImage("resources/port.png"));
            }
            {
              labelPort = new Label(compositeWebService, SWT.NONE);
              labelPort.setText("Port");
            }
            {
              GridData comboPortLData = new GridData();
              comboPortLData.widthHint = 180;
              comboPortLData.heightHint = 21;
              comboPort = new Combo(compositeWebService, SWT.READ_ONLY);
              comboPort.setLayoutData(comboPortLData);
              comboPort.setEnabled(false);
            }
            {
              labelIconMethod = new Label(
                  compositeWebService,
                  SWT.READ_ONLY);
              labelIconMethod.setImage(SWTResourceManager
                  .getImage("resources/method.png"));
            }
            {
              labelOperation = new Label(compositeWebService, SWT.NONE);
              labelOperation.setText("Operation");
            }
            {
              GridData comboMethodLData = new GridData();
              comboMethodLData.widthHint = 180;
              comboMethodLData.heightHint = 21;
              comboMethod = new Combo(compositeWebService, SWT.READ_ONLY);
              comboMethod.setLayoutData(comboMethodLData);
              comboMethod.setEnabled(false);
            }
          }
          {
            /*
				groupAttachment = new Group(compositeChoose, SWT.NONE);
				GridLayout groupAttachmentLayout = new GridLayout();
				groupAttachmentLayout.makeColumnsEqualWidth = true;
				groupAttachment.setLayout(groupAttachmentLayout);
				GridData groupAttachmentLData = new GridData();
				groupAttachmentLData.horizontalAlignment = GridData.END;
				groupAttachmentLData.widthHint = 170;
				groupAttachmentLData.heightHint = 90;
				groupAttachment.setLayoutData(groupAttachmentLData);
				groupAttachment.setText("Attachment");
				groupAttachment.setCursor(SWTResourceManager
					.getCursor(SWT.CURSOR_WAIT));
				{
					GridData tableAttachmentLData = new GridData();
					tableAttachmentLData.horizontalAlignment = GridData.FILL;
					tableAttachmentLData.grabExcessHorizontalSpace = true;
					tableAttachmentLData.horizontalSpan = 2;
					tableAttachmentLData.heightHint = 30;
					tableAttachment = new Table(groupAttachment, SWT.NONE);
					tableAttachment.setLayoutData(tableAttachmentLData);
				}
				{
					compositeButtonAttachment = new Composite(
						groupAttachment,
						SWT.NONE);
					RowLayout compositeButtonAttachmentLayout = new RowLayout(
						org.eclipse.swt.SWT.HORIZONTAL);
					GridData compositeButtonAttachmentLData = new GridData();
					compositeButtonAttachmentLData.horizontalAlignment = GridData.END;
					compositeButtonAttachment
						.setLayoutData(compositeButtonAttachmentLData);
					compositeButtonAttachment
						.setLayout(compositeButtonAttachmentLayout);
					{
						buttonAddAttachment = new Button(
							compositeButtonAttachment,
							SWT.PUSH | SWT.CENTER);
						RowData buttonAddAttachmentLData = new RowData(60, 23);
						buttonAddAttachmentLData.width = 60;
						buttonAddAttachmentLData.height = 23;
						buttonAddAttachment
							.setLayoutData(buttonAddAttachmentLData);
						buttonAddAttachment.setText("Add");
						buttonAddAttachment.setSize(60, 23);
					}
					{
						buttonRemoveAttachement = new Button(
							compositeButtonAttachment,
							SWT.PUSH | SWT.CENTER);
						RowData buttonRemoveAttachementLData = new RowData(
							60,
							23);
						buttonRemoveAttachementLData.width = 60;
						buttonRemoveAttachementLData.height = 23;
						buttonRemoveAttachement
							.setLayoutData(buttonRemoveAttachementLData);
						buttonRemoveAttachement.setText("Remove");
						buttonRemoveAttachement.setSize(60, 23);
					}
				}*/
          }
        }
      }
      {
        labelSeperator2 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData label1LData = new GridData();
        label1LData.horizontalAlignment = GridData.FILL;
        label1LData.grabExcessHorizontalSpace = true;
        labelSeperator2.setLayoutData(label1LData);
      }
      {
        compositeCenter = new Composite(this, SWT.NONE);
        GridLayout compositeCenterLayout = new GridLayout();
        compositeCenterLayout.makeColumnsEqualWidth = true;
        compositeCenterLayout.verticalSpacing = 0;
        compositeCenterLayout.marginWidth = 0;
        compositeCenterLayout.marginHeight = 0;
        compositeCenterLayout.horizontalSpacing = 0;
        GridData compositeCenterLData = new GridData();
        compositeCenterLData.horizontalAlignment = GridData.FILL;
        compositeCenterLData.verticalAlignment = GridData.FILL;
        compositeCenterLData.grabExcessVerticalSpace = true;
        compositeCenterLData.grabExcessHorizontalSpace = true;
        compositeCenter.setLayoutData(compositeCenterLData);
        compositeCenter.setLayout(compositeCenterLayout);
        {
          tabFolderEditorMode = new TabFolder(compositeCenter, SWT.NONE);
          {
            tabItemEditorRequest = new TabItem(tabFolderEditorMode, SWT.NONE);
            tabItemEditorRequest.setText("Request");
            {
              styledTextSOAPRequest = new StyledText(
                  tabFolderEditorMode,
                  SWT.V_SCROLL | SWT.H_SCROLL);
              tabItemEditorRequest.setControl(styledTextSOAPRequest);
              styledTextSOAPRequest.setFont(SWTResourceManager.getFont(
                  "Tahoma",
                  9,
                  0,
                  false,
                  false));
            }
          }
          {
            tabItemEditorResponse = new TabItem(tabFolderEditorMode, SWT.NONE);
            tabItemEditorResponse.setText("Response");
            {
              styledTextSOAPResponse = new StyledText(
                  tabFolderEditorMode,
                  SWT.V_SCROLL | SWT.H_SCROLL);
              tabItemEditorResponse.setControl(styledTextSOAPResponse);
              styledTextSOAPResponse.setFont(SWTResourceManager.getFont(
                  "Tahoma",
                  9,
                  0,
                  false,
                  false));
            }
          }
          GridData tabFolderTextSOAPLData = new GridData();
          tabFolderTextSOAPLData.horizontalAlignment = GridData.FILL;
          tabFolderTextSOAPLData.grabExcessHorizontalSpace = true;
          tabFolderTextSOAPLData.verticalAlignment = GridData.FILL;
          tabFolderTextSOAPLData.grabExcessVerticalSpace = true;
          tabFolderEditorMode.setLayoutData(tabFolderTextSOAPLData);
          tabFolderEditorMode.setSelection(0);
        }
        {
          tabFolderGUIMode = new TabFolder(compositeCenter, SWT.NONE);
          {
            tabItemGUIRequest = new TabItem(tabFolderGUIMode, SWT.NONE);
            tabItemGUIRequest.setText("Request");
            {
              scrolledCompositeGUIRequest = new ScrolledComposite(
                  tabFolderGUIMode, SWT.BORDER |
                  SWT.H_SCROLL | SWT.V_SCROLL);
              tabItemGUIRequest.setControl(scrolledCompositeGUIRequest);
              scrolledCompositeGUIRequest.setExpandHorizontal(true);
              scrolledCompositeGUIRequest.setExpandVertical(true);
              {
                compositeGUIRequest = new Composite(
                    scrolledCompositeGUIRequest,
                    SWT.NONE);
                scrolledCompositeGUIRequest.setContent(compositeGUIRequest);
                GridLayout compositeGUIRequestLayout = new GridLayout();
                compositeGUIRequest.setLayout(compositeGUIRequestLayout);
                compositeGUIRequest.addControlListener(new ControlAdapter() {
                  public void controlResized(ControlEvent evt) {
                    LOGGER.debug(evt);
                    scrolledCompositeGUIRequestUpdate();
                  }
                });
              }
            }
          }
          {
            tabItemGUIResponse = new TabItem(tabFolderGUIMode, SWT.NONE);
            tabItemGUIResponse.setText("Response");
            {
              scrolledCompositeGUIResponse = new ScrolledComposite(
                  tabFolderGUIMode, SWT.BORDER |
                  SWT.H_SCROLL | SWT.V_SCROLL);
              tabItemGUIResponse.setControl(scrolledCompositeGUIResponse);
              scrolledCompositeGUIResponse.setExpandHorizontal(true);
              scrolledCompositeGUIResponse.setExpandVertical(true);
              {
                compositeGUIResponse = new Composite(
                    scrolledCompositeGUIResponse,
                    SWT.NONE);
                scrolledCompositeGUIResponse.setContent(compositeGUIResponse);
                GridLayout compositeGUIResponseLayout = new GridLayout();
                compositeGUIResponse.setLayout(compositeGUIResponseLayout);
                compositeGUIResponse.addControlListener(new ControlAdapter() {
                  public void controlResized(ControlEvent evt) {
                    LOGGER.debug(evt);
                    scrolledCompositeGUIResponseUpdate();
                  }
                });
              }
            }
          }
          GridData tabFolderGUIModeLData = new GridData();
          tabFolderGUIModeLData.grabExcessHorizontalSpace = true;
          tabFolderGUIModeLData.grabExcessVerticalSpace = true;
          tabFolderGUIModeLData.horizontalAlignment = GridData.FILL;
          tabFolderGUIModeLData.verticalAlignment = GridData.FILL;
          tabFolderGUIModeLData.exclude = true;
          tabFolderGUIMode.setLayoutData(tabFolderGUIModeLData);
          tabFolderGUIMode.setSelection(0);
        }
      }
      {
        labelSeperator3 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData label2LData = new GridData();
        label2LData.horizontalAlignment = GridData.FILL;
        label2LData.grabExcessHorizontalSpace = true;
        labelSeperator3.setLayoutData(label2LData);
      }
      {
        compositeSouth = new Composite(this, SWT.NONE);
        GridLayout compositeSouthLayout = new GridLayout();
        compositeSouthLayout.makeColumnsEqualWidth = true;
        compositeSouthLayout.horizontalSpacing = 0;
        compositeSouthLayout.marginHeight = 0;
        compositeSouthLayout.marginWidth = 0;
        compositeSouthLayout.verticalSpacing = 0;
        GridData compositeSouthLData = new GridData();
        compositeSouthLData.horizontalAlignment = GridData.FILL;
        compositeSouth.setLayoutData(compositeSouthLData);
        compositeSouth.setLayout(compositeSouthLayout);
        {
          compositeStatus = new Composite(compositeSouth, SWT.NONE);
          GridLayout compositeStatusLayout = new GridLayout();
          compositeStatusLayout.horizontalSpacing = 0;
          compositeStatusLayout.marginHeight = 0;
          compositeStatusLayout.marginWidth = 0;
          compositeStatusLayout.verticalSpacing = 0;
          compositeStatusLayout.numColumns = 3;
          GridData compositeStatusLData = new GridData();
          compositeStatusLData.horizontalAlignment = GridData.FILL;
          compositeStatusLData.grabExcessHorizontalSpace = true;
          compositeStatus.setLayoutData(compositeStatusLData);
          compositeStatus.setLayout(compositeStatusLayout);
          {
            labelSSLIcon = new Label(compositeStatus, SWT.NONE);
            setSSLMode(false,null);

          }
          {
            GridData textStatusLData = new GridData();
            textStatusLData.horizontalAlignment = GridData.FILL;
            textStatusLData.grabExcessHorizontalSpace = true;
            textStatus = new Text(compositeStatus, SWT.BORDER);
            textStatus.setLayoutData(textStatusLData);
            textStatus.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
            textStatus.setEditable(false);
          }
          {
            GridData progressBarStatusLData = new GridData();
            progressBarStatusLData.horizontalAlignment = GridData.END;
            progressBar = new ProgressBar(compositeStatus, SWT.SMOOTH);
            progressBar.setForeground(SWTResourceManager.getColor(254, 162, 57));
            progressBar.setLayoutData(progressBarStatusLData);
          }
        }
      }
      {
        menu1 = new Menu(getShell(), SWT.BAR);
        getShell().setMenuBar(menu1);
        {
          menuItemFile = new MenuItem(menu1, SWT.CASCADE);
          menuItemFile.setText("File");
          {
            menuFile = new Menu(menuItemFile);
            {
              menuItemOpenService = new MenuItem(menuFile, SWT.CASCADE);
              menuItemOpenService.setText("&Open Service\tCtrl+O");
              menuItemOpenService.setImage(SWTResourceManager.getImage("resources/open.png"));
              menuItemOpenService.setAccelerator(SWT.MOD1+'O');
            }
            {
              menuItemSaveService = new MenuItem(menuFile, SWT.CASCADE);
              menuItemSaveService.setText("Save &Service\tCtrl+S");
              menuItemSaveService.setImage(SWTResourceManager.getImage("resources/save.png"));
              menuItemSaveService.setAccelerator(SWT.MOD1+'S');
            }
            {
              menuItemCloseService = new MenuItem(menuFile, SWT.CASCADE);
              menuItemCloseService.setText("Close Service\tCtrl+W");
              menuItemCloseService.setImage(SWTResourceManager.getImage("resources/close.png"));
              menuItemCloseService.setAccelerator(SWT.MOD1+'W');
            }
            {
              menuItemSeperator1 = new MenuItem(
                  menuFile,
                  SWT.SEPARATOR);
            }
            {
              menuItemImportSOAPMessage = new MenuItem(menuFile, SWT.PUSH);
              menuItemImportSOAPMessage.setText("&Import SOAP\tCtrl+I");
              menuItemImportSOAPMessage.setAccelerator(SWT.MOD1+'I');
              menuItemImportSOAPMessage.setImage(SWTResourceManager.getImage("resources/import.png"));
            }
            {
              menuItemExportSOAPMessage = new MenuItem(menuFile, SWT.PUSH);
              menuItemExportSOAPMessage.setText("&Export SOAP\tCtrl+P");
              menuItemExportSOAPMessage.setAccelerator(SWT.MOD1+'P');
              menuItemExportSOAPMessage.setImage(SWTResourceManager.getImage("resources/export.png"));
            }
            {
              menuItemSeperator2 = new MenuItem(
                  menuFile,
                  SWT.SEPARATOR);
            }
            {
              menuItemInsertXMLDocument = new MenuItem(menuFile, SWT.PUSH);
              menuItemInsertXMLDocument.setText("Insert XML Document");
              menuItemInsertXMLDocument.setImage(SWTResourceManager.getImage("resources/xml.png"));
            }
            {
              menuItemSeperator3 = new MenuItem(
                  menuFile,
                  SWT.SEPARATOR);
            }
            {
              menuItemExit = new MenuItem(menuFile, SWT.CASCADE);
              menuItemExit.setText("Exit\tAlt+F4");
              menuItemExit.setImage(SWTResourceManager.getImage("resources/exit.png"));
            }
            menuItemFile.setMenu(menuFile);
          }
        }
        {
          menuItemEdit = new MenuItem(menu1, SWT.CASCADE);
          menuItemEdit.setText("Edit");
          {
            menuEdit = new Menu(menuItemEdit);
            menuItemEdit.setMenu(menuEdit);
            {
              menuItemUndo = new MenuItem(menuEdit, SWT.PUSH);
              menuItemUndo.setText("Undo\tCtrl+Z");
              menuItemUndo.setImage(SWTResourceManager.getImage("resources/undo.png"));
              menuItemUndo.setAccelerator(SWT.MOD1+'Z');
            }
            {
              menuItemSeperator9 = new MenuItem(menuEdit, SWT.SEPARATOR);
            }
            {
              menuItemCut = new MenuItem(menuEdit, SWT.PUSH);
              menuItemCut.setText("Cut\tCtrl+X");
              menuItemCut.setImage(SWTResourceManager.getImage("resources/cut.png"));
              menuItemCut.setAccelerator(SWT.MOD1+'X');
            }
            {
              menuItemCopy = new MenuItem(menuEdit, SWT.PUSH);
              menuItemCopy.setText("&Copy\tCtrl+C");
              menuItemCopy.setImage(SWTResourceManager.getImage("resources/copy.png"));
              menuItemCopy.setAccelerator(SWT.MOD1+'C');
            }
            {
              menuItemPaste = new MenuItem(menuEdit, SWT.PUSH);
              menuItemPaste.setText("Paste\tCtrl+V");
              menuItemPaste.setImage(SWTResourceManager.getImage("resources/paste.png"));
              menuItemPaste.setAccelerator(SWT.MOD1+'V');
            }
            {
              menuItemSeperator3 = new MenuItem(menuEdit, SWT.SEPARATOR);
            }
            {
              menuItemDelete = new MenuItem(menuEdit, SWT.PUSH);
              menuItemDelete.setImage(SWTResourceManager.getImage("resources/delete.png"));
              menuItemDelete.setText("Delete\tDelete");

            }
            {
              menuItemSelectAll = new MenuItem(menuEdit, SWT.PUSH);
              menuItemSelectAll.setText("Select &All\tCtrl+A");
              menuItemSelectAll.setAccelerator(SWT.MOD1+'A');
            }
          }
        }
        {
          menuItemView = new MenuItem(menu1, SWT.CASCADE);
          menuItemView.setText("View");
          {
            menuView = new Menu(menuItemView);
            menuItemView.setMenu(menuView);
            {
              menuItemEditorMode = new MenuItem(menuView, SWT.CHECK);
              menuItemEditorMode.setText("XML Editor Mode\tCTRL+E");
              menuItemEditorMode.setImage(SWTResourceManager.getImage("resources/editor.png"));
              menuItemEditorMode.setAccelerator(SWT.MOD1+'E');
              menuItemEditorMode.setSelection(true);
            }
            {
              menuItemGUIMode = new MenuItem(menuView, SWT.CHECK);
              menuItemGUIMode.setText("GUI Mode\tCTRL+G");
              menuItemGUIMode.setAccelerator(SWT.MOD1+'G');
              menuItemGUIMode.setImage(SWTResourceManager.getImage("resources/gui.png"));
            }
          }
        }
        {
          menuItemSettings = new MenuItem(menu1, SWT.CASCADE);
          menuItemSettings.setText("Settings");
          {
            menuSettings = new Menu(menuItemSettings);
            menuItemSettings.setMenu(menuSettings);
            {
              menuItemJDKSettings = new MenuItem(menuSettings, SWT.CASCADE);
              menuItemJDKSettings.setText("Java SDK Settings");
              menuItemJDKSettings.setImage(SWTResourceManager
                  .getImage("resources/jdk_icon.png"));
            }
            {
              menuItemClearURLHistory = new MenuItem(menuSettings, SWT.CASCADE);
              menuItemClearURLHistory.setText("Clear URL History");
              menuItemClearURLHistory.setImage(SWTResourceManager
                  .getImage("resources/delete.png"));
            }
          }
        }
        {
          menuItemHelp = new MenuItem(menu1, SWT.CASCADE);
          menuItemHelp.setText("Help");
          {
            menuHelp = new Menu(menuItemHelp);
            {
              menuItemOnlineHelp = new MenuItem(menuHelp, SWT.CASCADE);
              menuItemOnlineHelp.setText("Online Help");
              menuItemOnlineHelp.setImage(SWTResourceManager.getImage("resources/help.png"));
            }
            {
              menuItemAbout = new MenuItem(menuHelp, SWT.CASCADE);
              menuItemAbout.setText("About");
              menuItemAbout.setImage(SWTResourceManager.getImage("resources/about.png"));
            }
            menuItemHelp.setMenu(menuHelp);
          }
        }
      }
      this.layout();
    } catch(Exception e) {
      throw new JCowsException(Properties.getMessage("error.Exception"),e);
    }
  }
  
  /**
   * Attaches a listener for the file menu open service item.
   * 
   * @param listener the listener which should be notified.
   */ 
  public void addMainWindowMenuItemOpenServiceListener(SelectionAdapter listener) {
    menuItemOpenService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu open service tool item.
   * 
   * @param listener the listener which should be notified.
   */  
  public void addMainWindowToolItemOpenServiceListener(SelectionAdapter listener) {
    toolItemOpenService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu save service item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemSaveServiceListener(SelectionAdapter listener) {
    menuItemSaveService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the save service tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemSaveServiceListener(SelectionAdapter listener) {
    toolItemSaveService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu close service item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemCloseServiceListener(SelectionAdapter listener) {
    menuItemCloseService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu import SOAP message item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemImportSOAPMessageListener(SelectionAdapter listener) {
    menuItemImportSOAPMessage.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu export SOAP message item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemExportSOAPMessageListener(SelectionAdapter listener) {
    menuItemExportSOAPMessage.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu import XML document item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemInsertXMLDocumentListener(SelectionAdapter listener) {
    menuItemInsertXMLDocument.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu insert XML document tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemCloseServiceListener(SelectionAdapter listener) {
    toolItemCloseService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the file menu exit item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemExitListener(SelectionAdapter listener) {
    menuItemExit.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemEditListener(SelectionAdapter listener) {
    menuItemEdit.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu undo item.
   * 
   * @param listener the listener which should be notified.
   */  
  public void addMainWindowMenuItemUndoListener(SelectionAdapter listener) {
    menuItemUndo.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu cut item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemCutListener(SelectionAdapter listener) {
    menuItemCut.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu cut tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemCutListener(SelectionAdapter listener) {
    toolItemCut.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu copy item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemCopyListener(SelectionAdapter listener) {
    menuItemCopy.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu copy tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemCopyListener(SelectionAdapter listener) {
    toolItemCopy.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu paste item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemPasteListener(SelectionAdapter listener) {
    menuItemPaste.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu paste tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemPasteListener(SelectionAdapter listener) {
    toolItemPaste.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu delete item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemDeleteListener(SelectionAdapter listener) {
    menuItemDelete.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the edit menu select all.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemSelectAllListener(SelectionAdapter listener) {
    menuItemSelectAll.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the view menu editor mode item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemEditorModeListener(SelectionAdapter listener) {
    menuItemEditorMode.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the view menu editor mode tool item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemEditorModeListener(SelectionAdapter listener) {
    toolItemEditorMode.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the view menu GUI mode item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemGUIModeListener(SelectionAdapter listener) {
    menuItemGUIMode.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the view menu GUI mode item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemGUIModeListener(SelectionAdapter listener) {
    toolItemGUIMode.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the send request button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemSendRequestListener(SelectionAdapter listener) {
    toolItemSendRequest.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the settings menu Java SDK settings item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemJDKSettingsListener(SelectionAdapter listener) {
    menuItemJDKSettings.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the settings menu clear URL history item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemClearURLHistoryListener(SelectionAdapter listener) {
    menuItemClearURLHistory.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the settings menu Java SDK settings item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowToolItemJDKSettingsListener(SelectionAdapter listener) {
    toolItemJDKSettings.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the help menu help item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemHelpListener(SelectionAdapter listener) {
    menuItemOnlineHelp.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the help menu about item.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowMenuItemAboutListener(SelectionAdapter listener) {
    menuItemAbout.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the WSDL URL go button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowConnectListener(SelectionAdapter listener) {
    buttonConnect.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the send SOAP message button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowSOAPToolItemSendRequestListener(SelectionAdapter listener) {
    toolItemSendRequest.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the Web Service service combo box.
   * 
   * @param listener the listener which should be notified.
   */  
  public void addMainWindowChangeServiceListener(SelectionAdapter listener) {
    comboService.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the Web Service port combo box.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowChangePortListener(SelectionAdapter listener) {
    comboPort.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the Web Service operation combo box.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowChangeOperationListener(SelectionAdapter listener) {
    comboMethod.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the add attachment button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowAttachmentAddListener(SelectionAdapter listener) {
    buttonAddAttachment.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the remove attachment button.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowAttachmentRemoveListener(SelectionAdapter listener) {
    buttonRemoveAttachement.addSelectionListener(listener);
  }
  
  /**
   * Attaches a listener for the SOAP message text box.
   * 
   * @param listener the listener which should be notified.
   */  
  public void addMainWindowSOAPMessageListener(Listener listener) {
    styledTextSOAPRequest.addListener(SWT.KeyDown,listener);
  }
  
  /**
   * Attaches a listener for the WSDL URL combo box.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowCComboWSDLURLLListener(Listener listener) {
    cComboWSDLURL.addListener(SWT.KeyDown,listener);
  }
  
  /**
   * Attaches a key listener for the main window.
   * 
   * @param listener the listener which should be notified.
   */
  public void addMainWindowKeyListener(Listener listener) {
    this.addListener(SWT.KeyUp,listener);
  }
  
  /** 
   * Returns the table with attachments.
   * 
   * @return the table with attachments
   */
  public Table getTableAttachment() {
    return tableAttachment;
  }
  
  /** 
   * Returns the text box for the SOAP message request.
   * 
   * @return the text box for the SOAP message request.
   */
  public StyledText getStyledTextSOAPRequest() {
    return styledTextSOAPRequest;
  }
  
  /** 
   * Returns the text box for the SOAP message response.
   * 
   * @return the text box for the SOAP message response.
   */
  public StyledText getStyledTextSOAPResponse() {
    return styledTextSOAPResponse;
  }
  
  /** 
   * Returns the combo box with the services of the Web Service.
   * 
   * @return the combo box with the services of the Web Service.
   */
  public Combo getComboService() {
    return comboService;
  }
  
  /** 
   * Returns the combo box with the ports of the Web Service.
   * 
   * @return the combo box with the ports of the Web Service.
   */
  public Combo getComboPort() {
    return comboPort;
  }
  
  /** 
   * Returns the combo box with the operations of the Web Service.
   * 
   * @return the combo box with the operations of the Web Service.
   */ 
  public Combo getComboMethod() {
    return comboMethod;
  }
  
  /** 
   * Returns the tab folder that contains the tabs for the request and response in view mode.
   * 
   * @return tab folder for the request and response in view mode.
   */
  public TabFolder getTabFolderEditorMode(){
    return tabFolderEditorMode;
  }
  
  /** 
   * Returns the tab folder that contains the tabs for the request and response in GUI mode.
   * 
   * @return tab folder for the request and response in GUI mode.
   */
  public TabFolder getTabFolderGUIMode(){
    return tabFolderGUIMode;
  }
  
  /**
   * Return the active styled text area. It depends on the tab selection.
   * 
   * @return the active styled text area.
   */
  public StyledText getActiveStyledText()
  {
    if(getTabFolderEditorMode().getSelectionIndex()==0)
      return styledTextSOAPRequest;
    if(getTabFolderEditorMode().getSelectionIndex()==1)
      return styledTextSOAPResponse;
    return null;
  }
  
  /**
   * Returns the WSDL URL combo box. This combo box is to enter the WSDL URL.
   * 
   * @return the WSDL URL combo box.
   */
  public CCombo getCComboWSDLURL() {
    return cComboWSDLURL;
  }
  
  /** 
   * Returns the status progress bar.
   * 
   * @return the status progress bar.
   */
  public ProgressBar getProgressBar() {
    return progressBar;
  }
  
  /**
   * Returns the import SOAP message menu item.
   * 
   * @return the import SOAP message menu item.
   */
  public MenuItem getMenuItemImportSOAPMessage(){
    return menuItemImportSOAPMessage;
  }
  /**
   * Returns the export SOAP message menu item.
   * 
   * @return the export SOAP message menu item.
   */
  public MenuItem getMenuItemExportSOAPMessage(){
    return menuItemExportSOAPMessage;
  }
  /**
   * Returns the insert XML document menu item.
   * 
   * @return the insert XML document menu item.
   */
  public MenuItem getMenuItemInsertXMLDocument(){
    return menuItemInsertXMLDocument;
  }
  /**
   * Returns the editor mode item. This is the button in the view menu
   * that is checked if the application is in the editor mode.
   * 
   * @return the editor mode item.
   */
  public MenuItem getMenuItemEditorMode() {
    return menuItemEditorMode;
  }
  
  /**
   * Returns the GUI mode item. This is the button in the view menu
   * that is checked if the application is in the GUI mode.
   * 
   * @return the GUI mode item.
   */
  public MenuItem getMenuItemGUIMode() {
    return menuItemGUIMode;
  }
  
  /**
   * Returns the editor mode tool item. This is the button
   * that is pressed if the application is in the editor mode.
   * 
   * @return the editor mode tool item.
   */
  public ToolItem getToolItemEditorMode() {
    return toolItemEditorMode;
  }
  
  /**
   * Returns the GUI mode tool item. This is the button
   * that is pressed if the application is in the GUI mode.
   * 
   * @return the GUI mode tool item.
   */
  public ToolItem getToolItemGUIMode() {
    return toolItemGUIMode;
  }
  
  /**
   * Returns the send request tool item. This is the button that
   * has to be pressed to send a request to a web service.
   * 
   * @return the send request tool item.
   */
  public ToolItem getToolItemSendRequest() {
    return toolItemSendRequest;
  }

  /**
   * Returns the center composite. This composite contains the
   * editor and GUI tab folder.
   * 
   * @return the center composite.
   */
  public Composite getCompositeCenter() {
    return compositeCenter;
  }
  
  /**
   * Returns the scrolled composite for the request composite.
   * 
   * @return the scrolled composite for the request composite.
   */
  public ScrolledComposite getScrolledCompositeGUIRequest() {
    return scrolledCompositeGUIRequest;
  }
  
  /**
   * Returns the scrolled composite for the response composite.
   * 
   * @return the scrolled composite for the response composite.
   */
  public ScrolledComposite getScrolledCompositeGUIResponse() {
    return scrolledCompositeGUIResponse;    
  }
  
  /**
   * Returns the composite where the request Visual Components are placed.
   * 
   * @return the response composite.
   */
  public Composite getCompositeGUIRequest() {
    return compositeGUIRequest;
  }

  /**
   * Returns the composite where the repsonse Visual Components are placed.
   * 
   * @return the response composite.
   */
  public Composite getCompositeGUIResponse() {
    return compositeGUIResponse;
  }
  
  /**
   * Updates the scrolled composite for the request composite.
   * This scrolled composite has to know the new size.
   */
  public void scrolledCompositeGUIRequestUpdate() {
    Point size=scrolledCompositeGUIRequest.computeSize(SWT.DEFAULT,SWT.DEFAULT);
    scrolledCompositeGUIRequest.setMinSize(size);
    scrolledCompositeGUIRequest.layout();
  }
  
  /**
   * Updates the scrolled composite for the repsonse composite.
   * This scrolled composite has to know the new size.
   */  
  public void scrolledCompositeGUIResponseUpdate() {
    Point size=scrolledCompositeGUIResponse.computeSize(SWT.DEFAULT,SWT.DEFAULT);
    scrolledCompositeGUIResponse.setMinSize(size);
    scrolledCompositeGUIResponse.layout();
  }
  
  /**
   * Returns the view mode of the main window.
   * 
   * @return the mode of the main window.
   */
  public int getViewMode() {
    return m_mode;
  }
  
  /**
   * Sets the view mode of the main window. The mode can differ differ between 
   * XML editor mode and GUI mode.
   * 
   * @param mode the view mode of the main window.
   */
  public void setViewMode(int mode) {
    m_mode=mode;
  }

  /**
   * Sets the look and feel of the main window if SSL (secure socket layer) activated.
   * Different colors and icons are showed in SSL mode.
   * 
   * @param ssl true, if SSL is activated.
   * @param certificateIssuer The string indicating the issuer of the certificate.
   */
  public void setSSLMode(boolean ssl, String certificateIssuer) {
    if(ssl) {
      labelSSLIcon.setImage(SWTResourceManager.getImage("resources/ssl.png"));
      cComboWSDLURL.setBackground(JCowsHelper.getColor(Properties.getConfig("ssl.SSLEnabledCComboURLColor")));
      labelSSLIcon.setToolTipText("Secure Connection: " + certificateIssuer);
    }
    else 
    {
      labelSSLIcon.setImage(SWTResourceManager.getImage("resources/no_ssl.png"));
      cComboWSDLURL.setBackground(JCowsHelper.getColor(Properties.getConfig("ssl.SSLDisabledCComboURLColor")));
      labelSSLIcon.setToolTipText("Plain Connection");
    }
  }

}
