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

import org.jcows.JCowsException;

/**
 * This interface defines the public methods needed to
 * interact with the model.
 * It defines the functionality that all the models have in common.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 165 $, $LastChangedDate: 2006-07-05 21:51:21 +0000 (Wed, 05 Jul 2006) $
 *  
 */
public interface IJcowsLogic {
  
  /**
   * Sets the current service to the specified one.
   * 
   * @param service the service name.
   * @throws JCowsException
   */
  public void setCurrentService(String service) throws JCowsException;
  
  /**
   * Sets the current port to the specified one.
   * 
   * @param port the port name.
   * @throws JCowsException
   */
  public void setCurrentPort(String port) throws JCowsException;
  
  /**
   * Sets the current operation to the specified one.
   * 
   * @param operation the operation name.
   * @throws JCowsException
   */
  public void setCurrentOperation(String operation) throws JCowsException;
  
  /**
   * Gets all available services.
   * 
   * @return an array of service names
   */
  public String[] getServices();
  
  /**
   * Gets all ports for the currently set service.
   * 
   * @return an array of port names
   */
  public String[] getPorts();
  
  /**
   * Gets all operations for the currently set port.
   * 
   * @return an array of operation names
   */
  public String[] getOperations();
  
  /**
   * Returns the local name of the wsdl.
   * 
   * @return the local name
   */
  public String getWsdlLocalName();
  
  /**
   * Returns the URL of the WSDL document.
   * 
   * @return the URL string
   */
  public String getWsdlUrl();
  
  /**
   * Saves the current web service configuration.
   * 
   * @param filename the filename.
   */
  public void saveWS(String filename) throws JCowsException;
  
  /**
   * Returns the issuer string of the certificate.
   * 
   * @return the issuer string
   */
  public String getSSLCertificateIssuer();
}
