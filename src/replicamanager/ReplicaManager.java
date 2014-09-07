/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 *
 * @author Oscar Montes
 */
public class ReplicaManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
       /** ConnectionControl adminBDOrigen = ConnectionControl.getInstanceSQLServer();
        ConnectionControl adminBDC1 = ConnectionControl.getInstanceMySQLCompany();
        ConnectionControl adminBDc2 = ConnectionControl.getInstanceMySQLCompany2();

        ControlReplicas control = new ControlReplicas(adminBDOrigen);
        control.agregarReplica(adminBDC1);
        control.agregarReplica(adminBDc2);
        ControlReplicasHilo hilo = new ControlReplicasHilo(control);
        adminBDc2.setEstado(false);
        new Thread(hilo).start();**/
      
        
        MySqlConnectionFactory company= 
        new MySqlConnectionFactory("localhost","root","123456","company");
        MySqlConnectionFactory company2= 
        new MySqlConnectionFactory("localhost","root","123456","company2");
        SqlServerConnectionFactory sqlserver =new SqlServerConnectionFactory("localhost","sa","123456","db");
       
        controlBase adminBDC1 = controlBase.getConexion(company);
        controlBase adminBDOrigen = controlBase.getConexion(sqlserver);
        controlBase adminBDc2 = controlBase.getConexion(company2);
        //ResultSet rs =adminBD.consultarTablaEventos();
        //System.out.println(rs.next());

        //adminBD.consultarEmpleados();

        //hiloPrueba t = new hiloPrueba();
        //new Thread(t).start();
        QueryCreator test = new QueryCreator();
        test.createAllTablesQuery();

        //HiloPrueba t = new HiloPrueba();
        //new Thread(t).start();
        
        

    }
    
}
