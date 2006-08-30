package olmgui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JApplet;

public class OLMApplet  extends JApplet 
{
    public void init() 
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new OLMGUI(this), BorderLayout.CENTER);
        setPreferredSize(new Dimension(800,600));
        setSize(new Dimension(800,600));

   }

}
