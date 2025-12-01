package UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardJumpButton extends JButton
{
	private ActionListener listioner;
	private String jumpName;
	public CardJumpButton(String jumpName, Object... args)
	{
		this.jumpName = jumpName;

		changeArgs(args);
	}

	public void changeArgs(Object... args)
	{
		if(listioner != null) removeActionListener(listioner);

		listioner = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Container ancestor = getParent();
				Container cardContainer = null;
				while (ancestor != null)
				{
					LayoutManager lm = ancestor.getLayout();
					if (lm instanceof CardLayout && cardContainer == null)
						cardContainer = ancestor;
					ancestor = ancestor.getParent();
				}

				if (cardContainer != null)
				{
					LayoutManager layout = cardContainer.getLayout();
					for (Component comp : cardContainer.getComponents())
					{
						if (comp instanceof CardJumpPanel)
						{
							CardJumpPanel panel = (CardJumpPanel) comp;
							if (jumpName.equals(panel.getName()))
								((Jumpable) panel).jumpLoad(args);
						}
					}

					((CardLayout) layout).show(cardContainer, jumpName);
				}
				else
					throw new WrongParentConditionException("CardJumpButton must be placed inside a container using CardLayout.");
			}
		};

		addActionListener(listioner);
	}
	
	public class WrongParentConditionException extends RuntimeException 
	{
		public WrongParentConditionException(String msg)
		{ super(msg); }
	}
}