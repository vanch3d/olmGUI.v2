/**
 * @file OLMGraphBrowser.java
 */
package olmgui.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.plaf.metal.MetalInternalFrameUI;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import olmgui.i18n.Messages;
import olmgui.i18n.TopicMaps;
import toulmin.BeliefDesc;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;
import com.sun.java.swing.plaf.windows.HintInternalFrameUI;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameUI;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPaintListener;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.HyperScroll;
import com.touchgraph.graphlayout.interaction.LocalityScroll;
import com.touchgraph.graphlayout.interaction.RotateScroll;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import com.touchgraph.graphlayout.interaction.ZoomScroll;

import config.OLMQueryResult;
import config.OLMTopicConfig;

/** 
 * OLMGraphBrowser contains code for adding scrollbars and interfaces to the TGPanel.
 * In this OLM version, the GUI supports zoom, rotation, locality and hyperbolic navigation.
 * Edition of the graph have been removed.
 * 
 * @see com.touchgraph.graphlayout.GLPanel
 * 
 * @todo Need to change the way nodes are added to the graph; use a local GraphEltSet
 *       and update using TGPanel#updateLocalityFromVisibility() instead of adding the 
 *       node directly to the graph.
 *
 * @todo There is still a problem when adding nodes to the set rather than the graph: updating 
 *       the graph is not working properly. The current solution is to add the new belief directly
 *       to the graph and enforce Expand on the node(s). A fix to the probelm need to be found.   
 *
 * @author  Alexander Shapiro
 * @author  Nicolas Van Labeke (adapted from GLPanel for the OLM)
 * @version $Revision: 1.31 $
 */
public class OLMGraphBrowser extends JPanel 
{
    //protected String STR_TOGGLE = Messages.getString("OLMGraphBrowser.Menu.Toggle");    // label for the menu //$NON-NLS-1$
    //protected String STR_EXPORT = Messages.getString("OLMGraphBrowser.Menu.Export");    // label for the menu //$NON-NLS-1$
    protected String STR_ZOOM = Messages.getString("OLMGraphBrowser.Menu.Zoom");         // label for zoom menu item //$NON-NLS-1$
    protected String STR_ROTATE = Messages.getString("OLMGraphBrowser.Menu.Rotate");     // label for rotate menu item //$NON-NLS-1$
    protected String STR_LOCALITY = Messages.getString("OLMGraphBrowser.Menu.Locality"); // label for locality menu item //$NON-NLS-1$
    protected String STR_HYPER = Messages.getString("OLMGraphBrowser.Menu.Hyperbolic");  // label for locality menu item //$NON-NLS-1$
    protected String STR_NAVIGATE = Messages.getString("OLMGraphBrowser.Menu.Navigate"); // label for the navigate radiobutton //$NON-NLS-1$
    protected String STR_RESET = Messages.getString("OLMGraphBrowser.Menu.Reset");      // label for the reset button//$NON-NLS-1$

    protected final static String STR_NODEON = "OLMGraphBrowser.Topics.On"; //$NON-NLS-1$
    protected final static String STR_NODEABOUT = "OLMGraphBrowser.Topics.About"; //$NON-NLS-1$
    protected final static String STR_NODEMAP = "OLMGraphBrowser.Topics.Maps"; //$NON-NLS-1$
    protected final static String STR_USER = "OLMGraphBrowser.Topics.User"; //$NON-NLS-1$
    
    //protected final static String ID_USERNODE = "Anon";   // ID of the top node //$NON-NLS-1$

    /**
     * A reference to the listener used for scrolling horizontaly/vertically.
     */
    protected HVScroll hvScroll;
    
    /**
     * A reference to the listener used for zoming in/out the graph. 
     */
    protected ZoomScroll zoomScroll;
    
    /**
     * A reference to the listener used for changing the hyperbolic lense on the graph.
     */
    protected HyperScroll hyperScroll;
    
    /**
     * A reference to the listener used for rotating the graph in the view.
     */
    protected RotateScroll rotateScroll;
    
    /**
     * A reference to the listener used for changing the locality or the graph.
     */
    protected LocalityScroll localityScroll;
    
    
    protected JPanel topPanel;  
    //private JPanel scrollSelect;
    //private JPanel scrollPanel;
    /**
     * 
     */
    protected TGLensSet tgLensSet;

    /**
     * 
     */
    protected TGPanel tgPanel;

    
    protected GraphEltSet completeEltSet;
    
    /**
     * 
     */
    //protected JPopupMenu glPopup;
    public HintIntFrame hintIntFrame;


    /**
     * 
     */
    protected Hashtable scrollBarHash;

    
    /**
     * A Reference to the UI Manager used in the TouchGraph Panel
     */
    protected TGUIManager tgUIManager;

    /**
     * 
     */
    protected String topNodeUser = null;

