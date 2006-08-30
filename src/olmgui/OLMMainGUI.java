/**
 * @file OLMMainGUI.java
 */
package olmgui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import olmgui.graph.OLMGraphBrowser;
import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.i18n.TopicMaps;
import olmgui.input.OLMBeliefConstructor;
import olmgui.input.OLMMoveSelector;
import olmgui.input.OLMTopicSelector;
import olmgui.output.OLMArgumentView;
import olmgui.output.OLMDialoguePane;
import olmgui.output.OLMHistoryPane;
import olmgui.utils.OLMSwingWorker;
import olmgui.utils.TopicWrapper;

import org.apache.xmlrpc.applet.SimpleXmlRpcClient;
import org.apache.xmlrpc.applet.XmlRpcException;

import toulmin.BeliefDesc;
import toulmin.Toulmin;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;

import config.DlgErrorMsg;
import config.OLMDebugMode;
import config.OLMPrefs;
import config.OLMQueryResult;
import config.OLMServerConfig;
import config.OLMTopicConfig;
import dialogue.DialogueMoveStartup;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;


/**
 * This class is the main element of the OLM Graphical User Interface.
 * OLMMainGUI is an applet that contains and organise all the various panes and
 * widgets used to support the interaction between the learner and the Learner
 * Model.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.63 $
 */
public class OLMMainGUI extends JApplet
{
    /**
     * Shortcuts for the various views used in the OLM.
     * The shortcuts are used both for referencing the view internally and 
     * for accessing the internationalized labels
     */
    final public static String  VIEW_BELIEF = "VIEW_BELIEF",            ///< Reference for the Summary Belief view 
                                VIEW_DISTRIB = "VIEW_DISTRIB",          ///< Reference for the Distribution view
                                VIEW_GRAPH = "VIEW_GRAPH",              ///< Reference for Topic Map view
                                VIEW_EVIDENCE = "VIEW_EVIDENCE",        ///< Reference for the Warrant/Backing view 
                                VIEW_DESCRIPTOR = "VIEW_DESCRIPTOR",    ///< Reference for the Belief Descriptor view
                                VIEW_DISAGREE = "VIEW_DISAGREE",        ///< Reference for the Challenge view
                                VIEW_TOULMIN = "VIEW_TOULMIN";          ///< Reference for the Toulmin view  //  @jve:decl-index=0:
    final public static String  VIEW_HISTORY = "VIEW_HISTORY";          ///< Reference for the Summary Belief view 
    final public static String  VIEW_PARTITION = "VIEW_PARTITION";      ///< Reference for the Partition view 
    //private String BTN_ABOUT = Messages.getString("OLMMoveSelector.ABOUT.name");

    /**
     * A reference to the XML-RPC client used fopr communicating with the core.
     */
    private SimpleXmlRpcClient client = null;
    /**
     * A reference to the URL of the OLM Core
     */
    private URL olmCoreServiceUrl = null;

    /**
     * The identifier of the learner using this instance of the OLM. 
     */
    private String user = null;  //  @jve:decl-index=0:
            
    /**
     * A reference to the dialogue planner associated with the OLM.
     */
    private DialoguePlanner planner = null;

    // References to the GUI elements
    private JPanel jContentPane = null;
    private JPanel jDescriptorPanel = null;  
    private JPanel jMainPanel = null;
    private JPanel jPanelCard = null;
    private JPanel jDialoguePanel = null;
    
    private JToolBar jToolBar = null;
    
    private OLMGraphBrowser jGraphPanel = null;
    private OLMDialoguePane jDialoguePane = null;
    private OLMTopicSelector jTopicSelector = null;
    private OLMMoveSelector jMoveSelector = null;
    private OLMBeliefConstructor jBeliefConstructor = null;
    private OLMPleaseWait jProcessToolBar = null;
    private OLMHistoryPane jHistoryPane = null;

    private ButtonGroup   jToogleGroup = null;  //  @jve:decl-index=0:
    private JButton       jHelpButton = null;
    private JToggleButton jToggleDescriptor = null;
    private JToggleButton jToggleTopics = null;
    private JToggleButton jToggleToulmin = null;
    private JToggleButton jToggleHistory = null;

    private JPanel jPanel = null;
    private OLMArgumentView OLMArgumentView = null;
    /**
     * Used by the OLM to intercept actions on the View toolbar and toggle
     * between the various views.
     */
    private class ToggleViewListener implements ActionListener,MouseListener
    { 
        public void actionPerformed(java.awt.event.ActionEvent e) 
        {    
            JToggleButton btn = (JToggleButton)e.getSource();
            activateView(btn.getName());
        }

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent evt) {
	           Object source = evt.getSource();
	            if (source instanceof AbstractButton) {
	            	AbstractButton btn = (AbstractButton) source;
	                String action = btn.getActionCommand();
	                if (btn.isEnabled())
	                   progressStop("OLMMainGUI." + action + ".Description");
	            }

		}

