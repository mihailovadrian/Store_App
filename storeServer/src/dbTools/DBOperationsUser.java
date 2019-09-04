package dbTools;

import java.sql.*;
import java.util.*;

import app.*;
import entities.*;

public class DBOperationsUser
{
	static protected User readUser( ResultSet rs, User user )
	{
		User result = null;

		try
		{
			if (rs != null)
			{
				if (user == null)
				{
					user = new User();
				}

				user.setId(rs.getInt("id_user"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setAddress(rs.getString("address"));
				user.setAdmin(rs.getInt("isadmin") == 1);

				result = user;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public User getUser( AppContext context, String email, String password )
	{
		User result = null;

		if ((email != null) && (password != null))
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "select * from users where lower(email) = ? and password = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setString(1, email.toLowerCase().trim());
				ps.setString(2, password.trim());

				rs = ps.executeQuery();

				if (rs.next())
				{
					result = readUser(rs, null);
					/*
					result = new User();
					
					result.setId(rs.getInt("id_user"));
					result.setName(rs.getString("name"));
					result.setEmail(rs.getString("email"));
					result.setPassword(rs.getString("password"));
					result.setAddress(rs.getString("address"));
					result.setAdmin(rs.getInt("isadmin") == 1);
					/**/
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
		}

		return result;
	}

	static public User getUser( AppContext context, int id )
	{
		User result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from users where id_user = ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readUser(rs, null);
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

	static public User getIdUser( AppContext context, String email )
	{
		User result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from users where email = ?";

		try
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readUser(rs, null);
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

	static public void deleteUser( AppContext context, User user )
	{
		if (user != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM users WHERE id_user=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, user.getId());
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

	static public int addUser( AppContext context, User user )
	{
		int result = 0;

		if (user != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert users (name, email, password, address, isadmin)  values (?, ?, ?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getPassword());
				ps.setString(4, user.getAddress());
				ps.setInt(5, user.isAdmin() ? 1 : 0);

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

	static public boolean editUser( AppContext context, User user )
	{
		boolean result = false;

		if (user != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update users set name = ?, email = ?, password = ?, address = ?, isadmin = ? where id_user = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getPassword());
				ps.setString(4, user.getAddress());
				ps.setInt(5, user.isAdmin() ? 1 : 0);
				ps.setInt(6, user.getId());

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

	static public List<User> listUsers( AppContext context, List<User> list, String nameFilter )
	{
		List<User> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user;

		String sql = "select * from users ";
		if (nameFilter != null)
		{
			nameFilter = nameFilter.trim().toLowerCase();
			if (nameFilter.length() > 0)
			{
				sql = sql + "where lower(name) like ? ";
			}
			else
			{
				nameFilter = null;
			}
		}
		sql = sql + "order by name";

		try
		{
			ps = con.prepareStatement(sql);

			if (nameFilter != null)
			{
				ps.setString(1, "%" + nameFilter + "%");
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
				user = readUser(rs, null);
				if (user != null)
				{
					list.add(user);
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
