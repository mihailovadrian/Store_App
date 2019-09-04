package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import app.AppContext;

import threads.ProcessProductRequestsThread;
import tools.ConnectionTools;
import tools.GridPanel;

import tools.MutableBoolean;

public class MainFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private AppContext context;
	private MutableBoolean stop;
	private ProcessProductRequestsThread thread;

	public MainFrame()
	{
		super();
		SetupGui();
	}

	private void SetupGui( )
	{
		setTitle("Store server");

		context = new AppContext(ConnectionTools.openConnection("192.168.200.112:1433;instanceName=meseriashu", "Adrian", "sa", "Meseriashu10"));

		stop = new MutableBoolean(false);
		thread = null;

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(this);
		btnStop.setActionCommand("STOP");

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStart.setActionCommand("START");

		GridPanel panel = new GridPanel(0, 0, 0, 0);
		panel.addObject(0, 0, 1.0, 0.0, 1, 1, btnStart);
		panel.addObject(1, 0, 1.0, 0.0, 1, 1, btnStop);

		setContentPane(panel);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("START"))
		{
			if (thread == null)
			{
				stop.setValue(false);

				thread = new ProcessProductRequestsThread(context, stop);
				thread.start();
			}
		}
		if (command.equalsIgnoreCase("STOP"))
		{
			if (thread != null)
			{
				stop.setValue(true);
				thread = null;
			}
		}
	}
}
