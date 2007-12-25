package org.jcows.test.view.vc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.jcows.JCowsException;
import org.jcows.model.vc.BooleanValidator;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;
import org.jcows.view.vc.VCContainer;
import org.jcows.view.vc.VCstring;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestVCContainer {

  private static Shell m_shell;
  private static Display m_display;
  
  private VCContainer m_vc;
  private ParamListItem m_paramListItem;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Properties.init();
    m_display=Display.getCurrent();
    m_shell=new Shell(m_display);
    m_shell.setLayout(new FillLayout());
  }
  
  @AfterClass
  public static void setUpAfterClass() {
    m_shell.open();
    m_shell.layout();
  }
  
  @Before
  public void setUp() throws JCowsException {
    ParamListItem paramListItemString=new ParamListItem("string",false,"string value",new String[]{"any string"},false);
    ArrayList<ParamListItem> list=new ArrayList<ParamListItem>();
    list.add(paramListItemString);
    m_paramListItem=new ParamListItem(list,"container",VCstring.class);
  }
  
  @Test
  public void testVCContainer() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
  }

  @Test
  public void testGetComposite() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    assertNotNull(m_vc.getComposite());
    assertSame(m_vc.getComposite().getClass(),Group.class);
  }

  @Test
  public void testGetLabel() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    m_vc.setLabel("any label");
    assertEquals(m_vc.getLabel(),"any label");
  }

  @Test
  public void testSetLabel() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    m_vc.setLabel("any label");
    assertEquals(m_vc.getLabel(),"any label");
  }

  @Test
  public void testAddValidator() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    IValidator validator1=new BooleanValidator();
    IValidator validator2=new BooleanValidator();
 
    /*
     * Nothing happens.
     */
    m_vc.addValidator(validator1);
    m_vc.addValidator(validator2);
      
    assertNull(m_vc.getValidators());
  }

  @Test
  public void testGetValidators() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    IValidator validator1=new BooleanValidator();
    IValidator validator2=new BooleanValidator();
    
    m_vc.addValidator(validator1);
    m_vc.addValidator(validator2);
    
    /*
     * Nothing happens.
     */
    m_vc.addValidator(validator1);
    m_vc.addValidator(validator2);
      
    assertNull(m_vc.getValidators());
  }

  @Test
  public void testValidate() {
    try {
      m_vc=new VCContainer(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    IValidator validator1=new BooleanValidator();
    IValidator validator2=new BooleanValidator();
    
    m_vc.addValidator(validator1);
    m_vc.addValidator(validator2);
    
    /*
     * Every validate() must return true.
     */
    assertTrue(m_vc.validate());
  }

}
