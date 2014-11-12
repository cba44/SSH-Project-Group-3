//Program 1 Blockip.java contains main method

import java.io.*;
import java.util.ArrayList;

class Blockip {
    
    public static void main(String [] args){
    
        String fileName = "auth_new.log";
        ExtractIP word;
        DBCreate dbcon = new DBCreate();    //Creating the database, table if they don't exist
        
        ArrayList <Iplist> myIPlist = new ArrayList <Iplist>();
  
        try {
            
	    FileReader fileRd = new FileReader(fileName); 
	    BufferedReader bufferRd = new BufferedReader(fileRd);
            
	    String line = null;
            
            while (true){
                
                line = bufferRd.readLine();
            
                if(line != null) { 
                
                    String [] s = line.split(" ");
                
                    if (s.length != 1 && (!s[s.length - 1].equals("[preauth]"))){    //IP addresses when [preauth] is not there
                    
                        for(int i=0; i < s.length; i++){
                            String [] l = s[i].split(":");      //Some lines have ":" after the ip address
                        
                            for(int k=0; k < l.length; k++){
                        
                                word = new ExtractIP(l[k]);     
                            
                                if (word.matchIP()){        //If word matches with the IP address regular expression, ipContains method called
                                
                                    ipContains(myIPlist,l[k]);      //If there is same invalid IP 5 times, it will be blocked
                                
                                }
                                                        
                            }
                    
                        }   
                    
                    }
                
        	}else {     //To daemonize the program
                    
                    try{
                    
                        Thread.sleep(1000); //http://stackoverflow.com/questions/557844/java-io-implementation-of-unix-linux-tail-f
                        
                    }catch(Exception ex){
                        
                        System.out.println(ex);
                        
                    }
                    
                }
            
            }

	} catch (FileNotFoundException x) {
            
	    System.out.println("Make sure " + fileName + " is also here!");
	    System.exit(-1);
            
	} catch (IOException x) {
            
	    System.out.println(x);
	    System.exit(-1);
            
	} 
    
    }
    
    private static void ipContains(ArrayList <Iplist> a,String s){
        
        Iplist myIP;
        DBConnect connect = new DBConnect();    //Creating connection with the database and table
        
        for (int i = 0 ; i < a.size() ; i++){
            
            int count;
            
            if (a.get(i).getIP().equals(s)){        //Check if current IP is in array list
                
                count = a.get(i).getCount();
                
                if (count == 5 && !connect.contIP(s)){      //When IP caught 5 times and still isn't in array list...
                    
                    connect = new DBConnect();
                    a.remove(i);        //Deleting IP from the array list
                    connect.addData(s);     //Adding IP to database
                    
                    return;
                    
                }else if(count == 5 && connect.contIP(s)){      //When IP caught 5 times and it is in array list...
                    
                    a.remove(i);    //Deleting IP from the array list
                    return;
                    
                }else{
                
                    a.get(i).incCount();        //If IP is in array list, but caught less than 5 times
                    return; 
                
                }
                
            }
            
        }
        
        myIP = new Iplist (s);  
        a.add(myIP);    //If IP is not in array list, inserting it
        
    }
    
}
