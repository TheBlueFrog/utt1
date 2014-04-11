package asim;

public class ScoreKeeper extends Agent
{
//	private static final String TAG = "myScoreListener";

	private static double mScore = 0.0;

	public ScoreKeeper(Framework f)
	{
		super(f);
	}

	@Override
	protected void consume(Message msg)
	{
		double d = (Double) msg.mMessage;

		mScore += d;
		mScore = Math.min(1.0, mScore);
		mScore = Math.max(0.0, mScore);
		
		Log.d(getID(true), String.format("Score changed by %s, delta %9.5f, now %9.5f", 
				msg.mSender.getID(true),
				d, 
				mScore));
	}

}
