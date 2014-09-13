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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author M. Taylor (el mas guapo)
 */
public class TriggerCreator {

    PrintWriter query;
    public void createTriggersQueryMysql(ConnectionFactory origen) throws SQLException, IOException {
       
        connection_control connection = connection_control.getConexion(origen);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if (Entidades.getString(3).equals("LOGTABLE") || Entidades.getString(3).equals("HISTORYTABLE")) {
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
            createTrigger+="DECLARE @idControl int\n";
            createTrigger+="DECLARE @nombrTabla NVARCHAR(128)\n";
            createTrigger+="SELECT @idControl = i.idControl FROM INSERTED i\n";
            createTrigger+="SELECT @nombrTabla = OBJECT_NAME(parent_object_id)\n";
            createTrigger+="FROM sys.objects ";
            createTrigger+="WHERE name = OBJECT_NAME(@@PROCID)\n";
            createTrigger+="INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)\n";
            createTrigger+="VALUES(@idControl,'Inserccion',@nombrTabla,'1')\n";
            createTrigger+="END\n";
         
            connection.createDDL(createTrigger);
        }
        
    }
    }
    public void createTriggersQuerySql(MySqlConnectionFactory origen) throws SQLException, IOException {
       
        connection_control connection = connection_control.getConexion(origen);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if (Entidades.getString(3).equals("logtable") || Entidades.getString(3).equals("historytable")) {
                continue;

            }
            else{
            String createTrigger ="CREATE TRIGGER insertarLogTable_";
            createTrigger += Entidades.getString(3);
            createTrigger += " ";
            createTrigger+="AFTER INSERT ON ";
            createTrigger+=Entidades.getString(3);
            createTrigger+="\n";
            createTrigger+= "FOR EACH ROW\n";
            createTrigger +="BEGIN\n";
            createTrigger+="INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)\n";
            createTrigger+="VALUES(NEW.idControl,'Inserccion','"+Entidades.getString(3)+"','1');\n";
            createTrigger+="END\n";
            System.out.println(createTrigger);
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
            connection.createDDL(createTrigger);
        }
        
    }
    }
    public void createTriggersQuerySql(SqlServerConnectionFactory origen) throws SQLException, IOException {
       
        connection_control connection = connection_control.getConexion(origen);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if (Entidades.getString(3).equals("LOGTABLE") || Entidades.getString(3).equals("HISTORYTABLE")) {
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
            createTrigger+="DECLARE @idControl int\n";
            createTrigger+="DECLARE @nombrTabla NVARCHAR(128)\n";
            createTrigger+="SELECT @idControl = i.idControl FROM INSERTED i\n";
            createTrigger+="SELECT @nombrTabla = OBJECT_NAME(parent_object_id)\n";
            createTrigger+="FROM sys.objects ";
            createTrigger+="WHERE name = OBJECT_NAME(@@PROCID)\n";
            createTrigger+="INSERT INTO LOGTABLE (id,tipoEvento,entidad,enable)\n";
            createTrigger+="VALUES(@idControl,'Inserccion',@nombrTabla,'1')\n";
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
            connection.createDDL(createTrigger);
        }
        
    }
    }
    public void createIdQuery(MySqlConnectionFactory origen) throws SQLException, IOException {
        

        connection_control connection = connection_control.getConexion(origen);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if(Entidades.getString(3).equals("LOGTABLE") || Entidades.getString(3).equals("HISTORYTABLE")){
                continue;
            }
            else {
                String createidControl = "";
                createidControl += "ALTER TABLE ";
                createidControl += Entidades.getString(3);
                createidControl += " ";
                createidControl += "add column idControl INT NOT NULL AUTO_INCREMENT PRIMARY KEY\n";
                connection.executeQuery(createidControl);
            }
        }
        
    }
    public void createIdQuery(SqlServerConnectionFactory origen) throws SQLException, IOException {
        

        connection_control connection = connection_control.getConexion(origen);
        ResultSet Entidades = connection.getAllTablas();
        while (Entidades.next()) {
            if(Entidades.getString(3).equals("LOGTABLE") || Entidades.getString(3).equals("HISTORYTABLE")){
                continue;
            }
            else {
                String createidControl = "";
                createidControl += "ALTER TABLE ";
                createidControl += Entidades.getString(3);
                createidControl += "\n";
                createidControl += "ADD idControl int IDENTITY(1,1)\n";
                connection.executeQuery(createidControl);
            }
        }
        
    }


    /*
     ALTER TABLE <nombre>
     ADD id int IDENTITY(1,1)
     */

    void crearLogTable(ConnectionFactory origen) {
        String createLOGTABLE = "CREATE TABLE LOGTABLE\n"
                + "(	id          INT NOT NULL,\n"
                + "	tipoEvento  VARCHAR(15),\n"
                + "	entidad     VARCHAR(50),\n"
                + "	enable      char(1)\n"
                + ");";
        connection_control connection = connection_control.getConexion(origen);
        try {
            connection.createDDL(createLOGTABLE);
        } catch (IOException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    void crearTriggerLogTableMySQL(MySqlConnectionFactory origen){
        String createTriggerLOGTABLE = 
                 "CREATE TRIGGER registraEventos\nAFTER INSERT ON logtable\n"
                + "FOR EACH ROW\n"
                + "BEGIN\n"
                + "DECLARE nuevo_id INT;\n"
                + "DECLARE nuevo_tipoEvento VARCHAR(15);\n"
                + "DECLARE nuevo_entidad VARCHAR(50);\n"
                + "SET nuevo_id = NEW.id;\n"
                + "SET nuevo_tipoEvento = NEW.tipoEvento;\n"
                + "SET nuevo_entidad= NEW.entidad;\n"
                + "INSERT INTO historytable(id,tipoEvento,entidad,fecha)\n"
                + "VALUES(nuevo_id,nuevo_tipoEvento,nuevo_entidad,NOW());\n"
                + "END";
        System.out.println(createTriggerLOGTABLE);
        connection_control connection = connection_control.getConexion(origen);
        try {
            connection.createDDL(createTriggerLOGTABLE);
        } catch (IOException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void crearTriggerLogTable(SqlServerConnectionFactory origen){
        String createTriggerLOGTABLE = "CREATE TRIGGER [dbo]."
                + "[TRG_createLogTable_history] ON [dbo].[logtable]\n"
                + "AFTER INSERT AS\n"
                + "BEGIN\n"
                + "DECLARE @id int\n"
                + "DECLARE @tipoEvento VARCHAR(15)\n"
                + "DECLARE @entidad VARCHAR(50)\n"
                + "SELECT @id = i.id FROM INSERTED i\n"
                + "SELECT @tipoEvento = i.tipoEvento FROM INSERTED i\n"
                + "SELECT @entidad= i.entidad FROM INSERTED i\n"
                + "INSERT INTO HISTORYTABLE(id,tipoEvento,entidad,fecha)\n"
                + "VALUES(@id,@tipoEvento,@entidad,CURRENT_TIMESTAMP)\n"
                + "END";
        connection_control connection = connection_control.getConexion(origen);
        try {
            connection.createDDL(createTriggerLOGTABLE);
        } catch (IOException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void crearHistoryTable(ConnectionFactory origen) {
        String createHISTORYTABLE = "CREATE TABLE HISTORYTABLE\n"
                + "(	id          INT NOT NULL,\n"
                + "	tipoEvento  VARCHAR(15),\n"
                + "	entidad     VARCHAR(50),\n"
                + "	fecha      DATETIME\n"
                + ");";
        connection_control connection = connection_control.getConexion(origen);
        try {
            connection.createDDL(createHISTORYTABLE);
        } catch (IOException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TriggerCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
