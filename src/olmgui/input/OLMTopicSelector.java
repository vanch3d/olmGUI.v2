/**
 * @file OLMTopicSelector.java
 */
package olmgui.input;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import olmgui.i18n.TopicMaps;
import olmgui.utils.DescriptorListWrapper;
import olmgui.utils.FilteringJList;
import olmgui.utils.TopicIcon;
import olmgui.utils.TopicListModel;
import olmgui.utils.TopicWrapper;
import olmgui.utils.FilteringJList.FilteringModel;
import toulmin.BeliefDesc;
import config.OLMQueryResult;
import config.OLMTopicConfig;
import dialogue.DialoguePlanner;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.24 $
 *
 */
public class OLMTopicSelector extends JPanel {

    private JTabbedPane jTabbedPane = null;

    private JList jListDomain = null;
    private JList jListCompet = null;
    private JList jListMotiv = null;
    private JList jListAffect = null;
    private JList jListMetacog = null;
    private JList jListCAPEs = null;
    private JScrollPane jScrollDomain = null;
    private JScrollPane jScrollCompet = null;
    private JScrollPane jScrollMotiv = null;
    private JScrollPane jScrollAffect = null;
    private JScrollPane jScrollMetacog = null;
    private JScrollPane jScrollCAPEs = null;
    private JScrollPane jScrollPane = null;
    private FilteringJList jList = null;

    /**
     * Private class used to render the items in the lists.
     *
     */
    private class TopicCellRenderer extends DefaultListCellRenderer {
        //private ImageIcon longIcon = null;//new ImageIcon(getClass().getResource("./images/Find16.gif"));
        //private ImageIcon shortIcon = null;//new ImageIcon(ClassLoader.getSystemResource("./images/Save16.gif"));
        //private Color bkColor = null;
        
        
        /**
         * 
         */
        private TopicCellRenderer(OLMTopicConfig cfg) {
            super();
            //bkColor = cfg.getColor();
        }


        /* This is the only method defined by ListCellRenderer.  We just
         * reconfigure the Jlabel each time we're called.
         */
        public Component getListCellRendererComponent(
            JList list,
        Object value,   // value to display
        int index,      // cell index
        boolean iss,    // is the cell selected
        boolean chf)    // the list and the cell have the focus
        {
            /* The DefaultListCellRenderer class will take care of
             * the JLabels text property, it's foreground and background
             * colors, and so on.
             */
            super.getListCellRendererComponent(list, value, index, iss, chf);

            ListModel model = list.getModel();
            if (model!=null)
            {
                Object obj = model.getElementAt(index);
                if (obj instanceof TopicWrapper) 
                {
                    TopicWrapper wrapper = (TopicWrapper) obj;
                    if (wrapper.isRoot())
                    {
                        Font ft = getFont();
                        Font ftnew = ft.deriveFont(Font.BOLD);
                        setFont(ftnew);
                    }
                }
                else if (obj instanceof DescriptorListWrapper)
                {
                	DescriptorListWrapper wrapper = (DescriptorListWrapper) obj;
                    if (wrapper.isNew)
                    {
                        Font ft = getFont();
                        Font ftnew = ft.deriveFont(Font.BOLD);
                        setFont(ftnew);
                    }
                                    	
                }
            }


        return this;
        }
    }
    
    
    private class DescriptorCellRenderer extends DefaultListCellRenderer 
    {
        public Component getListCellRendererComponent(
            JList list,
            Object value,   // value to display
            int index,      // cell index
            boolean iss,    // is the cell selected
            boolean chf)    // the list and the cell have the focus
            {
                super.getListCellRendererComponent(list, value, index, iss, chf);
                ListModel model = list.getModel();
                if (model instanceof FilteringModel) {
                    FilteringModel filtermodel = (FilteringModel) model;
                    Object obj = filtermodel.getElementAt(index);
                    if (obj instanceof DescriptorListWrapper)
                    {
                        DescriptorListWrapper wrapper = (DescriptorListWrapper) obj;
                        OLMTopicConfig cfg = OLMTopicConfig.getTopicFromDescriptor(wrapper.bdesc);
                        setIcon(new TopicIcon(cfg));
                    }
     
                }
                return this;
            }
        
    }
    /**
     * Event handler for a List item selection
     *
     */
    private class OLMListSelectionHandler implements ListSelectionListener {
        
