package asim;

import asim.agents.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Entity extends Thread
{
	private UUID mID;
	private final BlockingQueue<Message> queue;
	protected Environment mEnvironment;
    protected List<MutableStateNode> mState; // lower index, earlier in time

	public Entity(Environment e)
	{
		mEnvironment = e;
		mID = UUID.randomUUID();

		queue = new LinkedBlockingQueue<Message>();
		mEnvironment.register(this);

        mState = new ArrayList<MutableStateNode>();
        mState.add(new MutableStateNode(e.getTime()));
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
		mEnvironment.send(m);
	}

	protected void consume(Message msg) {
		Log.d(getTag(), String.format("drop message on floor", 0));
	}

	protected void send(Class<? extends Entity> class1, Object msg)
	{
		mEnvironment.send(new Message(this, class1, msg));
	}

	public String getTag()
	{
		return this.getClass().getSimpleName();
	}

}
