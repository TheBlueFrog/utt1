package asim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import asim.agents.ActivityHappenedThisTimeOfDayBefore;
import asim.agents.ActivityHappenedThisTimeOfDayWeekAgo;
import asim.agents.SameLocationThisTimeOfDayBefore;
import asim.agents.SameLocationThisTimeOfDayWeekAgo;

public class Main
{
	private static Framework mFramework;
	private static ScoreKeeper mScoreKeeper;
	private static DeviceHistory mDeviceHistory;
	private static Agent mPhysicalDevice;
	
	public static void main(String[] args)
	{
		mFramework = new Framework();

		mScoreKeeper = new ScoreKeeper(mFramework);
		mScoreKeeper.start();

		mDeviceHistory = new DeviceHistory(mFramework);
		mDeviceHistory.start();

		try
		{
			setupAgents();
		}
		catch(IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}

		// must create this AFTER DeviceHistory because the future starts
		// at the end of history...
		
		mPhysicalDevice = new PhysicalDevice (mFramework);
		mPhysicalDevice.start();
	}
	
	// all the agents that do the actual evaluation
	
	private static List<Class<? extends Agent>> mAgents = new ArrayList<>();
	static
	{
		mAgents.add(ActivityHappenedThisTimeOfDayBefore.class);
		mAgents.add(ActivityHappenedThisTimeOfDayWeekAgo.class);
		
		mAgents.add(SameLocationThisTimeOfDayBefore.class);
		mAgents.add(SameLocationThisTimeOfDayWeekAgo.class);
	};
	
	private static void setupAgents() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		for (Class<? extends Agent> x : mAgents)
		{
			Constructor<? extends Agent> c = x.getConstructor(Framework.class);
			Agent a = c.newInstance(mFramework);
			a.start();
		}
	}


}
