/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Carlos
 */
public class QueryCreator {
    PrintWriter query;
    
    //Este metodo es usado para crear un query encargado de crear todas las tablas de una base de datos
    public void createAllTablesQuery() throws FileNotFoundException, UnsupportedEncodingException, SQLException{
        query = new PrintWriter("tempQuery.sql", "UTF-8");
        new MySqlConnectionFactory("localhost", "root", "123456", "company2");
        SqlServerConnectionFactory sqlserver = new SqlServerConnectionFactory("localhost", "sa", "123456", "db");

        connection_control connection = controlBase.getConexion(sqlserver);
   
        ResultSet Entidades = connection.getAllTablas();
        query.println("CREATE DATABASE replication;");
        query.println("USE replication;");
        while(Entidades.next()){
            int resultSetCounter = 1; //Posicion actual
            int attributeAmount = this.getResultSetSize(connection.getAllAtributosDeTabla(Entidades.getString(3))); //Cantidad de Atributos
            query.print("CREATE TABLE ");
            query.print(Entidades.getString(3));
            query.print(" ( ");
            ResultSet Atributos = connection.getAllAtributosDeTabla(Entidades.getString(3));
            while(Atributos.next()){
                query.print(Atributos.getString(1) + " ");
                query.print(Atributos.getString(2));
                if(Atributos.getString(3) != null){
                    query.print("(" + Atributos.getString(3) + ")");
                }
                if("NO".equals(Atributos.getString(4))){
                    query.print(" NOT NULL");
                }
                if(resultSetCounter != attributeAmount){
                    query.print(", ");
                }else query.print(" ");
                resultSetCounter++;
            }
            query.print(")");
            query.println(";");
            resultSetCounter = 1;
        }
        query.close();
    }
    
    //Esta funcion auxiliar permite obtener el tamano de un ResultSet
    public int getResultSetSize(ResultSet input) throws SQLException{
      int size=0;
      while (input.next()) {
        size++;
      }  
      return size;
    }
}
