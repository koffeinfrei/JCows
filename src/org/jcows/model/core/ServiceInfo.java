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
package org.jcows.model.core;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.client.Service;

/**
 * This class represents a service and holds all information that are needed
 * by the JCows model.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
public class ServiceInfo {
  public String name;
  public Service serviceLocator;
  public List<PortInfo> ports;
  
  /**
   * Constructs a new instance of this class with
   * the specified service name.
   * 
   * @param name the name of the service.
   */
  ServiceInfo(String name){
    this.name = name;
    ports = new ArrayList<PortInfo>();
  }
  
  /**
   * Adds a port to the this service.
   * 
   * @param port the port to be added.
   */
  public void addPort(PortInfo port){
    ports.add(port);
  }
  
  /**
   * Returns the port at the specified position.
   * 
   * @param index the position of the port.
   * @return the port
   */
  public PortInfo getPort(int index){
    return ports.get(index);
  }
  
  /**
   * Returns the number of ports contained in this service.
   * 
   * @return the number of ports.
   */
  public int numPorts(){
    return ports.size();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString(){
    StringBuffer buf = new StringBuffer();
    
    buf.append("Service       [");
    buf.append(name);
    buf.append("]\n");
    for (PortInfo p : ports){
      buf.append(" Port         [");
      buf.append(p.name);
      buf.append("]\n");
      for (OperationInfo op : p.operations){
        buf.append("  Operation   [");
        buf.append(op.name);
        buf.append("]\n");
      }
    }
    
    return buf.toString();
  }
}

/**
 * This class represents a port and holds all information that are needed
 * by the JCows model.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
class PortInfo{
  public String name;
  public QName qName;
  public Class portClass;
  public Remote portInstance;
  public List<OperationInfo> operations;
  
  /**
   * Constructs a new instance of this class with
   * the specified port name.
   * 
   * @param name the name of the port.
   */
  PortInfo(String name){
    this.name = name;
    operations = new ArrayList<OperationInfo>();
  }
  
  /**
   * Adds an operation to this port.
   * 
   * @param operation the name of the operation.
   * @param args the parameter list for the operation.
   * @param returnType the return type of the operation.
   */
  public void addOperation(String operation, Class[] args, Class returnType){
    operations.add(new OperationInfo(operation, args, returnType));
  }
  
  /**
   * Returns the operation at the specified position.
   * 
   * @param index the position of the operation.
   * @return the operation.
   */
  public OperationInfo getOperation(int index){
    return operations.get(index);
  }
  
  /**
   * Returns the number of operations contained in this service.
   * 
   * @return the number of operations.
   */
  public int numOperations(){
    return operations.size();
  }
}

/**
 * This class represents a operation and holds all information that are needed
 * by the JCows model.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
class OperationInfo{
  public String name;
  public Class[] arguments;
  public Class returnType;
  
  /**
   * Creates a new instance of this class.
   * 
   * @param name the name of the operation.
   * @param arguments the paremeter list of the operation.
   * @param returnType the return type of the operation.
   */
  public OperationInfo(String name, Class[] arguments, Class returnType){
    this.name = name;
    this.arguments = arguments;
    this.returnType = returnType;
  }
}
