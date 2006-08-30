package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import com.sun.java.swing.SwingUtilities2;

public class HintInternalFrameTitlePane extends BasicInternalFrameTitlePane {
	private Color selectedTitleGradientColor;
	private Color notSelectedTitleGradientColor;
	private JPopupMenu systemPopupMenu;
	//private JLabel systemLabel;

	private Font titleFont;
	private int titlePaneHeight;
	private int buttonWidth, buttonHeight;

	public HintInternalFrameTitlePane(JInternalFrame f) {
		super(f);
	}

	protected void addSubComponents() {
		//add(systemLabel);
		add(iconButton);
		add(maxButton);
		add(closeButton);
	}

	protected void installDefaults() {
		super.installDefaults();

        closeIcon = new FrameButtonIcon("window.closebutton");


		Font ff = getFont();
		setFont(new Font(ff.getFontName(),Font.BOLD,10));
		titlePaneHeight = UIManager.getInt("InternalFrame.titlePaneHeight");
		buttonWidth     = UIManager.getInt("InternalFrame.titleButtonWidth")  - 4;
		buttonHeight    = UIManager.getInt("InternalFrame.titleButtonHeight") - 4;
		titlePaneHeight -= 4;
		buttonHeight -=4;
		
//		if (XPStyle.getXP() != null)
		{
			// Fix for XP bug where sometimes these sizes aren't updated properly
			// Assume for now that XP buttons are always square
			buttonWidth = buttonHeight;
		} 
//		else 
//		{
//			buttonWidth += 2;
			selectedTitleGradientColor =
				UIManager.getColor("InternalFrame.activeTitleGradient");
			notSelectedTitleGradientColor =
				UIManager.getColor("InternalFrame.inactiveTitleGradient");
			Color activeBorderColor =
				UIManager.getColor("InternalFrame.activeBorderColor");
			//setBorder(BorderFactory.createLineBorder(activeBorderColor, 1));
			setBorder(UIManager.getBorder("InternalFrame.border"));
//		}
	}

	protected void uninstallListeners() {
		// Get around protected method in superclass
		super.uninstallListeners();
	}

	protected void createButtons() {
		super.createButtons();
//		if (XPStyle.getXP() != null) 
		{
			iconButton.setContentAreaFilled(false);
			maxButton.setContentAreaFilled(false);
			closeButton.setContentAreaFilled(true);
            closeButton.setBorder(BorderFactory.createEmptyBorder());
		}
	}
		
		static boolean isLeftToRight(Component c) {
	        return c.getComponentOrientation().isLeftToRight();
	    }

	public void paintComponent(Graphics g)  {
//		XPStyle xp = XPStyle.getXP();

		paintTitleBackground(g);

		String title = frame.getTitle();
		if (title != null) {
			boolean isSelected = frame.isSelected();
			Font oldFont = g.getFont();
			Font newFont = (titleFont != null) ? titleFont : getFont();
			g.setFont(newFont);

			// Center text vertically.
			FontMetrics fm = SwingUtilities2.getFontMetrics(frame, g, newFont);
			int baseline = (getHeight() + fm.getAscent() - fm.getLeading() -
					fm.getDescent()) / 2;

			int titleX;
			Rectangle r = new Rectangle(0, 0, 0, 0);
			if (frame.isIconifiable())  r = iconButton.getBounds();
			else if (frame.isMaximizable())  r = maxButton.getBounds();
			else if (frame.isClosable())  r = closeButton.getBounds();
			int titleW;

			if(isLeftToRight(frame) ) {
				if (r.x == 0)  r.x = frame.getWidth()-frame.getInsets().right;
				titleX = /*systemLabel.getX() + systemLabel.getWidth() +*/ 2;
//				if (xp != null) {
					titleX += 2;
//				}
				titleW = r.x - titleX - 3;
				title = getTitle(frame.getTitle(), fm, titleW);
			} else {
				titleX = /*systemLabel.getX() */- 2
				- SwingUtilities2.stringWidth(frame,fm,title);
			}
//			if (xp != null) 
//			{
//				String shadowType = null;
//				if (isSelected) {
//					shadowType = xp.getString(this, "window.caption", "active", "textshadowtype");
//				}
//				if ("single".equalsIgnoreCase(shadowType)) 
//				{
//					Point shadowOffset = xp.getPoint(this, "window", "active", "textshadowoffset");
//					Color shadowColor  = xp.getColor(this, "window", "active", "textshadowcolor", null);
//					if (shadowOffset != null && shadowColor != null) 
//					{
//						g.setColor(shadowColor);
//						SwingUtilities2.drawString(frame, g, title,
//								titleX + shadowOffset.x,
//								baseline + shadowOffset.y);
//					}
//				}
//			}
			g.setColor(isSelected ? selectedTextColor : notSelectedTextColor);
			SwingUtilities2.drawString(frame, g, title, titleX, baseline);
			g.setFont(oldFont);
		}
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		Dimension d = new Dimension(super.getMinimumSize());
		d.height = titlePaneHeight + 2;

//		XPStyle xp = XPStyle.getXP();
//		if (xp != null) {
			// Note: Don't know how to calculate height on XP,
			// the captionbarheight is 25 but native caption is 30 (maximized 26)
			if (frame.isMaximum()) {
				d.height -= 1;
			} else {
				d.height += 3;
			}
//		}
		return d;
	}

