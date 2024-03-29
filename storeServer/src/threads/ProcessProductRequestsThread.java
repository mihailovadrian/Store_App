package threads;

import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import dbTools.DBOperationsProduct;
import dbTools.DBOperationsProductRequest;
import entities.Product;
import entities.ProductRequest;
import tools.MutableBoolean;

public class ProcessProductRequestsThread extends Thread
{
	private static final long SLEEP_BETWEEN_CHECKS = 1000;

	private AppContext context;
	private MutableBoolean stop;

	private List<ProductRequest> list;

	public ProcessProductRequestsThread( AppContext context, MutableBoolean stop )
	{
		super();

		this.context = context;
		this.stop = stop;
		list = new ArrayList<>();
	}

	public void run( )
	{
		boolean finished = false;

		int quantityWanted;
		int quantityToAllocate;

		while (!finished)
		{
			System.out.println("Cancel forgotten allocated requests ...");
			DBOperationsProductRequest.listProductRequests(context, list, DBOperationsProductRequest.FILTER_TYPE_ALLOCATED);
			for (ProductRequest pr : list)
			{
				DBOperationsProductRequest.cancelProductRequest(context, pr);
			}

			System.out.println("Allocate open requests ...");
			DBOperationsProductRequest.listProductRequests(context, list, DBOperationsProductRequest.FILTER_TYPE_OPEN);
			for (ProductRequest pr : list)
			{
				Product prod = DBOperationsProduct.getProduct(context, pr.getProductId());
				if (prod != null)
				{
					quantityWanted = pr.getQuantity();

					if (prod.getQuantity() >= quantityWanted)
					{
						quantityToAllocate = quantityWanted;
					}
					else
					{
						quantityToAllocate = prod.getQuantity();
					}

					pr.setQuantityAllocated(quantityToAllocate);

					DBOperationsProductRequest.allocateProductRequest(context, pr);

					System.out.println("Allocated " + quantityToAllocate + " for request " + pr.getId());
				}
			}

			try
			{
				sleep(SLEEP_BETWEEN_CHECKS);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			if (stop != null)
			{
				if (stop.isValue())
				{
					finished = true;
					System.out.println("Manually stopped the the product requets allocation.");
				}
			}
		}
	}
}
