package asim;

import asim.agents.HasHappenedBefore;

public class Main
{
	private static Framework mFramework;
	private static ScoreKeeper mScoreKeeper;
	private static DeviceHistory mDeviceHistory;
	private static Agent mPhysicalDevice;
	private static HasHappenedBefore mHasHappenedBefore;

	public static void main(String[] args)
	{
		mFramework = new Framework();

		mHasHappenedBefore = new HasHappenedBefore(mFramework);
		mHasHappenedBefore.start();
		
		mScoreKeeper = new ScoreKeeper(mFramework);
		mScoreKeeper.start();

		mDeviceHistory = new DeviceHistory(mFramework);
		mDeviceHistory.start();

		mPhysicalDevice = new PhysicalDevice (mFramework);
		mPhysicalDevice.start();
	}


}