        public void valueChanged(ListSelectionEvent e) 
        { 
            if (!e.getValueIsAdjusting())
            {
                JList slist = (JList)e.getSource();
                if (slist.getSelectedValue()!=null)
                {
                    //String str = ((JList)e.getSource()).getSelectedValue().toString();
                    //System.out.println("Topic Selected: " + str);
                }
            }
        }
 
    }

    /**
     * 
     */
    public OLMTopicSelector() 
    {
        super();
        initialize();
    }


    /*public void setParent(OLMMainGUI parent)
    {
        sParent = parent;   
    }*/

    
    /**
     * This method initializes jTabbedPane  
     *  
     * @return javax.swing.JTabbedPane  
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setName("jTabbedPane");
            
            jTabbedPane.addTab(OLMTopicConfig.DOMAIN.getName(), new TopicIcon(OLMTopicConfig.DOMAIN), getJScrollDomain(),null);
            jTabbedPane.addTab(OLMTopicConfig.COMPET.getName(), new TopicIcon(OLMTopicConfig.COMPET), getJScrollCompet(), null);
            jTabbedPane.addTab(OLMTopicConfig.MOTIV.getName(), new TopicIcon(OLMTopicConfig.MOTIV), getJScrollMotiv(), null);
            jTabbedPane.addTab(OLMTopicConfig.AFFECT.getName(), new TopicIcon(OLMTopicConfig.AFFECT), getJScrollAffect(), null);
            jTabbedPane.addTab(OLMTopicConfig.METACOG.getName(), new TopicIcon(OLMTopicConfig.METACOG), getJScrollMetacog(), null);
            jTabbedPane.addTab(OLMTopicConfig.CAPES.getName(), new TopicIcon(OLMTopicConfig.CAPES), getJScrollCAPEs(), null);
           
            if (!OLMTopicConfig.DOMAIN.isVisible())
                jTabbedPane.remove(getJScrollDomain());
            if (!OLMTopicConfig.COMPET.isVisible())
                jTabbedPane.remove(getJScrollCompet());
            if (!OLMTopicConfig.MOTIV.isVisible())
                jTabbedPane.remove(getJScrollMotiv());
            if (!OLMTopicConfig.AFFECT.isVisible())
                jTabbedPane.remove(getJScrollAffect());
            if (!OLMTopicConfig.METACOG.isVisible())
                jTabbedPane.remove(getJScrollMetacog());
            if (!OLMTopicConfig.CAPES.isVisible())
                jTabbedPane.remove(getJScrollCAPEs());
            //if (!OLMTopicConfig.BDESC.isVisible())
            //    jTabbedPane.remove(getJScrollPane());

            jTabbedPane.setEnabled(true);
            
        }
        return jTabbedPane;
    }


    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(260, 484);
        this.setPreferredSize(new java.awt.Dimension(260,200));
        this.setLayout(new BorderLayout());
        this.add(getJScrollPane(), java.awt.BorderLayout.SOUTH);
        this.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
    }
   

    /**
     * This method initializes jList    
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListDomain() 
    {
        if (jListDomain == null)
        {
            jListDomain = new JList();
            jListDomain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListDomain.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.DOMAIN));
            //jListDomain.setPreferredSize(new java.awt.Dimension(150,100));
            //jListDomain.setVisibleRowCount(5);
            //jListDomain.addMouseListener(new OLMListDbClickHandler(OLMTopicConfig.DOMAIN));
            jListDomain.setPrototypeCellValue("XXXXX");
            jListDomain.setDragEnabled(true);
            
        }
        return jListDomain;
    }

    /**
     * This method initializes jList1   
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListCompet() 
    {
        if (jListCompet == null) 
        {
            jListCompet = new JList();
            jListCompet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListCompet.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.COMPET));
            //jListCompet.setPreferredSize(new java.awt.Dimension(150,100));
            //jListCompet.addMouseListener(new OLMListDbClickHandler(OLMTopicConfig.COMPET));

        }
        return jListCompet;
    }

    /**
     * This method initializes jList2   
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListMotiv() 
    {
        if (jListMotiv == null) 
        {
            jListMotiv = new JList();
            jListMotiv.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListMotiv.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.MOTIV));
            //jListMotiv.setPreferredSize(new java.awt.Dimension(150,100));
            //jListMotiv.addMouseListener(new OLMListDbClickHandler(OLMTopicConfig.MOTIV));
        }
        return jListMotiv;
    }

    /**
     * This method initializes jList3   
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListAffect()
    {
        if (jListAffect == null) 
        {
            jListAffect = new JList();
            jListAffect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListAffect.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.AFFECT));
            //jListAffect.setPreferredSize(new java.awt.Dimension(150,100));
            //jListAffect.addMouseListener(new OLMListDbClickHandler(OLMTopicConfig.AFFECT));

        }
        return jListAffect;
    }

    /**
     * This method initializes jList4   
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListMetacog() 
    {
        if (jListMetacog == null) 
        {
            jListMetacog = new JList();
            jListMetacog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListMetacog.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.METACOG));
            jListMetacog.addListSelectionListener(new OLMListSelectionHandler());
        }
        return jListMetacog;
    }

    /**
     * This method initializes jList5   
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListCAPEs() {
        if (jListCAPEs == null) {
            jListCAPEs = new JList();
            jListCAPEs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListCAPEs.setCellRenderer(new TopicCellRenderer(OLMTopicConfig.CAPES));
            //jListCAPEs.setBackground(OLMTopicConfig.CAPES.getColor());
            jListCAPEs.addListSelectionListener(new OLMListSelectionHandler());
            //jListCAPEs.addMouseListener(new OLMListDbClickHandler(OLMTopicConfig.CAPES));
    }
        return jListCAPEs;
    }
    
    private void FillListData(OLMTopicConfig config,JList list,Hashtable htab, int index)
    {
        TopicListModel model = new TopicListModel(config);
        Vector vecnode = (Vector)htab.get(OLMQueryResult.CAT_NODES);
        //Vector vecedge = (Vector)htab.get(OLMQueryResult.CAT_EDGES);
        String root = (String)htab.get(OLMQueryResult.CAT_ROOT);
        
        int nb = vecnode.size();
        for (int i=0;i<nb;i++)
        {
            Vector node = (Vector)vecnode.get(i);
            String nodeid = (String)node.get(0);
            Integer n = (Integer)node.get(1);
            String nodetitled = (String)node.get(2);
            String nodeDescd = (String)node.get(3);
            
            //System.err.println(config.toString() + "." + nodeid + ".title\t=  " + nodetitle);
            //System.err.println(config.toString() + "." + nodeid + ".description\t=  " + nodeDesc);
            
            if (n.intValue()==0) continue;

            String nodetitle = TopicMaps.getTitle(config,nodeid);
            String nodeDesc = TopicMaps.getDescription(config,nodeid);
            if (nodetitle==null)
            	nodetitle = nodetitled;
            if (nodeDesc==null)
            	nodeDesc = nodeDescd;
            TopicWrapper item = new TopicWrapper(nodeid);
            item.setTitle(nodetitle);
            if (nodeDesc!=null && (nodeDesc.equals("")==false))
                    item.setDescription(nodeDesc);
            if (nodeid.equals(root))
            {
                item.setRoot(true);
                int indexCpn = getJTabbedPane().indexOfComponent(list);
                if (indexCpn!=-1)
                    getJTabbedPane().setTitleAt(indexCpn,nodetitle);
            }
            model.add(item);
        }
        config.setModel(model);
        list.setModel(model);
        
        

    }
    
    public ListModel getModelFromTopicMap(OLMTopicConfig config)
    {
        ListModel model = null;
        if (config == OLMTopicConfig.DOMAIN)
            model = (ListModel)jListDomain.getModel();
        else if (config == OLMTopicConfig.CAPES)
            model = (ListModel)jListCAPEs.getModel();
        else if (config == OLMTopicConfig.AFFECT)
            model = (ListModel)jListAffect.getModel();
        else if (config == OLMTopicConfig.COMPET)
            model = (ListModel)jListCompet.getModel();
        else if (config == OLMTopicConfig.METACOG)
            model = (ListModel)jListMetacog.getModel();
        else if (config == OLMTopicConfig.MOTIV)
            model = (ListModel)jListMotiv.getModel();

        return model;
    }
    
    public void setFilter(BeliefDesc desc)
    {
    	getJList().setFilter(desc);
    }
    
    public void setDescriptors(Vector list)
    {
        for (int i=0;i<list.size();i++)
        {
        	Vector vec = (Vector)list.get(i);
        	getJList().addElement(new DescriptorListWrapper(vec,false));
        }
 
//    	DescriptorListModel desclist = null;
//    	ListModel oldmodel = getJList().getModel();
//    	if (oldmodel instanceof DescriptorListModel) 
//    	{
//			desclist = (DescriptorListModel) oldmodel;
//			//Iterator iter = desclist.iterator();
//			//while (iter.hasNext())
//			//{
//			//	Object obj = iter.next();
//			//	if (obj instanceof DescriptorListWrapper) {
//			//		DescriptorListWrapper wrapper = (DescriptorListWrapper) obj;
//			//		wrapper.isNew = false;
//			//	}
//			//}
//			//desclist.clear();
//    	}
//    	else
//    	{
//    		desclist = new DescriptorListModel();
//    		//getJList().setModel(desclist);
//    	}
//        for (int i=0;i<list.size();i++)
//        {
//        	Vector vec = (Vector)list.get(i);
//        	desclist.add(new DescriptorListWrapper(vec,false));
//        }
        
   	
    	/*ListModel model2 = getJList().getModel();
    	if (model2 instanceof DescriptorListModel) 
    	{
			DescriptorListModel desclist = (DescriptorListModel) model2;
			desclist.clear();
    	}
        DescriptorListModel model = new DescriptorListModel();
        for (int i=0;i<list.size();i++)
        {
        	Vector vec = (Vector)list.get(i);
            model.add(new DescriptorListWrapper(vec,false));
        }
        getJList().setModel(model);*/
    }
        
    public void setTopicMap(Hashtable htab,String map)
    {
        if (htab==null)
        {
            //System.err.println("No topics defined! Check your network connection!");
            OLMTopicConfig maps[]={
                    OLMTopicConfig.DOMAIN,
                    OLMTopicConfig.COMPET,
                    OLMTopicConfig.MOTIV,
                    OLMTopicConfig.AFFECT,
                    OLMTopicConfig.METACOG,
                    OLMTopicConfig.CAPES
                };

            for (int i=0;i<maps.length;i++)
            {
                getJTabbedPane().setTitleAt(i,maps[i].getName());
            }
            return;
        }
 
        OLMTopicConfig config = OLMTopicConfig.getByName(map);
        if (map.equals(OLMTopicConfig.DOMAIN.toString()))
            FillListData(config,getJListDomain(),htab,0);
        if (map.equals(OLMTopicConfig.COMPET.toString()))
            FillListData(config,getJListCompet(),htab,1);
        if (map.equals(OLMTopicConfig.MOTIV.toString()))
            FillListData(config,getJListMotiv(),htab,2);
        if (map.equals(OLMTopicConfig.AFFECT.toString()))
            FillListData(config,getJListAffect(),htab,3);
        if (map.equals(OLMTopicConfig.METACOG.toString()))
            FillListData(config,getJListMetacog(),htab,4);
        if (map.equals(OLMTopicConfig.CAPES.toString()))
            FillListData(config,getJListCAPEs(),htab,5);
    }

    /**
     * This method initializes jScrollPane  
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollDomain() {
        if (jScrollDomain == null) {
            jScrollDomain = new JScrollPane();
            jScrollDomain.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollDomain.setViewportView(getJListDomain());
        }
        return jScrollDomain;
    }

    /**
     * This method initializes jScrollPane1 
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollCompet() {
        if (jScrollCompet == null) {
            jScrollCompet = new JScrollPane();
            jScrollCompet.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollCompet.setViewportView(getJListCompet());
        }
        return jScrollCompet;
    }

    /**
     * This method initializes jScrollPane2 
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollMotiv() {
        if (jScrollMotiv == null) {
            jScrollMotiv = new JScrollPane();
            jScrollMotiv.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollMotiv.setViewportView(getJListMotiv());
        }
        return jScrollMotiv;
    }

    /**
     * This method initializes jScrollPane3 
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollAffect() {
        if (jScrollAffect == null) {
            jScrollAffect = new JScrollPane();
            jScrollAffect.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollAffect.setViewportView(getJListAffect());
        }
        return jScrollAffect;
    }

    /**
     * This method initializes jScrollPane4 
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollMetacog() {
        if (jScrollMetacog == null) {
            jScrollMetacog = new JScrollPane();
            jScrollMetacog.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollMetacog.setViewportView(getJListMetacog());
        }
        return jScrollMetacog;
    }

    /**
     * This method initializes jScrollPane5 
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollCAPEs()
    {
        if (jScrollCAPEs == null)
        {
            jScrollCAPEs = new JScrollPane();
            jScrollCAPEs.setPreferredSize(new java.awt.Dimension(150,100));
            jScrollCAPEs.setViewportView(getJListCAPEs());
        }
        return jScrollCAPEs;
    }

    public void setListeners(DialoguePlanner planner)
    {
        if (planner==null) return;

        getJTabbedPane().addChangeListener((ChangeListener) planner.setTopicSelectionListener());
        
        getJListDomain().addMouseListener(planner.setTopicMouseListener());
        getJListCompet().addMouseListener(planner.setTopicMouseListener());
        getJListMotiv().addMouseListener(planner.setTopicMouseListener());
        getJListAffect().addMouseListener(planner.setTopicMouseListener());
        getJListMetacog().addMouseListener(planner.setTopicMouseListener());
        getJListCAPEs().addMouseListener(planner.setTopicMouseListener());

        getJList().addMouseListener(planner.setTopicMouseListener());
        
        getJListDomain().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
        getJListCompet().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
        getJListMotiv().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
        getJListAffect().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
        getJListMetacog().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
        getJListCAPEs().addListSelectionListener((ListSelectionListener) planner.setTopicSelectionListener());
    }


    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    public void setEnabled(boolean en) 
    {
        super.setEnabled(en);
        getJTabbedPane().setEnabled(en);
        getJListDomain().setEnabled(en);
        getJListAffect().setEnabled(en);
        getJListCAPEs().setEnabled(en);
        getJListCompet().setEnabled(en);
        getJListMetacog().setEnabled(en);
        getJListMotiv().setEnabled(en);
        
        getJList().setEnabled(en);
    }


    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJList());
            
            if (!OLMTopicConfig.BDESC.isVisible())
                jScrollPane.setVisible(false);
        }
        return jScrollPane;
    }


    /**
     * This method initializes jList	
     * 	
     * @return javax.swing.JList	
     */
    private FilteringJList getJList() {
        if (jList == null) {
            jList = new FilteringJList();
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList.setCellRenderer(new DescriptorCellRenderer());
       }
        return jList;
    }


 
    
} 
