package org.jcows.test.view.vc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcows.JCowsException;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.model.vc.RegexValidator;
import org.jcows.system.Properties;
import org.jcows.view.vc.VCstring;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestVCstring {
  
  private static Shell m_shell;
  private static Display m_display;
  
  private VCstring m_vc;
  private ParamListItem m_paramListItem;
  private ParamListItem m_paramListItemNull;

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
    m_paramListItem=new ParamListItem("string",true,"string value","any string",false);
    m_paramListItemNull=new ParamListItem("string",true,"string value",null,false);
  }
  
  @Test
  public void testVCstringParamListItemComposite() {   
    try {
      m_vc=new VCstring(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    try {
      m_vc=new VCstring(m_paramListItemNull,m_shell);
      fail("Should have raised an JCowsException.");
    }
    catch(JCowsException e) {
      
    }
  }

  @Test
  public void testVCstringParamListItemCompositeInt() {   
    try {
      m_vc=new VCstring(m_paramListItem,m_shell,0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    try {
      m_vc=new VCstring(m_paramListItemNull,m_shell,5);
      fail("Should have raised an JCowsException.");
    }
    catch(JCowsException e) {

    }
  }

  @Test
  public void testValidate() {
    IValidator validator1=new RegexValidator(".*");
    IValidator validator2=new RegexValidator("[a-z]+");
    
    try {
      m_vc=new VCstring(m_paramListItem,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    m_vc.addValidator(validator1);
    assertTrue(m_vc.validate());
    m_vc.addValidator(validator2);
    assertFalse(m_vc.validate());
    
    /*
     * Try to validate against a null string reference.
     * Validator must return false.
     */
    m_vc.addValidator(validator1);
    assertFalse(m_vc.validate());
    m_vc.addValidator(validator2);
    assertFalse(m_vc.validate());
  }

}
