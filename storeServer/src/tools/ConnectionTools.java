package tools;

import java.sql.*;

public class ConnectionTools
{
	public static Connection openConnection( String server, String instance, String username, String password )
	{
		Connection result = null;

		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();

			result = DriverManager.getConnection("jdbc:sqlserver://" + server + ";databaseName=" + instance, username, password);

			if (result != null)
			{
				result.setAutoCommit(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}

		return result;
	}

	public static void closeConnection( Connection connection )
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
			}
		}
	}
	//
	
}
