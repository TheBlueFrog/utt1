package asim;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

abstract public class Agent extends Thread
{
	private UUID mID;
	private final BlockingQueue<Message> queue;
	private Framework mFramework;

	public Agent(Framework f)
	{
		mFramework = f;
		mID = UUID.randomUUID();

		queue = new LinkedBlockingQueue<Message>();
	}

	public String getID(boolean little)
	{
		if (little)
			return mID.toString().substring(0, 8);

		return mID.toString();
	}
	public String getID()
	{
		return mID.toString();
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Message m = queue.take();
				Log.d(getID(true), String.format("Process from %s, %s", m.mSender.getID(true), m.mMessage));

				consume(m);
			}
		}
		catch (InterruptedException ex)
		{
		}
	}

	public void incoming (Message msg)
	{
//		synchronized (queue)
		{
			queue.add(msg);
		}
	}

	protected void send(Message m)
	{
		mFramework.send(m);
	}

	abstract void consume(Message msg);

}