	protected void paintTitleBackground(Graphics g) {
//		XPStyle xp = XPStyle.getXP();
//		if (xp != null) {
//			XPStyle.Skin skin = xp.getSkin(this, frame.isIcon() ? "window.mincaption"
//					: (frame.isMaximum() ? "window.maxcaption"
//							: "window.caption"));
//
//			skin.paintSkin(g, 0,  0, getWidth(), getHeight(), frame.isSelected() ? 0 : 1);
//		} else {
			Boolean gradientsOn = (Boolean)LookAndFeel.getDesktopPropertyValue(
					"win.frame.captionGradientsOn", Boolean.valueOf(false));
			if (gradientsOn.booleanValue() && g instanceof Graphics2D) {
				Graphics2D g2 = (Graphics2D)g;
				Paint savePaint = g2.getPaint();

				boolean isSelected = frame.isSelected();
				int w = getWidth();

				if (isSelected) {
					GradientPaint titleGradient = new GradientPaint(0,0, 
							selectedTitleColor,
							(int)(w*.75),0, 
							selectedTitleGradientColor);
					g2.setPaint(titleGradient);
				} else {
					GradientPaint titleGradient = new GradientPaint(0,0, 
							notSelectedTitleColor,
							(int)(w*.75),0, 
							notSelectedTitleGradientColor);   
					g2.setPaint(titleGradient);
				}
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setPaint(savePaint);
			} else {
				super.paintTitleBackground(g);
			}
//		}
	}

	protected void assembleSystemMenu() {
//		systemPopupMenu = new JPopupMenu();
//		addSystemMenuItems(systemPopupMenu);
//		enableActions();
//		systemLabel = new JLabel(frame.getFrameIcon()) {
//			protected void paintComponent(Graphics g) {
//				int x = 0;
//				int y = 0;
//				int w = getWidth();
//				int h = getHeight();
//				g = g.create();  // Create scratch graphics
//				if (isOpaque()) {
//					g.setColor(getBackground());
//					g.fillRect(0, 0, w, h);
//				}
//				Icon icon = getIcon();
//				int iconWidth = 0;
//				int iconHeight = 0;
//				if (icon != null &&
//						(iconWidth = icon.getIconWidth()) > 0 &&
//						(iconHeight = icon.getIconHeight()) > 0) {
//
//					// Set drawing scale to make icon scale to our desired size
//					double drawScale;
//					if (iconWidth > iconHeight) {
//						// Center icon vertically
//						y = (h - w*iconHeight/iconWidth) / 2;
//						drawScale = w / (double)iconWidth;
//					} else {
//						// Center icon horizontally
//						x = (w - h*iconWidth/iconHeight) / 2;
//						drawScale = h / (double)iconHeight;
//					}
//					((Graphics2D)g).translate(x, y);
//					((Graphics2D)g).scale(drawScale, drawScale);
//					icon.paintIcon(this, g, 0, 0);
//				}
//				g.dispose();
//			}
//		};
//		systemLabel.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				showSystemPopupMenu(e.getComponent());
//			}
//		});
	}

