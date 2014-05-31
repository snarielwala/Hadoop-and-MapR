//Author @Samarth - Rohan - Devaki


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EmailMapper
  extends Mapper<LongWritable, Text, EmailKey, Text> {

	
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException 
  {	  		
	  String input = value.toString();
	  input = input.substring(0, input.indexOf("X-From:"));
	  System.out.println(input);
	  int sfrom,sto;
	  int efrom,eto;
	  int scc,ecc;
	  int sbcc,ebcc;
	  String from;
	  ArrayList<String> toList = new ArrayList<String>();
	  StringTokenizer token; 
	  
	  
	  	sfrom = input.indexOf("From:")+6;
	  	efrom = input.indexOf(" ", sfrom);	
	  		from = input.substring(sfrom, efrom);
	  		//System.out.println("From:"+from);
	  	
	  	if(input.indexOf("To:")!=-1)
	  	{  	sto = input.indexOf("To:")+4;
		  	eto = input.indexOf("Subject:", sto);
		  	//System.out.println("To:"+input.substring(sto, eto));
		  	if(eto>sto)
		  	{	token = new StringTokenizer(input.substring(sto,eto),",");
		  		
		  		while(token.hasMoreTokens())
		  		{	String temp=token.nextToken().trim();
		  			if(!toList.contains(temp))
		  			toList.add(temp);
		  		}
		  	}
	  	}
	  	
//	  	if(input.indexOf("Cc:")!=-1)	
//	  	{  	scc= input.indexOf("Cc:")+4;
//		  	ecc= input.indexOf("Mime-Version:");
//		  	
//		  	token = new StringTokenizer(input.substring(scc,ecc),",");
//	  		
//	  		while(token.hasMoreTokens())
//	  		{	String temp=token.nextToken().trim();
//	  			if(!toList.contains(temp))
//	  			toList.add(temp);
//	  		}
//	  	}			
//	  
//	  	if(input.indexOf("Bcc:")!=-1)	
//	  	{  	sbcc= input.indexOf("Bcc:")+5;
//		  	ebcc= input.indexOf("X-From:");
//		  	
//		  	token = new StringTokenizer(input.substring(sbcc,ebcc),",");
//	  		
//	  		while(token.hasMoreTokens())
//	  		{	String temp=token.nextToken().trim();
//	  			if(!toList.contains(temp))
//	  			toList.add(temp);
//	  		}
//	  	}			
	  	
	  	Iterator<String> it = toList.iterator();
	  	
	  	while(it.hasNext())
	  	{
			context.write(new EmailKey(from,it.next()),new Text("1"));
					
		}
		
  }
}
  
