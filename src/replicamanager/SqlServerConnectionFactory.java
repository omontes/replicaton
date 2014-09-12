/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Oscar Montes
 */
public class SqlServerConnectionFactory implements ConnectionFactory {
    private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String databaseName;
    private String user;
    private String pass;
    private String ip;

    SqlServerConnectionFactory(String ip, String user, String password, String name){
        this.ip=ip;
        this.user=user;
        this.pass=password;
        this.databaseName= name;
    }
    @Override
    public Connection getConnectionFactory(){
         String database = "";
                 database+="jdbc:sqlserver://"
                         + ip+";instanceName=SQLSERVER;databaseName="
                         +databaseName;
             Connection conection = null;
            try {
            Class.forName(driver);
            conection=(DriverManager.getConnection(database, user, pass));
            
        } catch (Exception e) {
            System.out.println("Error al recuperar conexion "
                    + e.toString());
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos.");

        }
            return conection;
        
    }
    
       @Override
    public String getNombreBase(){
    
        return databaseName;
    }

}
