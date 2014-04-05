package asim;


public class Framework
{
	public void send(Message m)
	{
		m.mRecipient.incoming(m);
	}
}
