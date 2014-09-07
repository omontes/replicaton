/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

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
    
   
    
}
