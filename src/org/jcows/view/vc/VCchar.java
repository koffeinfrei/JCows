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
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcows.JCowsException;
import org.jcows.controller.JCowsController;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;
/**
 * The <code>VCchar/code> class represents the Java character type.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCchar extends VC {
  
  private static final Logger LOGGER = Logger.getLogger(VCchar.class);

  private Text m_stringValue;
  /*
   * Value that will be added to the current text
   * width.
   */
  private final int STRING_VALUE_SIZEEXT=10;

  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   */
  public VCchar(ParamListItem paramListItem,Composite parent) throws JCowsException {
    this(paramListItem,parent,0);
  }

  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   * @param arrayIndex the index where this component must save the values.
   */
  public VCchar(ParamListItem paramListItem,Composite parent,int arrayIndex) throws JCowsException {
    super(paramListItem,parent,arrayIndex);
    
    m_stringValue=new Text(this,SWT.BORDER);
    m_stringValue.setFont(m_font);
    
    Point defaultSize=m_stringValue.getSize();
    GridData data=new GridData();
    data.widthHint=defaultSize.x+STRING_VALUE_SIZEEXT;
    
    m_stringValue.setLayoutData(data);
    /*
     * Add a listener that sets the the current value in
     * the item and sends a request when the enter key is pressed.
     */
    m_stringValue.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent evt) {
        LOGGER.debug(evt);
        parseValue(m_stringValue.getText());
        if(evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)
          JCowsController.m_mainWindowController.sendRequest();
      }
      public void keyReleased(KeyEvent evt) {
        LOGGER.debug(evt);
      }
      });
    /*
     * Set default value to the component.
     */
    setDefaultParamListItemValue();
  }
  
  /**
   * Parses the string value into the correct Java type
   * and saves it in a {@link org.jcows.model.vc.ParamListItem} object.
   * 
   * @param value string value to parse.
   */
  private void parseValue(String value) {
    if(value.length()==1)
      m_paramListItem.getVectorData().set(m_arrayIndex,value.toCharArray()[0]);
  }
  
  /**
   * The default value of a {@link org.jcows.model.vc.ParamListItem} object is
   * a string or already a value of the correct Java type.
   * The string has to be parsed into the the correct Java type
   * and stored in the {@link org.jcows.model.vc.ParamListItem} object.
   *
   */  
  private void setDefaultParamListItemValue() throws JCowsException {
    Character value;
    try {
      /*
       * If ParamListItem has a string value.
       */
      if(m_paramListItem.getVectorData().get(0) instanceof String) {
        value=((String)m_paramListItem.getVectorData().get(0)).toCharArray()[0];
        m_paramListItem.getVectorData().set(0,value);
      }
      /*
       * If ParamListItem has already a value of the correct type.
       * This happens if a ParamListItem was created from the Web Service
       * response.
       */
      else if(m_paramListItem.getVectorData().get(m_arrayIndex) instanceof Character)
        value=(Character)m_paramListItem.getVectorData().get(m_arrayIndex);
      /*
       * Get default value (of correct Java type) if ParamListItem
       * holds more than one value (array mode).
       */
      else {
        value=(Character)m_paramListItem.getVectorData().get(0);
        m_paramListItem.getVectorData().set(m_arrayIndex,value);
      }
    }
    /*
     * May occurs if the default value is null, an empty string
     * or contains more than one character.
     */
    catch(ArrayIndexOutOfBoundsException e) {
      throw new JCowsException(Properties.getMessage("error.ArrayIndexOutOfBoundsException")
          +Properties.getMessage("error.VCMappingDefaultValueError",
              new String[]{m_paramListItem.getDatatype()}),e);
    }
    catch(ClassCastException e) {
      throw new JCowsException(Properties.getMessage("error.ClassCastException")
          +Properties.getMessage("error.VCMappingDefaultValueError",
              new String[]{m_paramListItem.getDatatype()}),e);
    }
    
    m_stringValue.setText(value.toString());
  }
  
  public boolean validate() {
    boolean valid=true;
    m_failedValidators.clear();
    for(IValidator validator:m_validators) {
      if(!validator.validate(m_stringValue.getText()))
        m_failedValidators.add(validator);
    }
    /*
     * Even if only one validator failed, validation failed.
     */
    if(m_failedValidators.size()>0)
      valid=false;
    
    if(valid==true) {
      /*
       * Change background color.
       */
      String color=Properties.getConfig("vc.ValidColor");
      m_stringValue.setBackground(JCowsHelper.getColor(color)); 
    }
    else if(valid==false) {
      /*
       * Change background color.
       */
      String color=Properties.getConfig("vc.InvalidColor");
      m_stringValue.setBackground(JCowsHelper.getColor(color));
      m_stringValue.setFocus();
    }
    return valid;
  }
  
  public void setEditable(boolean editable) {
    m_stringValue.setEditable(editable);
  }
}
