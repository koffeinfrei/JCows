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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcows.JCowsException;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;
/**
 * The <code>VCContainer</code> class represents an object that contains
 * nested data types. This class holds other Visual Components and manages
 * their layout.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCContainer extends VC {
  
  private static final Logger LOGGER = Logger.getLogger(VCContainer.class);

  private Group m_groupContainer=null;
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param paramListItem the correspondung {@link org.jcows.model.vc.ParamListItem} for this object.
   * @param parent the parent composite.
   */
  public VCContainer(ParamListItem paramListItem,Composite parent) throws JCowsException {
    super(paramListItem,parent);
    
    if(paramListItem.getItemtype()!=ParamListItem.ITEMTYPE_CONTAINER)
      throw new JCowsException(Properties.getMessage("error.VCContainerParamListItemNotContainerException"));
    
    RowLayout layout=new RowLayout(SWT.VERTICAL);
    /*
     * Reset the current default label.
     */
    m_label.setText("");
    
    m_groupContainer=new Group(this,SWT.NONE);
    m_groupContainer.setLayout(layout);
    m_groupContainer.setText(paramListItem.getLabel());
  }
  
  public Composite getComposite() {
    return m_groupContainer;
  }

  public void addValidator(IValidator validator) {
    /*
     * Do nothing.
     */
  }

  public String getLabel() {
    return m_groupContainer.getText();
  }
  
  public void setLabel(String label) {
    m_groupContainer.setText(label);
  }

  public IValidator[] getValidators() {
    return null;
  }

  public boolean validate() {
    return true;
  }
  
  public void setEditable(boolean editable) {
  }

}
