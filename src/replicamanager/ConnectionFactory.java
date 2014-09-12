/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replicamanager;

import java.sql.Connection;

/**
 *
 * @author Oscar Montes
 */
public interface ConnectionFactory {
   

  Connection getConnectionFactory();
  public String getNombreBase();
  public String getSchemaName();
}

