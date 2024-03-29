package dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.*;

public class DBOpeationsCart
{
	static protected Carts readCart( ResultSet rs, Carts cart )
	{
		Carts result = null;

		try
		{
			if (rs != null)
			{
				if (cart == null)
				{
					cart = new Carts();
				}

				cart.setId(rs.getInt("id_cart"));
				cart.setId_user(rs.getInt("id_user"));
				cart.setStatus(rs.getInt("status"));

				result = cart;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public Carts getCart( AppContext context, int id )
	{
		Carts result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from carts where id_cart= ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readCart(rs, null);
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

	static public int addCart( AppContext context, Carts cart )
	{
		int result = 0;

		if (cart != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert carts (id_user, status)  values (?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, cart.getId_user());
				ps.setInt(2, cart.getStatus());

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

	static public List<Carts> listCarts( AppContext context, List<Carts> list, int idFilter )
	{
		List<Carts> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Carts cart;

		String sql = "select * from carts ";
		if (idFilter > 0)
		{

			sql = sql + "where id_user = ? and status=1 ";

		}

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
				cart = readCart(rs, null);
				if (cart != null)
				{
					list.add(cart);
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

	static public void deleteCart( AppContext context, Carts cart )
	{
		if (cart != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM carts WHERE id_cart=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, cart.getId());
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

	static public boolean editCart( AppContext context, Carts cart )
	{
		boolean result = false;

		if (cart != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update carts set status = ? where id_cart = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, cart.getStatus());
				ps.setInt(2, cart.getId());
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
