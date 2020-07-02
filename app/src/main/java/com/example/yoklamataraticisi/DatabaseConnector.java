package com.example.yoklamataraticisi;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private String server_name,databaseName,dbUserName,dbPassword,port;

    public DatabaseConnector(){
        server_name="sql5053.site4now.net";
        databaseName="DB_A61CB0_doguskukul";
        dbUserName="DB_A61CB0_doguskukul_admin";
        dbPassword="789*****dD";
        port="1433";
    }

    public Connection Baglanti(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://"+server_name+":"+port+"/"+databaseName;
            connection = DriverManager.getConnection(ConnectionURL,dbUserName,dbPassword);
        }
        catch (ClassNotFoundException e) {
            System.out.println("error1");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("error2");
            e.printStackTrace();
        }
        return connection;
    }
}
