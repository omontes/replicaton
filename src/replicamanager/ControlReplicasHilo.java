/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar Montes
 */
public class ControlReplicasHilo implements Runnable {
    ControlReplicas controlRep;
    public ControlReplicasHilo(ControlReplicas control){
        this.controlRep=control;
    }

    @Override
    public void run() {
        
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlReplicasHilo.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            try {
                
                ResultSet eventos = this.controlRep.getBaseOrigen().
                        consultarTablaEventos();
                while (eventos.next()) {
                    int id = eventos.getInt("id");
                    String entidad = eventos.getString("entidad");
                    String tipoEvento = eventos.getString("tipoEvento");
                    String enable = eventos.getString("enable");
                    /**
                     * **LISTA DE EVENTOS ****
                     */

                    if (enable.charAt(0) == '1') {
                       
                        if (tipoEvento.equals("Inserccion")) {
                            this.insertarReplicas(id, entidad);

                        }
                        this.intenteLimpiar(id, entidad);
                    }

                }

            } catch (SQLException ex) {
                Logger.getLogger(ControlReplicasHilo.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
                
            }
        }

    
    
    private void insertarReplicas(int id, String entidad) {
        
        for (int i = 0; i < this.controlRep.BasesReplicas.size(); i++) {
            if (this.controlRep.BasesReplicas.get(i).isEstado()) {
                try {
                    this.insertDataToReplicaMySQL(entidad,id,
                            this.controlRep.BasesReplicas.get(i),
                            this.controlRep.getBaseOrigen());
                } catch (SQLException ex) {
                    Logger.getLogger(ControlReplicasHilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ControlReplicasHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
               
               
            }
        }
    }
    
    private boolean existeReplicaPausada() {
        boolean existePausado = false;
        for (int i = 0; i < this.controlRep.BasesReplicas.size(); i++) {
            if (!this.controlRep.BasesReplicas.get(i).isEstado()) {
                existePausado= true;
            }
        }
        return existePausado;
    }
    
    private void intenteLimpiar(int id, String entidad){
        
        /**
         * **PROCESO DE LIMPIEZA DE TABLA PRIORIDAD ***
         */
        if (this.existeReplicaPausada()) {
            this.controlRep.getBaseOrigen().cambiaTablaEventos();
        } else {
            /**
             * Borrar los que esten en lista de prioridad*
             */
            this.controlRep.getBaseOrigen().eliminarRegistroTablaEventos(id, entidad);
        }
    
    
    }
    
     public void insertDataToReplicaMySQL(String tableName,int id,connection_control destination,connection_control connection) throws SQLException, IOException{
        
        ResultSet resultset = connection.getAllData(tableName,id);
        int column = 1;//contador usado para iterar sobre las columnas
        while(resultset.next()){
            String insertData = "INSERT INTO " + tableName + " VALUES (";
            ResultSet Atributos = connection.getAllAtributosDeTabla(tableName,"dbo"); //ResultSet usado para saber que tipo es el dato 
                     
            while(true){
                try{
                    Atributos.next();
                    String atributo = Atributos.getString(1);
                    if(!atributo.equals("idControl")){
                        
                        if ("int".equals(Atributos.getString(2)) || null == resultset.getString(column)) {
                            insertData += resultset.getString(column);
                        } else {
                            insertData += "'" + resultset.getString(column) + "'";
                        }
                        try {
                            resultset.getString(column + 1);
                            insertData += ", ";
                        } catch (Exception e) {

                        }
                        column++;
                    }
                    else{
                        //Tiene que quitar la ultima coma
                        insertData=insertData.substring(0,insertData.length()-2);
                        
                    }
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
    
}
