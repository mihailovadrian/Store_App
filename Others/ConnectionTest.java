package frames;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ConnectionTest
{

	public void ReadUser( )
	{

		String connectionUrl = "jdbc:sqlserver://dub-mssql1:1433;databaseName=internship";

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl, "sa", "imsmaxims2013");

			// SQL
			String SQL = "SELECT * from Users";
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			while (rs.next())
			{
				System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));

			}

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (Exception e)
				{
				}
			if (stmt != null)
				try
				{
					stmt.close();
				}
				catch (Exception e)
				{
				}
			if (con != null)
				try
				{
					con.close();
				}
				catch (Exception e)
				{
				}
		}
	}

	public void FillTable( JTable table, String Query )
	{
		try
		{
			Connection con = null;
			String connectionUrl = "jdbc:sqlserver://dub-mssql1:1433;databaseName=internship";
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl, "sa", "imsmaxims2013");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(Query);

			//To remove previously added rows
			while (table.getRowCount() > 0)
			{
				((DefaultTableModel) table.getModel()).removeRow(0);
			}
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				Object[] row = new Object[columns];
				for (int i = 1; i <= columns; i++)
				{
					row[i - 1] = rs.getObject(i);
				}
				((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
			}

			rs.close();
			stat.close();
			con.close();
		}
		catch (SQLException e)
		{
		}
	}
}