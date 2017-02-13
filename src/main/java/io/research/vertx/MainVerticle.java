package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.Holder;
import io.research.vertx.io.research.vertx.utils.SumClass;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class MainVerticle extends AbstractVerticle {
    public void start(){
        System.out.println("Verticle Main Deployed::" + Thread.currentThread());

        Holder holder = Holder.getInstance();
        holder.setVertx(vertx);
        holder.setConfig(vertx.getOrCreateContext().config());
        System.out.println("Config::" + Holder.getInstance().getConfig());

        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        vertx.deployVerticle("io.research.vertx.WorkerVerticle",new DeploymentOptions().setWorker(true).setInstances(20));


        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(request -> {
            HttpServerResponse response = request.response();
            SumClass sumClass = new SumClass();

            for(int i = 1; i <= 4; i++) {
                JsonObject procNumber = new JsonObject();
                procNumber.put("procID",i);

                eventBus.send("ABC", procNumber, handler -> {
                    if (handler.succeeded()) {
                        JsonObject resultSet = new JsonObject(handler.result().body().toString());
                        sumClass.add(1);
                        if(sumClass.count == 4){

                            response.putHeader("content-type", "text/plain");
                            response.end("Sum:" + sumClass.getSum());
                        }
                    } else {
                        System.out.println("Error Getting response.");
                    }
                });
            }
        });
        httpServer.listen(8080);
    }
}
