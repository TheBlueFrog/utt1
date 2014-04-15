package asim;

public class ScoreKeeper extends Agent
{
	private static final String TAG = ScoreKeeper.class.getSimpleName();

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
		
		// we keep the overall score off zero because it can
		// never become non-zero after that
		// upper limit is arbitrary
		
		mScore = Math.min(10000.0, Math.max(0.1, mScore));	
		
		Log.d(TAG, String.format("%40s scored as %9.5f, global %9.5f", 
				msg.mSender.getTag(),
				d, 
				mScore));
	}

}
