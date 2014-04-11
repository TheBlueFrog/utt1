package asim;

public class DeviceHistory extends Agent
{
	public DeviceHistory(Framework f)
	{
		super(f);

		// load persisted history into the state graph 

		double curtod = 0.0;
		for (String s : mThePast)
		{
			String[] v = s.split(",");
			long time = System.currentTimeMillis();
			int dow = Integer.parseInt(v[0].trim());

			double duration = Double.parseDouble(v[1].trim());
			if (duration < 0.0)
			{
				// fill out rest of day
				duration = 1.0 - curtod;
			}

			StateNode n = new StateNode(
					time,
					dow, 
					curtod,
					Util.asMSDuration(duration),
					Integer.parseInt(v[2].trim()),
					new LatLng (Double.parseDouble(v[3].trim()), Double.parseDouble(v[4].trim())));

			curtod += duration;
			
			StateNode.add(n);
		}
	}

	@Override
	protected void consume(Message msg)
	{
		// the physical device produced some state, update current
		// state, put it into history and forward to the scoring agents

		StateNode n = (StateNode) msg.mMessage;
		Log.d(getID(true), n.toString());
		
		StateNode.add(n);
		// don't actually save it to history
		send(ScoringAgent.class, n);
	}

	
	static private String[] mThePast = 
	{
		// time and tod are filled in when this is used
		
	//  DoW 	duration (sec)   activity  lat            lon
		"1,		0.333,			3,      45.55,      -122.800",		// at home
		"1,		0.010, 			4,		45.55,		-122.800",		// get up
		"1,		0.020,  	    2,      45.35,      -122.800",		// to coffee
		"1,		0.010,	     	3,      45.35,      -122.800",		// at coffee
		"1,		0.030,			0,      45.35,      -122.600",		// to work
		"1,		0.333, 			3,      45.35,      -122.600",		// at work
		"1,		0.040,			0,      45.55,      -122.800",		// to home
		"1,		 -1.0,			0,      45.55,      -122.800",		// at home
		
		"2,		0.333,			3,      45.55,      -122.800",		// at home
		"2,		0.010, 			4,		45.55,		-122.800",		// get up
		"2,		0.020,  	    2,      45.35,      -122.800",		// to coffee
		"2,		0.010,	     	3,      45.35,      -122.800",		// at coffee
		"2,		0.030,			0,      45.35,      -122.600",		// to work
		"2,		0.333, 			3,      45.35,      -122.600",		// at work
		"2,		0.040,			0,      45.55,      -122.800",		// to home
		"2,		 -1.0,			0,      45.55,      -122.800",		// at home
	};

}
