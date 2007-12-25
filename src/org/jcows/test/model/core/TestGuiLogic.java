package org.jcows.test.model.core;

import java.util.Arrays;
import java.util.List;

import org.jcows.JCowsException;
import org.jcows.model.core.GuiLogic;
import org.jcows.model.core.IGuiLogic;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

import junit.framework.TestCase;

public class TestGuiLogic extends TestCase {

  private static final String WSDL_URL = 
    "http://distsys.ch/stocks/services/StockQuoterService?wsdl";
  
  private static final String SERVICE_NAME = "TestServiceService";
  private static final String PORT_NAME = "TestService";
  private static final String OPERATION_NAME = "getBooleanArray";
  private static final String WSDL_LOCAL_NAME = "TestService";
  
  private static final String JCOWS_FILE = 
    System.getProperty("java.io.tmpdir") + "/test.jcows";
    
  private IGuiLogic m_guiLogic;
  private List<ParamListItem> m_paramList;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    // init the JCows properties
    Properties.init();
    
    m_guiLogic = new GuiLogic(WSDL_URL);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for 'org.jcows.model.core.GuiLogic.setCurrentOperation(String)'
   */
  public void testSetCurrentOperation() {
    try {
      m_guiLogic.setCurrentOperation(OPERATION_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.GuiLogic.setCurrentPort(String)'
   */
  public void testSetCurrentPort() {
    try {
      m_guiLogic.setCurrentPort(PORT_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.GuiLogic.setCurrentService(String)'
   */
  public void testSetCurrentService() {
    try {
      m_guiLogic.setCurrentService(SERVICE_NAME);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.GuiLogic.getParamList()'
   */
  public void testGetParamList() {
      m_paramList = m_guiLogic.getParamList();
  }
  
  /*
   * Test method for 'org.jcows.model.core.GuiLogic.invoke(List<ParamListItem>, String[])'
   */
  public void testInvoke() {
    try {
      if (m_paramList == null)
        m_paramList = m_guiLogic.getParamList();
      // this represents an argument = Boolean[false, true]
      m_paramList.get(0).getVectorData().set(0, false);
      m_paramList.get(0).getVectorData().add(true);
      m_paramList = m_guiLogic.invoke(m_paramList, null);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getOperations()'
   */
  public void testGetOperations() {
    String[] operations = m_guiLogic.getOperations();
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
    String[] ports = m_guiLogic.getPorts();
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
    String[] services = m_guiLogic.getServices();
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
      m_guiLogic.saveWS(JCOWS_FILE);
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
      m_guiLogic = new GuiLogic(JCOWS_FILE);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsLogic.getWsdlLocalName()'
   */
  public void testGetWsdlLocalName() {
    String name = m_guiLogic.getWsdlLocalName();
    assertEquals(WSDL_LOCAL_NAME, name);
  }

}
