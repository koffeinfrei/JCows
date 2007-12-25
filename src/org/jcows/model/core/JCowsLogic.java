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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.rmi.Remote;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Service;
import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.jcows.JCowsException;
import org.jcows.controller.DialogSSLController;
import org.jcows.system.Properties;

/**
 * This Class provides the methods for the controller to
 * interact with the model.
 * It contains the functionality that all the models have in common.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 165 $, $LastChangedDate: 2006-07-05 21:51:21 +0000 (Wed, 05 Jul 2006) $
 *  
 */
public class JCowsLogic implements IJcowsLogic{
  protected static final Logger LOGGER = Logger.getLogger(SoapLogic.class);

  protected String m_wsdlUrl;
  
  protected JCowsFile m_jcowsFile;
  
  protected List<Class> m_wsdlTypes;
  protected List<Class> m_wsdlHolders;
  protected List<Class> m_wsdlSDI;
  protected List<Class> m_wsdlStub;
  protected List<Class> m_wsdlServiceInterface;
  protected List<Class> m_wsdlServiceLocator;
  
  protected List<ServiceInfo> m_services;
  
  protected ServiceInfo m_currService;
  protected PortInfo m_currPort;
  protected OperationInfo m_currOperation;
  
  /* URL class loader for the ws jar file */
  protected URLClassLoader m_jarLoader;
  
  protected ClassHelper m_classHelper;
  
  protected DialogSSLController m_dialogSSLController;
  protected JCowsX509TrustManager m_trustManager;
  
  /**
   * Constructs a new JCowsLogic instance for the web service described
   * by the specified WSDL URL. This instance does not support ssl.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @throws JCowsException
   */
  public JCowsLogic(String wsdlUrl) throws JCowsException{
    this(wsdlUrl, null);
  }
  
  /**
   * Constructs a new JCowsLogic instance for the web service described
   * by the specified WSDL URL. The DialogSSLController argument is used
   * in case of an untrusted certificate on an SSL connection.
   * 
   * @param wsdlUrl The URL of the WSDL document.
   * @param controller The controller for the SSL dialog.
   * @throws JCowsException
   */
  public JCowsLogic(String wsdlUrl, DialogSSLController controller) throws JCowsException{
    m_wsdlUrl = wsdlUrl;
    
    m_dialogSSLController = controller;
    
    m_wsdlTypes = new ArrayList<Class>();
    m_wsdlHolders = new ArrayList<Class>();
    m_wsdlSDI = new ArrayList<Class>();
    m_wsdlStub = new ArrayList<Class>();
    m_wsdlServiceInterface = new ArrayList<Class>();
    m_wsdlServiceLocator = new ArrayList<Class>();
    
    m_services = new ArrayList<ServiceInfo>();
    
    // get WSDL URL if m_wsdlUrl is the jcows file (open service functionality)
    openWS();
    
    // get JCowsFile instance
    m_jcowsFile = new JCowsFile(m_wsdlUrl); 
    // classHelper
    m_classHelper = new ClassHelper(m_jarLoader);
    
    initWebService();
    //loadClasses();
    //buildComponents();
    
    /* set current service and port */
    m_currService = m_services.get(0);
    m_currPort = m_currService.getPort(0);
    m_currOperation = m_currPort.getOperation(0);
    
    // classHelper 
    // TODO fix that jarloader problem, we don't want to create classhelper twice
    m_classHelper = new ClassHelper(m_jarLoader);
  }
  
  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#setCurrentOperation(java.lang.String)
   */
  public void setCurrentOperation(String operation) throws JCowsException{
    OperationInfo oi = null;
    for (OperationInfo o : m_currPort.operations){
      if (o.name.equals(operation)){
        oi = o;
        break;
      }
    }
    if (oi == null) 
      throw new JCowsException(
          Properties.getMessage("error.noSuchOperation", new String[]{operation}));
    m_currOperation = oi;
  }

  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#setCurrentPort(java.lang.String)
   */
  public void setCurrentPort(String port) throws JCowsException{
    PortInfo pi = null;
    for (PortInfo p : m_currService.ports){
      if (p.name.equals(port)){
        pi = p;
        break;
      }
    }
    if (pi == null) 
      throw new JCowsException(
          Properties.getMessage("error.noSuchPort", new String[]{port}));
    m_currPort = pi;
  }

  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#setCurrentService(java.lang.String)
   */
  public void setCurrentService(String service) throws JCowsException{
    ServiceInfo si = null;
    for (ServiceInfo s : m_services){
      if (s.name.equals(service)){
        si = s;
        break;
      }
    }
    if (si == null)
      throw new JCowsException(
          Properties.getMessage("error.noSuchService", new String[]{service}));
    m_currService = si;
  }

  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#getOperations()
   */
  public String[] getOperations() {
    String[] operations = new String[m_currPort.numOperations()];
    for (int i = 0; i < operations.length; ++i)
      operations[i] = m_currPort.getOperation(i).name;
    return operations;
  }
  
  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#getPorts()
   */
  public String[] getPorts() {
    String[] ports = new String[m_currService.numPorts()];
    for (int i = 0; i < ports.length; ++i)
      ports[i] = m_currService.getPort(i).name;
    return ports;
  }
  
  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#getServices()
   */
  public String[] getServices() {
    String[] services = new String[m_services.size()];
    for (int i = 0; i < services.length; ++i)
      services[i] = m_services.get(i).name;
    return services;
  }
  