		public void mouseExited(MouseEvent evt) {
			Object source = evt.getSource();
            if (source instanceof AbstractButton) {
            	AbstractButton btn = (AbstractButton) source;
                if (btn.isEnabled())
                    progressStop(null);
            }
		}   
    }

    /**
     * This is the default constructor
     */
    public OLMMainGUI()
    {
        super();
    }

    
    /* (non-Javadoc)
     * @see java.applet.Applet#getParameterInfo()
     */
    public String[][] getParameterInfo() 
    {
        String pinfo[][] = {
                {"olmCoreServiceUrl", "String","The URL to the OLMCore XML-RPC service (mandatory)"},
                {"domainId", "String","The identifier of the domain topic to explore (optional)"},
                {"competencyId", "String","The identifier of the competency to explore (optional)"},
                {"affectId", "String","The identifier of the affect to explore (optional)"},
                {"motivationId", "String","The identifier of the motivation to explore (optional)"},
                {"metacogId", "String","The identifier of the metacognition to explore (optional)"},
                {"capeId", "String","The identifier of the CAPEs to explore (optional)"},
                {"SHOWHISTORY", "boolean","true to display the Trend pane in the GUI (default: false)"},
                {"LOCALMOVES", "boolean","true to display the dialogue moves in the relevant panes (default: true)"},
                {"BACKING_ONLY", "boolean","true to display only the Toulmin's backing nodes, false to include the warrant nodes as well (default: true)"}
            };  
        
        return pinfo;
    }
    
    /**
     * Get the location of the OLM Core.
     * @return The URL of the OLM Core, <code>null</code> if it has not been passed 
     *         properly as a parameter of the applet.
     */
    public URL getOLMCore() {return this.olmCoreServiceUrl;}

    /**
     * Initialize the XML-RPC client, establishing connection with the OLM Core.
     * 
     */
    public SimpleXmlRpcClient initClient()
    {
        if (this.client != null) return this.client;
        
        String olmCoreServiceUrl = this.getParameter("olmCoreServiceUrl");
        if(olmCoreServiceUrl==null) 
        {
            System.err.println("Don't have the parameter olmCoreServiceUrl, expect an exception.");
        }

        try
        {
            this.olmCoreServiceUrl = new URL(olmCoreServiceUrl);
            client = new SimpleXmlRpcClient(this.olmCoreServiceUrl);
            System.out.println("Trying XML-RPC connection at: " + this.olmCoreServiceUrl);
        } 
        catch (MalformedURLException unlikely) 
        {
            this.olmCoreServiceUrl = null;
            System.err.println("Error constructing URL for XML-RPC: " + 
                    olmCoreServiceUrl+ ". "+ unlikely);
        }
        return client;
    }

    /**
     * Calls the XML-RPC server with the specified method name and argument list.
     *
     * @param methodName    The handler and method name to use from the server
     * @param arguments     A vector containing the parameters of the method
     *
     * @return Return an object defined by the method
     *
     * @throws XmlRpcException This is thrown by the XmlRpcClient if the remote server 
     *         reported an error. 
     * @throws IOException This is thrown by the XmlRpcClient if the remote server 
     *         reported a low-level error (ie no http connection) 
     */
    public Object executeRequest(String methodName, Vector arguments) 
        throws XmlRpcException, IOException
    {
     	SimpleXmlRpcClient client = initClient();
        Object returnValue = client.execute(methodName, arguments);
        return returnValue; 
    }


    /**
     * Used to get the parameters from the HTML source and 
     * to return a default values if problems or not found
     *
     * @param param The parameter to look for from the <PARAM> tag
     * @param def The default value to return if the parameter is not found
     *
     * @return the value associated with the parameters
     */
    private String getAppletParameter(String param, String def)
    {
        return (this.getParameter(param) != null) ? 
                this.getParameter(param) : def;
    }

    private void initVisibility()
    {
        String capeId = getAppletParameter(OLMTopicConfig.AFFECT.toString(),"true");
        if (Boolean.valueOf(capeId).booleanValue()== false)
            OLMTopicConfig.AFFECT.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.BDESC.toString(),"true");
        OLMTopicConfig.BDESC.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.CAPES.toString(),"true");
        OLMTopicConfig.CAPES.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.COMPET.toString(),"true");
        OLMTopicConfig.COMPET.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.DOMAIN.toString(),"true");
        OLMTopicConfig.DOMAIN.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.METACOG.toString(),"true");
        OLMTopicConfig.METACOG.setVisible(Boolean.valueOf(capeId).booleanValue());

        capeId = getAppletParameter(OLMTopicConfig.MOTIV.toString(),"true");
        OLMTopicConfig.MOTIV.setVisible(Boolean.valueOf(capeId).booleanValue());
        
        String pref = this.getParameter("SHOWHISTORY");
        if ("true".equals(pref)) OLMPrefs.SHOWHISTORY = true;
