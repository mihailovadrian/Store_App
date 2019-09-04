package tools;

import java.sql.*;
import org.apache.log4j.Logger;

public class ConnectionTools
{
	// ~ Static fields/initialisers -----------------------------------------------------------------

	private static Logger logger = Logger.getLogger(ConnectionTools.class.getName());

	public static boolean checkString( String s, String name )
	{
		boolean result = false;

		if ((s != null) && (s.trim().length() > 0))
		{
			result = true;
		}
		else
		{
			logger.fatal("The " + name + " is not specified!");
		}

		return result;
	}

	public static Connection openConnection( String server, String instance, String username, String password )
	{
		Connection result = null;

		boolean serverValid = checkString(server, "server");
		boolean instanceValid = checkString(instance, "instance");
		boolean usernameValid = checkString(username, "username");
		boolean passwordValid = checkString(password, "password");

		if (serverValid && instanceValid && usernameValid && passwordValid)
		{
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

				logger.fatal("Error: " + e.getMessage());
			}
			finally
			{
			}
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

				logger.info("The connection to database was CLOSED.");
			}
			catch (Exception e)
			{
				logger.fatal("Error: " + e.getMessage(), e);
			}
			finally
			{
			}
		}

		connection = null;
	}
}
