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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jcows.JCowsException;
import org.jcows.controller.DialogSSLController;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

/**
 * This Class provides the methods for the controller to
 * interact with the model.
 * It is responsible for the 
 * interaction for the GUI mode.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 165 $, $LastChangedDate: 2006-07-05 21:51:21 +0000 (Wed, 05 Jul 2006) $
 */
public class GuiLogic extends JCowsLogic implements IGuiLogic {
  
  private List<ParamListItem> m_paramList;
  
  /**
   * Constructs a new GuiLogic instance for the web service described
   * by the specified WSDL URL. The created instance does not support ssl.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @throws JCowsException
   */
  public GuiLogic(String wsdlUrl) throws JCowsException{
    this(wsdlUrl, null);
  }
  
  /**
   * Constructs a new GuiLogic instance for the web service described
   * by the specified WSDL URL. The DialogSSLController argument is used
   * in case of an untrusted certificate on an SSL connection.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @param controller The controller for the SSL dialog.
   * @throws JCowsException
   */
  public GuiLogic(String wsdlUrl, DialogSSLController controller) throws JCowsException{
    super(wsdlUrl, controller);
    
    /* generate the request param list */
    createParamList();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentOperation(java.lang.String)
   */
  public void setCurrentOperation(String operation) throws JCowsException{
    super.setCurrentOperation(operation);
    
    /* generate the request param list */
    createParamList();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentPort(java.lang.String)
   */
  public void setCurrentPort(String port) throws JCowsException{
    super.setCurrentPort(port);
    
    /* generate the request param list */
    createParamList();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentService(java.lang.String)
   */
  public void setCurrentService(String service) throws JCowsException{
    super.setCurrentService(service);
    
    /* generate the request param list */
    createParamList();
  }
  
  
  /* (non-Javadoc)
   * @see jcows.model.IGuiLogic#invoke(java.lang.Object[], java.lang.String[])
   */
  public List<ParamListItem> invoke(List<ParamListItem> paramList, String[] attachments) throws JCowsException{
    Object[] args = m_classHelper.constructArguments(paramList);
    Method method = null;
    Object result = null;
    try {
      method = m_currPort.portClass.getMethod(m_currOperation.name, m_currOperation.arguments);
      result = method.invoke(m_currPort.portInstance, args);
    }
    catch(NoSuchMethodException e){
      throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"), e);
    }
    catch(IllegalAccessException e){
      throw new JCowsException(Properties.getMessage("error.IllegalAccessException"), e);
    }
    catch(InvocationTargetException e){
      throw new JCowsException(Properties.getMessage("error.InvocationTargetException"), e);
    }
    catch(IllegalArgumentException e){
      throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"), e);
    }
    if(result==null)
      return new ArrayList<ParamListItem>();
    Class param = result.getClass();    
    String paramType = param.isArray()? 
          param.getComponentType().getSimpleName() + "[]": param.getSimpleName();
    
    /* get the paramList */
    List<ParamListItem> list = m_classHelper.getParamListFromFields(
        new Class[]{param}, new String[]{paramType}, new Object[]{result});
    
    return list;
  }
  
  /* (non-Javadoc)
   * @see org.jcows.model.core.IGuiLogic#getParamList()
   */
  public List<ParamListItem> getParamList(){
    return m_paramList;
  }

  /**
   * Creates the parameter list needed for invoking 
   * the current web service operation. The parameter list is
   * stored in the current instance.
   * 
   * @throws JCowsException
   */
  private void createParamList() throws JCowsException{
    /* get the method object for the current ws operation */
    Method method = null;
    try {
      method = m_currPort.portClass.getMethod(m_currOperation.name, m_currOperation.arguments);
    }
    catch(NoSuchMethodException e){
      throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"), e);
    }
    // get the parameter type names
    Class[] params = method.getParameterTypes();
    String[] paramTypes = new String[params.length];
    for (int i = 0; i < params.length; ++i)
      paramTypes[i] = (params[i].isArray())? 
          params[i].getComponentType().getName() + "[]": params[i].getName();
    
    String[] labels = m_classHelper.getParamNames(
        m_currPort.portClass, method.getName(), paramTypes);
    
    /* get the paramList */
    m_paramList = m_classHelper.getParamListFromConstructor(m_currOperation.arguments,labels);
  }
}
