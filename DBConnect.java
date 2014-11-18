//Program 1

package ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String query;
    
    private File file = new File("/home/chiran/Desktop/mylog.log");
    private FileWriter writer;
    private BufferedWriter bwriter;
    
    private final String pw = "mysql";
    
    public DBConnect(){
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sshblock","root",pw);
            st = con.createStatement();
            
        }catch(Exception ex){
            
            System.out.println(ex);
            
        }
        
    }
    
    public void getData(){      //Selecting every data from database
        
        try{
            
            query = "SELECT * FROM blockedIP";
            rs = st.executeQuery(query);
            
            while (rs.next()){
            
               String IPAddress = rs.getString("IPAddress");
               String bd = rs.getString("BlockedDate");
               String bt = rs.getString("BlockedTime");
               String relD = rs.getString("ReleaseDate");
               String relT = rs.getString("ReleaseTime");
               
               System.out.println("IP "+IPAddress+" "+bd+" "+bt+" "+relD+" "+relT);
            
            }
            
            
        }catch(Exception ex){
            
            System.out.println(ex);
            
        }
        
    }
    
    private static java.sql.Date getCurrentJavaSqlDate() { //http://www.java2s.com/Code/JavaAPI/java.sql/PreparedStatementsetTimeintparameterIndexTimex.htm
        
        java.util.Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
        
    }

    private static java.sql.Time getCurrentJavaSqlTime() {   //http://www.java2s.com/Code/JavaAPI/java.sql/PreparedStatementsetTimeintparameterIndexTimex.htm
        
        java.util.Date date = new java.util.Date();
        return new java.sql.Time(date.getTime());
    
    }
        
    public void addData(String s){  //http://alvinalexander.com/java/java-mysql-insert-example-preparedstatement
        
        try{
            
            query = "INSERT INTO blockedIP VALUES (?, ?, ?, ?, ?);";
            
            PreparedStatement preparedStmt = con.prepareStatement(query);
            
            preparedStmt.setString (1, s);
            
            java.sql.Time time = getCurrentJavaSqlTime();       //System.out.println(time);
            java.sql.Date date = getCurrentJavaSqlDate();       //System.out.println(date);
            
            String timeSt = time.toString();
            SimpleDateFormat df = new SimpleDateFormat ("HH:mm:ss"); 
                //http://stackoverflow.com/questions/9015536/java-how-to-add-10-mins-in-my-time
            java.util.Date d = df.parse(timeSt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.SECOND, 5);
            String newTime = df.format(cal.getTime());
            long ms = df.parse(newTime).getTime();
            java.sql.Time time1 = new Time(ms);
            
            java.sql.Date date1;
            
            long ms1 = df.parse("00:00:00").getTime();
            java.sql.Time timeA = new Time(ms1);
            
            long ms2 = df.parse("01:00:00").getTime();
            java.sql.Time timeB = new Time(ms2);
            
            
            //If expiry time passes 12 midnight while added time doesn't pass 12 midight , 1 day should be added manually
            
            
            if ((time1.after(timeA) && time1.before(timeB)) || time1.equals(timeA)){
                
                String dateSt = date.toString();
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dDay = df1.parse(dateSt);
                Calendar calDate = Calendar.getInstance();

                calDate.add(Calendar.DATE, 1);
                String newDate = df1.format(calDate.getTime());
                long msDate = df1.parse(newDate).getTime();
                date1 = new Date(msDate);
            
            }else date1 = date;
            
            preparedStmt.setDate (2,date);
            preparedStmt.setTime (3,time);
            preparedStmt.setDate (4,date1);
            preparedStmt.setTime (5,time1);  
            
            preparedStmt.executeUpdate();
            
            String myStr = date + "\t" + time + "\tiptables -A INPUT -s " + s + " -j DROP";
            
            if(!file.exists()){
                
    		file.createNewFile();
                        
            }
            
            System.out.println(myStr);
            
            writer = new FileWriter(file.getAbsolutePath(),true);
            bwriter = new BufferedWriter(writer);
            bwriter.write(myStr+"\n");
            bwriter.close();
            
            
        }catch(Exception ex){
            
            System.out.println(ex);
            
        }   
        
    }
    
    public Boolean contIP(String s){        //Check if given IP already exists in DB
            
        try{
            
            query = "SELECT IPAddress FROM blockedIP WHERE IPAddress = '" + s + "'";//System.out.println(query);
            rs = st.executeQuery(query);
            
            while (rs.next()){
            
               String IPAddress = rs.getString("IPAddress");
               
               if (s.equals(IPAddress)){

                   return true;
                   
               }
            
            };
               
        }catch (Exception ex){
               
            System.out.println(ex);
               
        }
    
        return false;
        
    }
    
}
