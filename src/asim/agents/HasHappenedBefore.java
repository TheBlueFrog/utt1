package asim.agents;

import asim.Framework;
import asim.Message;
import asim.ScoreKeeper;
import asim.ScoringAgent;
import asim.StateNode;

public class HasHappenedBefore extends ScoringAgent
{
	private static final String TAG = HasHappenedBefore.class.getSimpleName();
	
	public HasHappenedBefore(Framework f)
	{
		super(f);
	}

	@Override
	protected void consume(Message msg)
	{
		StateNode start = (StateNode) msg.mMessage;
		int i = 3;
		for (StateNode n = start.getPreceeding();
				n != null;
				n = n.getPreceeding(), --i)
		{
			if(    (start.getActivity() == n.getActivity())
			    || (start.getDayOfWeek() == n.getDayOfWeek())
			    || closeTimeOfDay (start.getTimeOfDay(), n.getTimeOfDay())
			    || closeDuration (start.getDuration(), n.getDuration()))
			{
				double delta = Math.exp((double) i);
				send(ScoreKeeper.class, delta);
			}
		}
	}

	/** are two durations (in ms) close, less than 5 minutes apart */
	private boolean closeDuration(long a, long b)
	{
		return Math.abs(a - b) < (5L * 60L * 1000L);
	}

	/** are two time-of-days close, less than 1/2 hour apart */
	private boolean closeTimeOfDay(double a, double b)
	{
		return Math.abs(a - b) < ((1.0 / 24.0) * 0.5);
	}

}
