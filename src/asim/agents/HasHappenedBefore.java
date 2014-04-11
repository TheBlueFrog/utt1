package asim.agents;

import asim.Framework;
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
	public double score(StateNode node)
	{
		int i = 3;
		for (StateNode n = node.getPreceeding();
				(n != null) && (i > -3);
				n = n.getPreceeding(), --i)
		{
			if(    (node.getActivity() == n.getActivity())
			    || (node.getDayOfWeek() == n.getDayOfWeek())
			    || closeTimeOfDay (node.getTimeOfDay(), n.getTimeOfDay())
			    || closeDuration (node.getDuration(), n.getDuration()))
			{
				double delta = Math.exp((double) i);
				return delta;
			}
		}
		
		return 1.0;	// no opinion
	}

	/** are two durations (in ms) close, less than 5 minutes apart */
	private boolean closeDuration(long a, long b)
	{
		return Math.abs(a - b) < (5L * 60L * 1000L);
	}

	/** are two times-of-day close, less than 1/2 hour apart */
	private boolean closeTimeOfDay(double a, double b)
	{
		return Math.abs(a - b) < ((1.0 / 24.0) * 0.5);
	}


}
