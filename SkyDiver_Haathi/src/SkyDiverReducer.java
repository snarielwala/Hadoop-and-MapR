//Author @Samarth - Rohan - Devaki - Steven
//This class SkyDiverReducer is the reducer
//This class calculates the average value for all values corresponding to a particular key 
//Further checks to see if the values fall in the accepted range 
//If so that key value pair is added to the output 



import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SkyDiverReducer
  extends Reducer<SkyDiverKey, SkyDiverValue, Text, Text> {
  
  @Override
  public void reduce(SkyDiverKey key, Iterable<SkyDiverValue> values,
      Context context)
      throws IOException, InterruptedException {
    
	  double total_temp=0,avg_temp = 0;
	  double total_visi=0,avg_visi = 0;
      double total_wind=0,avg_wind =0;
      double total_pressure=0,avg_pressure =0;
      int size=0;
      
     // for all values calculate the sum total of the values 
      
    for (SkyDiverValue value : values) 
    {
    	size++;
    	if(value.getTemperature().startsWith("-"))
    			{
    				total_temp+=Double.parseDouble(value.getTemperature());
    			}	
    	else
    				total_temp+=Double.parseDouble(value.getTemperature().substring(1));
    	
    				total_visi+=Double.parseDouble(value.getVisibility());
    				total_wind+=Double.parseDouble(value.getWindspeed());
    				total_pressure+=Double.parseDouble(value.getPressure());
    				
	 }
  //calculating the average values 
    
    avg_temp=(total_temp/size)/10; 
    avg_visi=total_visi/size;
    avg_wind=(total_wind/size)/10;
    avg_pressure=(total_pressure/size)/10;
    double latitude = Double.parseDouble(key.getLatitude())/1000;
    double longitude = Double.parseDouble(key.getLongitude())/1000;
    
    //check if the values fall in the acceptable range 
    
    if(avg_temp<=15 && avg_temp>=-5 &&avg_wind>3&& avg_wind<11 && avg_pressure>800 && avg_pressure<1050)
    context.write(new Text("Lat:"+latitude +" "+"Lon:"+longitude+" "+"Month:"+key.getMonth()), new Text("avg_temp="+avg_temp + " avg_wind="+avg_wind + " avg_visi="+avg_visi +"avg_pressure:"+avg_pressure));
  }
}