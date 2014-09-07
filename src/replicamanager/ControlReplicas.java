/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Oscar Montes
 */
public class ControlReplicas {
    ConnectionControl BaseOrigen;
    ArrayList<ConnectionControl> BasesReplicas = new ArrayList<ConnectionControl>();
   
    public ControlReplicas(ConnectionControl BaseOrigen){
        this.BaseOrigen=BaseOrigen;
    }
    
    public void agregarReplica(ConnectionControl BaseReplica){
        this.BasesReplicas.add(BaseReplica);
    }
    
    
    private boolean existeReplicaPausada() {
        boolean existePausado = false;
        for (int i = 0; i < BasesReplicas.size(); i++) {
            if (!BasesReplicas.get(i).isEstado()) {
                existePausado= true;
            }
        }
        return existePausado;
    }
    
    public void despausarReplica(ConnectionControl ReplicaParaDespausar) throws SQLException{
        /***SE ACTUALIZA LA REPLICA *****/
        //Se obtienen los datos para actualizar
        ResultSet obtenerDatos = this.BaseOrigen.consultarTablaEventos();
        //Se actualiza la replica
        ReplicaParaDespausar.actualizarReplica(obtenerDatos);
        ReplicaParaDespausar.setEstado(true);
        if(!existeReplicaPausada())
            this.BaseOrigen.eliminarTablaEventos();
        
    }
    
   
    
}
