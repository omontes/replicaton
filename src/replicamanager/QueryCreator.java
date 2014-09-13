/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Carlos
 */

public class QueryCreator {
    
    //Este metodo es usado para crear un query encargado de crear todas las tablas de SQLServer a MySQL
    public void replicatetoMySQL(ConnectionFactory origen, ConnectionFactory destino) throws FileNotFoundException, UnsupportedEncodingException, SQLException, IOException{
             
        connection_control destination = connection_control.getConexion(destino);
        connection_control connection = connection_control.getConexion(origen);
        
        /***Cambiar el nombre del schema ****/
        ResultSet Entidades = connection.getAllTablas();
        while(Entidades.next()){
            String createTableQuery = "";
            int resultSetCounter = 1; //Posicion actual
            int attributeAmount = this.getResultSetSize(connection.getAllAtributosDeTabla(Entidades.getString(3),"dbo")); //Cantidad de Atributos
            createTableQuery += "CREATE TABLE ";
            createTableQuery +=  Entidades.getString(3);
            createTableQuery += " ( ";
            ResultSet Atributos = connection.getAllAtributosDeTabla(Entidades.getString(3),"dbo");
            while(Atributos.next()){
                createTableQuery += Atributos.getString(1) + " ";
                createTableQuery += Atributos.getString(2);
                //Revisar tamano de tipo
                if(Atributos.getString(3) != null){
                    createTableQuery += "(" + Atributos.getString(3) + ")";
                }
                //Revisar si valor es Not Null
                if("NO".equals(Atributos.getString(4))){
                   createTableQuery += " NOT NULL";
                }
                //Revisar si valor contiene DEFAULT
                if(Atributos.getString(5) != null){
                    createTableQuery += " DEFAULT ";
                    String defaultValue = Atributos.getString(5);
                    createTableQuery +=  defaultValue.substring(1, defaultValue.length()-1); //Se tiene que cortar los extremos del string debido al formato de SQLServer
                }
                //Este if es necesario para no colocar la ultima ",".
                if(resultSetCounter != attributeAmount){
                    createTableQuery += ", ";
                }else createTableQuery += " ";
                resultSetCounter++;
            }
            createTableQuery += ")";
            createTableQuery += ";";
            resultSetCounter = 1;
            destination.executeQuery(createTableQuery);
            insertDataToMySQL(Entidades.getString(3),destination,connection); //Metodo para insertar la data
        }
    }
    
     //Este metodo es usado para crear un query encargado de crear todas las tablas de MySQL a SQLServer
    public void replicatetoSQLServer(connection_control origen,connection_control destino) throws FileNotFoundException, UnsupportedEncodingException, SQLException, IOException{
        
        System.out.println("entro a replicar porque origen es mysql");
        System.out.println(origen.schemaName);
       
        connection_control destination = destino;
        connection_control connection = origen;
     
        ResultSet Entidades = connection.getAllTablas();
        while(Entidades.next()){
            String createTableQuery = "";
            int resultSetCounter = 1; //Posicion actual
            int attributeAmount = this.getResultSetSize(connection.getAllAtributosDeTabla(Entidades.getString(3),origen.schemaName));//Cantidad de Atributos
            createTableQuery += "CREATE TABLE ";
            createTableQuery +=  Entidades.getString(3);
            createTableQuery += " ( ";
            ResultSet Atributos = connection.getAllAtributosDeTabla(Entidades.getString(3),origen.schemaName);
            while(Atributos.next()){
                createTableQuery += Atributos.getString(1) + " ";
                createTableQuery += Atributos.getString(2);
                //Revisar tamano de tipo
                if(Atributos.getString(3) != null){
                    createTableQuery += "(" + Atributos.getString(3) + ")";
                }
                //Revisar si valor es NOT NULL
                if("NO".equals(Atributos.getString(4))){
                   createTableQuery += " NOT NULL";
                }
                //Revisar si valor contiene DEFAULT
                if(Atributos.getString(5) != null){
                    createTableQuery += " DEFAULT ";
                    createTableQuery += "'" + Atributos.getString(5) + "'";
                }
                //If usado para evitar agregar un ',' al final
                if(resultSetCounter != attributeAmount){
                    createTableQuery += ", ";
                }else createTableQuery += " ";
                resultSetCounter++;
            }
            createTableQuery += ")";
            createTableQuery += ";";
            resultSetCounter = 1;
            destination.executeQuery(createTableQuery);
            insertDataSQLServer(Entidades.getString(3),origen,destino); //Metodo para insertar la data
        }
    }
    
    public void insertDataToMySQL(String tableName,connection_control destination,connection_control connection) throws SQLException, IOException{
        
        ResultSet resultset = connection.getAllData(tableName);
        int column = 1;//contador usado para iterar sobre las columnas
        while(resultset.next()){
            String insertData = "INSERT INTO " + tableName + " VALUES (";
            ResultSet Atributos = connection.getAllAtributosDeTabla(tableName,"dbo"); //ResultSet usado para saber que tipo es el dato 
            while(true){
                try{
                    Atributos.next();
                    if("int".equals(Atributos.getString(2))||null == resultset.getString(column)){
                        insertData += resultset.getString(column);
                    }else{
                        insertData += "'" + resultset.getString(column) + "'";
                    }
                    try{
                        resultset.getString(column+1);
                        insertData += ", ";
                    }catch(Exception e){
                        
                    }
                    column++;
                }catch(Exception e){
                    insertData += ");";
                    destination.executeQuery(insertData);
                    insertData = "";
                    break;
                }
            }
            column = 1;
        }
    }
    
    public void insertDataSQLServer(String tableName, connection_control origen, connection_control destino) throws SQLException, IOException{
     
       
        connection_control destination = destino;
        connection_control connection = origen;
        
      
        ResultSet resultset = connection.getAllData(tableName);
        int column = 1;//contador usado para iterar sobre las columnas
        while(resultset.next()){
            
            String insertData = "INSERT INTO " + tableName + "(";
            ResultSet Atributos = connection.getAllAtributosDeTabla(tableName, connection.schemaName); //ResultSet usado para saber que tipo es el dato 
            while (Atributos.next()) {
                String atributo = Atributos.getString(1);
               

                    insertData += ""+atributo+"";;
                    insertData += ",";

                    column++;
                 
                   

                }
             //Tiene que quitar la ultima coma
            insertData = insertData.substring(0, insertData.length() - 1);
            
            insertData += ") VALUES(";
            column = 1;
            ResultSet Atributos2 = connection.getAllAtributosDeTabla(tableName,connection.schemaName); //ResultSet usado para saber que tipo es el dato 
            //Iteracion para poder recorrer todos los datos y no causar un "null pointer".
            while(true){
                try{
                    Atributos2.next();
                    if("int".equals(Atributos2.getString(2))||null == resultset.getString(column)){
                        insertData += resultset.getString(column);
                    }else{
                        insertData += "'" + resultset.getString(column) + "'";
                    }
                    try{
                        resultset.getString(column+1);
                        insertData += ", ";
                    }catch(Exception e){
                        
                    }
                    column++;
                }catch(Exception e){
                    insertData += ");";
                    System.out.println("inserccion de replica");
                    System.out.println(insertData);
                    destination.executeQuery(insertData);
                    insertData = "";
                    break;
                }
            }
            column = 1;
        }
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