    /**
     * 
     */
    protected Color defaultColor = java.awt.SystemColor.control;

    /**
     * List of all the icons used to decorate the nodes in the graph.
     */
    private ImageIcon[] iconList = null;

    private HintButton hintBtn = null;

    private class HintButton extends JButton
    {

        OLMNode mouseOverN;
        boolean mouseOverButton;

        Timer timer = null;

        /**
         * @return
         */
        public Timer getTimer()
        {
            return this.timer;
        }

        HintButton()
        {
            super("info");
            mouseOverN = null;
            mouseOverButton = false;
            setSize(new Dimension(28, 14));
            setMargin(new Insets(1, 1, 1, 1));
            setBorder(null);
            setBackground(new Color(0.0F, 1.0F, 0.2F, 0.4F));
            setForeground(new Color(0.0F, 0.0F, 1.0F, 0.4F));
            setFont(new Font("Tahoma", 1, 9));
            
            timer = new Timer(2000,new ActionListener() 
            {
                public void actionPerformed(ActionEvent actionevent)
                {
                    setVisible(false);
                    timer.stop();
                    mouseOverN = null;
               }               
            });
            
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(mouseOverN != null && mouseOverN.getHint() != null)
                        hintIntFrame.showHint(mouseOverN);
                        setVisible(false);
                        timer.stop();
                        mouseOverN = null;                }

            });
            tgPanel.addPaintListener(new TGPaintListener() {

                public void paintFirst(Graphics g)
                {
                    if(mouseOverButton)
                        tgPanel.setMouseOverN(mouseOverN);
                }

                public void paintAfterEdges(Graphics g)
                {
                }

                public void paintLast(Graphics g)
                {
                    if (!mouseOverButton && (tgPanel.getMouseOverN() instanceof OLMNode))
                        mouseOverN = (OLMNode)tgPanel.getMouseOverN();
                    if(mouseOverN != null && mouseOverN.getHint() != null && mouseOverN.getHint().length() > 0)
                    {
                        setLocation((int)((Node) (mouseOverN)).drawx - Math.max(9, Math.min(40, mouseOverN.getWidth() / 2 - 1)), (int)((Node) (mouseOverN)).drawy - Math.max(17, mouseOverN.getHeight() / 2 + 11));
                        setVisible(true);
                        timer.restart();
                    } else
                    {
                        setVisible(false);
                    }
                }

            });
            addMouseListener(new MouseAdapter() 
            {

                public void mouseEntered(MouseEvent mouseevent)
                {
                    mouseOverButton = true;
                    timer.stop();
                }

                public void mouseExited(MouseEvent mouseevent)
                {
                    mouseOverButton = false;
                    tgPanel.setMouseOverN(null);
                    tgPanel.repaint();
                }

            });
        }
    }

    
    class HintLinkListener implements HyperlinkListener
    {

        public void hyperlinkUpdate(HyperlinkEvent e) 
        {
            if (e.getEventType()==EventType.ACTIVATED)
            {
                //System.out.println("hyper des: " + e.getDescription());
                //System.out.println("hyper url: " + e.getURL());
            }
        }
        
    }
    class HintIntFrame extends JInternalFrame
    {
    	private boolean first = true;
    	
    	private Point computeSize(OLMNode gbnode)
    	{
            int         globalHintWidth = 200;

            setTitle(gbnode.getLabel());
            int i = globalHintWidth;
            int j = (int)((Node) (gbnode)).drawx;
            int k = (int)((Node) (gbnode)).drawy;
                tpHint.setText(gbnode.getHint());
 
            spHint.getVerticalScrollBar().setVisible(false);
            int l = 10;//gbnode.getHintHeight();
            tpHint.setSize(new Dimension(i - 16, 10));
            l = tpHint.getPreferredScrollableViewportSize().height + 40;
            int i1 = 0;
            int j1 = tgPanel.getSize().height;
            int k1 = gbnode.getHeight() / 2;
            if(k > j1 / 2 || k - k1 > l)
            {
                l = Math.min(k - k1, l);
                i1 = k - l - k1;
            } else
            {
                l = Math.min(j1 - k - k1, l);
                i1 = k + k1;
            }
            int l1 = j - i / 2;
            l1 = Math.max(Math.min(l1, tgPanel.getWidth() - i), 0);
            setSize(new Dimension(i, l));
            
            return new Point(l1, i1);
            
    	}
        public void showHint(OLMNode gbnode)
        {
        	Point pt = computeSize(gbnode);
            setLocation(pt);
            setVisible(true);
            if (first)
            {
            	first=false;
            	computeSize(gbnode);
            }
            (new Thread() {

                public void run()
                {
                    try
                    {
                        Thread.currentThread();
                        Thread.sleep(400L);
                    }
                    catch(InterruptedException interruptedexception) { }
                    spHint.getVerticalScrollBar().setValue(0);
                }

            }).start();
        }

        JTextPane tpHint;
        JScrollPane spHint;

        public HintIntFrame()
        {
//        	UIManager.put("InternalFrame.titleButtonWidth", new Integer(16));
//        	UIManager.put("InternalFrame.titleButtonHeight", new Integer(16));
//        	UIManager.put("InternalFrame.titlePaneHeight", new Integer(16));
////        	titlePaneHeight = UIManager.getInt("InternalFrame.titlePaneHeight");
//        	buttonWidth     = UIManager.getInt("InternalFrame.titleButtonWidth")  - 4;
//        	buttonHeight    = UIManager.getInt("InternalFrame.titleButtonHeight") - 4;

        	//setFrameIcon(null);
//        	Icon ico = UIManager.getIcon("InternalFrame.closeIcon");
//        	UIManager.put("InternalFrame.paletteCloseIcon", ico);
//        	this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
//        	MetalInternalFrameUI plaf = new MetalInternalFrameUI(this);
        	setUI(new HintInternalFrameUI(this));
        	//plaf.setPalette(true);
            tpHint = new JTextPane();
            tpHint.setEditable(false);
            int size = 10;
            //tpHint.addHyperlinkListener(new _cls1());
            HTMLEditorKit editorKit = new HTMLEditorKit();
            StyleSheet styles = new StyleSheet();
            String css = "body {font-family:\"Verdana\"; font-size:\"" + size + "pt\"; color: #222222}";
            styles.addRule(css);
            css = "a:link, a:visited {text-decoration: none; color: blue}";
            styles.addRule(css);
            css = "a:hover { text-decoration: underline; color: red; background: yellow }";
            styles.addRule(css);
            editorKit.setStyleSheet(styles);
            tpHint.setEditorKit(editorKit);
            spHint = new JScrollPane(tpHint);
            spHint.setVerticalScrollBarPolicy(20);
            tpHint.addHyperlinkListener(new HintLinkListener());
            setResizable(false);
            setClosable(true);
            setMaximizable(false);
            setIconifiable(false);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(spHint);
        }
    }
    
    protected void enableHints()
    {
        hintIntFrame = new HintIntFrame();
        tgPanel.add(hintIntFrame);
        tgPanel.add(hintBtn = new HintButton());
    }
    
   /** 
    * Default constructor.
    * Make sure to explicitely call {@link #initialize()} after calling the constructor
    */
    public OLMGraphBrowser() 
    {
        topPanel = new JPanel();
        scrollBarHash = new Hashtable();
        tgLensSet = new TGLensSet();
        tgPanel = new TGPanel();
        hvScroll = new HVScroll(tgPanel, tgLensSet);
        zoomScroll = new ZoomScroll(tgPanel);
        hyperScroll = new HyperScroll(tgPanel);
        rotateScroll = new RotateScroll(tgPanel);
        localityScroll = new LocalityScroll(tgPanel);
        
        completeEltSet = new GraphEltSet();
        tgPanel.setGraphEltSet(completeEltSet);
        
        enableHints();
        
        setTopNode(STR_USER);
        //initialize();
        tgPanel.setBackColor(java.awt.SystemColor.control);
        //super.defaultColor = java.awt.SystemColor.control;

    }
    
    /**
     * Build an image from the (relative) URL
     * @param url   The location of the resource, relatively to the source.
     * @return      A reference to the image, <code>null</code> is non-existent.
     */
    private ImageIcon getImageFromIcon(String url)
    {
        ImageIcon ico = new ImageIcon(getClass().getResource(url));
        return ico;
    }
    /**
     * Set the list of icons to be used by the nodes.
     */
    public void setImageList()
    {
        //this.imgList=iconList;
        this.iconList = new ImageIcon[5];
        this.iconList[0]= getImageFromIcon("/res/buddy.gif"); 
        this.iconList[1]= getImageFromIcon("/res/level1.gif");
        this.iconList[2]= getImageFromIcon("/res/level2.gif");
        this.iconList[3]= getImageFromIcon("/res/level3.gif");
        this.iconList[4]= getImageFromIcon("/res/level4.gif");
    }
    
    /**
     * Return a reference to an image stored in the image list.
     * @param index The 0-based index of the image in the list
     * @return A reference to the indexed image, null if none.
     */
    private ImageIcon getImage(int index)
    {
        if (iconList==null || iconList.length < index)
            return null;
        else
            return iconList[index];
    }
    
    public ImageIcon getLevelImage(int lvl)
    {
        return getImage(lvl);
    }
    
    public ImageIcon getUserImage()
    {
        return getImage(0);
    }
    
    /**
     * Set the name of the top node of the graph
     * @param labelTopNode The label to set for the top node of the graph
     */
    public void setTopNode(String labelTopNode) {
        topNodeUser = labelTopNode;
        //Node tt = tgPanel.findNode(ID_USERNODE);
        Node tt = completeEltSet.findNode(STR_USER);
        if (tt!=null)
        {
            tt.setLabel(labelTopNode);
            tgPanel.repaint();
        }
            
    }
    
    void setNodeAttribute(Node node,String label,String hint)
    {
    	if (node==null) return;
    	if (label!=null)
    		node.setLabel(label);
    	String shint = Messages.getSafeString(hint + ".Hint");
    	if (shint!=null && node instanceof OLMNode) 
    	{
			OLMNode onode = (OLMNode) node;
			onode.setHint(shint);
		}
    }

    void setNodeAttribute(Node node,String key)
    {
    	if (node==null) return;
    	String label = Messages.getSafeString(key);
    	if (label!=null)
    		node.setLabel(label);
    	String hint = Messages.getSafeString(key + ".Hint");
    	if (hint!=null && node instanceof OLMNode) 
    	{
			OLMNode onode = (OLMNode) node;
			onode.setHint(hint);
		}
    }
    
    /** 
     * Initialize panel, lens, and establish the initial graph.
     */
    public void initialize() 
    {
        setImageList();
        buildPanel();
        buildLens();
        tgPanel.setLensSet(tgLensSet);
        addUIs();

        try 
        {
            //randomGraph();
            initGraph();
        } 
        catch ( TGException tge ) {tge.printStackTrace();}
        
        setVisible(true);
        setLocalityRadius(2);
        this.setSize(new java.awt.Dimension(182,185));
    }

    /** Return the TGPanel used with this OLMGraphBrowser. */
    public TGPanel getTGPanel() {
        return tgPanel;
    }

    // navigation .................

    /** Return the HVScroll used with this OLMGraphBrowser. */
    public HVScroll getHVScroll()
    {
        return hvScroll;
    }
    
    /** Return the HyperScroll used with this OLMGraphBrowser. */
    public HyperScroll getHyperScroll()
    {
        return hyperScroll;
    }

    /** Sets the horizontal offset to p.x, and the vertical offset to p.y
      * given a Point <tt>p<tt>. 
      */
    public void setOffset( Point p ) {
        hvScroll.setOffset(p);
    };

    /** Return the horizontal and vertical offset position as a Point. */
    public Point getOffset() {
        return hvScroll.getOffset();
    };

    // rotation ...................

    /** Return the RotateScroll used with this OLMGraphBrowser. */
    public RotateScroll getRotateScroll()
    {
        return rotateScroll;
    }

    /** Set the rotation angle of this OLMGraphBrowser (allowable values between 0 to 359). */
     public void setRotationAngle( int angle ) {
        rotateScroll.setRotationAngle(angle);
    }

    /** Return the rotation angle of this OLMGraphBrowser. */
    public int getRotationAngle() {
        return rotateScroll.getRotationAngle();
    }

    // locality ...................

    /** Return the LocalityScroll used with this OLMGraphBrowser. */
    public LocalityScroll getLocalityScroll()
    {
        return localityScroll;
    }

    /** Set the locality radius of this TGScrollPane  
      * (allowable values between 0 to 4, or LocalityUtils.INFINITE_LOCALITY_RADIUS). 
      */
    public void setLocalityRadius( int radius ) {
        localityScroll.setLocalityRadius(radius);
    }

    /** Return the locality radius of this OLMGraphBrowser. */
    public int getLocalityRadius() {
        return localityScroll.getLocalityRadius();
    }

    // zoom .......................

    /** Return the ZoomScroll used with this OLMGraphBrowser. */
    public ZoomScroll getZoomScroll() 
    {
        return zoomScroll;
    }

    /**
     * Set the zoom value of the OLMGraphBrowser .
     * @param zoomValue The zoom factor (values between -100 to 100).
     */
    public void setZoomValue( int zoomValue ) {
        zoomScroll.setZoomValue(zoomValue);
    }

    /** Return the zoom value of this OLMGraphBrowser. */
    public int getZoomValue() {
        return zoomScroll.getZoomValue();
    }

    // ....

    //public JPopupMenu getGLPopup() 
    //{
    //    return glPopup;
    //}

    public void buildLens() 
    {
        tgLensSet.addLens(hvScroll.getLens());
        tgLensSet.addLens(zoomScroll.getLens());
        tgLensSet.addLens(hyperScroll.getLens());
        tgLensSet.addLens(rotateScroll.getLens());
        tgLensSet.addLens(tgPanel.getAdjustOriginLens());
    }

    public void buildPanel() 
    {
        final JScrollBar horizontalSB = hvScroll.getHorizontalSB();
        final JScrollBar verticalSB = hvScroll.getVerticalSB();
        final JSlider zoomSB = zoomScroll.getZoomSB();
        final JSlider rotateSB = rotateScroll.getRotateSB();
        final JSlider localitySB = localityScroll.getLocalitySB();
        final JSlider hyperSB = hyperScroll.getHyperSB();

        setLayout(new BorderLayout());

        JPanel scrollPanel = new JPanel();
        scrollPanel.setBackground(defaultColor);
        scrollPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        topPanel.setBackground(defaultColor);
        topPanel.setLayout(new GridBagLayout());
        topPanel.setSize(new java.awt.Dimension(195,133));
        c.gridy=0; c.fill=GridBagConstraints.HORIZONTAL;

        scrollBarHash.put(STR_ZOOM, zoomSB);
        scrollBarHash.put(STR_ROTATE, rotateSB);
        scrollBarHash.put(STR_LOCALITY, localitySB);
        scrollBarHash.put(STR_HYPER, hyperSB);

        JPanel scrollSelect = scrollSelectPanel(new String[] {STR_ZOOM, STR_ROTATE, STR_LOCALITY,STR_HYPER});
        scrollSelect.setBackground(defaultColor);
        
        JButton btnReset = new JButton(STR_RESET);
        btnReset.setUI(new BlueishButtonUI());

        btnReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                Node tt = completeEltSet.findNode(STR_USER);
                if (tt==null) return;
                getZoomScroll().graphReset();
                getLocalityScroll().graphReset();
                getRotateScroll().graphReset();
                getHyperScroll().graphReset();
                try 
                {
                    tgPanel.setSelect(tt);
                    tgPanel.setLocale(tt,2);
                    getHVScroll().slowScrollToCenter(tt);
                    tgPanel.repaintAfterMove();
                } catch (TGException e1) {e1.printStackTrace();}
            }
        
        });
        c.gridx=1;c.weightx=0;
        c.insets=new Insets(0,0,0,5);
        topPanel.add(btnReset,c);

        c.gridx=2;c.weightx=1;
        c.insets=new Insets(0,0,0,0);
        topPanel.add(scrollSelect,c);

        add(topPanel, BorderLayout.NORTH);

        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1;
        scrollPanel.add(tgPanel,c);

        c.gridx = 1; c.gridy = 1; c.weightx = 0; c.weighty = 0;
        scrollPanel.add(verticalSB,c);

        c.gridx = 0; c.gridy = 2;
        scrollPanel.add(horizontalSB,c);

        add(scrollPanel,BorderLayout.CENTER);

