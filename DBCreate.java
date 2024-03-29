package ssh;

import java.sql.*;

public class DBCreate {
    

    private Connection con;
    private Statement st;
    private String query;
    
    private final String pw = "mysql";
    
    public DBCreate() {
        
        while (true) {
        
            try{

                Class.forName("com.mysql.jdbc.Driver");

                con = DriverManager.getConnection("jdbc:mysql://localhost:3306","root",pw);
                st = con.createStatement();

                query = "CREATE DATABASE IF NOT EXISTS sshblock";     //Creating DB if doesn't exist      
                st.executeUpdate(query);

                query = "USE sshblock";
                st.executeUpdate(query);

                //Creating table if doesn't exist

                query = "CREATE TABLE IF NOT EXISTS blockedIP(\n" +
                        "    IPAddress VARCHAR (15) PRIMARY KEY,\n" +
                        "    BlockedDate DATE NOT NULL,\n" +
                        "    BlockedTime TIME NOT NULL,\n" +
                        "    ReleaseDate DATE NOT NULL,\n" +
                        "    ReleaseTime TIME NOT NULL\n" +
                        ")";

                st.executeUpdate(query);
                
                query = "CREATE TABLE IF NOT EXISTS subnets(\n" +
                        "    IPAddress VARCHAR (15) PRIMARY KEY,\n" +
                        "    IPrangeStart VARCHAR (15),\n" +
                        "    IPrangeEnd VARCHAR (15),\n" +
                        "    AddedDate DATE NOT NULL,\n" +
                        "    AddedTime TIME NOT NULL" +
                        ")";
                
                st.executeUpdate(query);

                query = "SET global max_connections = 100000";            
                st.executeUpdate(query);
                
                query = "CREATE DATABASE IF NOT EXISTS googlemap";     //Creating DB if doesn't exist      
                st.executeUpdate(query);

                query = "USE googlemap";
                st.executeUpdate(query);

                //Creating table if doesn't exist

                query = "CREATE TABLE IF NOT EXISTS reportIP(\n" +
                        "    IPAddress VARCHAR (15) PRIMARY KEY,\n" +
                        "    BlockedDate DATE NOT NULL,\n" +
                        "    BlockedTime TIME NOT NULL\n" +
                        ")";

                st.executeUpdate(query);

                break;

            }catch(Exception ex){
                
                //Process waits 1 second until apache web server and mysql server starts
                //System.out.println(ex);

            }
            
        }
        
    }
    
}
