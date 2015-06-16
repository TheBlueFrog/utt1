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
        Log.d(getTag(), String.format("Construct Entity"));

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
        Log.d(getTag(), String.format("Entity run starts"));

		try
		{
			while (true)
			{
				Message m = queue.take();
				Log.d(getID(true), String.format("Process message from %s, %s", m.mSender.getID(true), m.mMessage));

				consume(m);
			}
		}
		catch (InterruptedException ex)
		{
		}
        Log.d(getTag(), String.format("Entity run ends"));
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

    private MutableStateNode moveToNewState(long newTime) {
        MutableStateNode now = getCurrentState();

        Log.d(getTag(), String.format("moveToNewState from %d to %d", now.getTime(), newTime));

        MutableStateNode newState = new MutableStateNode(getCurrentState(), newTime);
        return newState;
    }
    /**
     * update the entity to the new time, return the amount of
     * time we suggest before calling advance again
     *
     * @param newTime
     * @return
     */
    public long advance(long newTime) {
        long now = getCurrentState().getTime();

        Log.d(getTag(), String.format("advance from state time %d to %d", getCurrentState().getTime(), newTime));

        long delta = newTime - now;
        MutableStateNode newState = moveToNewState(newTime);
        mState.add(mState.size(), newState);

        return newTime;
    }

    private MutableStateNode getCurrentState ()
    {
        return mState.get(mState.size() - 1);
    }
}
