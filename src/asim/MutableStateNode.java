package asim;


/**
 * this class holds the data for a state node, it has setters
 * and getters but it is not part of data stream since those
 * can't have setters (history is not changable)
 */
public class MutableStateNode
{
	protected double mTime;
	protected int mDayOfWeek;
	protected double mTimeOfDay;
	protected double mDuration;
	protected int mActivity;
	protected LatLng mLocation;

	protected Object mTag;
	
	public MutableStateNode (double time, int dayOfWeek, double timeOfDay, double duration, int activity, LatLng loc)
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
	public MutableStateNode (MutableStateNode n)
	{
		this (n.mTime, n.mDayOfWeek, n.mTimeOfDay, n.mDuration, n.mActivity, n.mLocation);
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
	public Object getTag ()
	{
		return mTag;
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
	
	public void setTime(double d)
	{
		mTime = d;
	}
	public void setDayOfWeek(int i)
	{
		mDayOfWeek = i;
	}
	public void setTimeOfDay(double d)
	{
		mTimeOfDay = d;
	}
	public void setDuration(double d)
	{
		mDuration = d;
	}
	public void setActivity(int i)
	{
		mActivity = i;
	}
	public void setLocation(LatLng x)
	{
		mLocation = x;
	}

	/**
	 * 
	 * @param n
	 * @return	true if the two time spans intersect, we shift the given one by
	 * the number of days between us so that it overlays this, then check
	 * the TimeOfDay and Duration 
	 */
	public boolean intersectsTimeOfDay(StateNode n)
	{
		double d = Util.toStartOfDay(getTime()) - Util.toStartOfDay(n.getTime());
		
		MutableStateNode shifted = new MutableStateNode(n);
		shifted.setTime(shifted.getTime() + d);
		
		if (getEndTime() < shifted.getTime())
			return false;	// we end before other starts
		if (getTime() > shifted.getEndTime())
			return false;	// we start after other ends
		
		double time = Math.max(getTime(), shifted.getTime());
		double duration = Math.min(getEndTime(), shifted.getEndTime()) - time;
		return duration > 0.0;
	}

}
