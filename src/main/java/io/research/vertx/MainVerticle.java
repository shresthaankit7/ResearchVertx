package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.Holder;
import io.research.vertx.io.research.vertx.utils.SumClass;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

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


        Router router = Router.router(vertx);
        router.route("/person/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();

            JsonObject procNumber = new JsonObject();

            eventBus.send("ABC", procNumber,new DeliveryOptions().setSendTimeout(900 * 1000), handler -> {
                    if (handler.succeeded()) {
                        JsonObject resultSet = new JsonObject(handler.result().body().toString());
                        System.out.println("RESULT:::" + resultSet + "\n\n");

                        response.putHeader("content-type", "text/plain");
                        response.end("Sum:" + resultSet.getValue("Value").toString());
                        response.close();
                    } else {
                        System.out.println("Error Getting response.");
                    }
                });
        });

        httpServer.requestHandler(router::accept).listen(8080);


    }
}
