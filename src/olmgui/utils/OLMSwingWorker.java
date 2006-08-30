package olmgui.utils;

import olmgui.i18n.Messages;
import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;
import dialogue.DialogueMove;
import dialogue.DialoguePlanner;

public class OLMSwingWorker extends SwingWorker 
{
    public static final String USERINFO = "USERINFO";
    public static final String CMAPS = "CMAPS";
    public static final String BDESC = "BDESC";
    public static final String SUGGEST = "SUGGEST";
    public static final String BELIEF = "BELIEF";

    private DialoguePlanner    planner = null;
    private String  strError = null;
    
    /**
     * @param planner   A reference to the Dialogue Planner.
     */
    public OLMSwingWorker(DialoguePlanner planner,String help)
    {
        super();
        this.planner = planner;
        strError = help;
        DialogueMove move = planner.getCurrentState();
        move.activateGUI(false);
        move.getParent().getJContentPane().updateUI();
        move.getParent().progressStart();
        String str = Messages.getString("DlgErrorMsg.XMLRPC."+strError);
        System.out.println("START "+ str);
        move.getParent().progressText(str);
    }
    
    protected Object construct() throws Exception 
    {
        return null;
    }
    
//    public Hashtable sendServerRequest(String methodName,Vector params)
//    {
//        Hashtable ret = null;
//        try {
//            Object result = planner.getMainGUI().executeRequest(methodName, params);
//            ret = (Hashtable)result;
//        }
//        catch (XmlRpcException e) 
//        {
//            System.err.println("Connection failed for " + params.toString() + ": " + e); //$NON-NLS-1$ //$NON-NLS-2$
//            ret.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION); 
//        }
//        catch (IOException e)
//        {
//            System.err.println("Connection failed for " + params.toString() + ": " + e); //$NON-NLS-1$ //$NON-NLS-2$
//            ret.put(OLMQueryResult.CAT_ERROR,DlgErrorMsg.ERR_CONNECTION);
//        }
//        return ret;
//    }

    /* (non-Javadoc)
     * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#finished()
     */
    protected void finished() 
    {
        DialogueMove move = planner.getCurrentState();
        move.getParent().progressStop(null);
        move.activateGUI(true);
        super.finished();
        String str = Messages.getString("DlgErrorMsg.XMLRPC."+strError);
		System.out.println("STOP "+ str);
    }

}
