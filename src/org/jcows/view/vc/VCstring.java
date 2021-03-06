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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jcows.JCowsException;
import org.jcows.controller.JCowsController;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;
/**
 * The <code>VCstring</code> class represents the Java string type.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCstring extends VC {

  private static final Logger LOGGER = Logger.getLogger(VCstring.class);

  private Text m_stringValue;
  /*
   * Value that will be added to the current text
   * width.
   */
  private final int STRING_VALUE_SIZEEXT=150;

  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   */
  public VCstring(ParamListItem paramListItem,Composite parent) throws JCowsException {
    this(paramListItem,parent,0);
  }
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   * @param arrayIndex the index where this component must save the values.
   */  
  public VCstring(ParamListItem paramListItem,Composite parent,int arrayIndex) throws JCowsException {
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
        m_paramListItem.getVectorData().set(m_arrayIndex,m_stringValue.getText());
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
   * The default value of a {@link org.jcows.model.vc.ParamListItem} object is
   * a string or already a value of the correct Java type.
   * The string has to be parsed into the the correct Java type
   * and stored in the {@link org.jcows.model.vc.ParamListItem} object.
   *
   */  
  private void setDefaultParamListItemValue() throws JCowsException {
    String value=null;
    /*
     * If ParamListItem has a string value.
     */
    if(m_paramListItem.getVectorData().get(m_arrayIndex) instanceof String)
      value=(String)m_paramListItem.getVectorData().get(m_arrayIndex);
    /*
     * Get default value (of correct Java type) if ParamListItem
     * holds more than one value (array mode).
     */
    else {
      value=(String)m_paramListItem.getVectorData().get(0);
      m_paramListItem.getVectorData().set(m_arrayIndex,value);
    }
    
    if(value==null)
      throw new JCowsException(Properties.getMessage("error.VCstringNullPointerException"),new NullPointerException());
    
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
