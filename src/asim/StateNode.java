package asim;

import java.util.ArrayList;
import java.util.List;

public class StateNode
{
	private static List<StateNode> mNodes = new ArrayList<>();
	
	private int mIndex = -1;
	
	private long mTime;
	private int mDayOfWeek;
	private double mTimeOfDay;
	private long mDuration;
	private int mActivity;
	private LatLng mLocation;

	public Object mTag;
	
	public StateNode (long time, int dayOfWeek, double timeOfDay, long durationSeconds, int activity, LatLng loc)
	{
		if ((mNodes.size() > 0) && (time < mNodes.get(mNodes.size() - 1).mTime))
			throw new IllegalStateException("Events must be added chronologically");
		
		mTime = time;
		mDayOfWeek = dayOfWeek;
		mTimeOfDay = timeOfDay;
		mDuration = durationSeconds;
		mActivity = activity;
		mLocation = loc;

		mTag = null;		
	}
	
	static public void add (StateNode n)
	{
		n.mIndex = mNodes.size();
		mNodes.add(n);
	}

	@Override
	public String toString()
	{
		return String.format("StateNode { %d, %9.5f, %d, %d }", mDayOfWeek, mTimeOfDay, mDuration, mActivity);
	}
	
	public void setTag (Object t)
	{
		mTag = t;
	}
	
	/** 
	 * @return node just before this one chronologically or null if this is the first node 
	 */
	public StateNode getPreceeding ()
	{
		if (mIndex == 0)
			return null;
		
		return mNodes.get(mIndex - 1);
	}
	/** 
	 * @return node just after this one chronologically or null if this is the last node 
	 */
	public StateNode getFollowing ()
	{
		if (mIndex == mNodes.size() - 1)
			return null;
		
		return mNodes.get(mIndex + 1);
	}

	public int getDayOfWeek()
	{
		return mDayOfWeek;
	}
	public double getTimeOfDay()
	{
		return mTimeOfDay;
	}
	public long getDuration()
	{
		return mDuration;
	}
	public int getActivity()
	{
		return mActivity;
	}
}
