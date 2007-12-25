package org.jcows.test.model.core;

import java.util.Arrays;

import javax.xml.soap.SOAPEnvelope;

import junit.framework.TestCase;

import org.jcows.JCowsException;
import org.jcows.model.core.ISoapLogic;
import org.jcows.model.core.SoapLogic;
import org.jcows.system.Properties;

public class TestSoapLogic extends TestCase {

  private static final String WSDL_URL = 
    "http://distsys.ch/stocks/services/StockQuoterService?wsdl";
  
  private static final String SERVICE_NAME = "TestServiceService";
  private static final String PORT_NAME = "TestService";
  private static final String OPERATION_NAME = "getBooleanArray";
  private static final String WSDL_LOCAL_NAME = "TestService";
  
  private static final String XML_FILE = "vcmapping.xml";
  private static final String SOAP_FILE = 
    System.getProperty("java.io.tmpdir") + "/test.xml";
  
  private static final String JCOWS_FILE = 
    System.getProperty("java.io.tmpdir") + "/test.jcows";
  
  private ISoapLogic m_soapLogic;

  protected void setUp() throws Exception {
    super.setUp();
    
    // init the JCows properties
    Properties.init();
    
    m_soapLogic = new SoapLogic(WSDL_URL);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.setCurrentOperation(String)'
   */
  public void testSetCurrentOperation() {
    try {
      m_soapLogic.setCurrentOperation(OPERATION_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.setCurrentPort(String)'
   */
  public void testSetCurrentPort() {
    try {
      m_soapLogic.setCurrentPort(PORT_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.setCurrentService(String)'
   */
  public void testSetCurrentService() {
    try {
      m_soapLogic.setCurrentService(SERVICE_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.invoke(String, String[])'
   */
  public void testInvoke() {
    String msg = m_soapLogic.getSoapMessage().toString();
    try {
      m_soapLogic.invoke(msg, null);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }
  
  /*
   * Test method for 'org.jcows.model.core.SoapLogic.getSoapMessage()'
   */
  public void testGetSoapMessage() {
    SOAPEnvelope env = m_soapLogic.getSoapMessage();
    assertNotNull(env);
    assertTrue(env instanceof SOAPEnvelope);
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.getSoapResponse()'
   */
  public void testGetSoapResponse() {
    // precondition
    testInvoke();
    SOAPEnvelope env = m_soapLogic.getSoapResponse();
    assertNotNull(env);
    assertTrue(env instanceof SOAPEnvelope);
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.importSoapMessage(String)'
   */
  public void testImportSoapMessage() {
    // precondition
    testExportSoapMessage();
    
    try {
      m_soapLogic.importSoapMessage(SOAP_FILE);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.exportSoapMessage(String, String)'
   */
  public void testExportSoapMessage() {
    try {
      m_soapLogic.exportSoapMessage(SOAP_FILE, m_soapLogic.getSoapMessage().toString());
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.SoapLogic.insertXmlDocument(String)'
   */
  public void testInsertXmlDocument() {
    try {
      m_soapLogic.insertXmlDocument(XML_FILE);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getOperations()'
   */
  public void testGetOperations() {
    String[] operations = m_soapLogic.getOperations();
    // correct object?
    assertTrue(operations instanceof String[]);
    // at least one value?
    assertTrue(operations.length >= 1);
    // contains the value OPERATION_NAME
    assertTrue(Arrays.binarySearch(operations, OPERATION_NAME) >= 0);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getPorts()'
   */
  public void testGetPorts() {
    String[] ports = m_soapLogic.getPorts();
    // correct object?
    assertTrue(ports instanceof String[]);
    // at least one value?
    assertTrue(ports.length >= 1);
    // contains the value PORT_NAME
    assertTrue(Arrays.binarySearch(ports, PORT_NAME) >= 0);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getServices()'
   */
  public void testGetServices() {
    String[] services = m_soapLogic.getServices();
    // correct object?
    assertTrue(services instanceof String[]);
    // at least one value?
    assertTrue(services.length >= 1);
    // contains the value SERVICE_NAME
    assertTrue(Arrays.binarySearch(services, SERVICE_NAME) >= 0);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.saveWS(String)'
   */
  public void testSaveWS() {
    try {
      m_soapLogic.saveWS(System.getProperty("java.io.tmpdir") + "/test.jcows");
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test method for 'org.jcows.model.core.SoapLogic.JCowsLogic(String)'
   * with the string as jcows file (as opposed to WSDL URL) 
   */
  public void testOpenWS(){
    try {
      m_soapLogic = new SoapLogic(JCOWS_FILE);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }
  
  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getWsdlLocalName()'
   */
  public void testGetWsdlLocalName() {
    String name = m_soapLogic.getWsdlLocalName();
    assertEquals(WSDL_LOCAL_NAME, name);
  }

}
