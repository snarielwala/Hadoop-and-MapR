//Author @Samarth - Rohan - Devaki - Steven
//This class SkyDiverReducer is the mapper 
//This class extracts the key and values from the input data and creates a map of the same 
//Before creating a map checks for invalid and missing data as well 
//The output of this class is a <Key,Value> pair containing the SkyDiverKey and the SkyDiverValue 

import java.io.IOException;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SkyDiverMapper
  extends Mapper<LongWritable, Text, SkyDiverKey, SkyDiverValue> {

  private static final String miss_temp_wind = "9999";
  private static final String miss_vis = "999999";
  private static final String miss_lat = "+99999";
  private static final String miss_lon = "+999999";
  
  
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    
	
   String line = value.toString();
  // Text temp_key = new Text(line.substring(28,34)+" "+line.substring(34,41)+" "+line.substring(19,21));
   SkyDiverKey sky_key = new SkyDiverKey(line.substring(28,34),line.substring(34,41),line.substring(19,21));
   SkyDiverValue sky_value = new SkyDiverValue(line.substring(65,69),line.substring(78,84),line.substring(87,92),line.substring(99,104));
    
    String qualityofWind = line.substring(69,70);
    String qualityofTemp = line.substring(92, 93);
    String qualityofVis = line.substring(84, 85);
    
    boolean qualityCheck = qualityofWind.matches("[01459]")&&qualityofTemp.matches("[01459]")&&qualityofVis.matches("[01459]");
    
    if (isNotValueMissing(sky_value)&&qualityCheck) {
    	
      context.write(sky_key, sky_value);
    }
  }

public static boolean isNotKeyMissing(SkyDiverKey key){
	if(!key.getLatitude().equals(miss_lat)&&!key.getLongitude().equals(miss_lon))
		return true;
	else
		return false;
}

public static boolean isNotValueMissing(SkyDiverValue value)
{
	if(!value.getTemperature().equals(miss_temp_wind)&&!value.getVisibility().equals(miss_vis)&&!value.getWindspeed().equals(miss_temp_wind))
		return true;
	else
		return false;
	
}


}