//        pref = this.getParameter("LOCALMOVES");
//        if ("true".equals(pref)) OLMPrefs.LOCALMOVES = true;
        pref = this.getParameter("BACKING_ONLY");
        if ("true".equals(pref)) OLMPrefs.BACKING_ONLY = true;
        pref = this.getParameter("CHALLENGE_ENABLED");
        if ("true".equals(pref)) OLMPrefs.CHALLENGE_ENABLED = true;
        
    }
    
    /**
     * Initialise the user information by retrieving the parameters 
     * from the HTML tag.
     */
    private void initUser()
    {
        Messages.setLocale(Locale.ENGLISH);
        TopicMaps.setLocale(Locale.ENGLISH);

        progressStart();
        initClient();
        // Set the current move
        DialogueMoveStartup move = new DialogueMoveStartup(planner);
        planner.setCurrentState(move);       
        move.activateGUI(false);
        this.user = "guest";
        
        //progressText("Recovering user information ...");
        OLMSwingWorker worker = new OLMSwingWorker(planner,OLMSwingWorker.USERINFO) 
        {
            protected Object construct() throws Exception 
            {
                Vector params = new Vector();
                Hashtable result = (Hashtable)executeRequest(OLMServerConfig.HDR_GETUSERINFO, params);
                return result;
            }

//			protected void finished() 
//			{
//				super.finished();
//			}
            
            
        };        
        worker.start();
        Hashtable result = new Hashtable();
        try 
        {
            result = (Hashtable) worker.get();
            this.user = (String)result.get(OLMQueryResult.CAT_USERID);
            String locale = (String)result.get(OLMQueryResult.CAT_USERLOCALE);
            if (locale != null)
            {
                Messages.setLocale(new Locale(locale));
                TopicMaps.setLocale(new Locale(locale));
            }
            else
            {
                Messages.setLocale(null);
                TopicMaps.setLocale(null);
            }
            //System.out.println(Messages.getString("DlgMove.Startup.User"));
            
//            // Set the current move
//            DialogueMoveStartup move = new DialogueMoveStartup(planner);
//            planner.setCurrentState(move);            

            // Initialise the Topic Maps
            sendRequestForCMaps();
        } 
        catch (InterruptedException e) 
        {
            //DialogueMoveStartup move = new DialogueMoveStartup(planner); 
            //planner.setCurrentState(move);            
            //planner.getCurrentState().onMoveFail();
            result.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION);
        } 
        catch (InvocationTargetException e) 
        {
            //DialogueMoveStartup move = new DialogueMoveStartup(planner); 
            //planner.setCurrentState(move);            
            //planner.getCurrentState().onMoveFail();

        	System.err.println("Server not available - using dummy data instead.");
        	result.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION);
            planner.getCurrentState().setBeliefDesc(OLMDebugMode.getBeliefDesc());
            planner.getCurrentState().onMoveEntry();
            planner.getCurrentState().onMoveExecute();

        }

        move.activateGUI(true);
        progressStop((String)result.get(OLMQueryResult.CAT_ERROR));
    }
    
    
    
    /**
     * Get the user name.
     * @return A String containing the user name, <code>null</code> if not defined.
     */
    public String getUserName() {
        return this.user;
    }


    /**
     * Set the name of the user interacting with the OLM.
     * @param user A string containing the name of the user.
     */
    public void setUserName(String user) {
        this.user = user;
    }


    /* (non-Javadoc)
     * @see java.applet.Applet#start()
     */
    public void start() {
        super.start();
        
    }
    
 
    /* (non-Javadoc)
     * @see java.applet.Applet#init()
     */
    public void init()
    {
        super.init();
        
        try {
            LookAndFeelInfo info[] = UIManager.getInstalledLookAndFeels();
        	HashMap map = new HashMap();
        	for (int i=0;i<info.length;i++)
        	{
        		map.put(info[i].getName(),info[i].getClassName());
        	}
            if (map.get("Windows")!=null)
                UIManager.setLookAndFeel((String)map.get("Windows"));
            else if (map.get("Metal")!=null)
                UIManager.setLookAndFeel((String)map.get("Metal"));
        	else
        		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        
        planner = new DialoguePlanner(this);

        // Initialise the Visibility attributes for the OLM
        initVisibility();
        
        ///getJDialoguePane().setDialogueEnabled(false);
        
 
        // Initialise the content of the GUI
        setSize(800, 600);
        this.setContentPane(getJContentPane());
        planner.activateGUI(false);

        // Initialise the user and locale information
        initUser();
                   
    }

    /**
     * Initialise the various concept map by retrieving information from the server.
     * Fill the different elements of the GUI with the relevant information.
     */
    private void sendRequestForCMaps()
    {
        OLMSwingWorker worker = new OLMSwingWorker(planner,OLMSwingWorker.CMAPS) 
        {
            public Object construct() 
            {
                OLMTopicConfig topicMaps[]={
                    OLMTopicConfig.DOMAIN,
                    OLMTopicConfig.COMPET,
                    OLMTopicConfig.MOTIV,
                    OLMTopicConfig.AFFECT,
                    OLMTopicConfig.METACOG,
                    OLMTopicConfig.CAPES
                };
            
                Hashtable ret = new Hashtable();
                for (int i=0;i<topicMaps.length;i++)
                {
                    if (!topicMaps[i].isVisible()) continue;
                    
                    String str = Messages.getString("DlgErrorMsg.XMLRPC."+CMAPS);
                    progressText(str + " " + topicMaps[i]);
                    final Vector params = new Vector();
                    params.addElement(new String(topicMaps[i].toString()));
                
                    Object result = null;
                    //System.out.println("Loading CMAP for: " + params.toString());
                    try 
                    {
                        result = executeRequest(OLMServerConfig.HDR_CONCEPTMAP, params);
                        Hashtable hash = (Hashtable) result;
                        ret.put(topicMaps[i].toString(),hash);
                    }
                    catch (IOException e)
                    {
                        System.err.println("Connection failed for " + params.toString() + ": " + e);
                        ret.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION);
                    } catch (XmlRpcException e) {
                        System.err.println("Connection failed for " + params.toString() + ": " + e);
                        ret.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION);
                    }
                }
                return ret;
            }

            /* (non-Javadoc)
             * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#finished()
             */  
            protected void finished()
            {
                super.finished();

                try {
                    Hashtable hash = (Hashtable)get();
                    
                    if (hash.get(OLMQueryResult.CAT_ERROR)!=null)
                    {
                        getJTopicSelector().setTopicMap(null,null);
                        planner.getCurrentState().onMoveFail();
                    }
                    else 
                    {
                        for (Enumeration elt = hash.keys();elt.hasMoreElements();)
                        {
                            Object obj = elt.nextElement(); 
                            Hashtable lhash = (Hashtable) hash.get(obj.toString());
                            getJTopicSelector().setTopicMap(lhash,obj.toString());
                            if (getJGraphPanel()!=null)
                                getJGraphPanel().setTopicMap(lhash,obj.toString());
                        } 
                        if (getJGraphPanel()!=null)
                            getJGraphPanel().updateGraph();
                        
                        String domainId = getParameter("domainId");
                        if (domainId!=null)
                        {
                            // There is a belief descriptor to initialise the OLM with

                            BeliefDesc vec = new BeliefDesc();
                            String capeId = getAppletParameter("capeId","");
                            String competencyId = getAppletParameter("competencyId","");
                            String affectId = getAppletParameter("affectId","");
                            String motivationId = getAppletParameter("motivationId","");
                            String metacogId = getAppletParameter("metacogId","");

                            vec.add(domainId);
                            vec.add(capeId);
                            vec.add(competencyId);
                            vec.add(motivationId);
                            vec.add(affectId);
                            vec.add(metacogId);
                            planner.getCurrentState().setBeliefDesc(vec);
                        }
                        planner.getCurrentState().onMoveEntry();
                        planner.getCurrentState().onMoveExecute();
                    }                
                } catch (InterruptedException e) {
                    System.err.println("The thread collecting information from the server was interrupted: " + e);
                } catch (InvocationTargetException e) {
                    System.err.println("The thread collecting information from the server was interrupted: " + e);
                }
            }
        };
        worker.start();  
        
    }
      
    public void updateDialogueMove(String player,String text)
    {
        getJDialoguePane().addUserDialogue(player,text);
    }

    public void updateDialogueMove(String player,String template,Object[] arg)
    {
        getJDialoguePane().addUserDialogue(player,template,arg);
    }
    
    public BeliefDesc getBeliefDescriptor()
    {
        return getJBeliefConstructor().getBeliefDescriptor();
    }


 
    /**
     * This method is called when a dialogue move is fired, either by the user or
     * by the OLM.
     * 
     * @param move The identifier of the dialogue move initiated (see DlgMoveID)
     * @deprecated	The planner is calling directly the move.
     */
    public void sendDialogueMove(DlgMoveID move)
    {
        //planner.getCurrentState().goNextMove(move);
    }
    
    public ToulminNode getToulminSelectedNode()
    {
        return getOLMArgumentView().getToulminSelectedNode();
    }
    
    public void setSelectedToulminNode(ToulminNode node)
    {
        getOLMArgumentView().setSelectedToulminNode(node);
        
//        if (getJToulminGraph()!=null)
//            getJToulminGraph().getTGPanel().setSelect(node);
//        
//        getOLMArgumentView().setEvidence(null,null);
//        if (node.getNodeConfig()==NodeConfig.BACKING)
//        {
//            ToulminBacking backing = (ToulminBacking)node.getData();
//            ToulminWarrant warrant = backing.getWarrant();
//    
//            getOLMArgumentView().setEvidence(Toulmin.BACKING,warrant);
//            activateView(OLMMainGUI.VIEW_EVIDENCE);
//        }
//        else if (node.getNodeConfig()==NodeConfig.WARRANT)
//        {
//            getOLMArgumentView().setEvidence(Toulmin.WARRANT,(ToulminWarrant)node.getData());
//            activateView(OLMMainGUI.VIEW_EVIDENCE);
//        }
//        else if (node.getNodeConfig()==NodeConfig.CLAIM)
//        {
//            if (node.getToulminType().equals(Toulmin.SUBCLAIM))
//            {
//                activateView(OLMMainGUI.VIEW_PARTITION);
//                if (node.getData() instanceof ToulminSubClaim) 
//                {
//                    ToulminSubClaim claim = (ToulminSubClaim) node.getData();
//                    String val = claim.getValue();
//                    getOLMArgumentView().setPartition(val);
//                    //getJPartitionPane().setSelectedNode(node);
//                    
//                }
//            }
//            else
//                activateView(OLMMainGUI.VIEW_BELIEF);
//        }
//        else if (node.getNodeConfig()==NodeConfig.DATA)
//        {
//              activateView(OLMMainGUI.VIEW_DISTRIB);
//        }        
    }
    
    public void expandToulminNode(ToulminNode node)
    {
        ToulminNode ret = getOLMArgumentView().expandToulminNode(node);
        if (ret!=null)
        {
            planner.activateToulminView(ret);
        }
    }
    
    public void setToulmin(Toulmin toulmin)
    {
        getOLMArgumentView().setToulmin(toulmin);
        getJHistoryPane().setToulmin(toulmin);
    }
    
    public void setToulminUser()
    {
        if (getJGraphPanel()!=null)
            getJGraphPanel().setTopNode(getUserName()); 
        getOLMArgumentView().setToulminUser(getUserName());
    }
    
    public void enableToulmin(boolean show)
    {
        getOLMArgumentView().enableToulmin(show);
    }
    
    
    public void swapView(String view)
    {
    }
    /**
     * This method is called to add a topic in one of the placeholder of the belief constructor.
     * @param attr The attribute to update (see OLMTopicConfig)
     * @param id The topic wrapper to set at the attribute (see TopicWrapper). 
     * @see OLMBeliefConstructor#setLayerAttribute
     */
    public void setLayerAttribute(OLMTopicConfig attr,TopicWrapper id)
    {
        getJBeliefConstructor().setLayerAttribute(attr,id);
    }
    
    public void progressStart()
    {
        getJProcessToolBar().enableProgress(true);
        //getJProcessToolBar().setProgressText("Connecting to LeActiveMath server...");

    }
    
    public void progressText(String msg)
    {
        if (msg!=null)
        {
            getJProcessToolBar().setProgressString(msg);
        }
    }

    /**
     * Stop the progress of the progress bar and display the given message 
     * @param txt   The string to display; if <code>null</code>, then the empty string is used.
     */
    public void progressStop(String txt)
    {
        getJProcessToolBar().enableProgress(false);
        getJProcessToolBar().setProgressText(txt);
    }
    
    /**
     * This method is called to enable/disable a particular dialogue move 
     * @param mvt The identifier of the move to enable/disable
     * @param active True for enabling,false for diabling the move
     * 
     * @todo Need a more organic way for it, based on the next moves
     */
    public void enableMove(DlgMoveID mvt,boolean active)
    {
        planner.activateMove(mvt,active);
//        if (DlgMoveID.SHOWME == mvt)
//        {
//            // check if the belief descriptor is valid
//            boolean val = getJBeliefConstructor().isDescriptorValid();
//            getJMoveSelector().updateMove(mvt,active & val);
//        }
//        else
//            getJMoveSelector().updateMove(mvt,active);
    }

