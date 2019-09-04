package threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import app.*;
import dbTools.*;
import entities.*;
import tools.*;

public class RequestProductThread extends Thread
{
	private static final long SLEEP_BETWEEN_CHECKS = 1000;

	private ProductRequest productRequest;
	private AppContext context;
	private MutableBoolean stop;
	private ActionListener listener;

	public RequestProductThread( ActionListener listener, ProductRequest productRequest, AppContext context, MutableBoolean stop )
	{
		super();

		this.listener = listener;
		this.productRequest = productRequest;
		this.context = context;
		this.stop = stop;

	}

	public void run( )
	{
		boolean finished = false;

		ProductRequest pr = null;

		while (!finished)
		{
			pr = DBOperationsProductRequest.getProductRequest(context, productRequest.getId());
			if (pr.getStatus() == ProductRequest.ALLOCATED)
			{
				productRequest.setStatus(pr.getStatus());
				productRequest.setQuantityAllocated(pr.getQuantityAllocated());

				finished = true;
				if(listener!=null)
				{
					listener.actionPerformed(new ActionEvent(this, 0, "OKEY"));
				}
				System.out.println("Product Allocated");
			}
			else
			{
				try
				{
					sleep(SLEEP_BETWEEN_CHECKS);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			if (stop != null)
			{
				if (stop.isValue())
				{
					finished = true;
					System.out.println("Manually stopped the waiting for the allocation.");
				}
			}
		}
	}

}
