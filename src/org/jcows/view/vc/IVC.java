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
import org.eclipse.swt.widgets.Composite;
import org.jcows.model.vc.IValidator;
/**
 * Defines a standard set of methods that a Visual Component has to implement.
 * Visual Components represent a Java type in a grapical user interface.
 * They can hold and modify the string representation of a Java type value.
 * For example the {@link VCint VCint} class represents a integer as a text label
 * with a text box.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public interface IVC {
  
  /**
   * Returns the composite where new graphical elements can
   * be drawn.
   * 
   * @return composite where new graphical elements can be added.
   */
  
  abstract Composite getComposite();
  
  /**
   * Returns the text label of the component.
   * 
   * @return the text label of the component.
   */
	public String getLabel();
	/**
   * Sets the text label of the component. The text label is
   * to give more information about the value that has to be
   * represented. For example a Visual Component can represent
   * the temperature of your living room. So you can set a label
   * with the value <i>living room temperature</i>.
   * 
   * @param label the label text.
	 */
	public void setLabel(String label);
  
  /**
   * Returns all attached validators of this component.
   * 
   * @return all attached validators.
   */
  public IValidator[] getValidators();
  
  /**
   * Adds a validator to the components. A component can hold more that
   * one validator.
   * 
   * @param validator validator to attach.
   */
	public void addValidator(IValidator validator);
  
  /**
   * Removes the validator.
   * 
   * @param validator the validator that has to be removed.
   * @return removed validator instance.
   */
	public IValidator removeValidator(IValidator validator);
  
  /**
   * Returns all validators that failed after calling the {@link IVC#validate() validate}
   * method.
   * 
   * @return array of failed validators.
   */
  public IValidator[] getFailedValidators();
  
  /**
   * Validates the component. Every component can hold zero or
   * more validators. A validator parses a string for some rules
   * and returns true or false. If one validator or more validator
   * failed, validation failed.
   * 
   * @return true if no validator failed.
   */
  public boolean validate();
  
  /**
   * Set es component editable or not. If the component is not
   * editable, no value can be modified.
   * 
   * @param editable true if value can be modified.
   */
  public void setEditable(boolean editable);
  
}
