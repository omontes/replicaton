/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;


/**
 *
 * @author Oscar Montes
 */
public class MySqlConnectionFactory implements ConnectionFactory {
    private String driver = "com.mysql.jdbc.Driver";
    private String databaseName;
    private String user;
    private String pass;
    private String ip;

    MySqlConnectionFactory(String ip, String user, String password, String name){
        this.ip=ip;
        this.user=user;
        this.pass=password;
        this.databaseName= name;
    }
    @Override
    public Connection getConnectionFactory(){
         String database = "";
          database+= "jdbc:mysql://"
                  + ip+":3306/"
                  + databaseName;
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

   
    
}