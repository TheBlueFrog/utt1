package asim;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Util
{
	private static final String TAG = Util.class.getSimpleName();

	public static final long OneMinutems = (60L * 1000L);
	public static final long TenMinutesms = (10L * OneMinutems);
	public static final long OneHourms = (60L * OneMinutems);
	public static final long OneDayms = (24L * OneHourms);
	public static final long OneYearms = (365L * OneDayms);

	public static final double OneDay = 1.0;
	public static final double OneWeek = (7.0 * OneDay);
	public static final double OneHour = (OneDay / 24.0);
	public static final double OneMinute = (OneHour / 60.0);
	public static final double OneSecond = (OneMinute / 60.0);
	public static final double TenMinutes = (10.0 * OneMinute);

	// if two locations are this far apart (in meters) we consider them to 
	// be the same location, this is something like twice the average
	// accuracy of GPSs, sorta, very empirical
	public static final double SameLocationThreshold = 35.0;


	
	public static double metersBetween(LatLng a, LatLng b) 
	{
		return metersBetween(a.mLat, a.mLon, b.mLat, b.mLon);
	}
	static public double metersBetween(double lat1, double lon1, double lat2, double lon2)
	{
		//Spherical	law of cosines:	d = acos( sin(??1).sin(??2) + cos(??1).cos(??2).cos(????) ).R
		double R = 6371; // km
		double lat1r = Math.toRadians(lat1);
		double lat2r = Math.toRadians(lat2);
		double lon1r = Math.toRadians(lon1);
		double lon2r = Math.toRadians(lon2);
		
		// can get 1.000000000002 which blows up acos()
		double z = (Math.sin(lat1r) * Math.sin(lat2r) ) + ( Math.cos(lat1r) * Math.cos(lat2r) * Math.cos(lon2r - lon1r));
		if (z > 1.0)
			z = 1.0;
		
		double d = Math.acos(z) * R;	
		return d * 1000.0; // get to meters
	}

	
	static private Map<Integer, Integer> mDaysSince2010 = new HashMap<Integer, Integer>();
	static 
	{
		mDaysSince2010.put(2010, 0);
		mDaysSince2010.put(2011, mDaysSince2010.get(2010) + 365);	
		mDaysSince2010.put(2012, mDaysSince2010.get(2011) + 365);	
		mDaysSince2010.put(2013, mDaysSince2010.get(2012) + 366);	
		mDaysSince2010.put(2014, mDaysSince2010.get(2013) + 365);	
		mDaysSince2010.put(2015, mDaysSince2010.get(2014) + 365);
	}
	
	/** convert millisecond time to decimal time using our own zero of Jan 1 2010 */
	static public double asDecimalTime (long t)
	{
		// want some kind of linear day and fractional day, e.g. 123.57 for day 123 early afternoon
		// have a time zone issue though... for now do it in the local time zone, this is wrong
		// and needs more thought
		
//		DBTools.m_dateFormat2.format(new Date(t));
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t);

		int year = cal.get(Calendar.YEAR);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		int day2010 = mDaysSince2010.get(year) + doy;
		
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		return ((double) day2010) + ((((double) hour) * (60.0 * 60.0)) + (((double) minute) * 60.0) + second) / (24.0 * 60.0 * 60.0);
	}
	
	/** 
	 * @param day a decimal time
	 * @return day-of-week (1..7) given a decimal day
	 * 
	 * we know day zero is Jan 1, 2010, that was a Friday, hence all (day % 7) 
	 * which are 0 are Friday too
	 */
	public static int getDayOfWeek(double day)
	{
		int d = ((Double) day).intValue() % 7;
		return (d + 2) % 7;	// shift right from Friday to Sunday
	}
	
	/** convert millisecond duration to decimal duration */
	static public double asDecimalDuration (long d)
	{
		// same units as above but this is for a duration not a timestamp
		
		long days = d / OneDayms;
		long x = d % OneDayms;								// drop bits above 1 day
		long hour = x / (60L * 60L * 1000);							// isolate hours
		long minute = (x % (60L * 60L * 1000)) / (60L * 1000);		// isolate minutes
		long second = (x % (60L * 1000)) / (1000);					// seconds
		
		return days + (((((double) hour) * (60.0 * 60.0)) + (((double) minute) * 60.0) + second) / (24.0 * 60.0 * 60.0));
	}

	/** ensure a computed decimal day is only the fractional
	 * part, e.g. equivalent of integer %
	 */
	public static double asTimeOfDay(double d)
	{
		return d - Math.floor(d);
	}
	
	public static double toStartOfDay(double d)
	{
		return Math.floor(d);
	}
	
	/** convert decimal duration to millisecond duration */
	static public long asMSDuration (double d)
	{
		double fraction = d - Math.floor(d);
		return Math.round(fraction * OneDayms);
	}

	static public void log(String tag, FileOutputStream fos, String msg)
	{
		if (fos == null)
		{
			Log.d(tag, msg);
			return;
		}
		
		try
		{
			fos.write(msg.getBytes());
			fos.write("\n".getBytes());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static double daysBetween(double a, double b)
	{
		return Math.abs(toStartOfDay(a) - toStartOfDay(b));
	}
	
	/** convert string of form "hh::mm" (24hr day) to decimal time-of-day */
	public static double asTimeOfDay(String s)
	{
		String[]v1 = s.split(":");
		int hour = Integer.parseInt(v1[0]);
		int minutes = Integer.parseInt(v1[1]);
		return (((double) hour) * OneHour) + (((double) minutes) * OneMinute);
	}


	

}
