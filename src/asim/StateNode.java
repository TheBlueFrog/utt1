package asim;

import java.util.ArrayList;
import java.util.List;

public class StateNode extends MutableStateNode
{
	private static List<StateNode> mNodes = new ArrayList<>();
	
	private int mIndex = -1;
	
	private StateNode (double time, int dayOfWeek, double timeOfDay, double duration, int activity, LatLng loc)
	{
		super(time, dayOfWeek, timeOfDay, duration, activity, loc);
	}
	public StateNode (MutableStateNode n)
	{
		super(n.mTime, n.mDayOfWeek, n.mTimeOfDay, n.mDuration, n.mActivity, n.mLocation);
	}
	
	static public void add (StateNode n)
	{
		if ((mNodes.size() > 0) && (n.mTime < mNodes.get(mNodes.size() - 1).mTime))
			throw new IllegalStateException("Events must be added chronologically");

		n.mIndex = mNodes.size();
		mNodes.add(n);
	}

	/** 
	 * @return node just before this one chronologically or null if there is nothing
	 * in that direction
	 */
	public StateNode next (boolean backInTime)
	{
		if (backInTime)
		{
			if (mIndex == 0)
				return null;
			
			return mNodes.get(mIndex - 1);
		}
		else
		{
			if (mIndex < (mNodes.size() - 2))
				return null;
			
			return mNodes.get(mIndex + 1);
		}
	}
	/** 
	 * @return first node in a different day, or null
	 */
	public StateNode nextDay (boolean backInTime)
	{
		StateNode n = next(backInTime);
		while ((n != null) && (n.mDayOfWeek == mDayOfWeek))
		{
			n = n.next(backInTime);
		}
		return n;
	}
	
	/** 
	 * @param backInTime which direction to go
	 * @return node which happened during this same time-of-day or null if there is nothing in
	 * that direction
	 * 
	 * Both nodes are a time span, if the intersection of the two spans is not empty
	 * that is considered a match
	 */
	public StateNode nextThisTimeOfDay (boolean backInTime)
	{
		// get into the next day
		StateNode n = nextDay(backInTime);
		while ((n != null) && ( ! intersectsTimeOfDay(n)))
		{
			n = n.next(backInTime);
		}
		return n;
	}
	
	public StateNode getClosest ()
	{
		double d = 0;
		StateNode x = null;
		for (StateNode n : mNodes)
		{
			if (x == null)
			{
				x = n;
				d = 0;
			}
			else
			{
				double dd = Util.metersBetween(n.mLocation, x.mLocation);
				if (dd < d)
				{
					x = n;
					dd = d;
				}
			}
		}
		
		return x;
	}
	
	/** go back/forward by numDays not just one */
	public StateNode nextThisTimeOfDay(boolean backInTime, int numDays)
	{
		StateNode n = this;
		while (n != null)
		{
			for (int i = 0; i < numDays; ++i)
			{
				n = n.nextDay(backInTime);
				if (n == null)
					return null;
			}

			if (intersectsTimeOfDay(n))
				return n;
		}
		return null;
	}

}
