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
import java.sql.Statement;

/**
 *
 * @author Oscar Montes
 */
public class connection_control {
    private Connection conection;
    private Statement statement;
    private static connection_control AdminBDSQLSERVER;
    private static connection_control AdminBDMYSQL;
    public connection_control(Connection conection, Statement statement) {
        this.conection = conection;
        this.statement = statement;

    }

    public static connection_control getInstanceSQLServer() {
        if (AdminBDSQLSERVER == null) {
            Setting_upDBSqlServer setting = new Setting_upDBSqlServer();
            AdminBDSQLSERVER = new connection_control(setting.getConection(), setting.getStatement());
        }
        return AdminBDSQLSERVER;
    }
    public static connection_control getInstanceMySQLServer() {
        if (AdminBDMYSQL == null) {
            Setting_upDBMysql setting = new Setting_upDBMysql();
            AdminBDMYSQL = new connection_control(setting.getConection(), setting.getStatement());
        }
        return AdminBDMYSQL;
    }
    private String readSql(String filePath) throws IOException {
        InputStream inputfile = connection_control.class.getClass().getResourceAsStream(filePath);
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
            ResultSetMetaData metadata =stm.getMetaData();
            System.out.println(metadata.getColumnName(1));
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

}
