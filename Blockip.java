//Program 1 Blockip.java contains main method

package ssh;

import java.io.*;
import java.util.ArrayList;
import org.apache.commons.daemon.*;

public class Blockip implements Daemon {
    
    public static void main(String [] args){
        
        File fileName = new File("/home/chiran/log/auth_new.log");
        FileReader fileRd;
        BufferedReader bufferRd;
    
     //   String fileName = "auth_new.log";
        ExtractIP word;
        DBCreate dbcon = new DBCreate();    //Creating the database, table if it doesn't exist
        
        ArrayList <Iplist> myIPlist = new ArrayList <Iplist>();
  
        try {
            
	    fileRd = new FileReader(fileName.getAbsolutePath()); 
	    bufferRd = new BufferedReader(fileRd);
            
	    String line = null;
            
            while (true){
                
                line = bufferRd.readLine();
            
                if(line != null) { 
                
                    String [] s = line.split(" ");
                
                    if (s.length != 1 && (!s[s.length - 1].equals("[preauth]"))){    //IP addresses when [preauth] is not there
                    
                        if (s[6].equals("Invalid") || (s[6].equals("error:") && s[7].equals("PAM:"))){
                            
//                            System.out.println(line);
                        
                            for(int i=0; i < s.length; i++){
                                String [] l = s[i].split(":");      //Some lines have ":" after the ip address

                                for(int k=0; k < l.length; k++){

                                    word = new ExtractIP(l[k]);     

                                    if (word.matchIP()){        //If word matches with the IP address regular expression, ipContains method called

                                        ipContains(myIPlist,l[k],s[0],s[2],s[3]);      //If there is same invalid IP 5 times, it will be blocked

                                    }

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
    
    //http://stackoverflow.com/questions/7687159/how-to-convert-a-java-program-to-daemon-with-jsvc
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println("initializing ...");
    }
    
    @Override
    public void start() throws Exception {
        System.out.println("starting ...");
        main(null);     //Running the main method
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopping ...");
    }

    @Override
    public void destroy() {
        System.out.println("done.");
    }
    
    private static void ipContains(ArrayList <Iplist> a,String s,String str1,String str2,String str3){
        
        Iplist myIP;
        DBConnect connect = new DBConnect();    //Creating connection with the database and table
        
        for (int i = 0 ; i < a.size() ; i++){
            
            int count;
            
            if (a.get(i).getIP().equals(s)){        //Check if current IP is in array list
                
                count = a.get(i).getCount();
                
                if (count == 5){      //When IP caught 5 times and still isn't in array list...
                    
                    connect = new DBConnect();
                    connect.addData(s);     //Adding IP to database
                    a.remove(i);        //Deleting IP from the array list
                    
                    return;
                    
                }else{
                
                    a.get(i).incCount();        //If IP is in array list, but caught less than 5 times
                    return; 
                
                }
                
            }
            
        }
        
        if (!connect.contIP(s)){
        
            myIP = new Iplist (s,str1,str2,str3);  
            a.add(myIP);    //If IP is not in array list and not in database , inserting it
        
        }
        
    }
    
}

