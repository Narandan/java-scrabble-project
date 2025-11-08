package UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardJumpButton extends JButton
{
	public CardJumpButton(String jumpName)
	{
		addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Container parent = getParent();
				Container grandParent = parent.getParent();
				LayoutManager layout = grandParent.getLayout();
				if(grandParent != null && layout instanceof CardLayout && parent instanceof CardJumpPanel)
				{
					((CardJumpPanel) parent).jumpLoad();
					((CardLayout)layout).show(grandParent, jumpName);
				}
				else
				{
					throw new WrongParentConditionException("CardJumpButton parent must implement CardJumpPanel and CardJumpButton grandparent must not be null and have a LayoutManager of type CardLayout!");
				}
			}
		});
	}
	
	public class WrongParentConditionException extends RuntimeException 
	{
		public WrongParentConditionException(String msg)
		{
			super(msg);
		}
	}
}