//    public void enableAllMoves(boolean active)
//    {
//        if (!active)
//            getJMoveSelector().setEnabledAll(active);
//        else
//        {
//            planner.getCurrentState().activateGUI(active);
//        }
//    }
    
    public void enableFlipViews(boolean activ)
    {
        Component[] cpn = getJToolBar().getComponents();
        for (int i=0;i<cpn.length;i++)
        {
            if (cpn[i] instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) cpn[i];
                btn.setEnabled(activ);
            }
        }
        getJTopicSelector().setEnabled(activ);
    }
    
    
    public void enableTouminView(boolean show)
    {
        getJToggleToulmin().setEnabled(show);
    }
    
    public void enableDescriptorView(boolean show)
    {
        getJToggleDescriptor().setEnabled(show);
    }
            
    /**
     * This method initializes the Content Pane of the applet
     *
     * @return javax.swing.JPanel   A reference to the Content Pane widget
     */
    public JPanel getJContentPane()
    {
        if (jContentPane == null) {
            BorderLayout borderLayout1 = new BorderLayout();
            borderLayout1.setHgap(0);
            jContentPane = new JPanel();
            jContentPane.setLayout(borderLayout1);
            jContentPane.add(getJProcessToolBar(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getJToolBar(), java.awt.BorderLayout.WEST);
            jContentPane.add(getJMainPanel(), java.awt.BorderLayout.CENTER);
            
        }

        return jContentPane;
    }
    
    /**
     * This method initializes the left pane of the applet (containing the topics 
     * selector and the dialogue move selector)	
     * 	
     * @return javax.swing.JPanel   A reference to the pane
     */    
    private JPanel getJDescriptorPanel() {
    	if (jDescriptorPanel == null) {
    		jDescriptorPanel = new JPanel();
    		jDescriptorPanel.setLayout(new BorderLayout());
    		//jDescriptorPanel.setName("jDescriptorPanel");
    		jDescriptorPanel.add(getJBeliefConstructor(), java.awt.BorderLayout.CENTER);
    		jDescriptorPanel.add(getJTopicSelector(), java.awt.BorderLayout.WEST);
    	}
    	return jDescriptorPanel;
    }


    /**
     * This method initializes the progress bar of the applet 
     *
     * @return javax.swing.JToolBar   A reference to the pane
     */
    private OLMPleaseWait getJProcessToolBar()
    {
        if (jProcessToolBar == null) {
            jProcessToolBar = new OLMPleaseWait();
        }

        return jProcessToolBar;
    }
    
    /**
     * This method initializes the topic selector	
     * 	
     * @return org.activemath.xlm.openmodel.olmgui.OLMTopicSelector   A reference to the selector
     */    
    public OLMTopicSelector getJTopicSelector() {
    	if (jTopicSelector == null) {
    		jTopicSelector = new OLMTopicSelector();
            //jTopicSelector.setParent(this);
            jTopicSelector.setListeners(planner);

    	}
    	return jTopicSelector;
    }

    /**
     * This method initializes the dialogue move selector	
     * 	
     * @return org.activemath.xlm.openmodel.olmgui.OLMMoveSelector	  A reference to the selector
     */    
    private OLMMoveSelector getJMoveSelector() {
    	if (jMoveSelector == null) {
            jMoveSelector = new OLMMoveSelector();
            //jMoveSelector.setParent(this);
            jMoveSelector.setListeners(planner);
    	}
    	return jMoveSelector;
    }

    /**
     * This method initializes the right part of the applet (containing
     * the output pane and the belief constructor)
     * 	
     * @return javax.swing.JPanel   A reference to the pane
     */    
    private JPanel getJMainPanel() {
    	if (jMainPanel == null) {
    		jMainPanel = new JPanel();
    		jMainPanel.setLayout(new CardLayout());
    		jMainPanel.add(getJPanel(), VIEW_TOULMIN);
    		jMainPanel.add(getJGraphPanel(), VIEW_GRAPH);
    	}
    	return jMainPanel;
    }

    /**
     * This method initializes the belief constructor	
     * 	
     * @return org.activemath.xlm.openmodel.olmgui.OLMBeliefConstructor	      A reference to the constructor
     */    
    public OLMBeliefConstructor getJBeliefConstructor() {
    	if (jBeliefConstructor == null) {
    		jBeliefConstructor = new OLMBeliefConstructor();
            jBeliefConstructor.setParent(this);
            jBeliefConstructor.setListeners(planner);
    	}
    	return jBeliefConstructor;
    }

    /**
     * This method initializes the toolbar used to switch between the various display modes.	
     * 	
     * @return javax.swing.JToolBar	   A reference to the widget
     */    
    private JToolBar getJToolBar() {
    	if (jToolBar == null) {
    		jToolBar = new JToolBar();
    		jToolBar.setFloatable(false);
            jToolBar.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5));
    		jToolBar.setOrientation(javax.swing.JToolBar.VERTICAL);
    		//jToolBar.setPreferredSize(new Dimension(40, 110));
    		jToolBar.add(getJHelpButton());
    		jToolBar.addSeparator();
            jToolBar.add(getJToggleDescriptor());
            jToolBar.add(getJToggleToulmin());
            jToolBar.addSeparator();
            if (OLMPrefs.SHOWHISTORY)
            {
            	jToolBar.add(getJToggleHistory());
            	jToolBar.addSeparator();
            }
            jToolBar.add(getJToggleTopics());
            if (false)
            {
            	jToolBar.addSeparator();
            }
            
            jToogleGroup = new ButtonGroup();
            jToogleGroup.add(getJToggleDescriptor());
            jToogleGroup.add(getJToggleToulmin());
            jToogleGroup.add(getJToggleHistory());
            jToogleGroup.add(getJToggleTopics());
            
    	}
    	return jToolBar;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */    
    private JPanel getJPanelCard() {
        if (jPanelCard == null) 
        {
            jPanelCard = new JPanel();
            jPanelCard.setLayout(new CardLayout());
            jPanelCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5)));
            jPanelCard.add(getJDescriptorPanel(), VIEW_DESCRIPTOR);
            jPanelCard.add(getOLMArgumentView(), VIEW_TOULMIN);
            if (getJHistoryPane()!=null)
            	jPanelCard.add(getJHistoryPane(), VIEW_HISTORY);
        }
        return jPanelCard;
    }

    /**
     * This method initializes OLMDialoguePane1	
     * 	
     * @return org.activemath.xlm.openmodel.olmgui.OLMDialoguePane	
     */    
    private OLMDialoguePane getJDialoguePane() {
    	if (jDialoguePane == null) {
    		jDialoguePane = new OLMDialoguePane();
    		jDialoguePane.setName("OLMDialoguePane");
            //jDialoguePane.setParent(this);
            jDialoguePane.setPreferredSize(new java.awt.Dimension(100,100));
   	}
    	return jDialoguePane;
    }

    /**
     * This method initializes jGraphPanel  
     *  
     * @return org.activemath.xlm.openmodel.olmgui.OLMDistributionPane  
     */    
    public OLMGraphBrowser getJGraphPanel() {
        if (jGraphPanel == null) {

            //Added by Brendan to test image icons on nodes
//            Image[] imageList = new Image[5];
//            imageList[0]=getImage(getDocumentBase(), "res/buddy.gif");
//            imageList[1]=getImage(getDocumentBase(), "res/level1.gif");
//            imageList[2]=getImage(getDocumentBase(), "res/level2.gif");
//            imageList[3]=getImage(getDocumentBase(), "res/level3.gif");
//            imageList[4]=getImage(getDocumentBase(), "res/level4.gif");

            //getJTGPanel().setImageList(imageList);
            //getJTGPanel().initialize();
            
            jGraphPanel = new OLMGraphBrowser();
            jGraphPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5)));

            //jGraphPanel.setTopNode(getUserName());
            jGraphPanel.setName("jGraphPanel");
            //jGraphPanel.setName("jGraphPanel");
            //jGraphPanel.setImageList(imageList);
            jGraphPanel.initialize();
        }

        return jGraphPanel;
    }
    
    
    /**
     * This method initializes jToggleButton	
     * 	
     * @return javax.swing.JToggleButton	
     */    
    private JToggleButton getJToggleDescriptor() {
    	if (jToggleDescriptor == null) {
    		jToggleDescriptor = new JToggleButton();
    		jToggleDescriptor.setUI(new BlueishButtonUI());
            //jToggleDescriptor.setName(VIEW_DESCRIPTOR);
            //jToggleDescriptor.setToolTipText(TOOLTIP_BDESCRIPTOR);
            setViewAttributes(jToggleDescriptor,VIEW_DESCRIPTOR);
    		jToggleDescriptor.setIcon(new ImageIcon(getClass().getResource("/res/s_descriptor.gif")));
    		ToggleViewListener tt = new ToggleViewListener();
    		jToggleDescriptor.addActionListener(tt);
    		jToggleDescriptor.addMouseListener(tt);
            jToggleDescriptor.setSelected(true);
    	}
    	return jToggleDescriptor;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */    
    private JToggleButton getJToggleTopics() {
    	if (jToggleTopics == null) {
            jToggleTopics = new JToggleButton();
            jToggleTopics.setUI(new BlueishButtonUI());
            //jToggleGraph.setName(VIEW_GRAPH);
            //jToggleGraph.setToolTipText(TOOLTIP_GRAPH);
            setViewAttributes(jToggleTopics,VIEW_GRAPH);
            jToggleTopics.setIcon(new ImageIcon(getClass().getResource("/res/s_topics.gif")));
            jToggleTopics.setEnabled(true);
    		ToggleViewListener tt = new ToggleViewListener();
    		jToggleTopics.addActionListener(tt);
    		jToggleTopics.addMouseListener(tt);
    	}
    	return jToggleTopics;
    }
    
    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private JToggleButton getJToggleToulmin() {
        if (jToggleToulmin == null) {
            jToggleToulmin = new JToggleButton();
            jToggleToulmin.setUI(new BlueishButtonUI());

            //jToggleGraph.setName(VIEW_GRAPH);
            //jToggleGraph.setToolTipText(TOOLTIP_GRAPH);
            setViewAttributes(jToggleToulmin,VIEW_TOULMIN);
            jToggleToulmin.setIcon(new ImageIcon(getClass().getResource("/res/s_toulmin.gif")));
            jToggleToulmin.setEnabled(false);
    		ToggleViewListener tt = new ToggleViewListener();
    		jToggleToulmin.addActionListener(tt);
    		jToggleToulmin.addMouseListener(tt);

        }
        return jToggleToulmin;
    }    
    
    /**
     * Helper used to specify the command, name and attributes (such as tooltip, 
     * icon) of the toogle buttons in the switch bar.
     * @param btn   The button to define
     * @param id    The command of the button
     */
    private void setViewAttributes(JToggleButton btn,String id)
    {
        btn.setName(id);
        btn.setActionCommand(id);
        btn.setToolTipText(Messages.getString("OLMMainGUI." + id + ".name"));
        //btn.setIcon(new Icon());
    }
    
    
    /**
     * Used to synchronise the various Card Layouts embedded in the main GUI, 
     * given a particular view display. 
     * @param viewName  The identifer (command) of the view to activate
     */
    public void activateView(String viewName)
    {
        CardLayout cl0 = (CardLayout)(getJMainPanel().getLayout());
        CardLayout cl1 = (CardLayout)(getJPanelCard().getLayout());

        if (VIEW_GRAPH.equals(viewName))
        {
            cl0.show(getJMainPanel(),viewName);
            jToogleGroup.setSelected(getJToggleTopics().getModel(),true);
        }
        else 
        {
            cl0.show(getJMainPanel(),VIEW_TOULMIN);
                
            if (VIEW_DESCRIPTOR.equals(viewName) || VIEW_HISTORY.equals(viewName))
            {
                cl1.show(getJPanelCard(),viewName);
                if (VIEW_DESCRIPTOR.equals(viewName))
                    jToogleGroup.setSelected(getJToggleDescriptor().getModel(),true);
            }
            else if (VIEW_BELIEF.equals(viewName) || VIEW_DISTRIB.equals(viewName) ||
                    VIEW_EVIDENCE.equals(viewName) || VIEW_PARTITION.equals(viewName) 
                    || VIEW_DISAGREE.equals(viewName))
            {
                
               cl1.show(getJPanelCard(),VIEW_TOULMIN);
               getOLMArgumentView().switchToView(viewName);
               jToogleGroup.setSelected(getJToggleToulmin().getModel(),true);
            }
            else 
                cl1.show(getJPanelCard(),viewName);
        }
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */    
    private JPanel getJDialoguePanel() {
    	if (jDialoguePanel == null) {
    		jDialoguePanel = new JPanel();
    		jDialoguePanel.setLayout(new BorderLayout());
            jDialoguePanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5));
    		jDialoguePanel.add(getJDialoguePane(), java.awt.BorderLayout.CENTER);
    		jDialoguePanel.add(getJMoveSelector(), java.awt.BorderLayout.WEST);
    	}
    	return jDialoguePanel;
    }




    /**
	 * This method initializes jHistoryPane	
	 * 	
	 * @return olmgui.output.Combination	
	 */
	private OLMHistoryPane getJHistoryPane() 
	{
		// only build it if the history is required
		if (jHistoryPane == null && OLMPrefs.SHOWHISTORY) 
		{
        	//jHistoryPane = new OLMTrendPane();
        	jHistoryPane = new OLMHistoryPane();
        	jHistoryPane.setName(VIEW_HISTORY);
        	jHistoryPane.setListeners(planner);
		}
		return jHistoryPane;
	}


	/**
	 * This method initializes jToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJToggleHistory() {
		if (jToggleHistory == null) {
			jToggleHistory = new JToggleButton();
			jToggleHistory.setUI(new BlueishButtonUI());
			//jToggleHistory.setName(VIEW_HISTORY);
			//jToggleHistory.setActionCommand(VIEW_HISTORY);
            setViewAttributes(jToggleHistory,VIEW_HISTORY);

			jToggleHistory.setIcon(new ImageIcon(getClass().getResource("/res/s_history.gif")));
    		ToggleViewListener tt = new ToggleViewListener();
    		jToggleHistory.addActionListener(tt);
    		jToggleHistory.addMouseListener(tt);
            jToggleHistory.setEnabled(OLMPrefs.SHOWHISTORY);
            jToggleHistory.setVisible(OLMPrefs.SHOWHISTORY);
		}
		return jToggleHistory;
	}


    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.setName("jPanel");
            jPanel.add(getJDialoguePanel(), java.awt.BorderLayout.SOUTH);
            jPanel.add(getJPanelCard(), java.awt.BorderLayout.CENTER);
        }
        return jPanel;
    }


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJHelpButton() {
		if (jHelpButton == null) {
			jHelpButton = new JButton();
			jHelpButton.setUI(new BlueishButtonUI());
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.ABOUT);
            act.setPermanent(true);
            jHelpButton.setAction(act);
			jHelpButton.addMouseListener(act);//((MouseListener) planner.setMoveSelectorListener());
            jHelpButton.setText("");
		}
		return jHelpButton;
	}


    /**
     * This method initializes OLMArgumentView	
     * 	
     * @return olmgui.output.OLMArgumentView	
     */
    private OLMArgumentView getOLMArgumentView() {
        if (OLMArgumentView == null) {
            OLMArgumentView = new OLMArgumentView(planner);
            OLMArgumentView.setName("OLMArgumentView");
        }
        return OLMArgumentView;
    }
    
    public Hashtable getChallengeOutcome()
    {
        Hashtable hash = getOLMArgumentView().getChallengeOutcome();
        return hash;
    }
    
    public void setChallengeData(String viewName,BeliefDesc bdesc,int lvl)
    {
        getOLMArgumentView().setChallengeData(viewName, bdesc, lvl);
    }

}
