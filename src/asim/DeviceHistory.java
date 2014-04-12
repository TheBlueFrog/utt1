package asim;

import java.util.ArrayList;
import java.util.List;

public class DeviceHistory extends Agent
{
	static public double mGlobalTime = 0;
	
	public DeviceHistory(Framework f)
	{
		super(f);

		// load persisted history into the state graph 

		// we fake it here, align the data on a Monday
		
		mGlobalTime = Util.toStartOfDay(1000.0);
		do
		{
			mGlobalTime -= 1.0;
		}
		while (Util.getDayOfWeek(mGlobalTime) != 1);
		
		for (int k = 0; k < 7; ++k)
		{
			double curtod = 0.0;

			List<String> data = getDay(Util.getDayOfWeek(mGlobalTime));
			for (String s : data)
			{
				String[] v = s.split(",");
				int dow = Util.getDayOfWeek(mGlobalTime);
	
				double duration = Double.parseDouble(v[0].trim());
				if (duration < 0.0)
				{
					// fill out rest of day
					duration = 1.0 - curtod;
				}
	
				StateNode n = new StateNode(
						mGlobalTime,
						dow, 
						curtod,
						duration,
						Integer.parseInt(v[1].trim()),
						new LatLng (Double.parseDouble(v[2].trim()), Double.parseDouble(v[3].trim())));
	
				curtod = Util.asTimeOfDay(curtod + duration);
				mGlobalTime += duration;
				
				StateNode.add(n);
			}
			// handle imprecision
			mGlobalTime = Math.round(mGlobalTime);
		}
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

	
	
	static public List<String> getDay(int dow)
	{
		if ((dow == 0) || (dow == 6))
			return new ArrayList<String>(mPrototypeWeekendDay);
		else
			return new ArrayList<String>(mPrototypeWorkDay);
	}
	
	static public List<String> mPrototypeWorkDay = new ArrayList<String>();
	static 
	{
		//              duration (sec)  activity  lat         lon
		mPrototypeWorkDay.add("0.333,		3,      45.55,      -122.800");		// at home
		mPrototypeWorkDay.add("0.010, 		4,		45.55,		-122.800");		// get up
		mPrototypeWorkDay.add("0.020,  	    2,      45.35,      -122.800");		// to coffee
		mPrototypeWorkDay.add("0.010,     	3,      45.35,      -122.800");		// at coffee
		mPrototypeWorkDay.add("0.030,		0,      45.35,      -122.600");		// to work
		mPrototypeWorkDay.add("0.333,		3,      45.35,      -122.600");		// at work
		mPrototypeWorkDay.add("0.040,		0,      45.55,      -122.800");		// to home
		mPrototypeWorkDay.add(" -1.0,		0,      45.55,      -122.800");		// at home
	};

	static public List<String> mPrototypeWeekendDay = new ArrayList<String>();
	static 
	{
		//                    duration (sec)  activity  lat         lon
		mPrototypeWeekendDay.add("0.400,		3,      45.55,      -122.800");		// at home
		mPrototypeWeekendDay.add("0.010, 		4,		45.55,		-122.800");		// get up
		mPrototypeWeekendDay.add("0.020,  	    2,      45.35,      -122.800");		// to coffee
		mPrototypeWeekendDay.add("0.040,     	3,      45.35,      -122.800");		// at coffee
		mPrototypeWeekendDay.add("0.020,		0,      45.55,      -122.800");		// to home
		mPrototypeWeekendDay.add(" -1.0,		0,      45.55,      -122.800");		// at home
	};
}