  /* (non-Javadoc)
   * @see jcows.model.IJcowsLogic#saveWS(java.lang.String)
   */
  public void saveWS(String filename) throws JCowsException{
    try {
      FileChannel srcChannel = new FileInputStream(
          Properties.getConfig("parser.outputDirName") 
            + getWsdlLocalName() + Properties.getConfig("parser.JCowsFileEnding"))
          .getChannel();
  
      FileChannel dstChannel = new FileOutputStream(filename).getChannel();
  
      // Copy file contents from source to destination
      dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
  
      srcChannel.close();
      dstChannel.close();
    } 
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }


  }
  
  
  /**
   * Build all the components for the Web Service,
   * i.e. extract all services, ports and operations.
   * 
   * @throws JCowsException
   */
  private void buildComponents() throws JCowsException{
    try{ 
      
      // each class
      for (Class cl : m_wsdlServiceLocator){
        // Service Locator
        Service serviceLoc = (Service)cl.newInstance();

        QName servicename = serviceLoc.getServiceName();
        
        ServiceInfo serviceInfo = new ServiceInfo(servicename.getLocalPart());
        serviceInfo.serviceLocator = serviceLoc;
        
        Iterator<QName> it = serviceLoc.getPorts();

        // all ports
        while(it.hasNext()){
          QName name = it.next();
          
          Remote port = (Remote)serviceLoc.getPort(name, null);

          Class portClass = port.getClass();
          
          PortInfo portInfo = new PortInfo(name.getLocalPart());
          portInfo.portClass = portClass;
          portInfo.portInstance = port;
          portInfo.qName = name;
  
          Method[] methods = portClass.getDeclaredMethods();
          // all operations
          for (Method m : methods){
            if ((m.getModifiers() & Modifier.PUBLIC) != 0){;
              portInfo.addOperation(m.getName(), m.getParameterTypes(),m.getReturnType());
            }
          }
          serviceInfo.addPort(portInfo);
        }
        
        m_services.add(serviceInfo);
        
        /* assure everything is fine */
        if (m_services == null || m_services.size() == 0)
          throw new JCowsException(
              Properties.getMessage("error.soapLogicNoServices"));
        if (m_services.get(0).numPorts() == 0)
          throw new JCowsException(
              Properties.getMessage("error.soapLogicNoPorts"));
        if (m_services.get(0).getPort(0).numOperations() == 0)
          throw new JCowsException(
              Properties.getMessage("error.soapLogicNoOperations"));
        
        LOGGER.info("Web Service:\n" + m_services.get(m_services.size() -1));
      }
      
    }
    catch(InstantiationException e){
      throw new JCowsException(Properties.getMessage("error.InstantiationException"), e);
    }
    catch(IllegalAccessException e){
      throw new JCowsException(Properties.getMessage("error.IllegalAccessException"), e);
    }
    catch(ServiceException e){
      throw new JCowsException(Properties.getMessage("error.ServiceException"), e);
    }
  }
  
  /**
   * Opens a web service, i.e. loads the WSDL URL from the jcows file,
   * provided that the WSDL URL is currently set to a jcows file 
   * (which is the case when a jcows file is opened in the application). If the URL
   * is set to a file, the instance field is set accordingly. If the URL is an
   * URL that points to a WSDL URL the method doesn't do anything.
   * 
   * @throws JCowsException
   */
  private void openWS() throws JCowsException{
    /* open web service (load WSDL URL from jcows file */
    try {
      if (m_wsdlUrl.endsWith(Properties.getConfig("parser.JCowsFileEnding"))){
        LOGGER.info("Opening Web Service: " + m_wsdlUrl);
        BufferedReader in = new BufferedReader(new FileReader(m_wsdlUrl));
        m_wsdlUrl = in.readLine();
        in.close();
      }
    }
    catch (FileNotFoundException e) {
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  
  /**
   * This method start the initialization process of the web service. First
   * it calls the method {@link #getWsdl} which successively calls
   * {@link #compileWebService} and {@link #loadClasses}. Finishing this method 
   * calls the method {@link #buildComponents}. 
   * 
   * @throws JCowsException
   */
  private void initWebService() throws JCowsException{
    getWsdl();//-->compileWebService-->loadClasses
    buildComponents();
  }
  
  /**
   * This method downloads the WSDL document and stores its content to a file
   * as well as to the current instance. It also handles SSL connections and
   * the generation and verification of the checksum.<br/>
   * At the end it calls the method {@link #compileWebService}.
   * 
   * @throws JCowsException
   */
  private void getWsdl() throws JCowsException{
    boolean wsdlChanged = true;
    long newChecksum = 0;
    long oldChecksum = 0;
    String wsdlFile = null;
    
    File jarfile = new File(Properties.getConfig("parser.outputDirName") 
        + getWsdlLocalName() + ".jar");
    
    LOGGER.info("Getting wsdl: " + m_wsdlUrl);
    
    /* caching: checksum of WSDL file */
    if (Boolean.valueOf(Properties.getConfig("parser.enableCaching"))){
      
      try {
        URL wsdlUrl = new URL(m_wsdlUrl);
        
        URLConnection conn = null;
        
        /* dispatch https/http */
        if (wsdlUrl.getProtocol().equals("http")){
          conn = (HttpURLConnection) wsdlUrl.openConnection();
        }
        else if (wsdlUrl.getProtocol().equals("https")){
          m_trustManager = new JCowsX509TrustManager(m_dialogSSLController);
          TrustManager[] trustManager = 
            new TrustManager [] { m_trustManager };
          
          SSLContext sContext = null;
          try {
            sContext = SSLContext.getInstance("SSL");
            sContext.init(null, trustManager, new java.security.SecureRandom());
          }
          catch (KeyManagementException e) {
            throw new JCowsException(
                Properties.getMessage("error.KeyManagementException"), e);
          }
          catch (NoSuchAlgorithmException e) {
            throw new JCowsException(
                Properties.getMessage("error.NoSuchAlgorithmException"), e);
          }
          
          HttpsURLConnection.setDefaultSSLSocketFactory(sContext.getSocketFactory());
          conn = (HttpsURLConnection) wsdlUrl.openConnection();
          
          /* set the truststore
           * take temporary truststore if not accepted permanently 
           */
//          String trustStore = Properties.getConfig("network.keyStore");
//          if (m_trustManager.isTemporaryValid())
//            trustStore = System.getProperty("java.io.tmpdir") 
//            + File.separator + trustStore;
          System.setProperty("javax.net.ssl.trustStore", 
              m_trustManager.getkeystorePath());
        }
        
        // timeout values in seconds
        int connect_timeout = Integer.parseInt(Properties.getConfig("network.connectTimeout"));
        int read_timeout = Integer.parseInt(Properties.getConfig("network.readTimeout"));
        // timeout values needed in milliseconds
        conn.setConnectTimeout(connect_timeout * 1000);
        conn.setReadTimeout(read_timeout * 1000);

        InputStream in = conn.getInputStream();
        
        StringBuffer content = new StringBuffer();
        byte[] buf=new byte[0xFFFF];
        int len;
        while ((len = in.read(buf)) > 0) {
          // somehow axis makes too many line breaks. we don't want them
          String str = new String(buf, 0, len);
          content.append(str.replaceAll("[\n\r]{2,}", "\n"));
        }
        in.close();
        
        /* check if it is actually a WSDL document */
        if (!content.toString().toLowerCase().trim().endsWith("definitions>"))
          throw new JCowsException(Properties.getMessage("error.notAWSDLDocument"));
        
        // TODO: download WSDL first, such that afterwards it doesn't
        // have to be read remotely again
        // TODO: download referenced files too (e.g. external xsd files)
        // <xsd:import schemaLocation="XWebEmailValidation.xsd"
        
        // set wsdlUrl to local file (avoid refetching of remote one)
        wsdlFile = Properties.getConfig("parser.outputDirName") 
            + getWsdlLocalName() + ".wsdl";
        
        // write WSDL to local file
        BufferedWriter out = new BufferedWriter(
            new FileWriter(wsdlFile));
        out.write(content.toString());
        out.close();
        
        newChecksum = m_jcowsFile.generateChecksum(content.toString());
        oldChecksum = m_jcowsFile.getChecksum();
        LOGGER.info("Wsdl Checksum (" + getWsdlLocalName() + "): old " 
            + oldChecksum + ", new " + newChecksum);
        
        if (newChecksum == oldChecksum && jarfile.exists()){ 
          wsdlChanged = false;
          LOGGER.info("Checksum didn't change, no need to recompile");
        }
        //else addChecksum(newChecksum);
      }
      catch(SocketTimeoutException e){
        throw new JCowsException(Properties.getMessage("error.SocketTimeoutException"), e);
      }
      catch(MalformedURLException e){
        throw new JCowsException(Properties.getMessage("error.MalformedURLException"), e);
      }
      catch (IOException e) {
        throw new JCowsException(Properties.getMessage("error.IOException"), e);
      }
    }
    else wsdlChanged = true;
    
    /* now compile the web service */
    compileWebService(wsdlChanged, jarfile, newChecksum);
  }
  
  
  /**
   * This method parses the WSDL document, generates its classes, compiles
   * them and adds them and their sources to a jar file.<br/>
   * At the end it calls the method {@link #loadClasses}.
   * 
   * @param wsdlChanged indicates if the WSDL document has changed since
   *        the last time.
   * @param jarfile the jar file that contains the classes.
   * @param newChecksum the value of the new checksum.
   * @throws JCowsException
   */
  private void compileWebService(boolean wsdlChanged, File jarfile, long newChecksum) 
      throws JCowsException{
    
    Emitter parser = null;
    
    if (wsdlChanged){
      /* Parse the WSDL (invoke wsdl2java) */
      parser = new Emitter();
      parser.setOutputDir(Properties.getConfig("parser.outputDirName"));
      parser.setBuildFileWanted(true);
      parser.setAllWanted(true);
      //parser.setImports(true);
      //parser.setNowrap(true); // --> if set, methods may contain too many args
      //parser.setHelperWanted(true);
      //parser.setServerSide(true);
      //parser.setSkeletonWanted(true);
      parser.setWrapArrays(false);
      try{ 
//      TODO: download included files for WSDL too (see above)
        parser.run(/*wsdlFile*/m_wsdlUrl);
      }
      catch(Exception e){
        throw new JCowsException(Properties.getMessage("error.wsdl2javaParse"), e);
      }
      
      /* Compile the classes and create jar (invoke ant) */
      Project project = new Project();
      ProjectHelper helper = ProjectHelper.getProjectHelper();
  
      project.init();
      helper.parse(project, new File(Properties.getConfig("parser.outputAntFile")));
      
      /* add source files to jar file */
      // create copy task
      Copy copyTask = (Copy)project.createTask("copy");//new Copy();
      FileSet fileset = new FileSet();
      fileset.setDir(new File(project.getProperty("src")));
      fileset.setCaseSensitive(true);
      fileset.setIncludes("**/*.java");
      copyTask.setTodir(new File(project.getProperty("build.classes")));
      copyTask.addFileset(fileset);
      
      Target jarTarget = (Target)project.getTargets().get("jar");
      
      // create include source target (properties as jar target)
      Target newJarTarget = new Target();
      newJarTarget.setName("includeSrc");
      newJarTarget.setLocation(jarTarget.getLocation());
      newJarTarget.setProject(jarTarget.getProject());
      
      // add copy task to target
      newJarTarget.addTask(copyTask);
      // jar target depends on include src target 
      jarTarget.setDepends("includeSrc");
      // add target to project
      project.addOrReplaceTarget("includeSrc", newJarTarget); 
      
      /* disable ant warnings */
      Target compileTarget = (Target)project.getTargets().get("compile");
      Task[] compileTasks = compileTarget.getTasks();
      RuntimeConfigurable compileConfig;
      for(int i = 0; i < compileTasks.length; ++i){
        if(compileTasks[i].getTaskName().equals("javac")){
          compileConfig = compileTasks[i].getRuntimeConfigurableWrapper();
          compileConfig.setAttribute("nowarn","true");
        }
  
      }
      
      LOGGER.info("Starting Compilation...");
      try {
        project.executeTarget("jar");
      }
      catch (BuildException e) {
        throw new JCowsException(Properties.getMessage("error.BuildException"), e);
      }
      finally{
        // remove generated sources
        m_jcowsFile.cleanOutputDir();
      }
      
      LOGGER.info("Generated jar file: " + jarfile.getAbsolutePath());
    }
    
    try{ 
      m_jarLoader = new URLClassLoader(new URL[] { jarfile.toURL() }); 
    }
    catch(MalformedURLException e){
      throw new JCowsException(Properties.getMessage("error.MalformedURLException"), e);
    }
    
    /* now load the classes */
    loadClasses(wsdlChanged, parser, newChecksum);
  }
  
  /**
   * This method loads the generated classes and classifies them into the
   * following five types:
   * <ul>
   * <li>service interface</li>
   * <li>sdi (service definition interface, aka porttypes)</li>
   * <li>service locator </li>
   * <li>stub (aka binding)</li>
   * <li>holder</li>
   * <li>type (bean in most cases)</li>
   * </ul>
   * 
   * @param wsdlChanged indicates if the WSDL document has changed since
   *        the last time.
   * @param parser The {@link org.apache.axis.wsdl.toJava.Emitter Emitter} instance
   *        that generated the classes.
   * @param newChecksum the value of the new checksum.
   * @throws JCowsException
   */
  private void loadClasses(boolean wsdlChanged, Emitter parser, long newChecksum) 
      throws JCowsException{
    /* Classify classes */
    List generatedClasses = null;
    if (wsdlChanged){
      generatedClasses = parser.getGeneratedClassNames();
      m_jcowsFile.createJCowsFile(generatedClasses, newChecksum);
    }
    else
      generatedClasses = m_jcowsFile.getGeneratedClassNames();
    for(Object o : generatedClasses){
      if (o != null){
        String classname = o.toString();
        Class cl = null;
        try{ 
          cl = m_jarLoader.loadClass(classname);
        }
        catch(ClassNotFoundException e){
          throw new JCowsException(Properties.getMessage("error.ClassNotFoundException"), e);
        }
        
        /* categorize the class */
        if (cl.isInterface()){
          // service interface
          if (m_classHelper.hasInterface(cl, "javax.xml.rpc.Service")){
            m_wsdlServiceInterface.add(cl);
            LOGGER.info("Got class: service interface: " + cl.getCanonicalName());
          }
          // sdi (service definition interface, aka porttypes)
          else if(m_classHelper.hasInterface(cl, "java.rmi.Remote")){
            m_wsdlSDI.add(cl);
            LOGGER.info("Got class: SDI: " + cl.getCanonicalName());
          }
        }
        else if (!cl.isArray() && !cl.isPrimitive()){ // is class
          // service locator 
          if (m_classHelper.hasSuperclass(cl, "org.apache.axis.client.Service")){
            m_wsdlServiceLocator.add(cl);
            LOGGER.info("Got class: service locator: " + cl.getCanonicalName());
          }
          // stub (aka binding)
          else if (m_classHelper.hasSuperclass(cl, "org.apache.axis.client.Stub")){
            m_wsdlStub.add(cl);
            LOGGER.info("Got class: stub: " + cl.getCanonicalName());
          }
          // holder
          else if(m_classHelper.hasInterface(cl, "javax.xml.rpc.holders.Holder")){
            m_wsdlHolders.add(cl);
            LOGGER.info("Got class: holder: " + cl.getCanonicalName());
          }
          // type (bean in most cases)
          else if (m_classHelper.hasInterface(cl, "java.io.Serializable")){
            m_wsdlTypes.add(cl);
            LOGGER.info("Got class: type: " + cl.getCanonicalName());
          }
          // unknown class type
          else{
            LOGGER.error(Properties.getMessage("error.unknownClassType", 
                new String[]{cl.getCanonicalName()}));
          }
        }
      }
    }
  }
  
  
  /* (non-Javadoc)
   * @see jcows.model.ISoapLogic#getWsdlLocalName()
   */
  public String getWsdlLocalName() {
    return m_jcowsFile.getWsdlLocalName();
  }
  
  /* (non-Javadoc)
   * @see org.jcows.model.core.IJcowsLogic#getWsdlUrl()
   */
  public String getWsdlUrl(){
    return m_wsdlUrl;
  }
  
  /* (non-Javadoc)
   * @see org.jcows.model.core.IJcowsLogic#getSSLCertificateIssuer()
   */
  public String getSSLCertificateIssuer(){
    return m_trustManager.getAcceptedIssuers()[0]
      .getIssuerX500Principal().toString();
  }
}
