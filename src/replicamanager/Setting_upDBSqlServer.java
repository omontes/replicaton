/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Oscar Montes
 */
public class Setting_upDBSqlServer {
    
 /**
     * Parametros de conexion
     */
    private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String database = "jdbc:sqlserver://OSCARMONTES-HP;instanceName=SQLSERVER;databaseName=db";
    private String user = "sa";
    private String pass = "123456";
    private Connection conection;
    private Statement statement;
    DateFormat dateFormat = new SimpleDateFormat("EEEE");//formato fecha act

    /**
     * Intenta la conexion a la Base de Datos
     */
    public Setting_upDBSqlServer() {
        try {
            Class.forName(driver);
            this.setConection(DriverManager.getConnection(database, user, pass));
            this.setStatement(conection.createStatement());
        } catch (Exception e) {
            System.out.println("Error al recuperar conexion "
                    + e.toString());
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos.");

        }
    }

    /**
     * @return the statement
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * @param statement the statement to set
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    /**
     * @return the conection
     */
    public Connection getConection() {
        return conection;
    }

    /**
     * @param conection the conection to set
     */
    public void setConection(Connection conection) {
        this.conection = conection;
    }

}
