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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPEnvelope;
import org.jcows.JCowsException;
import org.jcows.controller.DialogSSLController;
import org.jcows.system.Properties;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class provides the methods for the controller to
 * interact with the model.
 * It provides the methods to 
 * interact on SOAP protocol level
 * for the editor mode.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 223 $, $LastChangedDate: 2006-11-08 16:17:36 +0000 (Wed, 08 Nov 2006) $
 *  
 */
public class SoapLogic extends JCowsLogic implements ISoapLogic{
  private javax.xml.soap.SOAPEnvelope m_soapMessage;
  private javax.xml.soap.SOAPEnvelope m_soapResponse;
  
  /**
   * Constructs a new SoapLogic instance for the web service described
   * by the specified WSDL URL. This instance does not support ssl.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @throws JCowsException
   */
  public SoapLogic(String wsdlUrl) throws JCowsException{
    this(wsdlUrl, null);
  }
  
  /**
   * Constructs a new SoapLogic instance for the web service described
   * by the specified WSDL URL. The DialogSSLController argument is used
   * in case of an untrusted certificate on an SSL connection.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @param controller The controller for the SSL dialog.
   * @throws JCowsException
   */
  public SoapLogic(String wsdlUrl, DialogSSLController controller) throws JCowsException{
    super(wsdlUrl, controller);
    
    /* generate the SOAP message */
    createSoapMessage();
  }

  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentOperation(java.lang.String)
   */
  public void setCurrentOperation(String operation) throws JCowsException{
    super.setCurrentOperation(operation);
    createSoapMessage();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentPort(java.lang.String)
   */
  public void setCurrentPort(String port) throws JCowsException{
    super.setCurrentPort(port);
    createSoapMessage();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.JCowsLogic#setCurrentService(java.lang.String)
   */
  public void setCurrentService(String service) throws JCowsException{
    super.setCurrentService(service);
    createSoapMessage();
  }
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#getSoapMessage()
   */
  public javax.xml.soap.SOAPEnvelope getSoapMessage() {
    return m_soapMessage;
  }
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#getSoapResponse()
   */
  public javax.xml.soap.SOAPEnvelope getSoapResponse(){
    return m_soapResponse;
  }
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#importSoapMessage(java.lang.String)
   */
  public void importSoapMessage(String filename) throws JCowsException{
    try {
      StringBuffer msg = new StringBuffer();
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String buf;
      while ((buf = in.readLine()) != null){
        msg.append(buf);
        msg.append("\n");
      }
      
      MessageFactory messageFactory = MessageFactory.newInstance();
      SOAPMessage request = messageFactory.createMessage();
      request.getSOAPHeader().detachNode();
      
      StreamSource domSource = new StreamSource(new ByteArrayInputStream(msg.toString().getBytes()));
      request.getSOAPPart().setContent(domSource);
      //normalizeDomNode(request.getSOAPPart());
      
      m_soapMessage = request.getSOAPPart().getEnvelope();
      
      // normalize here instead, as if the document is not well formed
      // we will get a soapexception from getenvelope, not a not so 
      // informative domexception from normalizeDomNode()
      m_jcowsFile.normalizeDomNode(m_soapMessage);
      
      LOGGER.info("Imported SOAP message from " + filename);
    }
    catch(SOAPException e){
      throw new JCowsException(Properties.getMessage("error.SOAPException"), e);
    }
    catch (FileNotFoundException e) {
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#exportSoapMessage(java.lang.String, java.lang.String)
   */
  public void exportSoapMessage(String filename, String soap_msg) throws JCowsException{
    try {
      PrintWriter out = new PrintWriter(new FileWriter(filename, false));
      out.print(soap_msg);
      out.close();
      
      LOGGER.info("exported SOAP message to " + filename);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#insertXmlDocument(java.lang.String)
   */
  public String insertXmlDocument(String url) throws JCowsException{
    try{
      MessageFactory messageFactory = MessageFactory.newInstance();
      SOAPMessage request = messageFactory.createMessage();
      request.getSOAPHeader().detachNode();
      
      DocumentBuilderFactory factory = 
        DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      //factory.setValidating(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(url);
      
      m_jcowsFile.normalizeDomNode(request.getSOAPPart());
      
      javax.xml.soap.SOAPEnvelope envelope = request.getSOAPPart().getEnvelope();
      envelope = m_soapMessage;
      request.getSOAPBody().addDocument(doc);
      
      return request.getSOAPPart().getEnvelope().toString();
    }
    catch(SOAPException e){
      throw new JCowsException(Properties.getMessage("error.SOAPException"), e);
    }
    catch(ParserConfigurationException e){
      throw new JCowsException(
          Properties.getMessage("error.ParserConfigurationException"), e);
    }
    catch(SAXException e){
      throw new JCowsException(Properties.getMessage("error.SAXException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }

  /* (non-Javadoc)
   * @see org.jcows.model.core.ISoapLogic#invoke(java.lang.String, java.lang.String[])
   */
  public SOAPEnvelope invoke(String soapMessage, String[] attachments) throws JCowsException{
    try{
      
      /* create a SOAP message */
      MessageFactory messageFactory = MessageFactory.newInstance();
      SOAPMessage request = messageFactory.createMessage();
      request.getSOAPHeader().detachNode();
      
      StreamSource domSource = new StreamSource(new ByteArrayInputStream(soapMessage.getBytes()));
      request.getSOAPPart().setContent(domSource);
      m_jcowsFile.normalizeDomNode(request.getSOAPPart());
      
      LOGGER.info("Sending Soap Message: " + request.getSOAPPart().getEnvelope());
      
      /* invoke the service */
      Call call = ((Stub)m_currPort.portInstance)._getCall(); // get the call object containing the correct soapaction
      //call.setRequestMessage((Message)request);
      
      // TODO add attachments
      /*
      if (attachments != null)
        for (String attachment : attachments){
          DataHandler handler = new DataHandler(new FileDataSource(new File(attachment)));
          AttachmentPart attachment_part = request.createAttachmentPart(handler);
          call.addAttachmentPart(attachment_part);
        }
      */
      
      SOAPEnvelope responseEnvelope = call.invoke((SOAPEnvelope)request.getSOAPPart().getEnvelope());
      
      LOGGER.info("Response Soap Message: " + responseEnvelope);
      m_soapResponse = responseEnvelope;
      return responseEnvelope;
      
    }
    catch(SOAPException e){
      throw new JCowsException(Properties.getMessage("error.SOAPException"), e);
    }
    catch(AxisFault e){
      throw new JCowsException(Properties.getMessage("error.AxisFault"), e);
    }
  }
  
  /**
   * Creates a default Soap Message that can be edited and sent
   * to the Web Service.
   * 
   * @throws JCowsException
   */
  private void createSoapMessage() throws JCowsException{
    List<HandlerInfo> handlerchain = null;
    try{
      LOGGER.info("Creating Soap Message...");
      
      Class portClass = m_currPort.portClass;
  
      Method method = portClass.getMethod(m_currOperation.name, m_currOperation.arguments);
      Service serviceLocator = m_currService.serviceLocator;
      LOGGER.debug(method.toString());
      
      /* add the soaphandler */
      handlerchain = 
        serviceLocator.getHandlerRegistry().getHandlerChain(m_currPort.qName);
      HandlerInfo hi = new HandlerInfo(SoapHandler.class, /*handlerConfig*/null, null);
      handlerchain.add(hi);
      
      /* invoke web service */    
      Object[] args = m_classHelper.constructArguments(m_currOperation.arguments);
      method.invoke(m_currPort.portInstance, args);
    }
    catch(NoSuchMethodException e){
      throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"), e);
    }
    catch(IllegalAccessException e){
      throw new JCowsException(Properties.getMessage("error.IllegalAccessException"), e);
    }
    catch(InvocationTargetException e){
      /* don't rethrow, as we want to carry on if the response messages
       * is null, as we don't care about the response at this point.
       * Only rethrow if the soapMessage from the handler is null, as in that case
       * we really have a serious invocationtargetexception
       */
      if (SoapHandler.soapMessage == null)
        throw new JCowsException(Properties.getMessage("error.InvocationTargetException"), e);
    }
    catch(IllegalArgumentException e){
      throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"), e);
    }

    m_soapMessage = SoapHandler.soapMessage;
    
    /* clear the handlerchain (else the message would be sent later on) */
    handlerchain.clear();
  }

}
