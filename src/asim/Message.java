package asim;

public class Message
{
	public Entity mSender = null;
	public Class<? extends Entity> mRecipient = null;
	public Object mMessage;
	
//	public Message (Entity sender, Entity recipient, String msg)
//	{
//		mSender = sender;
//		mRecipient = recipient;
//		mMessage = msg;
//	}
//
//	public Message(String msg)
//	{
//		mMessage = msg;
//	}

	public Message(Entity sender, Class<? extends Entity> class1, Object msg)
	{
		mSender = sender;
		mRecipient = class1;
		mMessage = msg;
	}

}
