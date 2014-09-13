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
        this.crearBase();
    }
    
     MySqlConnectionFactory(String ip,String name){
        this.ip=ip;
        this.user="root";
        this.pass="123456";
        this.databaseName= name;
        this.obtenerConexionOrigen();
    }
    
    public void crearBase(){
        String url = "";
          url+= "jdbc:mysql://"
                  + ip+":3306"
                  ;
            Connection conection = null;
            try {
            Class.forName(driver);
            conection=(DriverManager.getConnection(url,user, pass));
            Statement statement = conection.createStatement();
            /***CUIDADO GENERA SQL INJECTION *****/
            String dropDatabaseSiExiste = "DROP DATABASE IF EXISTS "+databaseName;
            String crearBase ="CREATE DATABASE "+databaseName;
            statement.executeUpdate(dropDatabaseSiExiste);
            statement.executeUpdate(crearBase);
        } catch (Exception e) {
            System.out.println("Error al crear base "
                    + e.toString());
            JOptionPane.showMessageDialog(null,
                    "No se pudo crear la base de datos.");

        }
    }
    public void obtenerConexionOrigen(){
        String url = "";
          url+= "jdbc:mysql://"
                  + ip+":3306"
                  ;
            Connection conection = null;
            try {
            Class.forName(driver);
            conection=(DriverManager.getConnection(url,user, pass));
            

        } catch (Exception e) {
            System.out.println("Error al crear base "
                    + e.toString());
            JOptionPane.showMessageDialog(null,
                    "No se pudo crear la base de datos.");

        }
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
    
    @Override
    public String getNombreBase(){
    
        return databaseName;
    }
    public String getSchemaName(){
        return databaseName;
    }
   
    
}
