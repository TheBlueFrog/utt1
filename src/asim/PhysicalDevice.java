package asim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * class that emulates a physical device, it produces an event stream for
 * the rest of the system to consider
 * 
 * currently does not implement any commands to the device from the system...
 * 
 */
public class PhysicalDevice extends Entity
{
	private static final String TAG = PhysicalDevice.class.getSimpleName();

	/** amount of time-of-day and duration jitter 
	 * we introduce when building history.  0.0 disables it.
	 */
	public double mHistoryJitter = 0.0;
	private Random mRandom;

	private List<MutableStateNode> mFuture;

	public PhysicalDevice(Framework f)
	{
		super(f);
		
		mRandom = new Random(71);
		mFuture = setupFuture(DeviceHistory.mGlobalTime, 2);		
	}

	// we have a different run
	@Override
	public void run()
	{
		int i = 0;
		double curtod = 0.0;
		
		try
		{
			while (true)
			{
				// we inject an event every couple of seconds until there is no more future
				
				if (isQueueEmpty() && mFuture.size() > 0)
				{
					sleep(2000);
					
					StateNode n = new StateNode (mFuture.remove(0));
					
					Log.d(TAG, "Inject event " + n.toString());
					
					send(DeviceHistory.class, n);

					curtod = Util.asTimeOfDay(curtod + n.getDuration());
					DeviceHistory.mGlobalTime += n.getDuration();
					
					// rounding error
					if (Math.abs(n.getTimeOfDay() + n.getDuration() - 1.0) < Util.OneMinute)
					{
						DeviceHistory.mGlobalTime = Math.round(DeviceHistory.mGlobalTime);
					}
				}
				else
				{
					Message m = takeFromQueue();
	
					consume(m);
				}
			}
		}
		catch (InterruptedException ex)
		{
		}
	}

	@Override
	protected void consume(Message m)
	{
		// message from this world to the physical device world
		//
		//push the message to the physical device, maybe something
		// like increase sampling rate or get location now

		Log.d(TAG, String.format("NYI - consume message from %s, %s", m.mSender.getID(true), m.mMessage));
	}
	
	private List<MutableStateNode> setupFuture (double globalTime, int numDays)
	{
		List<MutableStateNode> nodes = new ArrayList<>();
		for (int k = 0; k < numDays; ++k)
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
	
	
	private List<String> getDay(int dow)
	{		
		if ((dow == 0) || (dow == 6))
			return new ArrayList<String>(mWeekendDay);
		else
			return new ArrayList<String>(mWorkday);
	}

	static private List<String> mWorkday = new ArrayList<String>();
	static 
	{
		//         duration (sec)  activity  lat         lon
		mWorkday.add("0.333,		3,      45.55,      -122.800");		// at home
		mWorkday.add("0.010, 		4,		45.55,		-122.800");		// get up
		mWorkday.add("0.020, 	    2,      45.35,      -122.800");		// to coffee
		mWorkday.add("0.010,     	3,      45.35,      -122.800");		// at coffee
		mWorkday.add("0.030,		0,      45.35,      -122.600");		// to work
		mWorkday.add("0.333,		3,      45.35,      -122.600");		// at work
		mWorkday.add("0.040,		0,      45.55,      -122.800");		// to home
		mWorkday.add(" -1.0,		0,      45.55,      -122.800");		// at home
	};
	static private List<String> mWeekendDay = new ArrayList<String>();
	static 
	{
		//         duration (sec)  activity  lat         lon
		mWeekendDay.add("0.400,		3,      45.55,      -122.800");		// at home
		mWeekendDay.add("0.010, 	4,		45.55,		-122.800");		// get up
		mWeekendDay.add("0.020,     2,      45.35,      -122.800");		// to coffee
		mWeekendDay.add("0.040,    	3,      45.35,      -122.800");		// at coffee
		mWeekendDay.add("0.020,		0,      45.55,      -122.800");		// to home
		mWeekendDay.add(" -1.0,		0,      45.55,      -122.800");		// at home
	};
}
