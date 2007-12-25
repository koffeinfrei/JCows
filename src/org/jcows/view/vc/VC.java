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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcows.JCowsException;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;
/**
 * This class is the abstract super class of all Visual Component classes.
 * It defines the general layout and behaviour of the component. For example every component
 * need a validator list and a item, where values of the component can be saved.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public abstract class VC extends Composite implements IVC {
  
  private static final Logger LOGGER = Logger.getLogger(VC.class);
  
  protected Font m_font=SWTResourceManager.getFont("Tahoma",8,0,false,false);
  protected Font m_font_bold=SWTResourceManager.getFont("Tahoma",8,1,false,false);
	protected List<IValidator> m_validators=new ArrayList<IValidator>();
	protected List<IValidator> m_failedValidators=new ArrayList<IValidator>();
	protected ParamListItem m_paramListItem;
  protected Label m_label;
  
  protected int m_arrayIndex;

  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   */
  public VC(ParamListItem paramListItem,Composite parent) throws JCowsException {
    this(paramListItem,parent,0);
  }
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem item where values of the component can be saved.
   * @param parent parent composite.
   * @param arrayIndex the index where this component must save the values.
   */
	public VC(ParamListItem paramListItem,Composite parent,int arrayIndex) throws JCowsException {
		super(parent,SWT.NONE);
    m_paramListItem=paramListItem;
    m_arrayIndex=arrayIndex;
    
    /*
     * getVectorData() can return a null reference if ParamListItem
     * is a container type and contains no direct data.
     */
    if(paramListItem.getVectorData()!=null && arrayIndex>paramListItem.getVectorData().size())
      throw new JCowsException(Properties.getMessage("error.ArrayIndexOutOfBoundsException"),new ArrayIndexOutOfBoundsException(arrayIndex));

    GridLayout layout=new GridLayout();
    layout.makeColumnsEqualWidth=false;
    layout.numColumns=2;
    setLayout(layout);
    
    m_label=new Label(this,SWT.NONE);
    m_label.setFont(m_font_bold);
    m_label.setText(paramListItem.getLabel());
    
	}
  
  public Composite getComposite() {
    return this;
  }
  
  public String getLabel() {
    return m_label.getText();
  }
  
  public void setLabel(String label) {
    this.m_label.setText(label);
  }
	
	public void addValidator(IValidator validator) {
		m_validators.add(validator);
	}
	public IValidator[] getValidators() {
		return (IValidator[])m_validators.toArray();
	}
  
  public IValidator removeValidator(IValidator validator) {
    int index=m_validators.indexOf(validator);
    if(index>-1)
      return m_validators.remove(index);
    return null;
  }
	
  public IValidator[] getFailedValidators() {
    if(m_failedValidators.size()>0) {
      return m_failedValidators.toArray(new IValidator[]{});
    }
    else
      return null;
  }
  
}
