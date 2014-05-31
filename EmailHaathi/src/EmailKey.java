//Author @Samarth - Rohan - Devaki

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.hadoop.io.WritableComparable;


public class EmailKey implements WritableComparable<EmailKey>{

String from;
String to;

	
	
public EmailKey()
{
	
}

public EmailKey(String infrom, String into)
{
	from=infrom;
	to=into;
}
 
@Override
  public void readFields(DataInput in) throws IOException {
 
	
	from = in.readUTF();
    to = in.readUTF();
    
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(from);
    out.writeUTF(to);
    
  }

@Override
  public int compareTo(EmailKey o) {
	if(from.compareTo(o.from)!=0)
		return
				from.compareTo(o.from);
	else 
		return to.compareTo(o.to);
}
  
  @Override
  public int hashCode()
  {
	  return from.hashCode()+to.hashCode();
  }
  
  
  @Override
  public boolean equals(Object o)
  {	return true;
  }

public String getFrom() {
	return from;
}

public void setFrom(String from) {
	this.from = from;
}

public String getTo() {
	return to;
}

public void setTo(String to) {
	this.to = to;
}
 

}