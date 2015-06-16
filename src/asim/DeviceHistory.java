package asim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceHistory extends Entity
{
	static public double mGlobalTime = 0;

	// assemble this much history from external places
	public int numDaysOfHistory = 7;
	
	private Random mRandom;
	
	public DeviceHistory()
	{
		super(null);

		mRandom = new Random(57);
		
		mGlobalTime = 1000; // units
	}
	
}
