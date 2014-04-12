package asim;


public class PhysicalDevice extends Agent
{
	private static final String TAG = PhysicalDevice.class.getSimpleName();
	
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
		boolean atEndOfDay = false;
		
		try
		{
			while (true)
			{
				// we inject an event every couple of seconds until there is no more future
				
				if (isQueueEmpty())
				{
					sleep(2000);

					// inject an event, ones we inject have to fit together with the
					// DeviceHistory events because we use one 'clock' to set the
					// time the event happened
					
					int dow = Util.getDayOfWeek(DeviceHistory.mGlobalTime);
					
					String data = ((dow == 0) || (dow == 6)) 
							? DeviceHistory.mPrototypeWeekendDay.get(i++) 
							: DeviceHistory.mPrototypeWorkDay.get(i++);
					
					String[] v = data.split(",");
					double duration = Double.parseDouble(v[0].trim());
					if (duration < 0.0)
					{
						// fill out rest of day
						duration = 1.0 - curtod;
						i = 0;
						atEndOfDay = true;
					}

					StateNode n = new StateNode(
							DeviceHistory.mGlobalTime,
							dow, 
							curtod,
							duration,
							Integer.parseInt(v[1].trim()),
							new LatLng (Double.parseDouble(v[2].trim()), Double.parseDouble(v[3].trim())));

					Log.d(TAG, "Inject event " + n.toString());
					
					send(DeviceHistory.class, n);

					curtod = Util.asTimeOfDay(curtod + duration);
					DeviceHistory.mGlobalTime += duration;
					
					// rounding error
					if (atEndOfDay)
					{
						DeviceHistory.mGlobalTime = Math.round(DeviceHistory.mGlobalTime);
						atEndOfDay = false;
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

	// some mechanism to read from the database of collected data
	// and send it periodically
	// timer maybe
	
	@Override
	protected void consume(Message m)
	{
		// message from this world to the physical device world
		//
		//push the message to the physical device, maybe something
		// like increase sampling rate or get location now

		Log.d(TAG, String.format("NYI - consume message from %s, %s", m.mSender.getID(true), m.mMessage));
	}
	
	static private String[] mTheFuture = 
	{
	//  duration (sec)  activity  lat          lon
		"0.333,			3,      45.55,      -122.800",		// at home
 		"0.010, 		4,		45.55,		-122.800",		// get up
		"0.020,  	    2,      45.35,      -122.800",		// to coffee
		"0.010,	     	3,      45.35,      -122.800",		// at coffee
		"0.030,			0,      45.35,      -122.600",		// to work
		"0.333, 		3,      45.35,      -122.600",		// at work
		"0.040,			0,      45.55,      -122.800",		// to home
		" -1.0,			0,      45.55,      -122.800",		// at home
		
		"0.333,			3,      45.55,      -122.800",		// at home
 		"0.010, 		4,		45.55,		-122.800",		// get up
		"0.020,  	    2,      45.35,      -122.800",		// to coffee
		"0.010,	     	3,      45.35,      -122.800",		// at coffee
		"0.030,			0,      45.35,      -122.600",		// to work
		"0.333, 		3,      45.35,      -122.600",		// at work
		"0.040,			0,      45.55,      -122.800",		// to home
		" -1.0,			0,      45.55,      -122.800",		// at home
		/*		
		 */
	};
}
