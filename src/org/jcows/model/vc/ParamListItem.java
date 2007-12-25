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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jcows.JCowsException;
/**
 * The <code>ParamListItem</code> is a data structure class that holds data for a Web Service
 * request or response. In fact, it is a data protocol between the {@link org.jcows.model.core.GuiLogic} and the
 * Visual Components. Data entered in a Visual Component or from a Web Service response is stored
 * in a <code>ParamListItem</code>. A <code>ParamListItem</code> object can contain one data field,
 * an array or inside again one or more <code>ParamListItem</code>.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class ParamListItem {

  private static final Logger LOGGER = Logger.getLogger(ParamListItem.class);

  /*
   * Defines the type of a ParamListItem.
   * A field means one only one data field, a container means
   * the ParamListItem contains inside again one or more ParamListItem.
   */
  public static final byte ITEMTYPE_FIELD=0;
  public static final byte ITEMTYPE_CONTAINER=1;
  public static final byte ITEMTYPE_ARRAY=2;
  private byte m_type;
  
  private boolean m_isPrimitive;
  private boolean m_isArray;
  
  private String m_datatype;
  private String m_label;
  
  /*
   * Vector that holds effective data.
   */
  private Vector m_VectorData;
  private List<ParamListItem> m_list;
  private Class m_containerComponentType;

  /**
   * Constructs a new instance of this class.
   * 
   * @param datatype the datatype.
   * @param isPrimitive true, if a primitive datatype.
   * @param label the label showed in the Visual Component.
   * @param isArray true, if it an array.
   */
  public ParamListItem(String datatype,boolean isPrimitive,String label,boolean isArray) {
    m_VectorData=new Vector();
    if(isArray)
      m_type=ParamListItem.ITEMTYPE_ARRAY;
    else
      m_type=ParamListItem.ITEMTYPE_FIELD;
    m_datatype=datatype;
    m_isPrimitive=isPrimitive;
    m_isArray=isArray;
    m_label=label;
  }

  /**
   * Constructs a new instance of this class.
   * 
   * @param datatype the datatype.
   * @param isPrimitive true, if a primitive datatype.
   * @param label the label showed in the Visual Component.
   * @param defaultValue the default value of the data.
   * @param isArray true, if it an array.
   */
  public ParamListItem(String datatype,boolean isPrimitive,String label,Object defaultValue,boolean isArray) throws JCowsException {
    m_VectorData=new Vector();
    
    if(isArray){
      m_type=ParamListItem.ITEMTYPE_ARRAY;
      int len = Array.getLength(defaultValue);
      for (int i = 0; i < len; ++i)
        m_VectorData.add(Array.get(defaultValue, i));
    }
    else{
      m_type=ParamListItem.ITEMTYPE_FIELD;
      m_VectorData.add(defaultValue);
    }
    m_isArray=isArray;
    m_label=label;
    m_datatype=datatype;
    m_isPrimitive=isPrimitive;
  }

  /**
   * Constructs a new instance of this class.
   * 
   * @param list the list containing other <code>ParamListItem<code> objects.
   * @param label the label showed in the Visual Component.
   * @param componentType the class of the component type.
   */
  public ParamListItem(List<ParamListItem> list,String label, Class componentType) {
    m_type=ParamListItem.ITEMTYPE_CONTAINER;
    m_label=label;
    m_list=list;
    m_containerComponentType=componentType;
  } 

  /**
   * Returns true, if the <code>ParamListItem</code> object represents a container.
   * 
   * @return true, if a container, otherwise false.
   */
  public boolean isContainer() {
    return m_type==ParamListItem.ITEMTYPE_CONTAINER;
  }

  /**
   * Returns true, if the <code>ParamListItem</code> contains only one data field.
   * 
   * @return true, if field, otherwise false.
   */
  public boolean isField() {
    return m_type==ParamListItem.ITEMTYPE_FIELD;
  }
  
  /**
   * Returns true, if the <code>ParamListItem</code> object contains a data array.
   * 
   * @return true, if array, otherwise false.
   */
  public boolean isArray() {
    return m_type==ParamListItem.ITEMTYPE_ARRAY;
  }
  
  /**
   * Returns true, if the <code>ParamListItem</code> object data is of primitive data type.
   * 
   * @return true, if primitive, otherwise false.
   */
  public boolean isPrimitive() {
    return m_isPrimitive;
  }
  
  /**
   * Returns the data hold by the <code>ParamListItem</code> object.
   * 
   * @return the data vector.
   */
  public Vector getVectorData() {
    return m_VectorData;
  }
  
  /**
   * Returns the Java data type.
   * 
   * @return the datatype.
   */
   public String getDatatype() {
    return m_datatype;
  }

  /**
   * Returns the list of <code>ParamListItem</code> objects inside
   * the <code>ParamListItem</code> object.
   * 
   * @return the list of <code>ParamListItem</code> objects.
   */
  public List<ParamListItem> getList() {
    return m_list;
  }

  /**
   * Returns the label of the <code>ParamListItem</code> object.
   * 
   * @return the label of the <code>ParamListItem</code> object.
   */
  public String getLabel() {
    return m_label;
  }

  /**
   * Returns the type of the <code>ParamListItem</code> object.
   * 
   * @return the type of the <code>ParamListItem</code> object.
   */
  public byte getItemtype() {
    return m_type;
  }

  /**
   * Returns the data in the correct format. This method converts the data
   * stored in the {@link org.jcows.model.vc.ParamListItem} object.
   * 
   * @return data in the correct format.
   */
  public Object getValue() {
    /*
     * Tests, if data is of Java primitive data type.
     */
    if(m_isPrimitive) {
      if(m_VectorData.get(0) instanceof Boolean)
        return toBooleanPrimitive();
      else if(m_VectorData.get(0) instanceof Byte)
        return toBytePrimitive();
      else if(m_VectorData.get(0) instanceof Short)
        return toShortPrimitive();
      else if(m_VectorData.get(0) instanceof Integer)
        return toIntPrimitive();
      else if(m_VectorData.get(0) instanceof Long)
        return toLongPrimitive();
      else if(m_VectorData.get(0) instanceof Float)
        return toFloatPrimitive();
      else if(m_VectorData.get(0) instanceof Double)
        return toDoublePrimitive();
    }
    /*
     * Return array.
     */
    if(m_isArray) {
      if(m_VectorData.get(0) instanceof Boolean) {
        Boolean[] array = new Boolean[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Byte) {
        Byte[] array = new Byte[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Short) {
        Short[] array = new Short[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Integer) {
        Integer[] array = new Integer[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Long) {
        Long[] array = new Long[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Float) {
        Float[] array = new Float[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof Double) {
        Double[] array = new Double[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
      else if(m_VectorData.get(0) instanceof String) {
        String[] array = new String[m_VectorData.size()];
        Object retval=m_VectorData.toArray(array);
        return retval;
      }
    }
    return m_VectorData.get(0);
  }

  public Class getContainerComponentType(){
    return m_containerComponentType;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toBooleanPrimitive() {
    Object retval=null;
    if(!m_isArray) {
      boolean retval2=(Boolean)m_VectorData.get(0);
      return retval2;
    }
    retval=new boolean[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setBoolean(retval,i,(Boolean)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toBytePrimitive() {
    Object retval=null;
    if(!m_isArray) {
      byte retval2=(Byte)m_VectorData.get(0);
      return retval2;
    }
    retval=new byte[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setByte(retval,i,(Byte)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toShortPrimitive() {
    Object retval=null;
    if(!m_isArray) {
      short retval2=(Short)m_VectorData.get(0);
      return retval2;
    }
    retval=new short[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setShort(retval,i,(Short)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toIntPrimitive() {
    Object retval=null;
    if(!m_isArray) {
      int retval2=(Integer)m_VectorData.get(0);
      return retval2;
    }
    retval=new int[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setInt(retval,i,(Integer)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toLongPrimitive() {
    Object retval=null;
    if(!m_isArray) {
      long retval2=(Long)m_VectorData.get(0);
      return retval2;
    }
    retval=new long[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setLong(retval,i,(Long)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toFloatPrimitive() {
    Object retval=null;
    if(!m_isArray) {
      float retval2=(Float)m_VectorData.get(0);
      return retval2;
    }
    retval=new float[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setFloat(retval,i,(Float)m_VectorData.get(i));
    return retval;
  }

  /**
   * Returns object of Java primitives. This method converts the effective
   * data to an object of Java primitives. The object can be a single primitive
   * or an array of primitives.
   * 
   * @return object of Java primitives.
   */
  private Object toDoublePrimitive() {
    Object retval=null;
    if(!m_isArray) {
      double retval2=(Double)m_VectorData.get(0);
      return retval2;
    }
    retval=new double[m_VectorData.size()];
    for(int i=0;i<m_VectorData.size();i++)
      Array.setDouble(retval,i,(Double)m_VectorData.get(i));
    return retval;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if(obj instanceof ParamListItem) {
      ParamListItem paramListItem=(ParamListItem)obj;
      return ( paramListItem.getDatatype().equals(m_datatype) &&
          paramListItem.getItemtype()==m_type &&
          paramListItem.getVectorData().equals(m_VectorData) );
    }
    return super.equals(obj);
  }

}
