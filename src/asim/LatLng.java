package asim;

public class LatLng
{
	public double mLat;
	public double mLon;
	
	public LatLng (double lat, double lon)
	{
		mLat = lat;
		mLon = lon;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%9.5f, %9.5f)", mLat, mLon);
	}
}
