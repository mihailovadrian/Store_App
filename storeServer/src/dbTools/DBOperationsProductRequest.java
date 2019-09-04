package dbTools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.*;

public class DBOperationsProductRequest
{
	public static final int FILTER_TYPE_OPEN = 1;
	public static final int FILTER_TYPE_ALLOCATED = 2;

	static public ProductRequest readProductRequest( ResultSet rs, ProductRequest pr )
	{
		ProductRequest result = null;
		try
		{
			if (rs != null)
			{
				if (pr == null)
				{
					pr = new ProductRequest();
				}
				pr.setId(rs.getInt("id_prodreq"));
				pr.setProductId(rs.getInt("id_product"));
				pr.setCartId(rs.getInt("id_cart"));
				pr.setEntryDateTime(rs.getDate("entry_datetime"));
				pr.setQuantity(rs.getInt("quantity"));
				pr.setQuantityAllocated(rs.getInt("quantityallocated"));
				pr.setAllocatedDatetime(rs.getDate("allocated_datetime"));
				pr.setStatus(rs.getInt("status"));

				result = pr;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	static public ProductRequest getProductRequest( AppContext context, int id )
	{
		ProductRequest result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from ProductRequests where id_ProdReq= ?";
		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readProductRequest(rs, null);
			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			onFinally(null, rs, ps);
		}

		return result;
	}

	static public List<ProductRequest> listProductRequests( AppContext context, List<ProductRequest> list, int filterType )
	{
		List<ProductRequest> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		ProductRequest pr;

		String sql = "select * from ProductRequests ";
		if (filterType == FILTER_TYPE_OPEN)
		{
			sql = sql + " where (status=" + ProductRequest.OPEN + ") and (Allocated_DateTime is null) ";
		}
		if (filterType == FILTER_TYPE_ALLOCATED)
		{
			sql = sql + " where (status=" + ProductRequest.ALLOCATED + ") and (Allocated_DateTime is not null) and (dateadd(second,10,Allocated_DateTime)<current_timestamp) ";
		}
		sql = sql + "order by id_prodreq;";

		try
		{
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			if (list == null)
			{
				list = new ArrayList<>();
			}
			else
			{
				list.clear();
			}

			while (rs.next())
			{
				pr = readProductRequest(rs, null);
				if (pr != null)
				{
					list.add(pr);
				}
			}

			result = list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = null;
		}
		finally
		{
			onFinally(null, rs, ps);
		}

		return result;
	}

	static public int addProductRequest( AppContext context, ProductRequest pr )
	{
		int result = 0;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert ProductRequests (id_cart,id_product,entry_datetime,quantity,status) values (?, ?, current_timestamp, ?, " + ProductRequest.OPEN + ")";

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, pr.getCartId());
				ps.setInt(2, pr.getProductId());
				ps.setInt(3, pr.getQuantity());

				ps.executeUpdate();

				rs = ps.getGeneratedKeys();
				if (rs.next())
				{
					result = rs.getInt(1);
				}

				con.commit();
			}

			catch (Exception e)
			{
				e.printStackTrace();
				result = 0;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, rs, ps);
			}
		}
		return result;
	}

	static public boolean allocateProductRequest( AppContext context, ProductRequest pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement psreq = null;
			PreparedStatement psprod = null;

			String sqlreq = "update ProductRequests set quantityallocated = ?, Allocated_DateTime = current_timestamp, status = ? where id_ProdReq = ?";
			String sqlprod = "update Products set quantity = quantity - ? where id_Product = ?";

			try
			{
				psreq = con.prepareStatement(sqlreq);
				psprod = con.prepareStatement(sqlprod);

				psreq.setInt(1, pr.getQuantityAllocated());
				psreq.setInt(2, ProductRequest.ALLOCATED);
				psreq.setInt(3, pr.getId());

				psprod.setInt(1, pr.getQuantityAllocated());
				psprod.setInt(2, pr.getProductId());

				psreq.executeUpdate();
				psprod.executeUpdate();

				con.commit();

				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, null, psreq);
				onFinally(null, null, psprod);
			}
		}

		return result;
	}

	static public boolean consumeProductRequest( AppContext context, ProductRequest pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();

			PreparedStatement psreq = null;
			PreparedStatement pscart = null;

			String sqlreq = "update ProductRequests set status = ? where id_ProdReq = ?";
			String sqlcart = "insert into CartProducts (id_cart,id_product,entry_datetime,quantity) values (?, ?, current_timestamp, ?)";

			try
			{
				psreq = con.prepareStatement(sqlreq);
				pscart = con.prepareStatement(sqlcart);

				psreq.setInt(1, ProductRequest.CONSUMED);
				psreq.setInt(2, pr.getId());

				pscart.setInt(1, pr.getCartId());
				pscart.setInt(2, pr.getProductId());
				pscart.setInt(3, pr.getQuantityAllocated());

				psreq.executeUpdate();
				pscart.executeUpdate();

				con.commit();

				result = true;
			}

			catch (Exception e)
			{
				e.printStackTrace();
				result = false;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, null, psreq);
				onFinally(null, null, pscart);
			}
		}

		return result;
	}

	static public boolean cancelProductRequest( AppContext context, ProductRequest pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement psreq = null;
			PreparedStatement psprod = null;

			String sqlreq = "update ProductRequests set status = ? where id_ProdReq = ?";
			String sqlprod = "update Products set quantity = quantity + ? where id_Product = ?";

			try
			{
				psreq = con.prepareStatement(sqlreq);
				psprod = con.prepareStatement(sqlprod);

				psreq.setInt(1, ProductRequest.CANCELLED);
				psreq.setInt(2, pr.getId());

				psprod.setInt(1, pr.getQuantityAllocated());
				psprod.setInt(2, pr.getProductId());

				psreq.executeUpdate();
				psprod.executeUpdate();

				con.commit();

				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, null, psreq);
				onFinally(null, null, psprod);
			}
		}

		return result;
	}

	public static void onFinally( Statement st, ResultSet rs, PreparedStatement ps )
	{
		if (rs != null)
			try
			{
				rs.close();
			}
			catch (Exception ex)
			{
			}
		if (st != null)
			try
			{
				st.close();
			}
			catch (Exception ex)
			{
			}
		try
		{
			if (ps != null)
			{
				ps.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
