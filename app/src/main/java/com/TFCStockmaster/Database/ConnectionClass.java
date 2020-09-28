package com.TFCStockmaster.Database;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    public static Connection con;
    public void setConnection(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String ip = "192.168.2.3";
            String ConnURL = "jdbc:jtds:sqlserver://"+ ip + ";port= 1430" + ";user=PLC;password=presse;databasename=MASCHINENDB;";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(ConnURL);
            Log.e("ASK","Connection Called");
        }
        catch (Exception e){
            Log.e("ASK","EXCEPTION"+e.getMessage());
        }
    }
}
