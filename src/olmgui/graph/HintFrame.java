package olmgui.graph;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;

/**
 * @author vanlabeke
 * @version $Revision: 1.3 $
 * @deprecated NOT IN USE ANYMORE
 */
public class HintFrame extends JInternalFrame
{

    private JPanel jContentPane = null;

    public HintFrame() 
    {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(300, 200);
        //this.setClosed(false);
        this.setClosable(true);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() 
    {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }

}
