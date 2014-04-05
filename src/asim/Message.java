package asim;

public class Message
{
	public Agent mSender;
	public Agent mRecipient;
	public String mMessage;
	
	public Message (Agent sender, Agent recipient, String msg)
	{
		mSender = sender;
		mRecipient = recipient;
		mMessage = msg;
	}
}
