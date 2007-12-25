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

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPEnvelope;

import org.apache.log4j.Logger;

/**
 * This class implements a handler that is used to intercept the SOAP messages
 * that travel between the axis engine and the server. Its purpose is to extract the
 * request SOAP message from an RPC request.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
public class SoapHandler implements Handler {
  
  private static final Logger LOGGER = Logger.getLogger(SoapHandler.class);
  
  public static SOAPEnvelope soapMessage;
  
  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#handleRequest(javax.xml.rpc.handler.MessageContext)
   */
  public boolean handleRequest(MessageContext context) {
    SOAPMessageContext msgContext = (SOAPMessageContext)context;
    /* get SOAP message */
    try{
      // get the whole SOAP message (including envelope)
      soapMessage = msgContext.getMessage().getSOAPPart().getEnvelope();
      
      LOGGER.info("Request Soap Message: " + soapMessage);
    }
    catch(Exception e){ e.printStackTrace(); }

    return false;
  }

  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#handleResponse(javax.xml.rpc.handler.MessageContext)
   */
  public boolean handleResponse(MessageContext context) {
    org.apache.axis.MessageContext msg = (org.apache.axis.MessageContext)context;
    try{
      javax.xml.soap.SOAPBody body = msg.getMessage().getSOAPBody();
      LOGGER.info("Response Soap Message: " + body);
    }
    catch(Exception e){}
    return true;
  }

  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#handleFault(javax.xml.rpc.handler.MessageContext)
   */
  public boolean handleFault(MessageContext context) {
    return true;
  }

  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#init(javax.xml.rpc.handler.HandlerInfo)
   */
  public void init(HandlerInfo config) {
  }

  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#destroy()
   */
  public void destroy() {
  }

  /* (non-Javadoc)
   * @see javax.xml.rpc.handler.Handler#getHeaders()
   */
  public QName[] getHeaders() {
    return null;
  }

}
