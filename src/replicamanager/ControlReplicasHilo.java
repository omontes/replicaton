/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

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
        boolean flag=true;
        while(true){
            try {
                
                if(flag){
                ResultSet eventos =this.controlRep.BaseOrigen.consultarTablaEventos();
                while (eventos.next()) {
                    int id = eventos.getInt("id");
                    String entidad= eventos.getString("entidad");
                    String tipoEvento = eventos.getString("tipoEvento");
                    String enable= eventos.getString("enable");
                    
                    /****LISTA DE EVENTOS *****/
                    if (tipoEvento.equals("Inserccion") && enable.equals("1")) {
                        this.insertarReplicas(id, entidad);
                        
                    }
                    flag=false;
                    /****PROCESO DE LIMPIEZA DE TABLA PRIORIDAD ****/
                    if(this.existeReplicaPausada()){
                        /***** No puede borrar y tiene que
                         * poner en cero los que esten en la lista de prioridad
                         */
                        System.out.println("hola osarcar");
                    }
                    else{
                        /**Borrar los que esten en lista de prioridad**/
                        System.out.println("jlj");
                    }
                }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControlReplicasHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        }

    
    
    
    private void insertarReplicas(int id, String entidad) {
        
        for (int i = 0; i < this.controlRep.BasesReplicas.size(); i++) {
            if (this.controlRep.BasesReplicas.get(i).isEstado()) {
                this.controlRep.BasesReplicas.get(i).insertarDatoReplica(id,
                        entidad.toLowerCase());
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

    
    
}
