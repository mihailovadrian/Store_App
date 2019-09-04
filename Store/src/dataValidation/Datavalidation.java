package dataValidation;

public class Datavalidation
{
	static boolean userNameValidation( String name )
	{
		if (name.length() <= 3 && name.length() > 200)
		{
			return false;
		}
		
		return true;

	}
}
