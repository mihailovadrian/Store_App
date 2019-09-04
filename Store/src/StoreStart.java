import java.awt.*;
import javax.swing.*;

import frames.*;

public class StoreStart
{
	public static void main( String[] args )
	{
		JFrame mainFrame = new MainFrame();
		if (mainFrame != null)
		{
			mainFrame.setLocation(100, 100);
			mainFrame.setSize(new Dimension(800, 600));
			mainFrame.setMinimumSize(new Dimension(400, 300));
			mainFrame.setPreferredSize(new Dimension(400, 300));

			Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (screen_size.width - mainFrame.getWidth()) / 2;
			int y = (screen_size.height - mainFrame.getHeight()) / 2;

			mainFrame.setLocation(x, y);
			mainFrame.setVisible(true);
		}
	}
}
