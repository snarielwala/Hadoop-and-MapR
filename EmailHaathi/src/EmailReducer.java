//Author @Samarth - Rohan - Devaki




import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class EmailReducer
  extends Reducer<EmailKey, Text, Text, Text> 
{
  
  @Override
  public void reduce(EmailKey key, Iterable<Text> values,Context context)
      throws IOException, InterruptedException
  {
	  Integer count=0;
	  
	  for(Text value:values)
	  {	
		count++;
	  }	  
	  
	  context.write(new Text("From:"+key.from + " To:"+key.to),new Text(count.toString()) );
  }

}