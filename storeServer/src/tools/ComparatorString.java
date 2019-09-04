package tools;

public class ComparatorString
{
	static public void stringComparator( String s1, String s2 )
	{
		if (s1 == null)
		{
			s1 = "";
		}
		else
		{
			s1 = s1.toLowerCase();
		}
		if (s1.trim().length() == 0)
		{
			s1 = "zzzzzzz";
		}

		if (s2 == null)
		{
			s2 = "";
		}
		else
		{
			s2 = s2.toLowerCase();
		}
		if (s2.trim().length() == 0)
		{
			s2 = "zzzzzzz";
		}
	}
}
