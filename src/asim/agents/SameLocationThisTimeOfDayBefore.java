package asim.agents;

import asim.Framework;
import asim.Log;
import asim.ScoringAgent;
import asim.StateNode;
import asim.Util;

public class SameLocationThisTimeOfDayBefore extends ScoringAgent
{
	private static final String TAG = SameLocationThisTimeOfDayBefore.class.getSimpleName();
	
	private static final double K = 2.0;
	
	public SameLocationThisTimeOfDayBefore(Framework f)
	{
		super(f);
		Log.d(TAG, getID(true));
	}

	@Override
	public double score(StateNode node)
	{
		StateNode n = node;
		while (true)
		{
			n = n.nextThisTimeOfDay(true);
			
			if (n == null)
				return 1.0;	// no more history, no opinion?
			
			if (Util.metersBetween(n.getLocation(), n.getLocation()) < 35.0)
			{
				// preserve direction of time
				double howFar = n.getTime() - node.getTime();
				return Math.exp(howFar + K);
			}
		}
	}

}
