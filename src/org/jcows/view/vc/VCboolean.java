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
package org.jcows.view.vc;

import java.lang.reflect.Array;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcows.JCowsException;
import org.jcows.controller.JCowsController;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;
/**
 * The <code>VCboolean</code> class represents the Java boolean type.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCboolean extends VC {
  
  private static final Logger LOGGER = Logger.getLogger(VCboolean.class);
  
  private Button m_radioValueTrue;
  private Button m_radioValueFalse;
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   */
  public VCboolean(ParamListItem paramListItem,Composite parent) throws JCowsException {
    this(paramListItem,parent,0);
  }
  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   * @param arrayIndex the index where this component must save the values.
   */  
  public VCboolean(final ParamListItem paramListItem,Composite parent,int arrayIndex) throws JCowsException {
    super(paramListItem,parent,arrayIndex);
    
    FillLayout layout=new FillLayout();
    Composite compositeRadioButton=new Composite(this,SWT.NONE);
    compositeRadioButton.setLayout(layout);
    
    m_radioValueTrue=new Button(compositeRadioButton,SWT.RADIO);
    m_radioValueTrue.setFont(m_font);
    m_radioValueTrue.setText("true");
    m_radioValueFalse=new Button(compositeRadioButton,SWT.RADIO);
    m_radioValueFalse.setFont(m_font);
    m_radioValueFalse.setText("false");
    /*
     * Add a listener sends a request when the enter key is pressed.
     */
    m_radioValueTrue.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent evt) {
        LOGGER.debug(evt);
        if(evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)
          JCowsController.m_mainWindowController.sendRequest();
      }
      public void keyReleased(KeyEvent evt) {
        LOGGER.debug(evt);
      }
      });
    /*
     * Add a listener sends a request when the enter key is pressed.
     */
    m_radioValueFalse.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent evt) {
        LOGGER.debug(evt);
        if(evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)
          JCowsController.m_mainWindowController.sendRequest();
      }
      public void keyReleased(KeyEvent evt) {
        LOGGER.debug(evt);
      }
      });
    /*
     * Add a listener that sets the the current value in
     * the item.
     */
    m_radioValueTrue.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent evt) {
        m_radioValueTrue.setSelection(true);
        m_radioValueFalse.setSelection(false);
        setParamListItemValue(true);
      }});
    /*
     * Add a listener that sets the the current value in
     * the item.
     */
    m_radioValueFalse.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent evt) {
        m_radioValueTrue.setSelection(false);
        m_radioValueFalse.setSelection(true);
        setParamListItemValue(false);
      }});
    /*
     * Set default value to the component.
     */
    setDefaultItemValue();
  }
  
  /**
   * Sets the value in the {@link org.jcows.model.vc.ParamListItem} object.
   * 
   * @param value boolean value (true or false).
   */
  private void setParamListItemValue(boolean value) {
    m_paramListItem.getVectorData().set(m_arrayIndex,value);
  }
  
  /**
   * The default value of a {@link org.jcows.model.vc.ParamListItem} object is
   * a string or already a value of the correct Java type.
   * The string has to be parsed into the the correct Java type
   * and stored in the {@link org.jcows.model.vc.ParamListItem} object.
   */
  private void setDefaultItemValue() throws JCowsException {
    boolean value;
    try {
      /*
       * If ParamListItem has a string value.
       */
      if(m_paramListItem.getVectorData().get(0) instanceof String) {
        value=(Boolean)Boolean.parseBoolean((String)m_paramListItem.getVectorData().get(0));
        m_paramListItem.getVectorData().set(0,value);
      }
      /*
       * If ParamListItem has already a value of the correct type.
       * This happens if a ParamListItem was created from the Web Service
       * response.
       */
      else if(m_paramListItem.getVectorData().get(m_arrayIndex) instanceof Boolean)
        value=(Boolean)m_paramListItem.getVectorData().get(m_arrayIndex);
      /*
       * Get default value (of correct Java type) if ParamListItem
       * holds more than one value (array mode).
       */
      else {
        value=(Boolean)m_paramListItem.getVectorData().get(0);
        m_paramListItem.getVectorData().set(m_arrayIndex,value);
      }
    }
    catch(NumberFormatException e) {
      throw new JCowsException(Properties.getMessage("error.NumberFormatException")
          +Properties.getMessage("error.VCMappingDefaultValueError",
              new String[]{m_paramListItem.getDatatype()}),e);
    }
    catch(ClassCastException e) {
      throw new JCowsException(Properties.getMessage("error.ClassCastException")
          +Properties.getMessage("error.VCMappingDefaultValueError",
              new String[]{m_paramListItem.getDatatype()}),e);
    }
    m_radioValueTrue.setSelection(value);
    m_radioValueFalse.setSelection(!value); 
  }
  
  public boolean validate() {
    /*
     * There is no possibility to enter a wrong boolean
     * value.
     */
    return true;
  }
  
  public void setEditable(boolean editable) {
    m_radioValueFalse.setEnabled(editable);
    m_radioValueTrue.setEnabled(editable);
  }
}
