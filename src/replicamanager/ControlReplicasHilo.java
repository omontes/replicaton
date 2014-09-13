/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;
//mysqlserver2 = new MySqlConnectionFactory("localhost","root","123456","miBase2");
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar Montes
 */
public class ControlReplicasHilo implements Runnable {
    ControlReplicas controlRep;
    int controlOrigenes;
    boolean flag;
    public ControlReplicasHilo(ControlReplicas control){
        this.controlRep=control;
        flag=true;
    }

    @Override
    public void run() {
        
        while(flag){
            try {
                Thread.sleep(300);
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
                this.cambiarOrigen();
                

            } catch (SQLException ex) {
                Logger.getLogger(ControlReplicasHilo.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
                
            }
        }

    
    
    private void insertarReplicas(int id, String entidad) {
        Iterator listaReplicas=this.controlRep.ColaReplica.iterator();
        while(listaReplicas.hasNext()){
            connection_control miReplica = (connection_control)listaReplicas.next();
            if (miReplica.isEstado()) {
                try {
                    this.insertDataToReplica(entidad,id,
                            miReplica,
                            this.controlRep.getBaseOrigen());
                    miReplica.eliminarRegistroTablaEventos(id, entidad);
                } catch (SQLException ex) {
                    Logger.getLogger(ControlReplicasHilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ControlReplicasHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
               
               
            
        }
    }
    }
    
    public boolean existeReplicaPausada() {
        boolean existePausado = false;
        Iterator listaReplicas=this.controlRep.ColaReplica.iterator();
        while(listaReplicas.hasNext()){
            connection_control miReplica = (connection_control)listaReplicas.next();
            if (!miReplica.isEstado()) {
                existePausado= true;
            }
        }
        return existePausado;}
    
    
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
     public void cambiarOrigen(){
        connection_control replicaAPromocionar = this.controlRep.ColaReplica.remove();
        
        
        if (replicaAPromocionar.isEstado()) {
            //Como la replica si esta activa se realiza el swap
            //Inserta en la cola el origen actual  
            this.controlRep.ColaReplica.add(this.controlRep.getBaseOrigen());
            //Promociona dicha replica a base origen
            this.controlRep.setBaseOrigen(replicaAPromocionar);
        } else {
            //Como la replica que saco de la lista esta pausada
            //Vuelva a ingresarla en la cola de ultimo para que la prox vez
            // siga con la siguiente de la cola
            this.controlRep.ColaReplica.offer(replicaAPromocionar);
        }
     }
     public void insertDataToReplica(String tableName,int id,connection_control destination,connection_control connection) throws SQLException, IOException{
        
        ResultSet resultset = connection.getAllData(tableName,id);
        int column = 1;//contador usado para iterar sobre las columnas
        
        while(resultset.next()){
            String insertData = "INSERT INTO " + tableName + "(";
            ResultSet Atributos = connection.getAllAtributosDeTabla(tableName, connection.schemaName); //ResultSet usado para saber que tipo es el dato 
            while (Atributos.next()) {
                String atributo = Atributos.getString(1);
                if (!atributo.equals("idControl")) {

                    insertData += ""+atributo+"";;
                    insertData += ",";

                    column++;
                } else {
                    //Tiene que quitar la ultima coma
                    insertData = insertData.substring(0, insertData.length() - 1);

                }
            }
            insertData += ") VALUES(";
            column = 1;
            ResultSet Atributos2 = connection.getAllAtributosDeTabla(tableName, connection.schemaName); //ResultSet usado para saber que tipo es el dato 
            while(true){
                try{
                    Atributos2.next();
                    String atributo = Atributos2.getString(1);
                    if(!atributo.equals("idControl")){
                        
                        if ("int".equals(Atributos2.getString(2)) || null == resultset.getString(column)) {
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
                    System.out.println("Inserccion del hilo");
                    System.out.println(insertData);
                    destination.executeQuery(insertData);
                    insertData = "";
                    break;
                }
            }
            column = 1;
        }
    }
    
}
