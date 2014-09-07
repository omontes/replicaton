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
 * @author M. Taylor (el mas guapo)
 */
public class TriggerCreator {

    PrintWriter query;

    public void createTriggersQuery() throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        query = new PrintWriter("triggers.sql", "UTF-8");
        new MySqlConnectionFactory("localhost", "root", "123456", "company2");
        SqlServerConnectionFactory sqlserver = new SqlServerConnectionFactory("localhost", "sa", "123456", "db");

        connection_control connection = controlBase.getConexion(sqlserver);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            query.print("CREATE TRIGGER [db].[TRG_createLogTable_");
            query.print(Entidades.getString(3));
            query.print("] ");
            query.print("ON [db].[");
            query.print(Entidades.getString(3));
            query.print("]\n");
            query.println("AFTER INSERT AS");
            query.println("BEGIN");
            query.println("DECLARE @id int");
            query.println("DECLARE @nombrTabla NVARCHAR(128)");
            query.println("SELECT @id = i.id FROM INSERTED i");
            query.println("SELECT @nombrTabla = OBJECT_NAME(parent_object_id) ");
            query.println("FROM sys.objects ");
            query.println("WHERE name = OBJECT_NAME(@@PROCID)");
            query.println("INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)");
            query.println("VALUES(@id,'Create',@nombrTabla,'1')\n");

            query.print("CREATE TRIGGER [db].[TRG_deleteLogTable_");
            query.print(Entidades.getString(3));
            query.print("] ");
            query.print("ON [db].[");
            query.print(Entidades.getString(3));
            query.print("]\n");
            query.println("FOR DELETE");
            query.println("BEGIN");
            query.println("DECLARE @id int");
            query.println("DECLARE @nombrTabla NVARCHAR(128)");
            query.println("SELECT @id = i.id FROM DELETED i");
            query.println("SELECT @nombrTabla = OBJECT_NAME(parent_object_id) ");
            query.println("FROM sys.objects ");
            query.println("WHERE name = OBJECT_NAME(@@PROCID)");
            query.println("INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)");
            query.println("VALUES(@id,'Delete',@nombrTabla,'1')\n");

            query.print("CREATE TRIGGER [db].[TRG_updateLogTable_");
            query.print(Entidades.getString(3));
            query.print("] ");
            query.print("ON [db].[");
            query.print(Entidades.getString(3));
            query.print("]\n");
            query.println("FOR UPDATE");
            query.println("BEGIN");
            query.println("DECLARE @id int");
            query.println("DECLARE @nombrTabla NVARCHAR(128)");
            query.println("SELECT @id = i.id FROM UPDATED i");
            query.println("SELECT @nombrTabla = OBJECT_NAME(parent_object_id) ");
            query.println("FROM sys.objects ");
            query.println("WHERE name = OBJECT_NAME(@@PROCID)");
            query.println("INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)");
            query.println("VALUES(@id,'Update',@nombrTabla,'1')\n");
        }
        query.close();
    }

    public void createIdQuery() throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        query = new PrintWriter("alterTable.sql", "UTF-8");
        new MySqlConnectionFactory("localhost", "root", "123456", "company2");
        SqlServerConnectionFactory sqlserver = new SqlServerConnectionFactory("localhost", "sa", "123456", "db");

        connection_control connection = controlBase.getConexion(sqlserver);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            query.print("ALTER TABLE ");
            query.print(Entidades.getString(3));
            query.print("\n");
            query.println("ADD id int IDENTITY(1,1)\n");
        }
        query.close();
    }


    /*
     ALTER TABLE <nombre>
     ADD id int IDENTITY(1,1)
     */
}
