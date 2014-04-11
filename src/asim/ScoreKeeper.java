package asim;

public class ScoreKeeper extends Agent
{
//	private static final String TAG = "myScoreListener";

	private static double mScore = 1.0;

	public ScoreKeeper(Framework f)
	{
		super(f);
	}

	@Override
	protected void consume(Message msg)
	{
		double d = (Double) msg.mMessage;

		mScore *= d;
//		mScore = Math.max(0.0, Math.min(1.0, mScore));
		
		Log.d(getID(true), String.format("%s scored as %9.5f, global %9.5f", 
				msg.mSender.getID(true),
				d, 
				mScore));
	}

}
