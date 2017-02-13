package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.DatabaseService;
import io.research.vertx.io.research.vertx.utils.Holder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class WorkerVerticle extends AbstractVerticle {
    public void start(){
        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        eventBus.consumer("ABC",message -> {
            System.out.println("Executing Thread:::" + Thread.currentThread());

            vertx.executeBlocking(future -> {
                Handler<JsonObject> resultSetHandler = new Handler<JsonObject>() {
                    @Override
                    public void handle(JsonObject resultSet) {
                        if(resultSet != null) {
                            future.complete(resultSet);
                        }else{
                            future.complete(new JsonObject());
                        }
                    }
                };

                JsonObject requestBody = new JsonObject(message.body().toString());
                String procId  = requestBody.getValue("procID").toString();

                // call Proc_1()
                String query =  "call " + "Proc_" +  procId + "();";

                DatabaseService databaseService = new DatabaseService();
                databaseService.query(resultSetHandler,query);

            },res->{
                if(res.failed()){
                    System.out.println("Execution failed in event bus\n"+ res.cause());
                }else{
                    message.reply(res.result());
                }
            });
        });

    }
}
