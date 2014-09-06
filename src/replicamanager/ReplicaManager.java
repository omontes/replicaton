/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

/**
 *
 * @author Oscar Montes
 */
public class ReplicaManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //connection_control adminBD =connection_control.getInstance();
        //adminBD.consultarEmpleados();
        hiloPrueba t = new hiloPrueba();
        new Thread(t).start();
        
        
    }
    
}
