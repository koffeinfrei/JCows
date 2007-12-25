package org.jcows.test.system;

import org.jcows.system.Properties;

import junit.framework.TestCase;

public class TestProperties extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();
    
    Properties.init();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for 'org.jcows.system.Properties.getMessage(String, String[])'
   */
  public void testGetMessageStringStringArray() {
    String msg = Properties.getMessage("error.noSuchOperation", 
        new String[]{"Testoperation"});
    // assert that message was found
    assertFalse(msg.startsWith("!"));
    // assert that text was replaced 
    assertFalse(msg.contains("\"{"));
    
    msg = Properties.getMessage("asdfasdf", 
        new String[]{"Testoperation"});
    assertTrue(msg.startsWith("!"));
  }

  /*
   * Test method for 'org.jcows.system.Properties.getMessage(String)'
   */
  public void testGetMessageString() {
    String msg = Properties.getMessage("error.soapLogicNoServices");
    assertFalse(msg.startsWith("!"));
    
    msg = Properties.getMessage("asdfasdf");
    assertTrue(msg.startsWith("!"));
  }

  /*
   * Test method for 'org.jcows.system.Properties.getConfig(String)'
   */
  public void testGetConfig() {
    String msg = Properties.getConfig("title");
    assertFalse(msg.startsWith("!"));
    
    msg = Properties.getConfig("asdfasdf");
    assertTrue(msg.startsWith("!"));
  }

  /*
   * Test method for 'org.jcows.system.Properties.getHistory(String)'
   */
  public void testGetHistory() {
    String msg = Properties.getHistory(Properties.KEY_HISTORY_URL);
    assertFalse(msg.startsWith("!"));
    
    msg = Properties.getHistory("asdfasdf");
    assertTrue(msg.startsWith("!"));
  }

  /*
   * Test method for 'org.jcows.system.Properties.setConfig(String, String)'
   */
  public void testSetConfig() {
    String titleOrig = Properties.getConfig("title");
    Properties.setConfig("title", "asdfasdf");
    String titleNew = Properties.getConfig("title");
    assertEquals(titleNew, "asdfasdf");
    // restore title
    Properties.setConfig("title", titleOrig);
    titleNew = Properties.getConfig("title");
    assertEquals(titleNew, titleOrig);
  }

  /*
   * Test method for 'org.jcows.system.Properties.setHistory(String, String)'
   */
  public void testSetHistory() {
    String histOrig = Properties.getConfig("title");
    Properties.setConfig("title", "asdfasdf");
    String histNew = Properties.getConfig("title");
    assertEquals(histNew, "asdfasdf");
    // restore title
    Properties.setConfig("title", histOrig);
    histNew = Properties.getConfig("title");
    assertEquals(histNew, histOrig);
  }

}
