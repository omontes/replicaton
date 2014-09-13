/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar Montes
 */
public class ControlReplicas {
    private connection_control BaseOrigen;
    ConcurrentLinkedQueue <connection_control> ColaReplica = new ConcurrentLinkedQueue();
 
    public ControlReplicas(){
        
    }
    
    public void agregarReplica(connection_control BaseReplica){
        this.ColaReplica.add(BaseReplica);
    }
    
    
    private boolean existeReplicaPausada() {
        boolean existePausado = false;
        Iterator listaReplicas=ColaReplica.iterator();
        while(listaReplicas.hasNext()){
            connection_control miReplica = (connection_control)listaReplicas.next();
            if (miReplica.isEstado()) {
                existePausado= true;
            }
        }
        return existePausado;
    }
    
  
    /**
     * @return the BaseOrigen
     */
    public connection_control getBaseOrigen() {
        return BaseOrigen;
    }

    /**
     * @param BaseOrigen the BaseOrigen to set
     */
    public void setBaseOrigen(connection_control BaseOrigen) {
        this.BaseOrigen = BaseOrigen;
    }
    
    public void pausarReplica(String nombreReplica){
        Iterator listaReplicas=ColaReplica.iterator();
        while(listaReplicas.hasNext()){
            connection_control miReplica = (connection_control)listaReplicas.next();
            if (miReplica.nombreBD.equals(nombreReplica)) {
                miReplica.setEstado(false);
                System.out.println("*********************************SI PAUSOOOOOOO*******************");
                
            }
        }
        if(this.getBaseOrigen().nombreBD.equals(nombreReplica)){
            this.getBaseOrigen().setEstado(false);
            this.ColaReplica.offer(this.getBaseOrigen());
            this.setBaseOrigen(this.ColaReplica.remove());
            System.out.println("**************************SI PAUSOOOOOOO ERA ORIGEN*******************");
        }
    }

    void despausarReplica(String NombreBDReplica, ControlReplicasHilo hilo, Thread miHilo) {
        miHilo.interrupt();
        Iterator listaReplicas=ColaReplica.iterator();
        while(listaReplicas.hasNext()){
            
            connection_control miReplica = (connection_control)listaReplicas.next();
            if (miReplica.nombreBD.equals(NombreBDReplica)) {
                try {
                    
                    //Se obtienen los datos para actualizar
                    ResultSet datosActualizar = this.getBaseOrigen().consultarTablaEventos();
                    //Se actualiza la replica 
                    while (datosActualizar.next()) {
                        int id = datosActualizar.getInt("id");
                        String entidad = datosActualizar.getString("entidad");
                        String tipoEvento = datosActualizar.getString("tipoEvento");
                        if (tipoEvento.equals("Inserccion")) {
                             this.insertDataToReplica(entidad,id,miReplica,BaseOrigen);
                        // Se busca en las replicas si hay algo que deba de insertar
                             miReplica.eliminarRegistroTablaEventos(id, entidad);
                        }
                    }
                    //Busca datos en las demas replicas
                    this.buscarEventosEnReplicas(miReplica);
                    //Eliminar en el registro
                    
                    // Se activa la replica
                   
                    miReplica.setEstado(true);
                    System.out.println("*********************************SI DESPAUSOOOOOOO*******************");
                    // Se borra los registros de tablas logevent
                    if (!hilo.existeReplicaPausada()) {
                        
                        this.getBaseOrigen().eliminarTablaEventos();
                        this.borrarEnReplicas();
                        
                    }
                    
              
                    
                }
                
                catch (SQLException ex) {
                    Logger.getLogger(ControlReplicas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ControlReplicas.class.getName()).log(Level.SEVERE, null, ex);
                }
                miHilo= new Thread(hilo);
                miHilo.start();
                
            }
            
        }
       
//new Thread(hilo).start();
    }

    private void buscarEventosEnReplicas(connection_control ReplicaparaInsertar) {
        Iterator listaReplicas=ColaReplica.iterator();
        while (listaReplicas.hasNext()) {
            connection_control replicaOrigen = (connection_control) listaReplicas.next();
            if (replicaOrigen.isEstado() && !replicaOrigen.nombreBD.equals(ReplicaparaInsertar.nombreBD)) {
                try {
                    ResultSet datosActualizar = replicaOrigen.consultarTablaEventos();
                    while (datosActualizar.next()) {
                        int id = datosActualizar.getInt("id");
                        String entidad = datosActualizar.getString("entidad");
                        String tipoEvento = datosActualizar.getString("tipoEvento");
                        if (tipoEvento.equals("Inserccion")) {
                            this.insertDataToReplica(entidad,id,ReplicaparaInsertar,replicaOrigen);
                            // Se busca en las replicas si hay algo que deba de insertar
                            ReplicaparaInsertar.eliminarRegistroTablaEventos(id, entidad);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControlReplicas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ControlReplicas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void borrarEnReplicas() {
        Iterator listaReplicasParaBorrar=ColaReplica.iterator();
        while (listaReplicasParaBorrar.hasNext()) {
                            connection_control replica = (connection_control) listaReplicasParaBorrar.next();
                            replica.eliminarTablaEventos();
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
                    System.out.println("Inserccion de Despausa");
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
