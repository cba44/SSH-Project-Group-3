//Program 1

package ssh;

import java.util.regex.*;

public class ExtractIP {

    private String ipAddressRegex = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        //http://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
    
    private Pattern ipPattern = Pattern.compile(ipAddressRegex);
    
    private String word;
    
    public ExtractIP(String word){
        
        this.word = word;
        
    }
    
    public boolean matchIP(){       //Matching word with IP address pattern
        
        Matcher ipMatch = ipPattern.matcher(this.word);
        return ipMatch.matches();
        
    }
    
}
