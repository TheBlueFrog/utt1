package asim;

public class PhysicalDevice extends Agent
{
	public PhysicalDevice(Framework f)
	{
		super(f);
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
				
				if (isQueueEmpty() && (i < mTheFuture.length))
				{
					sleep(2000);
		
					String s = mTheFuture[i++];
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

						send(DeviceHistory.class, n);

						curtod += duration;
					}
				}
				else
				{
					Message m = takeFromQueue();
					Log.d(getID(true), String.format("Process message from %s, %s", m.mSender.getID(true), m.mMessage));
	
					consume(m);
				}
			}
		}
		catch (InterruptedException ex)
		{
		}
	}

	// some mechanism to read from the database of collected data
	// and send it periodically
	// timer maybe
	
	@Override
	protected void consume(Message msg)
	{
		// message from this world to the physical device world
		//
		//push the message to the physical device, maybe something
		// like increase sampling rate or get location now
	}
	
	static private String[] mTheFuture = 
	{
		//  DoW 	duration (sec)   activity  lat            lon
		"3,		0.333,			3,      45.55,      -122.800",		// at home
		"3,		0.010, 			4,		45.55,		-122.800",		// get up
		"3,		0.020,  	    2,      45.35,      -122.800",		// to coffee
		"3,		0.010,	     	3,      45.35,      -122.800",		// at coffee
		"3,		0.030,			0,      45.35,      -122.600",		// to work
		"3,		0.333, 			3,      45.35,      -122.600",		// at work
		"3,		0.040,			0,      45.55,      -122.800",		// to home
		"3,		 -1.0,			0,      45.55,      -122.800",		// at home
		
		"4,		0.333,			3,      45.55,      -122.800",		// at home
		"4,		0.010, 			4,		45.55,		-122.800",		// get up
		"4,		0.020,  	    2,      45.35,      -122.800",		// to coffee
		"4,		0.010,	     	3,      45.35,      -122.800",		// at coffee
		"4,		0.030,			0,      45.35,      -122.600",		// to work
		"4,		0.333, 			3,      45.35,      -122.600",		// at work
		"4,		0.040,			0,      45.55,      -122.800",		// to home
		"4,		 -1.0,			0,      45.55,      -122.800",		// at home
	};
}
