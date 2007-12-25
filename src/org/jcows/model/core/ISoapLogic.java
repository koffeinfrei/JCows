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

import javax.xml.soap.SOAPEnvelope;

import org.jcows.JCowsException;


/**
 * This interface defines the public methods needed to
 * interact with the model.
 * It defines the methods to 
 * interact on SOAP protocol level for the editor mode.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 223 $, $LastChangedDate: 2006-11-08 16:17:36 +0000 (Wed, 08 Nov 2006) $
 *  
 */
public interface ISoapLogic extends IJcowsLogic{
  
  /**
   * Returns the raw request SOAP message.
   * 
   * @return the request SOAP message.
   */
  public SOAPEnvelope getSoapMessage();
  
  /**
   * Returns the response SOAP message.
   * 
   * @return the response SOAP message.
   */
  public SOAPEnvelope getSoapResponse();
  
  /**
   * Sends a request to the web service (invokes the web service).
   * 
   * @param soapMessage the message to be sent.
   * @param attachments the paths of the attachments for the request.
   * @return the response SOAP message.
   * @throws JCowsException
   */
  public SOAPEnvelope invoke(String soapMessage, String[] attachments) throws JCowsException;
  
  /**
   * Imports a SOAP message from the specified location.
   * 
   * @param filename the filename that contains the SOAP message.
   * @throws JCowsException
   */
  public void importSoapMessage(String filename) throws JCowsException;
  
  /**
   * Exports a SOAP message to the specified location.
   * 
   * @param filename the name of the file.
   * @param soapMessage the SOAP message.
   * @throws JCowsException
   */
  public void exportSoapMessage(String filename, String soapMessage) throws JCowsException;  
  
  /**
   * Inserts an XML document into the SOAP body.
   * 
   * @param url the URL of the document.
   * @return the content of the document.
   * @throws JCowsException
   */
  public String insertXmlDocument(String url) throws JCowsException;
}