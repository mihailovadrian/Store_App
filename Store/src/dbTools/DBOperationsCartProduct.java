package dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.*;

public class DBOperationsCartProduct
{
	static protected CartProducts readCartProduct( ResultSet rs, CartProducts cartProd )
	{
		CartProducts result = null;

		try
		{
			if (rs != null)
			{
				if (cartProd == null)
				{
					cartProd = new CartProducts();
				}

				cartProd.setId(rs.getInt("id_cartproduct"));
				cartProd.setId_cart(rs.getInt("id_cart"));
				cartProd.setId_product(rs.getInt("id_product"));
				cartProd.setEntry_DataTime(rs.getString("entry_dateTime"));
				cartProd.setQuantity(rs.getInt("quantity"));

				result = cartProd;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public CartProducts getCartProductById( AppContext context, int id )
	{
		CartProducts result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from cartproducts where id_cartproduct= ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readCartProduct(rs, null);
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

	static public int addCartProduct( AppContext context, CartProducts cartProd )
	{
		int result = 0;

		if (cartProd != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert into cartproducts (id_cart, id_product, entry_datetime,quantity)  values (?, ?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, cartProd.getId_cart());
				ps.setInt(2, cartProd.getId_product());
				ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				ps.setInt(4, cartProd.getQuantity());

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

	static public List<CartProducts> listCartProducts( AppContext context, List<CartProducts> list, int idFilter )
	{
		List<CartProducts> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		CartProducts cartProd;

		String sql = "select * from cartproducts ";
		if (idFilter > 0)
		{

			sql = sql + "where id_cart = ? ";

		}
		if (idFilter == 0)
		{
			sql = sql + "where id_cart=0";
		}
		sql = sql + "order by entry_datetime";

		try
		{
			ps = con.prepareStatement(sql);

			if (idFilter > 0)
			{
				ps.setInt(1, idFilter);
			}

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
				cartProd = readCartProduct(rs, null);
				if (cartProd != null)
				{
					list.add(cartProd);
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

	static public void deleteCartProudct( AppContext context, CartProducts cartProd )
	{
		if (cartProd != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM cartproducts WHERE id_cartproduct=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, cartProd.getId());
				ps.executeUpdate();

				con.commit();
			}
			catch (Exception e)
			{
				e.printStackTrace();

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
				onFinally(null, null, ps);
			}
		}
	}

	static public boolean editCartProduct( AppContext context, CartProducts cartProd )
	{
		boolean result = false;

		if (cartProd != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update cartProducts set quantity=? where id_cartProduct = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, cartProd.getQuantity());
				ps.setInt(2, cartProd.getId());

				ps.executeUpdate();

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
				onFinally(null, rs, ps);
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
