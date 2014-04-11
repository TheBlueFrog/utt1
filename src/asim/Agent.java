package asim;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

abstract public class Agent extends Thread
{
	private UUID mID;
	private final BlockingQueue<Message> queue;
	protected Framework mFramework;

	public Agent(Framework f)
	{
		mFramework = f;
		mID = UUID.randomUUID();

		queue = new LinkedBlockingQueue<Message>();
		
		mFramework.register (this);
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

	protected boolean isQueueEmpty()
	{
		return queue.peek() == null;
	}
	protected Message takeFromQueue() throws InterruptedException
	{
		return queue.take();
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Message m = queue.take();
//				Log.d(getID(true), String.format("Process message from %s, %s", m.mSender.getID(true), m.mMessage));

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

	abstract protected void consume(Message msg);

	protected void send(Class<? extends Agent> class1, Object msg)
	{
		mFramework.send(new Message(this, class1, msg));
	}


}
