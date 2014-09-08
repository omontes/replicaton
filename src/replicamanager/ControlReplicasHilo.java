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
                ResultSet atributos =this.controlRep.getBaseOrigen().getAllAtributosDeTabla(entidad, "dbo");
                ResultSet datos = this.controlRep.getBaseOrigen().obtenerDatos(entidad,id);
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
    
}