	protected void addSystemMenuItems(JPopupMenu menu) {
		JMenuItem mi = (JMenuItem)menu.add(restoreAction);
		mi.setMnemonic('R');
		mi = (JMenuItem)menu.add(moveAction);
		mi.setMnemonic('M');
		mi = (JMenuItem)menu.add(sizeAction);
		mi.setMnemonic('S');
		mi = (JMenuItem)menu.add(iconifyAction);
		mi.setMnemonic('n');
		mi = (JMenuItem)menu.add(maximizeAction);
		mi.setMnemonic('x');
		systemPopupMenu.add(new JSeparator());
		mi = (JMenuItem)menu.add(closeAction);
		mi.setMnemonic('C');
	}

	protected void showSystemMenu(){
		//showSystemPopupMenu(systemLabel);
	}

	private void showSystemPopupMenu(Component invoker){
		Dimension dim = new Dimension();
		Border border = frame.getBorder();
		if (border != null) {
			dim.width += border.getBorderInsets(frame).left +
			border.getBorderInsets(frame).right;
			dim.height += border.getBorderInsets(frame).bottom +
			border.getBorderInsets(frame).top;
		}
		if (!frame.isIcon()) {
			systemPopupMenu.show(invoker,
					getX() - dim.width,
					getY() + getHeight() - dim.height);
		} else {
			systemPopupMenu.show(invoker,
					getX() - dim.width,
					getY() - systemPopupMenu.getPreferredSize().height -
					dim.height);
		}
	}

	protected PropertyChangeListener createPropertyChangeListener() {
		return new WindowsPropertyChangeHandler();
	}

	protected LayoutManager createLayout() {
		return new WindowsTitlePaneLayout();
	}


	private static class FrameButtonIcon implements Icon, Serializable {
	    private String category;

	    private FrameButtonIcon(String category) {
	        this.category = category;
	    }

	    public void paintIcon(Component c, Graphics g, int x0, int y0) {
	        int width = getIconWidth();
	        int height = getIconHeight();

//	        XPStyle xp = XPStyle.getXP();
//	        if (xp != null) 
//            {
//	            XPStyle.Skin skin = xp.getSkin(c, category);
//	            JButton b = (JButton)c;
//	            ButtonModel model = b.getModel();
//	            int index = 0;
//	            if (!model.isEnabled()) 
//	            {
//	                index = 3;
//	            } else if (model.isArmed() && model.isPressed()) 
//                {
//	                index = 2;
//	            } else if (model.isRollover())
//                {
//	                index = 1;
//	            }
//	            // Find out if frame is inactive
//	            JInternalFrame jif = (JInternalFrame)SwingUtilities.
//	            getAncestorOfClass(JInternalFrame.class, b);
//	            if (jif != null && !jif.isSelected())
//                {
//	                index += 4;
//	            }
//	            skin.paintSkin(g, 0, 0, width, height, index);
//	        } else 
            {
	            g.setColor(Color.black);
	            int x = width / 12 + 2;
	            int y = height / 5;
	            int h = height - y * 2 - 1;
	            int w = width * 3/4 -3;
	            int thickness2 = Math.max(height / 8, 2);
	            int thickness  = Math.max(width / 15, 1);
	            if (category == "window.closebutton") {
	                int lineWidth;
	                if      (width > 47) lineWidth = 6;
	                else if (width > 37) lineWidth = 5;
	                else if (width > 26) lineWidth = 4;
	                else if (width > 16) lineWidth = 3;
	                else if (width > 12) lineWidth = 2;
	                else                 lineWidth = 1;
	                y = height / 12 + 2;
	                if (lineWidth == 1) {
	                    if (w % 2 == 1) { x++; w++; }
	                    g.drawLine(x,     y, x+w-2, y+w-2);
	                    g.drawLine(x+w-2, y, x,     y+w-2);
	                } else if (lineWidth == 2) {
	                    if (w > 6) { x++; w--; }
	                    g.drawLine(x,     y, x+w-2, y+w-2);
	                    g.drawLine(x+w-2, y, x,     y+w-2);
	                    g.drawLine(x+1,   y, x+w-1, y+w-2);
	                    g.drawLine(x+w-1, y, x+1,   y+w-2);
	                } else {
	                    x += 2; y++; w -= 2;
	                    g.drawLine(x,     y,   x+w-1, y+w-1);
	                    g.drawLine(x+w-1, y,   x,     y+w-1);
	                    g.drawLine(x+1,   y,   x+w-1, y+w-2);
	                    g.drawLine(x+w-2, y,   x,     y+w-2);
	                    g.drawLine(x,     y+1, x+w-2, y+w-1);
	                    g.drawLine(x+w-1, y+1, x+1,   y+w-1);
	                    for (int i = 4; i <= lineWidth; i++) {
	                        g.drawLine(x+i-2,   y,     x+w-1,   y+w-i+1);
	                        g.drawLine(x,       y+i-2, x+w-i+1, y+w-1);
	                        g.drawLine(x+w-i+1, y,     x,       y+w-i+1);
	                        g.drawLine(x+w-1,   y+i-2, x+i-2,   y+w-1);
	                    }
	                }
	            } else if (category == "window.minbutton") {
	                g.fillRect(x, y+h-thickness2, w-w/3, thickness2);
	            } else if (category == "window.maxbutton") {
	                g.fillRect(x, y, w, thickness2);
	                g.fillRect(x, y, thickness, h);
	                g.fillRect(x+w-thickness, y, thickness, h);
	                g.fillRect(x, y+h-thickness, w, thickness);
	            } else if (category == "window.restorebutton") {
	                g.fillRect(x+w/3, y, w-w/3, thickness2);
	                g.fillRect(x+w/3, y, thickness, h/3);
	                g.fillRect(x+w-thickness, y, thickness, h-h/3);
	                g.fillRect(x+w-w/3, y+h-h/3-thickness, w/3, thickness);

	                g.fillRect(x, y+h/3, w-w/3, thickness2);
	                g.fillRect(x, y+h/3, thickness, h-h/3);
	                g.fillRect(x+w-w/3-thickness, y+h/3, thickness, h-h/3);
	                g.fillRect(x, y+h-thickness, w-w/3, thickness);
	            }
	        }
	    }

