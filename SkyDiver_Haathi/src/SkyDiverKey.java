//Author @Samarth - Rohan - Devaki - Steven
//This is the composite key for our map
//The class implements the Writable Comparable interface
//The class over rides the hashcode, equals, compareTo, readFields and the write methods
//The class also contains the getter setters for all the class variables 

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


public class SkyDiverKey implements WritableComparable<SkyDiverKey>{

  private String latitude;	//the key is a triple containing location information and the month
  private String longitude;
  private String month;
  
  
  //getter setters for all the variables 
  
  public String getLatitude() {
	return latitude;
}

public void setLatitude(String latitude) {
	this.latitude = latitude;
}

public String getLongitude() {
	return longitude;
}

public void setLongitude(String longitude) {
	this.longitude = longitude;
}

public String getMonth() {
	return month;
}

public void setMonth(String month) {
	this.month = month;
}

public SkyDiverKey()
{
	
}

public SkyDiverKey(String latitude, String longitude, String month) {
	
	this.latitude = latitude;
	this.longitude = longitude;
	this.month = month;
}

@Override
  public void readFields(DataInput in) throws IOException {
    latitude = in.readUTF();
    longitude = in.readUTF();
    month = in.readUTF();
    
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(latitude);
    out.writeUTF(longitude);
    out.writeUTF(month);
  }

  @Override
  public int compareTo(SkyDiverKey o) {
	
	  int latcomp= latitude.compareTo(o.getLatitude());
	  int loncomp = longitude.compareTo(o.getLongitude());
	  int moncomp = month.compareTo(o.getMonth());
	  
	  if(latcomp!=0)
		  return latcomp;
	  else
		  if(loncomp!=0)
			  return loncomp;
		  else
			  return moncomp;
  
  }
  
  @Override
  public int hashCode()
  {
	  return latitude.hashCode()+longitude.hashCode()+month.hashCode();
  }
  
  
  @Override
  public boolean equals(Object o)
  {	  if(o instanceof SkyDiverKey )
  {
	  SkyDiverKey skykey = (SkyDiverKey)o;
	  if(this.latitude.equals(skykey.getLatitude()) && this.longitude.equals(skykey.getLatitude())&&this.month.equals(skykey.getMonth()))
	    	return true;
	  else
		  return false;
  }
	  
	    else 
	    	return false;
  }
 

}