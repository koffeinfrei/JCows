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
package org.jcows.model.vc;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcows.JCowsException;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;
import org.jcows.view.vc.IVC;
import org.jcows.view.vc.VCArray;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
/**
 * This class maps a Java datatype to a Visual Component class.
 * For every primitive Java datatype and their wrapper classes exist a corresponding
 * component. The information about which Java datatype has to be mapped to which
 * {@link org.jcows.view.vc.IVC IVC} class is stored in a XML file. The XML file vcmapping.xml
 * has a structure like this:<br/><br/>
 * 
 * &lt;mapping datatype="boolean"&gt;<br/>
 *  &lt;visualclass>org.jcows.view.vc.VCboolean&lt;/visualclass&gt;<br/>
 *  &lt;javatype>java.lang.Boolean&lt;/javatype&gt;<br/>
 *  &lt;javatype>boolean&lt;/javatype&gt;<br/>
 *  &lt;itemtype>field&lt;/itemtype&gt;<br/>
 *  &lt;defaultvalue>false&lt;/defaultvalue&gt;<br/>
 *  &lt;defaultvalidatorclass>org.jcows.model.vc.BooleanValidator&lt;/defaultvalidatorclass&gt;<br/>
 * &lt;/mapping&gt;<br/>
 * <br/>
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class VCMapper {
  
  //private static final Logger LOGGER = Logger.getLogger(VCMapper.class);
  
  private static VCMapper m_VCMapper=null;

  /*
   * Defines constants for attribute and element tag name.
   * For example <mapping> datatype="boolean">...</mapping>
   */
  public static final String ATTRIBUTENAME_DATATYPE="datatype";
  public static final String ELEMENTNAME_JAVATYPE="javatype";
  public static final String ELEMENTNAME_VISUALCLASS="visualclass";
  public static final String ELEMENTNAME_ITEMTYPE="itemtype";
  public static final String ELEMENTNAME_DEFAULTVALUE="defaultvalue";
  public static final String ELEMENTNAME_DEFAULTVALIDATORCLASS="defaultvalidatorclass";
  
  /*
   * Defines constants for the type of data structure for the
   * Visual Component.
   */
  public static final String ITEMTYPE_FIELD="field";
  public static final String ITEMTYPE_CONTAINER="container";
  public static final String ITEMTYPE_ARRAY="array";
  
	private Map<String,Class[]> m_mapVC;
  private Element m_elementRoot=null;
  private Element m_elementContainer=null;
  private Namespace m_namespaceDefault=null;
  
  /**
   * Constructs a new instance of this class.
   * This class is a singleton and can only be instantiated by
   * the class itself. The construction of the new instance parses
   * the VCMapping XML and XSD file and creates a map of Visual Components.
   * 
   * @throws JCowsException if XML parsing failed.
   */
  private VCMapper() throws JCowsException {

    m_mapVC=new HashMap<String,Class[]>();
    Document doc = null;
    try {
      SAXBuilder builder=new SAXBuilder(true);
      builder.setFeature("http://apache.org/xml/features/validation/schema",true);
      /*
       * Enables XML schema validation that validates the vcmapper.xml against vcschema.xsd.
       */
      builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation","http://www.jcows.org/VCSchema vcschema.xsd");
      File file=JCowsHelper.getFile("vcschema.xsd",false);
      file=JCowsHelper.getFile("vcmapping.xml",false);
      doc=builder.build(file);
    }
    catch(IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException")+
          Properties.getMessage("error.VCMappingError"),e);
    }
    catch(JDOMException e) {
      throw new JCowsException(Properties.getMessage("error.JDOMException")+
          Properties.getMessage("error.VCMappingError"),e);
    }

    m_elementRoot=doc.getRootElement();
    m_namespaceDefault=m_elementRoot.getNamespace();

    List<Element> children=m_elementRoot.getChildren();
    for(Element element:children) {

      String datatype=element.getAttributeValue(ATTRIBUTENAME_DATATYPE);
      String vcClassName=element.getChildText(ELEMENTNAME_VISUALCLASS,m_namespaceDefault);
      String validatorClassName=element.getChildText(ELEMENTNAME_DEFAULTVALIDATORCLASS,m_namespaceDefault);

      ClassLoader classloader=Thread.currentThread().getContextClassLoader();
      try {
        Class vcClass=classloader.loadClass(vcClassName);
        /*
         * Tests, if default validator is available.
         */
        if(validatorClassName!=null && !validatorClassName.equals("")) {
          /*
           * Creates a new Class object of the default validator.
           */
          Class validatorClass=classloader.loadClass(validatorClassName);
          m_mapVC.put(datatype,new Class[]{vcClass,validatorClass});
        }
        /*
         * If no default validator is available.
         */
        else
          m_mapVC.put(datatype,new Class[]{vcClass});
      }
      catch(ClassNotFoundException e) {
        throw new JCowsException(Properties.getMessage("error.ClassNotFoundException"),e);
      }
    }
  }
  
  /**
   * Constructs a new instance of this class or return the current.
   * 
   * @return {@link org.jcows.model.vc.VCMapper} instance.
   * @throws JCowsException if XML parsing failed.
   */
  public static VCMapper getInstance() throws JCowsException {
    if(m_VCMapper==null)
      m_VCMapper=new VCMapper();
    return m_VCMapper;
  }
	
  /**
   * Maps a given list of {@link org.jcows.model.vc.ParamListItem} to a list of Visual Components.
   * This method constructs Visual Components for a Web Service request. The values in the {@link org.jcows.model.vc.ParamListItem}
   * objects are set to the default. After changing the values, the list of {@link org.jcows.model.vc.ParamListItem}
   * can be sent to the Web Service.
   * 
   * @param parent parent component.
   * @param list list of {@link org.jcows.model.vc.ParamListItem} objects.
   * @throws JCowsException if {@link org.jcows.view.vc.IVC} classes couldn't generated dynamically.
   * @see org.jcows.model.vc.ParamListItem
   */
	public List<IVC> mapVCRequest(Composite parent,List<ParamListItem> list) throws JCowsException {
    List<IVC> addedVC=new ArrayList<IVC>();
    if(list.size()==0) {
      Label labelInfo=new Label(parent,SWT.NONE);
      labelInfo.setText("Method requires no arguments.");
    }
    IVC vc=null;
    for(ParamListItem item:list) {
      if(item.isContainer()) {
        Class clazz=m_mapVC.get(ITEMTYPE_CONTAINER)[0];
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Constructor constructor=clazz.getConstructor(new Class[]{ParamListItem.class,Composite.class});
          vc=(IVC)constructor.newInstance(new Object[]{item,parent});
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
        addedVC.addAll(mapVCRequest(vc.getComposite(),item.getList()));
      }
      else if(item.isArray()) {
        Class clazz1=m_mapVC.get(ITEMTYPE_ARRAY)[0];
        Class clazz2=m_mapVC.get(item.getDatatype())[0];
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Constructor constructor=clazz1.getConstructor(new Class[]{Composite.class,List.class,ParamListItem.class,Composite.class,Class.class});
          vc=(IVC)constructor.newInstance(new Object[]{parent,addedVC,item,parent,clazz2});
          /*
           * Add first array element by default.
           */
          ((VCArray)vc).addArrayElement();
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
        addedVC.add(vc);
      }
      else if(item.isField()){
        IValidator validator=null;
        Class classValidator=null;
        Class classVC=m_mapVC.get(item.getDatatype())[0];
        if(m_mapVC.get(item.getDatatype()).length>1)
          classValidator=m_mapVC.get(item.getDatatype())[1];
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Constructor constructor=classVC.getConstructor(new Class[]{ParamListItem.class,Composite.class});
          vc=(IVC)constructor.newInstance(new Object[]{item,parent});
          /*
           * Add default validator to the Visual Component.
           */
          addedVC.add(vc);
          /*
           * Constructs new instance of the default validator.
           */
          if(classValidator!=null) {
            constructor=classValidator.getConstructor(new Class[]{});
            validator=(IValidator)constructor.newInstance();
            vc.addValidator(validator);
          }
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
      }
    }
    parent.layout();
    return addedVC;
	}

  /**
   * Maps a given list of {@link org.jcows.model.vc.ParamListItem} to a list of Visual Components.
   * This method constructs Visual Components from a Web Service response. The response generated
   * {@link org.jcows.model.vc.ParamListItem} objects that contain the effective data that has to be shown
   * in the Visual Components. For example a {@link org.jcows.model.vc.ParamListItem} object may contain
   * the integer 5 that now has to be shown in text box.
   * 
   * @param parent parent component.
   * @param list list of {@link org.jcows.model.vc.ParamListItem}ParamListItem{@link org.jcows.model.vc.ParamListItem} objects.
   * @return list of {@link org.jcows.view.vc.IVC} objects.
   * @throws JCowsException if {@link org.jcows.view.vc.IVC} classes couldn't generated dynamically.
   */
  public List<IVC> mapVCResponse(Composite parent,List<ParamListItem> list) throws JCowsException {
    List<IVC> addedVC=new ArrayList<IVC>();
    if(list.size()==0) {
      Label labelInfo=new Label(parent,SWT.NONE);
      labelInfo.setText("Method returned void.");
    }
    IVC vc=null;
    for(ParamListItem item:list) {
      if(item.isContainer()) {
        Class clazz=m_mapVC.get(ITEMTYPE_CONTAINER)[0];
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Constructor constructor=clazz.getConstructor(new Class[]{ParamListItem.class,Composite.class});
          vc=(IVC)constructor.newInstance(new Object[]{item,parent});
          vc.setEditable(false);
          addedVC.addAll(mapVCRequest(vc.getComposite(),item.getList()));
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
      }
      else if(item.isArray()) {
        IVC vcarray=null;
        Class clazz1=m_mapVC.get(ITEMTYPE_ARRAY)[0];
        Class clazz2=m_mapVC.get(item.getDatatype())[0];
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Constructor constructor=clazz1.getConstructor(new Class[]{Composite.class,List.class,ParamListItem.class,Composite.class,Class.class});
          vcarray=(IVC)constructor.newInstance(new Object[]{parent,addedVC,item,parent,clazz2});
          vcarray.setEditable(false);
          addedVC.add(vcarray);
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
        for(int index=0;index<item.getVectorData().size();index++) {
          /*
           * Creates item.size() Visual Components. Every created represents
           * one position in the array. 
           */
          try {
            /*
             * Constructs new instance of a Visual Component.
             */
            Constructor constructor=clazz2.getConstructor(new Class[]{ParamListItem.class,Composite.class,int.class});
            vc=(IVC)constructor.newInstance(new Object[]{item,vcarray.getComposite(),index});
            
            String label=vc.getLabel();
            vc.setLabel(label.replace("[]","["+index+"]"));
            vc.setEditable(false);
            /*
             * Do not add first element because this is added by default.
             */
            addedVC.add(vc);
          }
          catch(SecurityException e) {
            throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
          }
          catch(IllegalArgumentException e) {
            throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
          }
          catch(NoSuchMethodException e) {
            throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
          }
          catch(InstantiationException e) {
            throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
          }
          catch(IllegalAccessException e) {
            throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
          }
          catch(InvocationTargetException e) {
            throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
          }
        }
      }
      else if(item.isField()) {
        try {
          /*
           * Constructs new instance of a Visual Component.
           */
          Class clazz=m_mapVC.get(item.getDatatype())[0];
          Constructor constructor=clazz.getConstructor(new Class[]{ParamListItem.class,Composite.class});
          vc=(IVC)constructor.newInstance(new Object[]{item,parent});
          vc.setEditable(false);
          addedVC.add(vc);
        }
        catch(SecurityException e) {
          throw new JCowsException(Properties.getMessage("error.SecurityException"),e);
        }
        catch(IllegalArgumentException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalArgumentException"),e);
        }
        catch(NoSuchMethodException e) {
          throw new JCowsException(Properties.getMessage("error.NoSuchMethodException"),e);
        }
        catch(InstantiationException e) {
          throw new JCowsException(Properties.getMessage("error.InstantiationException"),e);
        }
        catch(IllegalAccessException e) {
          throw new JCowsException(Properties.getMessage("error.IllegalAccessException"),e);
        }
        catch(InvocationTargetException e) {
          throw new JCowsException(Properties.getMessage("error.InvocationTargetException"),e);
        }
      }
    }   
    parent.layout();
    return addedVC;
  }
	
  /**
   * Returns the Visual Component for the corresponding data type.
   * 
   * @param datatype data type for the Visual Component.
   * @return the corresponding Visual Component.
   */
	public Class getClass(String datatype) {
		return m_mapVC.get(datatype)[0];
	}
  
  /**
   * Returns the default validator class for the corresponding
   * data type.
   * 
   * @param datatype data type for the default validator class.
   * @return the default validator class.
   */
  public Class geDefaultValidatorClass(String datatype) {
    return m_mapVC.get(datatype)[1];
  }
  
  /**
   * Returns a map of attribute and element values from the VCMapper XML file.
   * Every &lt;mapping datatype="..."&gt;...&lt;/mapping&gt; element contains
   * child elements with detailed information about the mapping for a Visual Component.<br/><br/>
   * Notice: If no corresponding Visual Component was found for the javatype, then
   * a container Visual Component gets mapped.
   * 
   * @param javatype the Java data type (for example java.lang.Integer or only int for primitive).
   * @return a map containing information about the mapping.
   */
  public Map<String,String> getMapping(String javatype) throws JCowsException {
    boolean found=false;
    Map<String,String> map=new HashMap<String,String>();
    /*
     * Get <mapping> elements
     */
    List<Element> mappings=m_elementRoot.getChildren();
    for(Element mapping:mappings) {
      if(mapping.getAttributeValue(ATTRIBUTENAME_DATATYPE).equals(ITEMTYPE_CONTAINER))
          m_elementContainer=mapping;
      /*
       * Get <javatype> elements.
       */
      List<Element> javatypes=mapping.getChildren(ELEMENTNAME_JAVATYPE,m_namespaceDefault);
      for(Element javatype1:javatypes) {
        if(javatype1.getText().equals(javatype)) {
          String datatype=mapping.getAttributeValue(ATTRIBUTENAME_DATATYPE);
          String itemtype=mapping.getChildText(ELEMENTNAME_ITEMTYPE,m_namespaceDefault);        
          String defaultValue=mapping.getChildText(ELEMENTNAME_DEFAULTVALUE,m_namespaceDefault);
          
          map.put(ATTRIBUTENAME_DATATYPE,datatype);
          map.put(ELEMENTNAME_ITEMTYPE,itemtype);
          map.put(ELEMENTNAME_DEFAULTVALUE,defaultValue);
          found=true;
          break;
        }
      }
      if(found==true)
        break;
    }
    /*
     * If no corresponding Visual Component was found for the javatype.
     * This occurs if the javatype is an unrecognized class or primitive.
     * A Java Bean would not be unrecognized and mapped to a container Visual Component.
     */
    if (map.size() == 0) {
      /*
       * m_elementContainer is the default container Visual Component.
       */
      String datatype = m_elementContainer.getAttributeValue(ATTRIBUTENAME_DATATYPE);
      String itemtype = m_elementContainer.getChildText(ELEMENTNAME_ITEMTYPE,m_namespaceDefault);
      String defaultValue = m_elementContainer.getChildText(ELEMENTNAME_DEFAULTVALUE,m_namespaceDefault);

      map.put(ATTRIBUTENAME_DATATYPE, datatype);
      map.put(ELEMENTNAME_ITEMTYPE, itemtype);
      map.put(ELEMENTNAME_DEFAULTVALUE, defaultValue);
    }

    return map;
  }
	
}
