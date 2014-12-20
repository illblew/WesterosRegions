package com.westeroscraft.regions;

import java.sql.DriverManager;
import org.apache.commons.pool.BasePoolableObjectFactory;
 
public class MySqlPoolableObjectFactory extends BasePoolableObjectFactory {
     private String host;
     private int port;
     private String schema;
     private String user;
     private String password;
 
     public MySqlPoolableObjectFactory(String host, int port, String schema,
                                                String user, String password) {
          this.host = host;
          this.port = port;
          this.schema = schema;
          this.user = user;
          this.password = password;
     }
 
     public Object makeObject() throws Exception {
          Class.forName("com.mysql.jdbc.Driver").newInstance();
          String url = "jdbc:mysql://" + host + ":" + port + "/"
               + schema + "?autoReconnectForPools=true";
          return DriverManager.getConnection(url, user, password);
      }
}
