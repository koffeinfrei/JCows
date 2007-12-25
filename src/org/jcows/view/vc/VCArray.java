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
package org.jcows.view.vc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jcows.JCowsException;
import org.jcows.controller.JCowsController;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

import com.cloudgarden.resource.SWTResourceManager;
/**
 * The <code>VCArray</code> class represents an array of Java types.
 * This class holds other Visual Components and manages their layout.
 * <code>VCArray</code> can add or remove (increase or decrease the array size) components.<br/><br/>
 * For example a <code>VCArray</code> object can add several <code>VCint</code>
 * objects. An <code>int[5]</code> is represented as a <code>VCArray</code> object
 * that contains five <code>VCint</code> objects.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCArray extends VC {

  private static final Logger LOGGER = Logger.getLogger(VCArray.class);

  private Composite m_compositeGUI;
  private List<IVC> m_addedVC;
  private Class m_visualClass;
  
  private Composite m_compositeElements;
  private Composite m_compositeToolBar;
  private Group m_groupArray=null;
  private ToolItem m_toolItemAdd=null;
  private ToolItem m_toolItemRemove=null;
  
  private Object m_startValue;
  private int m_arraypos=-1;

  /**
   * Constructs a new instance of this class.
   * 
   * @param compositeGUI the composite where new components can be drawn.
   * @param addedVC the list that contains all visual components.
   * @param paramListItem the correspondung {@link org.jcows.model.vc.ParamListItem} for this object.
   * @param parent the parent composite.
   * @param visualClass the visual class that will be instantiated for a new array element.
   */
  public VCArray(final Composite compositeGUI,final List<IVC> addedVC,ParamListItem paramListItem,
      final Composite parent,final Class visualClass) throws JCowsException {
    super(paramListItem,parent);
    
    m_compositeGUI=compositeGUI;
    m_addedVC=addedVC;
    m_visualClass=visualClass;

    GridLayout layoutGroup=new GridLayout();
    layoutGroup.makeColumnsEqualWidth=false;
    layoutGroup.numColumns=1;

    FillLayout layoutButtons=new FillLayout();

    GridLayout layoutElements=new GridLayout();
    layoutElements.makeColumnsEqualWidth=false;
    layoutElements.numColumns=1;

    m_groupArray=new Group(this,SWT.NONE);
    m_groupArray.setLayout(layoutGroup);
    m_groupArray.setText(paramListItem.getLabel());

    /*
     * Reset the current default label.
     */
    m_label.setText("");

    m_compositeToolBar=new Composite(m_groupArray,SWT.NONE);
    m_compositeToolBar.setLayout(layoutButtons);

    m_compositeElements=new Composite(m_groupArray,SWT.NONE);
    m_compositeElements.setLayout(layoutElements);

    ToolBar toolBar=new ToolBar(m_compositeToolBar,SWT.FLAT);

    m_toolItemAdd=new ToolItem(toolBar,SWT.NONE);
    m_toolItemAdd.setText("Add");
    m_toolItemAdd.setImage(SWTResourceManager.getImage("resources/add.png"));

    m_toolItemRemove=new ToolItem(toolBar,SWT.NONE);
    m_toolItemRemove.setText("Remove");
    m_toolItemRemove.setImage(SWTResourceManager.getImage("resources/remove.png"));

    /*
     * Add a listener to add components.
     */
    m_toolItemAdd.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent evt) {
        LOGGER.debug(evt);
        addArrayElement();
      }
    });
    /*
     * Add a listener to remove components.
     */
    m_toolItemRemove.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent evt) {
        LOGGER.debug(evt);
        removeArrayElement();
      }
    });
    m_startValue=paramListItem.getVectorData().get(0);
    /*
     * Add one array element by default.
     */
    // TODO: is one element really necessary?
    //addArrayElement();
  }
  
  public void addArrayElement() {
    IVC vc=null;
    try {
      m_arraypos++;
      
      if(m_arraypos>0)
        m_paramListItem.getVectorData().add(m_startValue);
      
      Constructor constructor=m_visualClass.getConstructor(new Class[]{ParamListItem.class,Composite.class,int.class});
      vc=(IVC)constructor.newInstance(new Object[]{m_paramListItem,m_compositeElements,m_arraypos});

      m_addedVC.add(vc);
      /*
       * Updates the request composite size.
       * In test cases, the reference may be null.
       */
      if(JCowsController.m_mainWindowController!=null)
        JCowsController.m_mainWindowController.m_mainWindow.scrolledCompositeGUIRequestUpdate();
    }
    catch(SecurityException e) {
      new JCowsException(Properties.getMessage("error.SecurityException"),e);
    }
    catch(IllegalArgumentException e) {
      m_arraypos--;
      new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
    }
    catch(NoSuchMethodException e) {
      m_arraypos--;
      new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
    }
    catch(InstantiationException e) {
      m_arraypos--;
      new JCowsException(Properties.getMessage("error.InstantiationException"),e);
    }
    catch(IllegalAccessException e) {
      m_arraypos--;
      new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
    }
    catch(InvocationTargetException e) {
      m_arraypos--;
      new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
    }    
  }
  
  private void removeArrayElement() {
    if(m_arraypos>0) {
      
      m_paramListItem.getVectorData().remove(m_arraypos);
      
      Control[] controls=m_compositeElements.getChildren();
      m_addedVC.remove(controls[controls.length-1]);
      controls[controls.length-1].dispose();
      /*
       * Updates the request composite size.
       * In test cases, the reference may be null.
       */
      if(JCowsController.m_mainWindowController!=null)
        JCowsController.m_mainWindowController.m_mainWindow.scrolledCompositeGUIRequestUpdate();
      m_arraypos--;
    }    
  }

  public Composite getComposite() {
    return m_groupArray;
  }

  public void addValidator(IValidator validator) {
    /*
     * Do nothing.
     */
  }

  public IValidator[] getValidators() {
    return null;
  }

  public String getLabel() {
    return m_groupArray.getText();
  }

  public void setLabel(String value) {
    m_groupArray.setText(value);
  }

  public boolean validate() {
    return true;
  }
  
  // TODO: Implement error state in VCArray.
  public void setErrorState() {

  }

  public void setEditable(boolean editable) {
    m_compositeToolBar.dispose();
    m_groupArray.layout();
  }

}
