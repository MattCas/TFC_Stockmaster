package com.TFCStockmaster.Database;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Matthias Casula on 2/12/2020.
 */

public class SqlAnywhereConnClass {


    // Connection vars
    Connection con = null;
    public String ip, bd, us, pass;

    @SuppressLint("NewApi")
    public SqlAnywhereConnClass (String username, String password, String DBname, String ServerIp){
        us=username;
        bd=DBname;
        pass=password;
        ip=ServerIp;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ConectionURL =null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ConectionURL="jdbc:jtds:odbc:driver={Adaptive Server Anywhere 9.0};ENG=ARCHIVO;UID=" + us + ";PWD="+ pass + ";DBN="+bd + ";LINK=TCPIP(HOST="+ip+":2638)";
            con = DriverManager.getConnection("jdbc:jtds:sybase://192.168.2.4:2638;user=mcas;password=Gedumu49;databasename=tfc_neu;");

        }catch (SQLException se)
        {
            Log.e("Error SQL 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException  e)
        {
            Log.e("Error Class 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("Error Exception 3 : ", e.getMessage());
        }

    }


    public Connection getConnection(){
        return con;
    }
    public void disconnect(){
        con = null;
    }
}

/*

    y este es el codigo de mi mainactivity

    package com.example.systemsecpc3.aplicacionsystemsproductos.activities;

            import android.support.v7.app.AppCompatActivity;
            import android.os.Bundle;
            import android.util.Log;
            import android.view.View;
            import android.widget.Button;
            import android.widget.TextView;

            import com.example.systemsecpc3.aplicacionsystemsproductos.R;
            import com.example.systemsecpc3.aplicacionsystemsproductos.connections.Conexion;

            import java.sql.Connection;
            import java.sql.ResultSet;
            import java.sql.SQLException;
            import java.sql.Statement;
            import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    //declaramos elementos del layout
    Button probar;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtenemos en la variable los elementos del layout
        probar = (Button) findViewById(R.id.button);
        texto = (TextView) findViewById(R.id.textView);

        probar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conexion cc=new Conexion("dba","sql", "SYSTEMS","192.168.1.126");
                Connection cn= cc.getConnection();
                Statement st=null;
                ResultSet rs;
                try {
                    st=(Statement) cn.createStatement();
                    rs=st.executeQuery("select max(*) from bo_producto");
                    if(rs.next())
                    {
                        texto.setText(rs.getString("ENCTONTRÓ DATOS"));
                    }
                    else
                    {
                        texto.setText(rs.getString("NO ENCONTRÓ DATOS"));
                    }
                } catch (SQLException ex) {
                    Log.e("Error here 1 : ", ex.getMessage());
                }
            }
        });

    }


}


 */