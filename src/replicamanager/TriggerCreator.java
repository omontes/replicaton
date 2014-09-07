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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author M. Taylor (el mas guapo)
 */
public class TriggerCreator {

    PrintWriter query;

    public void createTriggersQuery() throws FileNotFoundException, UnsupportedEncodingException, SQLException, IOException {
        query = new PrintWriter("triggers.sql", "UTF-8");
        SqlServerConnectionFactory sqlserver = new SqlServerConnectionFactory("localhost", "sa", "123456", "db2");

        connection_control connection = controlBase.getConexion(sqlserver);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if (Entidades.getString(3).equals("LOGTABLE")) {
                continue;

            }
            else{
            String createTrigger = "";
            createTrigger +="CREATE TRIGGER [dbo].[TRG_createLogTable_";
            createTrigger+= Entidades.getString(3);
            createTrigger+= "] ";
            createTrigger+= "ON [dbo].[";
            createTrigger+=Entidades.getString(3);
            createTrigger+="]\n";
            createTrigger+="AFTER INSERT AS\n";
            createTrigger+="BEGIN\n";
            createTrigger+="DECLARE @id int\n";
            createTrigger+="DECLARE @nombrTabla NVARCHAR(128)\n";
            createTrigger+="SELECT @id = i.id FROM INSERTED i\n";
            createTrigger+="SELECT @nombrTabla = OBJECT_NAME(parent_object_id)\n";
            createTrigger+="FROM sys.objects ";
            createTrigger+="WHERE name = OBJECT_NAME(@@PROCID)\n";
            createTrigger+="INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)\n";
            createTrigger+="VALUES(@id,'Inserccion',@nombrTabla,'1')\n";
            createTrigger+="END\n";
            
            /***
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
            query.println("VALUES(@id,'Update',@nombrTabla,'1')\n");**/
            connection.createTrigger(createTrigger);
        }
        
    }
    }

    public void createIdQuery() throws FileNotFoundException, UnsupportedEncodingException, SQLException, IOException {
        SqlServerConnectionFactory sqlserver = new SqlServerConnectionFactory("localhost", "sa", "123456", "db2");

        connection_control connection = controlBase.getConexion(sqlserver);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if(Entidades.getString(3).equals("LOGTABLE")){
                continue;
            }
            else {
                String createidControl = "";
                createidControl += "ALTER TABLE ";
                createidControl += Entidades.getString(3);
                createidControl += "\n";
                createidControl += "ADD id int IDENTITY(1,1)\n";
                connection.executeQuery(createidControl);
            }
        }
        
    }


    /*
     ALTER TABLE <nombre>
     ADD id int IDENTITY(1,1)
     */
}
