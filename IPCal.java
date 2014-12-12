
package ssh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IPCal {
    
    private long IPlong;
    
    public long IPint(String ip){
        
        String [] s = ip.split("\\.");
        int i1 = Integer.parseInt(s[0]);
        int i2 = Integer.parseInt(s[1]);
        int i3 = Integer.parseInt(s[2]);
        int i4 = Integer.parseInt(s[3]);
        
        this.IPlong = 1l * i4 + 256l * i3 + 65536l * i2 + 16777216l * i1;
        
        return IPlong;
        
    }
        
    public String findRange (long myIP){
        
        String fileName = "GeoIPCountryWhois.csv";
        
        try { 
            
	    FileReader fileRd = new FileReader(fileName); 
	    BufferedReader bufferRd = new BufferedReader(fileRd);
	    String line = null; 
            
	    while( (line = bufferRd.readLine()) != null) { 
                
		String [] s = line.split(",");
                
                String [] s1 = s[2].split("\"");
                long ip1 = Long.parseLong(s1[1], 10);
                
                String [] s2 = s[3].split("\"");
                long ip2 = Long.parseLong(s2[1], 10);
                    
                if (ip2 > myIP && ip1 < myIP){
                    
                    String [] ipRng1 = s[0].split("\"");
                    String [] ipRng2 = s[1].split("\"");
                    
                    String k = ipRng1[1] + " " + ipRng2[1];
                    return k;
                    
                }
                
	    }
            
	    fileRd.close();
	    bufferRd.close();

	} catch (FileNotFoundException x) { 
            
	    System.out.println("Make sure " + fileName + " is also here!");
	    System.exit(-1);
            
	} catch (IOException x) { 
            
	    System.out.println(x);
	    System.exit(-1);
            
	}
        
        return null;
        
    }  
    
}
