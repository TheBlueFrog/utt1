package asim;

import asim.agents.Environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main
{
	public static Framework mFramework;
	private static Agent mPhysicalDevice;
	
	public static void main(String[] args)
	{
		mFramework = new Framework(new Environment());

		mPhysicalDevice = new PhysicalDevice (mFramework);
		mPhysicalDevice.start();
	}


}
