package asim;


public class Main
{

	private static Framework mFramework;

	public static void main(String[] args)
	{
		mFramework = new Framework();
		
	     Agent a = new SimpleBank (mFramework);
	     a.start();
	     
	     Agent b = new SimpleBank (mFramework);
	     b.start();
	     
	     mFramework.send(new Message(a, b, "deposit 100"));
	}

}
