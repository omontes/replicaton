/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Oscar Montes
 */
public class ControlReplicas {
    private connection_control BaseOrigen;
    PriorityQueue <connection_control> ColaReplica = new PriorityQueue();
 
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
    
    public void despausarReplica(connection_control ReplicaParaDespausar) throws SQLException{
        /***SE ACTUALIZA LA REPLICA *****/
        //Se obtienen los datos para actualizar
        ResultSet obtenerDatos = this.getBaseOrigen().consultarTablaEventos();
        //Se actualiza la replica
        ReplicaParaDespausar.actualizarReplica(obtenerDatos);
        ReplicaParaDespausar.setEstado(true);
        if(!existeReplicaPausada())
            this.getBaseOrigen().eliminarTablaEventos();
        
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
    
   
    
}