        public int getIconWidth() {
            int width;
//            if (XPStyle.getXP() != null) {
//            // Fix for XP bug where sometimes these sizes aren't updated properly
//            // Assume for now that XP buttons are always square
//            width = UIManager.getInt("InternalFrame.titleButtonHeight") -2-4;
//            } else 
            {
            width = UIManager.getInt("InternalFrame.titleButtonWidth") -2-4;
            }
//            if (XPStyle.getXP() != null) {
            width -= 2;
//            }
            return width;
        }

        public int getIconHeight() {
            int height = UIManager.getInt("InternalFrame.titleButtonHeight")-4-4;
            return height;
        }
	}



	public class WindowsTitlePaneLayout extends BasicInternalFrameTitlePane.TitlePaneLayout {
		private Insets captionMargin = null;
		private Insets contentMargin = null;
//		private XPStyle xp = XPStyle.getXP();

		WindowsTitlePaneLayout() {
//			if (xp != null) {
//				Component c = WindowsInternalFrameTitlePane.this;
//				captionMargin = xp.getMargin(c, "window.caption", null, "captionmargins");
//				contentMargin = xp.getMargin(c, "window.caption", null, "contentmargins");
//			}
			if (captionMargin == null) {
				captionMargin = new Insets(0, 2, 0, 2);
			}
			if (contentMargin == null) {
				contentMargin = new Insets(0, 0, 0, 0);
			}
		}

		private int layoutButton(JComponent button, String category,
				int x, int y, int w, int h, int gap,
				boolean leftToRight) {
			if (!leftToRight) {
				x -= w;
			}
			button.setBounds(x, y, w, h);
			if (leftToRight) {
				x += w + 2;
			} else {
				x -= 2;
			}
			return x;
		}

