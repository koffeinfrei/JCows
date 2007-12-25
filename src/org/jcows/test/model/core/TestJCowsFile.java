package org.jcows.test.model.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.soap.SOAPEnvelope;

import org.jcows.JCowsException;
import org.jcows.model.core.ISoapLogic;
import org.jcows.model.core.JCowsFile;
import org.jcows.model.core.SoapLogic;
import org.jcows.system.Properties;

import junit.framework.TestCase;

public class TestJCowsFile extends TestCase {

  private static final String WSDL_URL = 
    "http://distsys.ch/stocks/services/StockQuoterService?wsdl";
  
  private static final String WSDL_LOCAL_NAME = "TestService";
  
  private static final String CRC_STR = "asdf23gbxfhgwez345a__22QJcoWS";
  private static final long CRC_CHECKSUM = 832161027;
  
  private JCowsFile m_jcowsFile;
  private List m_generatedClasses;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    // init the JCows properties
    Properties.init();
    
    m_jcowsFile = new JCowsFile(WSDL_URL);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.getWsdlLocalName()'
   */
  public void testGetWsdlLocalName() {
    String name = m_jcowsFile.getWsdlLocalName();
    
    assertEquals(WSDL_LOCAL_NAME, name);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.normalizeDomNode(Node)'
   */
  public void testNormalizeDomNode() {
    ISoapLogic soapLogic = null;
    try {
      soapLogic = new SoapLogic(WSDL_URL);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
    SOAPEnvelope env = soapLogic.getSoapMessage();
    
    m_jcowsFile.normalizeDomNode(env);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.generateChecksum(String)'
   */
  public void testGenerateChecksum() {
    long checksum = m_jcowsFile.generateChecksum(CRC_STR);
    assertEquals(checksum, CRC_CHECKSUM);
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.getChecksum()'
   */
  public void testGetChecksum() {
    try {
      m_jcowsFile.getChecksum();
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.getGeneratedClassNames()'
   */
  public void testGetGeneratedClassNames() {
    try {
      m_generatedClasses = m_jcowsFile.getGeneratedClassNames();
      assertTrue(m_generatedClasses.size() > 0);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.createJCowsFile(List, long)'
   */
  public void testCreateJCowsFile() {
    // precondition
    testGetGeneratedClassNames();
    
    try {
      m_jcowsFile.createJCowsFile(m_generatedClasses, m_jcowsFile.getChecksum());
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.cleanOutputDir()'
   */
  public void testCleanOutputDir() {
    // create a testfile that should be deleted
    File testFile = new File(
        Properties.getConfig("parser.outputDirName") + "/asdf.java");
    try {
      testFile.createNewFile();
    }
    catch (IOException e) {
      fail(e.getMessage());
    }
    m_jcowsFile.cleanOutputDir();
    
    // file should not exist anymore
    assertFalse(testFile.exists());
  }

  /*
   * Test method for 'org.jcows.model.core.JCowsFile.deleteDir(File)'
   */
  public void testDeleteDir() {
    // create a dir with subdir
    File testDir1 = new File(System.getProperty("java.io.tmpdir")
        + "/asdf1");
    File testDir2 = new File(testDir1.getAbsolutePath() + "/asdf2");
    //testDir1.mkdir();
    testDir2.mkdirs();
    m_jcowsFile.deleteDir(testDir1);
    
    // both dirs should be gone
    assertFalse(testDir2.exists());
    assertFalse(testDir1.exists());
  }

}