//        glPopup = new JPopupMenu();
//        glPopup.setBackground(defaultColor);
//
//        JMenuItem menuItem = new JMenuItem(STR_TOGGLE);
//        ActionListener toggleControlsAction = new ActionListener() {
//                boolean controlsVisible = true;
//
//                public void actionPerformed(ActionEvent e) {
//                    controlsVisible = !controlsVisible;
//                    horizontalSB.setVisible(controlsVisible);
//                    verticalSB.setVisible(controlsVisible);
//                    topPanel.setVisible(controlsVisible);
//                }
//            };
//        menuItem.addActionListener(toggleControlsAction);
//        glPopup.add(menuItem);
//
//        JMenuItem menuItem2 = new JMenuItem(STR_EXPORT);
//        ActionListener xportControlsAction = new ActionListener() 
//        {
//
//            // Returns a generated image.
//            public RenderedImage myCreateImage() {
//                int width = 1000;
//                int height = 1000;
//            
//                // Create a buffered image in which to draw
//                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            
//                // Create a graphics contents on the buffered image
//                Graphics2D g2d = bufferedImage.createGraphics();
//            
//                // Draw graphics
//                tgPanel.paint(g2d);
//            
//                // Graphics context no longer needed so dispose it
//                g2d.dispose();
//                return bufferedImage;
//            }
//                
//            public void actionPerformed(ActionEvent e) 
//            {
//                // Create an image to save
//                RenderedImage rendImage = myCreateImage();
//                
//                // Write generated image to a file
//                try {
//                    // Save as PNG
//                    File file = new File("newimage.png"); //$NON-NLS-1$
//                    ImageIO.write(rendImage, "png", file); //$NON-NLS-1$
//                
//                    // Save as JPEG
//                    file = new File("newimage.jpg"); //$NON-NLS-1$
//                    ImageIO.write(rendImage, "jpg", file); //$NON-NLS-1$
//                } catch (IOException edd) {
//                }
//            }
//        };
//        menuItem2.addActionListener(xportControlsAction);
//        glPopup.add(menuItem2);
    }

    protected JPanel scrollSelectPanel(String[] scrollBarNames) {
        final JComboBox scrollCombo = new JComboBox(scrollBarNames);
        scrollCombo.setBackground(defaultColor);
        scrollCombo.setPreferredSize(new Dimension(80,20));
        scrollCombo.setSelectedIndex(0);
        final JSlider initialSB = (JSlider) scrollBarHash.get(scrollBarNames[0]);
        scrollCombo.addActionListener(new ActionListener() {
            JSlider currentSB = initialSB;
            public void actionPerformed(ActionEvent e) {
                JSlider selectedSB = (JSlider) scrollBarHash.get(
                        (String) scrollCombo.getSelectedItem());
                if (currentSB!=null) currentSB.setVisible(false);
                if (selectedSB!=null) selectedSB.setVisible(true);
                currentSB = selectedSB;
            }
        });

        final JPanel sbp = new JPanel(new GridBagLayout());
        sbp.setBackground(defaultColor);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx= 0;
        sbp.add(scrollCombo,c);
        c.gridx = 1; c.gridy = 0; c.weightx = 1; c.insets=new Insets(0,10,0,17);
        c.fill=GridBagConstraints.HORIZONTAL;
        for (int i = 0;i<scrollBarNames.length;i++) {
            JSlider sb = (JSlider) scrollBarHash.get(scrollBarNames[i]);
              if(sb==null) continue;
              if(i!=0) sb.setVisible(false);
              //sb.setMinimumSize(new Dimension(200,17));
              sbp.add(sb,c);
        }
        return sbp;
    }

    public void addUIs() {
        tgUIManager = new TGUIManager();
        OLMNavigateUI navigateUI = new OLMNavigateUI(this);
        //GLEditUI navigateUI = new GLEditUI(tgPanel);

        tgUIManager.addUI(navigateUI,STR_NAVIGATE);
        tgUIManager.activate(STR_NAVIGATE);
    }

    public void initGraph() throws TGException
    {
        tgPanel.clearAll();

        try 
        {
            // Build the top node ("Belief")
            OLMNode node = new OLMNode(STR_USER);
            setNodeAttribute(node,STR_USER);
            tgPanel.addNode(node);
            NodeConfig.TOPNODE.setConfig(node);
            node.setImage(getUserImage());
            //node.setHint("That's you, Geezer!");
        } 
        catch (TGException e1) {e1.printStackTrace();}
        //Node tt = tgPanel.findNode(ID_USERNODE);
        //tgPanel.setSelect(tt);
        //tgPanel.repaintAfterMove();
        //tgPanel.selectFirstNode();
    }
    
    protected OLMNode getDescriptorNode(BeliefDesc desc,String key)
    {
        OLMNode belief = new OLMNode(desc.toString(),desc.toString());
        // retrieve and format the description of the descriptor
        String str = Messages.getDescriptionTemplate(desc);
        Object[] args = desc.getArgs();
        for (int i=0;i<args.length;i++)
        {
            if (args[i]!=null)
                args[i] = "<b>" + args[i] + "</b>";
        }
        str = MessageFormat.format(str,args);            
        Object arg[]=
        {
        		Messages.getLayerOn(desc),
        		desc.toString(),
        		str
        };
        
        String template = Messages.getString(key + ".Hint");
        String hint = MessageFormat.format(template,arg);
        belief.setHint(hint);

        return belief;
       
    }
    
    /**
     * @param beliefdesc    The belief descriptor.
     * @param lvl           The level the learner is assumed to be.
     * 
     * @todo I'm relying on the exception to detect existence of node; it would be better 
     *       to do it manually.
     */
    public void addBelief(BeliefDesc beliefdesc,int lvl)
    {
        try 
        {
            // Build the node for the belief
//            OLMNode node = new OLMNode(beliefdesc.toString(),beliefdesc.toString());
//            NodeConfig.BELIEF.setConfig(node);
//            node.setImage(getLevelImage(lvl));
//
//            // retrieve and format the description of the descriptor
//            String str = Messages.getDescriptionTemplate(beliefdesc);
//            Object[] args = beliefdesc.getArgs();
//            for (int i=0;i<args.length;i++)
//            {
//                if (args[i]!=null)
//                    args[i] = "<b>" + args[i] + "</b>";
//            }
//            str = MessageFormat.format(str,args);            
//            Object arg[]=
//            {
//            		Messages.getLayerOn(beliefdesc),
//            		beliefdesc.toString(),
//            		str
//            };
//            
//            String template = Messages.getString("OLMGraphBrowser.Topics.Descriptor.Hint");
//            String hint = MessageFormat.format(template,arg);
//            node.setHint(hint);
        	OLMNode node = getDescriptorNode(beliefdesc,"OLMGraphBrowser.Topics.Descriptor");
        	NodeConfig.BELIEF.setConfig(node);
        	node.setImage(getLevelImage(lvl));
        	node.setVisible(true);
            
            //completeEltSet.addNode(node);
            tgPanel.addNode(node);
            
            // Build the link to all related topics
            for (Iterator e=beliefdesc.iterator();e.hasNext();)
            {
                String topic = (String)e.next();
                Node org = tgPanel.findNode(topic);
                //Node org = completeEltSet.findNode(topic);
                
                if (org!=null)
                {
                    OLMNode assoc = new OLMNode(null);
                    setNodeAttribute(assoc,STR_NODEON);
                    assoc.setVisible(true);
                    NodeConfig.BELIEFASSOC.setConfig(assoc);
                    tgPanel.addNode(assoc);
                    //completeEltSet.addNode(assoc);

                    tgPanel.addEdge(new OLMEdge(node,assoc,Edge.DEFAULT_LENGTH));
                    tgPanel.addEdge(new OLMEdge(assoc,org,Edge.DEFAULT_LENGTH));

                    tgPanel.expandNode(assoc);
                    //Edge edge;
                    //completeEltSet.addEdge(edge = new OLMEdge(node,assoc,Edge.DEFAULT_LENGTH));
                    //edge.setVisible(false);
                    //completeEltSet.addEdge(edge = new OLMEdge(assoc,org,Edge.DEFAULT_LENGTH));
                    //edge.setVisible(false);
                }
               
            }
            
            // Build the link to the top node
            //Node org = tgPanel.findNode(ID_USERNODE);
            Node org = completeEltSet.findNode(STR_USER);
            if (org!=null)
            {
                OLMNode assoc = new OLMNode(null);
                setNodeAttribute(assoc,STR_NODEABOUT);
                assoc.setVisible(true);
                NodeConfig.BELIEFASSOC.setConfig(assoc);
                tgPanel.addNode(assoc);
                //completeEltSet.addNode(assoc);
                
                tgPanel.addEdge(new OLMEdge(assoc,org,Edge.DEFAULT_LENGTH));
                tgPanel.addEdge(new OLMEdge(node,assoc,Edge.DEFAULT_LENGTH));
                //Edge edge;
                //completeEltSet.addEdge(edge = new OLMEdge(org,assoc,Edge.DEFAULT_LENGTH));
                //edge.setVisible(false);
                //completeEltSet.addEdge(edge = new OLMEdge(assoc,node,Edge.DEFAULT_LENGTH));
                //edge.setVisible(false);
                //tgPanel.setSelect(org);
                //tgPanel.setLocale(org,2);
                
                //tgPanel.updateLocalityFromVisibility();
                //tgPanel.repaintAfterMove();

            }
            //tgPanel.setSelect(node);
           // updateGraph();

        } 
        catch (TGException e1) 
        {
        	System.err.println("The node " + beliefdesc + " is already in the graph.");//$NON-NLS-1$ //$NON-NLS-2$ 
            //e1.printStackTrace();
        }
       
    }
    
    public void setTopicMap(Hashtable htab,String mapname)
    {
        Vector vecnode = (Vector)htab.get(OLMQueryResult.CAT_NODES);
        Vector vecedge = (Vector)htab.get(OLMQueryResult.CAT_EDGES);
        String root = (String)htab.get(OLMQueryResult.CAT_ROOT);

        OLMTopicConfig cfg= OLMTopicConfig.getByName(mapname);

        // Set the nodes
        for (Iterator e=vecnode.iterator();e.hasNext();)
        {
            Vector nodev = (Vector)e.next();
            String nodeId = (String)nodev.get(0);
            Integer nodetype = (Integer)nodev.get(1);
            String nodeTitled = (String)nodev.get(2);
            String nodeDescd = (String)nodev.get(3);
            
            String nodeTitle = TopicMaps.getTitle(cfg,nodeId);
            String nodeDesc = TopicMaps.getDescription(cfg,nodeId);
            if (nodeTitle==null)
            	nodeTitle = nodeTitled;
            if (nodeDesc==null)
            	nodeDesc = nodeDescd;
           
            try 
            {
                
                if (nodeTitle.equals("")) //$NON-NLS-1$
                        nodeTitle = nodeId;
                
                // tricks for the generated map
                //if (nodeTitle.startsWith("ABSTRACTLY")) //$NON-NLS-1$
                //    nodeTitle = "is a";
                //if (nodeTitle.startsWith("educational")) //$NON-NLS-1$
                //    nodeTitle = "depends";

                OLMNode node = new OLMNode(nodeId,nodeTitle);
                node.setVisible(false);
                //tgPanel.addNode(node);
                completeEltSet.addNode(node);
                //tgPanel.stopDamper();
                
                if (nodetype.intValue()!=-1)
                {
                    NodeConfig.TOPICASSOC.setConfig(node,cfg);
                    //node.setVisible(false);
                }
                else if (nodeId.equals(root))
                {
                    NodeConfig.TOPICROOT.setConfig(node,cfg);
                    node.setVisible(true);
                }
                else
                {
                    NodeConfig.TOPIC.setConfig(node,cfg);
                    //node.setVisible(false);
                }
                if (nodeDesc!=null && !nodeDesc.equals(""))
                {
                    node.setHint(new String(nodeDesc));
                }
            } 
            catch (TGException e1) {e1.printStackTrace();}
            
        }
        
        // Set the edges
        for (Iterator e=vecedge.iterator();e.hasNext();)
        {
            Vector vec = (Vector)e.next();
            if (vec.size()<=1) continue;
            
            
            Node org = completeEltSet.findNode((String)vec.get(0));
            //Node org = tgPanel.findNode((String)vec.get(0));
            if (org==null) continue;
            
            for (Iterator e2=vec.iterator();e2.hasNext();)
            {
                String id = (String)e2.next();
                ///Node des = tgPanel.findNode(id);
                Node des = completeEltSet.findNode(id);
                if (des!=null)
                {
                    Edge edge;
                    completeEltSet.addEdge(edge = new OLMEdge(org,des,Edge.DEFAULT_LENGTH));
                    //edge.setVisible(false);
                    //tgPanel.addEdge(new OLMEdge(org,des,Edge.DEFAULT_LENGTH));
                }
            }
            
        }
        
        // Set the topnode
        //Node topnode = tgPanel.findNode(root);
        Node topnode = completeEltSet.findNode(root);
        //tgPanel.collapseNode(topnode);
        
        // Build the link to the top node
        //Node org = tgPanel.findNode(ID_USERNODE);
        Node org = completeEltSet.findNode(STR_USER);
        if (org!=null)
        {
            try 
            {
                OLMNode assoc = new OLMNode(STR_NODEMAP+mapname);
                setNodeAttribute(assoc,STR_NODEMAP);
                assoc.setVisible(false);
                NodeConfig.BELIEFASSOC.setConfig(assoc);
                //tgPanel.addNode(assoc);
                completeEltSet.addNode(assoc);
                
                //tgPanel.addEdge(new OLMEdge(org,assoc,Edge.DEFAULT_LENGTH));
                //tgPanel.addEdge(new OLMEdge(assoc,topnode,Edge.DEFAULT_LENGTH));
                Edge edge;
                completeEltSet.addEdge(edge = new OLMEdge(org,assoc,Edge.DEFAULT_LENGTH));
                //edge.setVisible(false);
                completeEltSet.addEdge(edge = new OLMEdge(assoc,topnode,Edge.DEFAULT_LENGTH));
                //edge.setVisible(true);
                //tgPanel.selectFirstNode();
                //tgPanel.setLocale(org,2);
                //tgPanel.updateLocalityFromVisibility();
                
//                tgPanel.setSelect(org);
//                tgPanel.setLocale(org,2);
//                tgPanel.updateLocalityFromVisibility();
//                tgPanel.repaintAfterMove();

            } 
            catch (TGException e) {e.printStackTrace();}
        }
    }

    public void updateGraph() 
    {
        Node org = completeEltSet.findNode(STR_USER);
        //if (org==null) return;
        try 
        {
            tgPanel.updateLocalityFromVisibility();
            
            if (tgPanel.getSelect()==null && org!=null)
            {
                tgPanel.setSelect(org);
                tgPanel.setLocale(org,getLocalityRadius());
                //hvScroll.slowScrollToCenter(org);
                tgPanel.repaintAfterMove();

            }
            else
            {
                tgPanel.expandNode(tgPanel.getSelect());
                //tgPanel.setSelect(org);
                //tgPanel.setLocale(tgPanel.getSelect(),getLocalityRadius());
                tgPanel.repaintAfterMove();
            }
        } 
        catch (TGException e) 
        {
            e.printStackTrace();
        }
    }

    public void showControlPanel(boolean showPanel) 
    {
        if (topPanel!=null)
            topPanel.setVisible(showPanel);
    }

//    /**
//     * @return Returns the topPanel.
//     */
//    public JPanel getTopPanel() 
//    {
//        if (topPanel==null)
//            topPanel = new JPanel();
//        return topPanel;
//    }

    public void setEnabled(boolean enabled) 
    {
       // TODO Auto-generated method stub
       super.setEnabled(enabled);
       tgPanel.setEnabled(enabled);
       zoomScroll.getZoomSB().setEnabled(enabled);
       hyperScroll.getHyperSB().setEnabled(enabled);
       hvScroll.getHorizontalSB().setEnabled(enabled);
       hvScroll.getVerticalSB().setEnabled(enabled);
       hvScroll.getVerticalSB().setEnabled(enabled);
       rotateScroll.getRotateSB().setEnabled(enabled);
       localityScroll.getLocalitySB().setEnabled(enabled);
       if (hintBtn!=null)        
           hintBtn.getTimer().stop();

       tgPanel.repaint();
   }

    
}