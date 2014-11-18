//Program 1

package ssh;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Iplist {
    
        
    private String ipaddress;
    private int count = 1;
    private Date date;
    
    
    public Iplist(String ipaddress,String str1,String str2,String str3){
        
        this.ipaddress = ipaddress;
        
        String str = str1 + "-" + str2 + " "+str3;
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd hh:mm:ss");    //http://stackoverflow.com/questions/18604408/convert-java-string-to-time-not-date
        Date date = null;
        
        try {
            
            date = sdf.parse(str);
            
        } catch (Exception ex) {
            
            System.out.println(ex);
            
        }
        
        this.date = date;   //To check the time for 5 unsuccessful attempts
        
    }
    
    public String getIP(){
        
        return this.ipaddress;
        
    }
    
    public int getCount(){
        
        return count;
        
    }
    
    public void incCount(){
        
        count++;
        
    }
    
}


