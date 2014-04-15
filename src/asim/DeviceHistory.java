package asim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceHistory extends Agent
{
	static public double mGlobalTime = 0;
	
	public int numDaysOfHistory = 7;
	
	/** amount of time-of-day and duration jitter 
	 * we introduce when building history.  0.0 disables it.
	 */
	public double mHistoryJitter = 0.0;
	private Random mRandom;
	
	public DeviceHistory(Framework f)
	{
		super(f);

		mRandom = new Random(57);
		
		// align the historical data to start on a Monday
		
		mGlobalTime = Util.toStartOfDay(1000.0);
		do
		{
			mGlobalTime -= 1.0;
		}
		while (Util.getDayOfWeek(mGlobalTime) != 1);

		for (MutableStateNode n : setup())
		{
			// load persisted history into the state graph 
			StateNode.add(new StateNode(n));
			mGlobalTime += n.getDuration();
		}
		
		// get globaltime aligned to start of day, not really really 
		// close to it cause then it's in the previous day which is
		// not good
		mGlobalTime = Math.round(mGlobalTime);
	}
	
	private List<MutableStateNode> setup ()
	{
		// stuff in some number of days of history
		double globalTime = mGlobalTime;
		
		List<MutableStateNode> nodes = new ArrayList<>();
		
		for (int k = 0; k < numDaysOfHistory; ++k)
		{
			double curtod = 0.0;
			boolean endOfDay = false;

			List<String> data = getDay(Util.getDayOfWeek(globalTime));
			for (String s : data)
			{
				String[] v = s.split(",");
				int dow = Util.getDayOfWeek(globalTime);
	
				double duration = Double.parseDouble(v[0].trim());
				if (duration < 0.0)
				{
					// fill out rest of day
					duration = 1.0 - curtod;
					endOfDay = true;
				}
	
//				MutableStateNode n = new MutableStateNode(
//						globalTime,
//						dow, 
//						curtod,
//						duration,
//						Integer.parseInt(v[1].trim()),
//						new LatLng (Double.parseDouble(v[2].trim()), Double.parseDouble(v[3].trim())));

				MutableStateNode n = jitter(
						nodes,
						globalTime,
						dow, 
						curtod,
						duration,
						Integer.parseInt(v[1].trim()),
						new LatLng (Double.parseDouble(v[2].trim()), Double.parseDouble(v[3].trim())));
				
				nodes.add(n);
				
				curtod = Util.asTimeOfDay(n.getTimeOfDay() + n.getDuration());
				globalTime += n.getDuration();
				
				if (endOfDay)
				{
					curtod = 0.0;
					globalTime = Math.round(globalTime);
				}
				
			}
		}
		
		return nodes;
	}

	private MutableStateNode jitter(List<MutableStateNode> v, double globalTime, int dow, double tod, double duration, int activity, LatLng latLng)
	{
		// don't jitter it out of the current day
		
//		double todJitter = (mHistoryJitter * Util.OneHour * mRandom.nextDouble());
//		todJitter -= todJitter * 0.5;
//		tod = node.getTimeOfDay() + todJitter);
		
		double jitter = (mHistoryJitter * duration * mRandom.nextDouble());
		jitter -= jitter / 2.0;
		duration += jitter;
		
		if ((tod + duration) > 1.0)
			duration = 1.0 - tod;
		
		if (duration < Util.OneMinute)
			duration += Util.OneMinute;

		if ((tod + duration) > 1.0)
			throw new IllegalStateException("got stuck jittering");
		
		MutableStateNode n = new MutableStateNode(
				globalTime,
				dow, 
				tod,
				duration,
				activity,
				latLng);
		return n;
	}

	@Override
	protected void consume(Message msg)
	{
		// the physical device produced some state, update current
		// state, put it into history and forward to the scoring agents

		StateNode n = (StateNode) msg.mMessage;
//		Log.d(getID(true), n.toString());
		
		StateNode.add(n);
		// don't actually save it to history
		send(ScoringAgent.class, n);
	}

	
	
	private List<String> getDay(int dow)
	{		
		if ((dow == 0) || (dow == 6))
			return new ArrayList<String>(mWeekendDay);
		else
			return new ArrayList<String>(mWorkDay);
	}
	
	static private List<String> mWorkDay = new ArrayList<String>();
	static 
	{
		//          duration (sec)  activity  lat         lon
		mWorkDay.add("0.333,		3,      45.55,      -122.800");		// at home
		mWorkDay.add("0.010, 		4,		45.55,		-122.800");		// get up
		mWorkDay.add("0.020,  	    2,      45.35,      -122.800");		// to coffee
		mWorkDay.add("0.010,     	3,      45.35,      -122.800");		// at coffee
		mWorkDay.add("0.030,		0,      45.35,      -122.600");		// to work
		mWorkDay.add("0.333,		3,      45.35,      -122.600");		// at work
		mWorkDay.add("0.040,		0,      45.55,      -122.800");		// to home
		mWorkDay.add(" -1.0,		0,      45.55,      -122.800");		// at home
	};

	static private List<String> mWeekendDay = new ArrayList<String>();
	static 
	{
		//           duration (sec)  activity  lat         lon
		mWeekendDay.add("0.400,		3,      45.55,      -122.800");		// at home
		mWeekendDay.add("0.010, 	4,		45.55,		-122.800");		// get up
		mWeekendDay.add("0.020,     2,      45.35,      -122.800");		// to coffee
		mWeekendDay.add("0.040,    	3,      45.35,      -122.800");		// at coffee
		mWeekendDay.add("0.020,		0,      45.55,      -122.800");		// to home
		mWeekendDay.add(" -1.0,		0,      45.55,      -122.800");		// at home
	}
}
