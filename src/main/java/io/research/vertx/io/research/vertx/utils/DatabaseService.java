package io.research.vertx.io.research.vertx.utils;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class DatabaseService {


    public void query(Handler<JsonObject> resultSetHandler,String storedProcCall){
        JsonObject config = Holder.getInstance().getConfig().getJsonObject("databaseConfig");
        JDBCClient jdbcClient = JDBCClient.createShared(Holder.getInstance().getVertx(),
                config);
            execute(jdbcClient,storedProcCall,resultSetHandler);

        System.out.println("Executing query!");
    }

    public void execute(JDBCClient client,String query,Handler<JsonObject> done){
        client.getConnection(sqlConnectionAsyncResult -> {
            if(sqlConnectionAsyncResult.failed()){
                System.out.println("Error Connection to the database with config.");
                done.handle(null);
            }else{
                SQLConnection connection = sqlConnectionAsyncResult.result();
                connection.query(query,resultSetAsyncResult -> {
                   if( resultSetAsyncResult.failed()){
                       System.out.println("Failed Execute Query." + resultSetAsyncResult.cause());
                       connection.close();
                   }
                    if(resultSetAsyncResult.succeeded()){
                        JsonObject output = new JsonObject();
                        output.put("Value",resultSetAsyncResult.result().getRows().get(0).getValue("output").toString());
                       done.handle(output);
                        connection.close();
                   }
                });
            }
        });
    }
}
