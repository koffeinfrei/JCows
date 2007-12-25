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
package org.jcows.model.core;

import java.util.List;

import org.jcows.JCowsException;
import org.jcows.model.vc.ParamListItem;


/**
 * This interface defines the public methods needed to
 * interact with the model.
 * It defines the methods for the
 * interaction on object level for the GUI mode.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 165 $, $LastChangedDate: 2006-07-05 21:51:21 +0000 (Wed, 05 Jul 2006) $
 */
public interface IGuiLogic extends IJcowsLogic {
  
  /**
   * Invokes the web service with the specified parameter list.
   * 
   * @param paramList a list of {@link org.jcows.model.vc.ParamListItem ParamListItem}s.
   * @param attachments the paths of the attachments for the request.
   * @return a {@link java.util.List List} representing the response 
   *           parameter list containing the values.
   * @throws JCowsException
   */
  public List<ParamListItem> invoke(List<ParamListItem> paramList, String[] attachments) throws JCowsException;
  
  /**
   * Returns a {@link java.util.List List} containing all 
   * {@link org.jcows.model.vc.ParamListItem ParamListItem}s needed 
   * to invoke the current operation of the web service.
   * 
   * @return an array of String containing the type names
   * @throws JCowsException
   */
  public List<ParamListItem> getParamList();
}
