package org.jcows.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jcows.JCowsException;
import org.jcows.model.core.GuiLogic;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTestWebService {

  private static final String URL_JUNIT_TESTWEBSERVICE="http://www.marcoschmid.com/axis/JUnitTestWebService.jws?wsdl";
  
  private static GuiLogic m_logic;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Properties.init();
    m_logic=new GuiLogic(URL_JUNIT_TESTWEBSERVICE);
  }
  
  
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBoolean() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("boolean",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Boolean(true));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBoolean");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }

  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBooleanArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("boolean",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Boolean(true));
    paramListItemSend.getVectorData().add(new Boolean(false));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBooleanArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }  
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBooleanPrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("boolean",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((boolean)true);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBooleanPrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBooleanPrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("boolean",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((boolean)true);
    paramListItemSend.getVectorData().add((boolean)false);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBooleanPrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getByte() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("byte",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Byte((byte)5));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getByte");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getByteArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("byte",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Byte((byte)5));
    paramListItemSend.getVectorData().add(new Byte((byte)6));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getByteArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBytePrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("byte",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((byte)5);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBytePrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getBytePrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("byte",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((byte)5);
    paramListItemSend.getVectorData().add((byte)6);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getBytePrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getDouble() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("double",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Double((double)5));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getDouble");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getDoubleArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("double",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Double((double)5));
    paramListItemSend.getVectorData().add(new Double((double)6));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getDoubleArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getDoublePrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("double",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((double)5);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getDoublePrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getDoublePrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("double",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((double)5);
    paramListItemSend.getVectorData().add((double)6);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getDoublePrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }

  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getFloat() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("float",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Float(5.5F));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getFloat");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getFloatArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("float",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Float(5.5F));
    paramListItemSend.getVectorData().add(new Float(6.5F));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getFloatArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getFloatPrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("float",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(5.5F);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getFloatPrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getFloatPrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("float",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(5.5F);
    paramListItemSend.getVectorData().add(6.5F);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getFloatPrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getInteger() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("int",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Integer(5));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getInteger");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getIntegerArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("int",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Integer(5));
    paramListItemSend.getVectorData().add(new Integer(6));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getIntegerArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getInt() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("int",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((int)5);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getInt");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getIntArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("int",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((int)5);
    paramListItemSend.getVectorData().add((int)6);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getIntArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getLong() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("long",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Long(5L));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getLong");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getLongArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("long",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Long(5L));
    paramListItemSend.getVectorData().add(new Long(6L));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getLongArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getLongPrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("long",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(5L);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getLongPrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getLongPrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("long",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(5L);
    paramListItemSend.getVectorData().add(6L);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getLongPrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getShort() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("short",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Short((short)5));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getShort");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getShortArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("short",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add(new Short((short)5));
    paramListItemSend.getVectorData().add(new Short((short)6));
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getShortArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getShortPrimitive() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("short",true,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((short)5);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getShortPrimitive");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getShortPrimitiveArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("short",true,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add((short)5);
    paramListItemSend.getVectorData().add((short)6);
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getShortPrimitiveArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getString() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("string",false,"value",false);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add("any string");
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getString");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }
  
  /**
   * Tests, that the request ParamListItem object is the same as
   * the response.
   *
   */
  @Test
  public void getStringArray() {
    List<ParamListItem> list=new ArrayList<ParamListItem>();
    ParamListItem paramListItemSend=new ParamListItem("string",false,"value",true);
    ParamListItem paramListItemResponse=null;
    paramListItemSend.getVectorData().add("any string 1");
    paramListItemSend.getVectorData().add("any string 2");
    list.add(paramListItemSend);
    try {
      m_logic.setCurrentOperation("getStringArray");
      list=m_logic.invoke(list,null);
      paramListItemResponse=list.get(0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertEquals(paramListItemSend,paramListItemResponse);
  }

}
