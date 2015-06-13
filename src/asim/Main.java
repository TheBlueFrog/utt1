package asim;

import asim.agents.Environment;
import asim.agents.Simulator;

public class Main
{
	public static Simulator mSimulator;
	private static Entity mPhysicalDevice;
	
	public static void main(String[] args)
	{
		mSimulator = new Simulator();

		mPhysicalDevice = new PhysicalDevice (mFramework);
		mPhysicalDevice.start();
	}


}
