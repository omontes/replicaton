/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Oscar Montes
 */
public class ConnectionControl {
    private Connection conection;
    private Statement statement;
    private static ConnectionControl AdminBD;
   
    public ConnectionControl(Connection conection, Statement statement) {
        this.conection = conection;
        this.statement = statement;

    }

    public static ConnectionControl getInstanceSQLServer() {
      
            Setting_upDBSqlServer setting = new Setting_upDBSqlServer();
            AdminBD = new ConnectionControl(setting.getConection(), setting.getStatement());
      
            return AdminBD;
    }
    public static ConnectionControl getInstanceMySQLCompany() {
       
            Setting_upDBMysql setting = new Setting_upDBMysql();
            AdminBD = new ConnectionControl(setting.getConection(), setting.getStatement());
   
             return AdminBD;
    }
    public static ConnectionControl getInstanceMySQLCompany2() {
        
            Setting_upDBMysqlCompany2 setting = new Setting_upDBMysqlCompany2();
            AdminBD = new ConnectionControl(setting.getConection(), setting.getStatement());
        
             return AdminBD;
    }
    private String readSql(String filePath) throws IOException {
        InputStream inputfile = ConnectionControl.class.getClass().getResourceAsStream(filePath);
        StringBuilder sb;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputfile))) {
            String str;
            sb = new StringBuilder();
            while ((str = input.readLine()) != null) {
                sb.append(str).append("\n");
            }
        }
        return sb.toString();
    }

    void consultarEmpleados() {
         try {

            String valorInventario = this.readSql("/sql_files/consultarEmpleados.sql");
            PreparedStatement stm = this.conection.prepareStatement(valorInventario);
            ResultSet resultset = stm.executeQuery();
            //Imprime el resultado obtenido del valor del inventario
            while (resultset.next()) {
                System.out.println(resultset.getString(1)
                        );
            }

        } catch (Exception e) {
            System.out.println("Error al obtener los nombres de los empleados");
        }
    }
    
    public void insertarDatoReplica(int id, String entidad) {
         try {

            String insertarDatoRep = this.readSql("/sql_files/insertarEmpleado.sql");
            String query =insertarDatoRep.replace("$tableName",entidad);
            PreparedStatement stm = conection.prepareStatement(query);
            stm.setInt(1, id);
            stm.executeUpdate();
            
           
           

        } catch (Exception e) {
            System.out.println("Error al insertar un dato en una replica");
        }
    }
    
     public ResultSet consultarTablaEventos() {
         ResultSet resultset = null;
         try {

            String consultarEventos = this.readSql("/sql_files/consultarTablaEventos.sql");
            PreparedStatement stm = this.conection.prepareStatement(consultarEventos);
            resultset = stm.executeQuery();
            //Imprime el resultado obtenido del valor del inventario
            

        } catch (Exception e) {
            System.out.println("Error al obtener los nombres de los empleados");
        }
         
        return resultset;
        
         
        
         
    }

}