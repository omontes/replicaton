/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar Montes
 */
public class HiloPrueba implements Runnable 
    {
    
     
    public void run ( ) 
    {
        //while (!Thread.currentThread().isInterrupted()){
        for (int i = 0; i < 5; i++) {

            try {
                //Matar hilo
                /**
                 * if(i==1){ Thread.currentThread().interrupt();
                }*
                 */
                if ((i % 2) == 0) {
                    ConnectionControl adminBD = ConnectionControl.getInstanceSQLServer();
                    adminBD.consultarEmpleados();
                } else {
                    System.out.println("-------Company1----------");
                    ConnectionControl adminBD = ConnectionControl.getInstanceMySQLCompany();
                    adminBD.consultarEmpleados();
                    System.out.println("-------Company2----------");
                    ConnectionControl adminBD2 = ConnectionControl.getInstanceMySQLCompany2();
                    adminBD2.consultarEmpleados();
                }

                System.out.println("-----------------------");

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // good practice
                Thread.currentThread().interrupt();
                return;
            }

        }
    }
       
    
    
}
