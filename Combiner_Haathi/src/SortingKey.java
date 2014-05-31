//Author @Samarth - Rohan - Devaki - Steven

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.WritableComparable;


public class SortingKey implements WritableComparable<SortingKey>{

String side;
String ticker;
String time;
String price;

	
	
public SortingKey()
{
	
}

public SortingKey(String inside, String inticker, String intime,String inprice)
{
	side=inside;
	ticker=inticker;
	time=intime;
	price=inprice;
	
}
 
@Override
  public void readFields(DataInput in) throws IOException {
 
	
	side = in.readUTF();
    ticker = in.readUTF();
    time = in.readUTF();
    price=in.readUTF();
    
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(side);
    out.writeUTF(ticker);
    out.writeUTF(time);
    out.writeUTF(price);
  }

  public String getPrice() {
	return price;
}

public void setPrice(String price) {
	this.price = price;
}

public String getSide() {
	return side;
}

public void setSide(String side) {
	this.side = side;
}

public String getTicker() {
	return ticker;
}

public void setTicker(String ticker) {
	this.ticker = ticker;
}

public String getTime() {
	return time;
}

public void setTime(String time) {
	this.time = time;
}

@Override
  public int compareTo(SortingKey o) {
	System.out.println("Sorting in compareTo");
	Double in = Double.parseDouble(price);  
	Double out = Double.parseDouble(o.getPrice());
	
	int sidecomp=side.compareTo(o.getSide()); 
	int tickercomp=ticker.compareTo(o.getTicker());
	int datecomp=time.compareTo(o.getTime());
	int pricecomp=in.compareTo(out);
	
	  if(tickercomp!=0)
		  return tickercomp;
	  else
		  if(datecomp!=0)
			  return datecomp;
		  else
	  			if
				  (sidecomp!=0)
				  return sidecomp;
	  			else
	  				return pricecomp;
  }
  
  @Override
  public int hashCode()
  {
	  return side.hashCode()+ticker.hashCode()+time.hashCode();
  }
  
  
  @Override
  public boolean equals(Object o)
  {	  if(o instanceof SortingKey )
  {
	  SortingKey sortkey = (SortingKey)o;
	  if(this.side.equals(sortkey.getSide()) && this.ticker.equals(sortkey.getTicker())&&this.time.equals(sortkey.getTime()))
	    	return true;
	  else
		  return false;
  }
	  
	    else 
	    	return false;
  }
 

}