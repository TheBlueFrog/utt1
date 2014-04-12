package asim;

import java.util.ArrayList;
import java.util.List;

public class StateNode
{
	private static List<StateNode> mNodes = new ArrayList<>();
	
	private int mIndex = -1;
	
	private double mTime;
	private int mDayOfWeek;
	private double mTimeOfDay;
	private double mDuration;
	private int mActivity;
	private LatLng mLocation;

	public Object mTag;
	
	public StateNode (double time, int dayOfWeek, double timeOfDay, double duration, int activity, LatLng loc)
	{
		if ((timeOfDay < 0) || (timeOfDay > 1.0))
			throw new IllegalStateException("Event time-of-day < 0.0 or > 1.0");
		
		mTime = time;
		mDayOfWeek = dayOfWeek;
		mTimeOfDay = timeOfDay;
		mDuration = duration;
		mActivity = activity;
		mLocation = loc;

		mTag = null;		
	}
	
	static public void add (StateNode n)
	{
		if ((mNodes.size() > 0) && (n.mTime < mNodes.get(mNodes.size() - 1).mTime))
			throw new IllegalStateException("Events must be added chronologically");
		
		n.mIndex = mNodes.size();
		mNodes.add(n);
	}

	@Override
	public String toString()
	{
		return String.format("StateNode { %9.5f, %d, %9.5f, %9.5f, %d, %s }", mTime, mDayOfWeek, mTimeOfDay, mDuration, mActivity, mLocation.toString());
	}
	
	public void setTag (Object t)
	{
		mTag = t;
	}
	
	public double getTime()
	{
		return mTime;
	}
	public int getDayOfWeek()
	{
		return mDayOfWeek;
	}
	public double getTimeOfDay()
	{
		return mTimeOfDay;
	}
	public double getDuration()
	{
		return mDuration;
	}
	public int getActivity()
	{
		return mActivity;
	}
	public double getEndTime()
	{
		return mTime + mDuration;
	}
	public LatLng getLocation()
	{
		return mLocation;
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
	
	public StateNode shift (double t)
	{
		StateNode n = new StateNode(mTime + t, mDayOfWeek, mTimeOfDay, mDuration, mActivity, mLocation);
		return n;
	}
	
	/**
	 * 
	 * @param n
	 * @return	true if the two time spans intersect, we shift one by
	 * the number of days between them so that they overlay each other
	 */
	public boolean intersectsTimeOfDay(StateNode n)
	{
		double d = Util.daysBetween(n.getTime(), getTime());
		StateNode shifted = n.shift (d);
		
		if (getEndTime() < shifted.getTime())
			return false;	// we end before other starts
		if (getTime() > shifted.getEndTime())
			return false;	// we start after other ends
		
		double time = Math.max(getTime(), shifted.getTime());
		double duration = Math.min(getEndTime(), shifted.getEndTime()) - time;
		return duration > 0.0;
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

}
