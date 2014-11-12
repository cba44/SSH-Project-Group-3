//Program 1

public class Iplist {
    
        
    private String ipaddress;
    private int count = 1;
    
    public Iplist(String ipaddress){
        
        this.ipaddress = ipaddress;
        
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
