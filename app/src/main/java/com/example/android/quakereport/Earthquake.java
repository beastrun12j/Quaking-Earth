package com.example.android.quakereport;

public class Earthquake
{
   double mMagnitude;
   String mLocation;
   long mTimeINMilliseconds;
   String mUrl;

   public Earthquake(double Magnitude,String Location,long Date,String Url)
   {
       mMagnitude = Magnitude;
       mLocation = Location;
       mTimeINMilliseconds = Date;
       mUrl = Url;
   }

   public double getMagnitude() { return mMagnitude;}

   public String getLocation() { return mLocation;}

   public long getTimeINMilliseconds() { return mTimeINMilliseconds;}

   public String getUrl() { return mUrl;}

}
