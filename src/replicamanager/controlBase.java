/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Oscar Montes
 */
public class controlBase extends connection_control {
     private static controlBase adminBD;
    
    
     public controlBase(Connection conexion, Statement statement) throws SQLException {
        this.conection=conexion;
        this.statement=statement;
        
    }
    public static controlBase getConexion(ConnectionFactory fabrica) throws SQLException {
        Connection conexion = fabrica.getConnectionFactory();

        adminBD = new controlBase(conexion, conexion.createStatement());
        return adminBD;
    }

   
    
    
}
