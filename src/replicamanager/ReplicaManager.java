/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Oscar Montes
 */
public class ReplicaManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, UnsupportedEncodingException, IOException {
       /** ConnectionControl adminBDOrigen = ConnectionControl.getInstanceSQLServer();
        ConnectionControl adminBDC1 = ConnectionControl.getInstanceMySQLCompany();
        ConnectionControl adminBDc2 = ConnectionControl.getInstanceMySQLCompany2();

        ControlReplicas control = new ControlReplicas(adminBDOrigen);
        control.agregarReplica(adminBDC1);
        control.agregarReplica(adminBDc2);
        ControlReplicasHilo hilo = new ControlReplicasHilo(control);
        adminBDc2.setEstado(false);
        new Thread(hilo).start();**/
      
        QueryCreator test = new QueryCreator();
        test.replicatetoSQLServer();
        
        /**SqlServerConnectionFactory R1= 
        new SqlServerConnectionFactory("localhost","sa","123456","r1");
        SqlServerConnectionFactory R2= 
        new SqlServerConnectionFactory("localhost","sa","123456","r2");
        SqlServerConnectionFactory sqlserver =new SqlServerConnectionFactory("localhost","sa","123456","db2");
       
        connection_control r1 = connection_control.getConexion(R1);
        connection_control adminBDOrigen = connection_control.getConexion(sqlserver);
        connection_control r2 = connection_control.getConexion(R2);
        ControlReplicas control = new ControlReplicas();
        control.setBaseOrigen(adminBDOrigen);
        
        generarReplica(sqlserver,R1);
        generarReplica(sqlserver,R2);
        
        control.agregarReplica(r2);
        control.agregarReplica(r1);
        
        generarTriggers(sqlserver);
        generarTriggers(R1);
        generarTriggers(R2);
        
        ControlReplicasHilo hilo = new ControlReplicasHilo(control);
        new Thread(hilo).start();
        //ResultSet rs =adminBD.consultarTablaEventos();**/
        //System.out.println(rs.next());

        //adminBD.consultarEmpleados();
        
        //hiloPrueba t = new hiloPrueba();
        //new Thread(t).start();
        
        //No olvidar quitar el crearBase() en mysql Fabrica si no vacia la base
        
        
        
        //HiloPrueba t = new HiloPrueba();
        //new Thread(t).start();
        //TriggerCreator triggerCreator = new TriggerCreator();
        /**ANTES DE CREAR TRIGGER SE DEBE HACER DOS COSAS 
         * 1- CREAR LA TABLA LOGTABLE Y HISTORYTABLE
         * 2- EL TRIGGER DE LA TABLA LOGTABLE
         * 3- CREAR LOS ID EN TDAS LAS TABLAS triggerCreator.createIdQuery();
         */
        //triggerCreator.createIdQuery();
        //triggerCreator.createTriggersQuery();
       
        //MySqlConnectionFactory db= 
        //new MySqlConnectionFactory("localhost","root","123456","db");

    }
     public static void generarReplica(ConnectionFactory origen , ConnectionFactory destino){
        try {
            QueryCreator test = new QueryCreator();
            test.replicatetoMySQL(origen,destino);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void generarTriggers(SqlServerConnectionFactory origen) {
           try {
               TriggerCreator triggerCreator = new TriggerCreator();
               /**Se crea la tabla del control para replicas en origen **/
               triggerCreator.crearLogTable(origen);
               /**Se crea la tabla del historial en el origen **/
               triggerCreator.crearHistoryTable(origen);
               /** Se crea el trigger que llena el historial **/
               triggerCreator.crearTriggerLogTable(origen);
               /** Se crean los ids en tdas las tablas del origen **/
               triggerCreator.createIdQuery(origen);
               /** Se crean todos los triggers genericos en origen **/
               triggerCreator.createTriggersQuerySql(origen);
           } 
           catch (SQLException ex) {
               Logger.getLogger(ReplicaManagerApp.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(ReplicaManagerApp.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
    
    
}
