/**
 * @file DialogueMoveHereIs.java
 */
package dialogue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminList;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;
import config.OLMPrefs;
import config.OLMQueryResult;

/**
 * This class implements a dialogue move used by the OLM to present to learners
 * evidence supporting a previous statement.
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.47 $
 */
public class DialogueMoveHereIs extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     * Note that identifier ending by underscore (_) are expected by be chained 
     * with another string.  
     */
    private final static String HEREIS_ABOUT_  = "DlgMove.HereIs.";  //$NON-NLS-1$
   
    /**
     * The identifier of the target of the previous move from the learner.
     */
    private String      sTarget = null;

    /**
     * @param parent
     * 
     */
    public DialogueMoveHereIs(DialogueMove parent) {
        super(parent, DlgMoveID.HEREIS);
        //addPossibleMove(DlgMoveID.SHOWME);
        //addPossibleMove(DlgMoveID.LOST);
        addPossibleMove(DlgMoveID.TELLMORE);
        addPossibleMove(DlgMoveID.BAFFLED);
        if (OLMPrefs.CHALLENGE_ENABLED)
        {
        	addPossibleMove(DlgMoveID.AGREE);
        	addPossibleMove(DlgMoveID.DISAGREE);
        }
        addPossibleMove(DlgMoveID.MOVEON);
    }
    
    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute() 
    {
        super.onMoveExecute();

        // Get the target node from the relevant pane
        ToulminNode node = null;
        node = getParent().getToulminSelectedNode();
//        if (getParent().getJToulminGraph()!=null)
//            node = getParent().getJToulminGraph().getSelectedNode();
        if (node==null) return this;
        if (node.getToulminType()==Toulmin.CLAIM)
        {
            int lvl = node.getToulmin().getClaim().getClaimLevel();
            ArrayList arr = node.getToulmin().getData().analysePignistic(lvl);
            String type= (String)arr.get(0);
            String template = Messages.getString(HEREIS_ABOUT_ + node.getToulminType() + type);
            // remove the tenmplate and process the arguments 
            arr.remove(0);
            for (int i=0;i<arr.size();i++)
            {
                String argument= (String)arr.get(i);
                arr.set(i,Messages.getString(argument));
            }
            setLogItem(template,arr.toArray());
            
        }
        else if (node.getToulminType()==Toulmin.SUBCLAIM)
        {
            ToulminSubClaim sub = (ToulminSubClaim) node.getData();
            int nb = node.getToulmin().getList().size();
            String template = Messages.getRandomString(HEREIS_ABOUT_ + node.getToulminType());
            Object[] arg = {
                    new Integer(nb), // total number of evidence
                    new Integer(0), // number of evidence displayed
                    Messages.getString(sub.getDimension()),
                    Messages.getString(sub.getValue()),
            };
            Set set = node.getToulmin().getList().getSortedEvidence();
            Iterator ter = set.iterator();
            nb=0;
            while (ter.hasNext())
            {
                ToulminWarrant war = (ToulminWarrant)ter.next();
                System.out.println("index: " + war.getIndex() + " - relevance: "+war.getRelevance());
                if (nb>4)
                    war.setExpanded(false);
                else
                {
                    nb++;
                    war.setExpanded(true);
                }
                    
            }
            arg[1] = new Integer(nb);
            setLogItem(template,arg);
            
       }
        else if (node.getToulminType()==Toulmin.DATA)
        {
            // setup the default string, just in case
            String template = Messages.getRandomString(HEREIS_ABOUT_ + node.getToulminType());
            Object[] arg = {
                    new Integer(0), // total number of evidence
                    new Integer(0), // number of evidence displayed
                    Messages.getJudgementOn(getBeliefDesc(),node.getToulmin().getClaim().getClaimLevel())
            };
            ToulminList list = node.getToulmin().getList();
            //int maxRelevance = getParent().getJToulminGraph().getEvidenceRadius();
            if (list!=null)
            {
                template = Messages.getRandomString(HEREIS_ABOUT_ + node.getToulminType() + 
                                                    "." +list.getPartition());
                
                if (ToulminList.CLUSTER_NONE.equals(list.getPartition()))
                {
                	
                	Set set = list.getSortedEvidence();
                	Iterator ter = set.iterator();
                	int nb=0;
                	while (ter.hasNext())
                	{
                		ToulminWarrant war = (ToulminWarrant)ter.next();
                		System.out.println("index: " + war.getIndex() + " - relevance: "+war.getRelevance());
                		if (nb>4)
                			war.setExpanded(false);
                		else
                        {
                            nb++;
                			war.setExpanded(true);
                        }
                			
                	}
                    arg[0] = new Integer(nb);
                    arg[1] = new Integer(set.size());

//                    int nev = 0;
//                    int maxcluster = -1;
//                    int maxindex = -1;
//                    for (int i=0;i<list.size();i++)
//                    {
//                        ToulminWarrant war = (ToulminWarrant)list.get(i);
//                        int nr = war.getRelevance();
//                        if (nr>=maxRelevance) nev++;
//                        if (nr>maxcluster)
//                        {
//                            maxcluster = nr;
//                            maxindex = i;
//                        }
//                    }
//                    if (nev==0)
//                    {
//                        nev = 1;
//                        //getParent().getJToulminGraph().setEvidenceRadius(maxcluster);
//                    }
//                    arg[1] = new Integer(nev);
//                    System.out.println(maxcluster + " - " + nev);
                }
                else
                {
                	Toulmin subToulmin = list.analysePartition();
                	if (subToulmin!=null)
                	{
                		ToulminSubClaim sub = (ToulminSubClaim)subToulmin.getClaim();
                		System.err.println(sub.getDimension() + " " + sub.getValue());
                        
                		String str = HEREIS_ABOUT_ + node.getToulminType() +  "." +list.getPartition();
                        template = Messages.getRandomString(str);
                        arg[0] = new Integer(subToulmin.getList().size());
                        arg[1] = Messages.getString(sub.getDimension());
                        arg[2] = Messages.getString(sub.getValue());
                	}
//                    ArrayList map = new ArrayList();
//                    ToulminList allitem = new ToulminList();
//                    for (int i=0;i<list.size();i++)
//                    {
//                        Toulmin war = (Toulmin)list.get(i);
//                        ToulminList ll = war.getList();
//                        allitem.addAll(ll);
//                        //ToulminSubClaim sub = (ToulminSubClaim)war.getClaim();
//                        map.add(new Integer(0));
//                    }
//                    SortedSet set = allitem.getSortedEvidence();
//                    Iterator iter = set.iterator();
//                    while (iter.hasNext())
//                    {
//                        ToulminWarrant war = (ToulminWarrant)iter.next();
//                        if (war.getRelevance()<30) break;
//                        for (int i=0;i<list.size();i++)
//                        {
//                            Toulmin dd = (Toulmin)list.get(i);
//                            ToulminList ll = dd.getList();
//                            if (ll.contains(war))
//                            {
//                                int nb =((Integer)map.get(i)).intValue();
//                                map.set(i,new Integer(nb+1));
//                            }
//                        }
//                    }
//                    int index = -1;
//                    for (int i=0;i<map.size();i++)
//                    {
//                        index = Math.max(((Integer)map.get(i)).intValue(),index);
//                    }
//                    int gg = map.indexOf(new Integer(index));
//                    System.out.println(map);
//                    String str = HEREIS_ABOUT_ + node.getToulminType() +  "." +list.getPartition();
//                    template = Messages.getRandomString(str);
//                    arg[0] = (Integer)map.get(gg);
//                    Toulmin war = (Toulmin)list.get(gg);
//                    ToulminSubClaim sub = (ToulminSubClaim)war.getClaim();
//                    arg[1] = Messages.getString(sub.getDimension());
//                    arg[2] = Messages.getString(sub.getValue());
//                    HashMap map = new HashMap();
//                    for (int i=0;i<list.size();i++)
//                    {
//                        Toulmin war = (Toulmin)list.get(i);
//                        ToulminSubClaim sub = (ToulminSubClaim)war.getClaim();
//                        map.put(sub.getValue(),war);
//                    }
//                    int lvl = node.getToulmin().getClaim().getClaimLevel();
//                    String str = HEREIS_ABOUT_ + node.getToulminType() +  "." +list.getPartition();
//                    String cat = "ATTRIBUTE.OTHERS";
//                    if (lvl>=3)
//                    {
//                        cat = "ATTRIBUTE.PERFORMANCE.80";
//                    }
//                    else
//                    {
//                        cat = "ATTRIBUTE.PERFORMANCE.40";
//                    }
//                    Toulmin sublist = (Toulmin)map.get(cat);
//                    arg[0] = new Integer(sublist.getList().size());
//                    ToulminSubClaim sub = (ToulminSubClaim)sublist.getClaim();
//                    arg[1] = Messages.getString(sub.getDimension());
//                    arg[2] = Messages.getString(sub.getValue());
//                    template = Messages.getRandomString(str);
                }
            }
            setLogItem(template,arg);
        }
        else if (node.getToulminType()==Toulmin.WARRANT ||
                node.getToulminType()==Toulmin.BACKING)
        {
            //ToulminAlt toul = node.getToulmin();
            ToulminBacking backing = null;
            if (node.getData() instanceof ToulminBacking)
            	backing = (ToulminBacking)node.getData();
            else if (node.getData() instanceof ToulminWarrant)
            {
            	ToulminWarrant warrant = (ToulminWarrant)node.getData();
            	backing =warrant.getBacking();
            }

           if (backing!=null)
            {
                String template = backing.getTemplate(getPlanner());
                ArrayList arg = backing.getArguments(getPlanner());
                setLogItem(template,arg.toArray());
            }
        }
       
        //getParent().getJArgumentPane().activateArgument(sTarget);
        ToulminNode ret = null;
        getParent().expandToulminNode(node);
//        if (getParent().getJToulminGraph()!=null)
//        {
//            ret = getParent().getJToulminGraph().expandToulminNode(node.getID());
//            if (ret!=null)
//            {
//                getPlanner().activateToulminView(ret);
//                getParent().getJToulminGraph().centerOnNode(ret);
//            }
//        }
        // Update the move buttons
        //getParent().enableMove(DlgMoveID.AGREE,true);
        //getParent().enableMove(DlgMoveID.DISAGREE,true);
       
        return this;

    }
    
    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#goNextMove(org.activemath.xlm.openmodel.dialogue.DlgMoveID)
     */
    public DialogueMove goNextMove(DlgMoveID moveid)
    {
        DialogueMove next = super.goNextMove(moveid);
        if (next instanceof DialogueMoveAgree)
        {
            DialogueMoveAgree agree = (DialogueMoveAgree) next;
            agree.setTarget(sTarget);
        }
        if (next instanceof DialogueMoveBaffled)
        {
            DialogueMoveBaffled baffled = (DialogueMoveBaffled) next;
            baffled.setTarget(sTarget);
        }
        if (next!=null)
            next.onMoveExecute();

        return next;
    }
    
    /**
     * Set the target of the agreement/disagreement, if relevant.
     * @param tg    The identifier of the TAP element, target of the challenge.
     */
    public void setTarget(String tg) 
    {
        this.sTarget = tg;
    }
    
    /* (non-Javadoc)
     * @see dialogue.DialogueMove#getLogParams()
     */
    public Hashtable getLogParams() {
        Hashtable params = super.getLogParams();
        if (sTarget!=null) params.put(OLMQueryResult.LOG_TARGET,sTarget);
        return params;
    }
    
}
