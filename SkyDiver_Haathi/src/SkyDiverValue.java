//Author @Samarth - Rohan - Devaki - Steven
//This is the composite Value 
//The class implements the Writable interface
//The class over rides the readFields and the write methods
//The class also contains the getter setters for all the class variables 
//Contains the windspeed, visibility, temperature and pressure information


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


public class SkyDiverValue implements Writable {

  private String windspeed;
  private String visibility;
  private String temperature;
  private String pressure;
  
  
  public String getWindspeed() {
	return windspeed;
}

public void setWindspeed(String windspeed) {
	this.windspeed = windspeed;
}

public String getVisibility() {
	return visibility;
}

public void setVisibility(String visibility) {
	this.visibility = visibility;
}

public String getTemperature() {
	return temperature;
}

public void setTemperature(String temperature) {
	this.temperature = temperature;
}

public SkyDiverValue()
{
	
}

public SkyDiverValue(String windspeed, String visibility, String temperature,String pressure) {
	
	this.windspeed = windspeed;
	this.visibility = visibility;
	this.temperature = temperature;
	this.pressure = pressure;
}


  @Override
  public void readFields(DataInput in) throws IOException {
    windspeed = in.readUTF();
    visibility = in.readUTF();
    temperature = in.readUTF();
    pressure=in.readUTF();
    
    
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(windspeed);
    out.writeUTF(visibility);
    out.writeUTF(temperature);
    out.writeUTF(pressure);
  }

public String getPressure() {
	return pressure;
}

public void setPressure(String pressure) {
	this.pressure = pressure;
}


}
