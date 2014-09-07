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
public class connection_control {
    public Connection conection;
    public Statement statement;
    private static ConnectionControl AdminBD;
    private boolean estado;
   
    public connection_control () {
        
        this.setEstado(true);

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

    public void actualizarReplica(ResultSet datosActualizar) throws SQLException {

        
        while (datosActualizar.next()) {
            int id = datosActualizar.getInt("id");
            String entidad = datosActualizar.getString("entidad");
            String tipoEvento = datosActualizar.getString("tipoEvento");
            if (tipoEvento.equals("Inserccion")) {
                this.insertarDatoReplica(id, entidad);
            }

            

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
     public void eliminarTablaEventos() {
         
         try {

            String eliminarTablaEventos = this.readSql("/sql_files/eliminaLogEventCompleto.sql");
            PreparedStatement stm = this.conection.prepareStatement(eliminarTablaEventos);
            stm.executeUpdate();
            //Imprime el resultado obtenido del valor del inventario
            

        } catch (Exception e) {
            System.out.println("Error al eliminar todos los registros del log event");
        }
      }
     
 
      public void eliminarRegistroTablaEventos(int id, String entidad) {
         
         try {

            String eliminarRegistroTablaEventos = this.readSql("/sql_files/eliminaRegistroLogEvent.sql");
            PreparedStatement stm = this.conection.prepareStatement(eliminarRegistroTablaEventos);
            stm.setInt(1, id);
            stm.setString(2, entidad);
            stm.executeUpdate();
            //Imprime el resultado obtenido del valor del inventario
            

        } catch (Exception e) {
            System.out.println("Error al eliminar registro de tabla logevent");
        }
      }
         
        public void cambiaTablaEventos() {
         
         try {

            String cambiarRegistroTablaEventos = this.readSql("/sql_files/cambiarEnableLogTable.sql");
            this.statement.executeUpdate(cambiarRegistroTablaEventos);
         
            //Imprime el resultado obtenido del valor del inventario
            

        } catch (Exception e) {
            System.out.println("Error al cambiar registro de tabla logevent");
        } 
         
       
        
         
        
         
    }
    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
        
    }
   
    
   

}