		public void layoutContainer(Container c) {
			boolean leftToRight = isLeftToRight(frame);
			int x, y;
			int w = getWidth();
			int h = getHeight();

			// System button
			// Note: this icon is square, but the buttons aren't always.
			//int iconSize = (xp != null) ? (h-2)*6/10 : h-4;
			int iconSize = (h-2)*6/10;
//			if (xp != null) {
				x = (leftToRight) ? captionMargin.left + 2 : w - captionMargin.right - 2;
//			} else {
//				x = (leftToRight) ? captionMargin.left : w - captionMargin.right;
//			}
			y = (h - iconSize) / 2;
//			layoutButton(systemLabel, "window.sysbutton",
//					x, y, iconSize, iconSize, 0,
//					leftToRight);

			// Right hand buttons
//			if (xp != null) {
				x = (leftToRight) ? w - captionMargin.right - 2 : captionMargin.left + 2;
				y = 0;	// XP seems to ignore margins and offset here
				if (frame.isMaximum()) {
					y += 1;
				} else {
					y += 5;
				}
//			} else {
//				x = (leftToRight) ? w - captionMargin.right : captionMargin.left;
//				y = (h - buttonHeight) / 2;
//			}

			if(frame.isClosable()) {
				x = layoutButton(closeButton, "window.closebutton",
						x, y, buttonWidth, buttonHeight, 2,
						!leftToRight);
			} 

			if(frame.isMaximizable()) {
				x = layoutButton(maxButton, "window.maxbutton",
//						x, y, buttonWidth, buttonHeight, (xp != null) ? 2 : 0,
						x, y, buttonWidth, buttonHeight, 2 ,
								!leftToRight);
			}

			if(frame.isIconifiable()) {
				layoutButton(iconButton, "window.minbutton",
						x, y, buttonWidth, buttonHeight, 0,
						!leftToRight);
			} 
		}
	} // end WindowsTitlePaneLayout

	public class WindowsPropertyChangeHandler extends PropertyChangeHandler {
		public void propertyChange(PropertyChangeEvent evt) {
			String prop = (String)evt.getPropertyName();

			// Update the internal frame icon for the system menu.
//			if (JInternalFrame.FRAME_ICON_PROPERTY.equals(prop) &&
//					systemLabel != null) {
//				systemLabel.setIcon(frame.getFrameIcon());
//			}

			super.propertyChange(evt);
		}
	}

	/**
	 * A versatile Icon implementation which can take an array of Icon
	 * instances (typically <code>ImageIcon</code>s) and choose one that gives the best
	 * quality for a given Graphics2D scale factor when painting.
	 * <p>
	 * The class is public so it can be instantiated by UIDefaults.ProxyLazyValue.
	 * <p>
	 * Note: We assume here that icons are square.
	 */
	public static class ScalableIconUIResource implements Icon, UIResource {
		// We can use an arbitrary size here because we scale to it in paintIcon()
		private static final int SIZE = 12;

		private Icon[] icons;

		/**
		 * @params objects an array of Icon or UIDefaults.LazyValue
		 * <p>
		 * The constructor is public so it can be called by UIDefaults.ProxyLazyValue.
		 */
		public ScalableIconUIResource(Object[] objects) {
			this.icons = new Icon[objects.length];

			for (int i = 0; i < objects.length; i++) {
				if (objects[i] instanceof UIDefaults.LazyValue) {
					icons[i] = (Icon)((UIDefaults.LazyValue)objects[i]).createValue(null);
				} else {
					icons[i] = (Icon)objects[i];
				}
			}	    
		}

		/**
		 * @return the <code>Icon</code> closest to the requested size
		 */
		protected Icon getBestIcon(int size) {
			if (icons != null && icons.length > 0) {
				int bestIndex = 0;
				int minDiff = Integer.MAX_VALUE;
				for (int i=0; i < icons.length; i++) {
					Icon icon = icons[i];
					int iconSize;
					if (icon != null && (iconSize = icon.getIconWidth()) > 0) {
						int diff = Math.abs(iconSize - size);
						if (diff < minDiff) {
							minDiff = diff;
							bestIndex = i;
						}
					}
				}
				return icons[bestIndex];
			} else {
				return null;
			}
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D)g.create();
			// Calculate how big our drawing area is in pixels
			// Assume we are square
			int size = getIconWidth();
			double scale = g2d.getTransform().getScaleX();
			Icon icon = getBestIcon((int)(size * scale));
			int iconSize;
			if (icon != null && (iconSize = icon.getIconWidth()) > 0) {
				// Set drawing scale to make icon act true to our reported size
				double drawScale = size / (double)iconSize;
				g2d.translate(x, y);
				g2d.scale(drawScale, drawScale);
				icon.paintIcon(c, g2d, 0, 0);
			}
			g2d.dispose();
		}

		public int getIconWidth() {
			return SIZE;
		}

		public int getIconHeight() {
			return SIZE;
		}
	}
}
