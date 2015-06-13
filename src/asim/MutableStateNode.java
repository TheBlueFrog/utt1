package asim;


/**
 */
public class MutableStateNode
{
    protected long mTime;
    protected Object mTag;

	public MutableStateNode (long time)
	{
        mTime = time;
		mTag = null;
	}
	public MutableStateNode (MutableStateNode n)
	{
		this (n.mTime);
	}

	@Override
	public String toString()
	{
		return String.format("MutableStateNode { %d, %s }", mTime, (mTag != null ? "not null" : "null"));
	}
	
	public void setTag (Object t)
	{
		mTag = t;
	}
	public Object getTag ()
	{
		return mTag;
	}

	public long getTime()
	{
		return mTime;
	}
}
