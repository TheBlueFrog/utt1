package asim;

import java.util.HashMap;
import java.util.Map;

public class SimpleBank extends Agent
{
	Map<Agent, Long> mAccounts = new HashMap<>();
	
	public SimpleBank(Framework f)
	{
		super(f);
	}

	@Override
	void consume(Message msg)
	{
		String[] p = msg.mMessage.split("[ \t]+");
		// deposit amount
		// withdraw amount
		// balance

		if (p[0].equals("deposit"))
		{
			if ( ! mAccounts.containsKey(msg.mSender))
				mAccounts.put(msg.mSender, 0L);
			
			mAccounts.put(msg.mSender, mAccounts.get(msg.mSender) + Long.parseLong(p[1]));
			
			send (new Message(this, msg.mSender, mAccounts.get(msg.mSender).toString()));
		}
		else if (p[0].equals("withdraw"))
		{
			if (mAccounts.containsKey(msg.mSender))
			{
				mAccounts.put(msg.mSender, mAccounts.get(msg.mSender) - Long.parseLong(p[1]));
			
				send (new Message(this, msg.mSender, mAccounts.get(msg.mSender).toString()));
				return;
			}

			send (new Message(this, msg.mSender, "No account"));
		}
		else if (p[0].equals("balance"))
		{
			send (new Message(this, msg.mSender, mAccounts.get(msg.mSender).toString()));
		}
		else
		{
			send (new Message(this, msg.mSender, "What? " + msg.mMessage));
		}
	}

}
