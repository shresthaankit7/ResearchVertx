package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.Holder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class MainVerticle extends AbstractVerticle {

    HttpClientOptions options = new HttpClientOptions().setSsl(false).setVerifyHost(false).setTrustAll(false);

    public void start(){
        System.out.println("Verticle Main Deployed::" + Thread.currentThread());

        Holder holder = Holder.getInstance();
        holder.setVertx(vertx);
        holder.setConfig(vertx.getOrCreateContext().config());
        System.out.println("Config::" + Holder.getInstance().getConfig());

        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        vertx.deployVerticle("io.research.vertx.WorkerVerticle",new DeploymentOptions().setWorker(true).setInstances(40));


        HttpServer httpServer = vertx.createHttpServer();


        Router router = Router.router(vertx);
        router.route("/person/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();

            String input = routingContext.request().getParam("input");
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);

            System.out.println("INPUT time : " + strDate);

            JsonObject procNumber = new JsonObject();

            String query = "call Proc_1();";

//            HttpClientOptions options = new HttpClientOptions().setKeepAlive(false);
            HttpClientOptions options = new HttpClientOptions().setSsl(false).setVerifyHost(false).setTrustAll(true);
            HttpClient client = vertx.createHttpClient();
            client.getNow(8080, "localhost", "/GrailsAngular/person/index", response1 -> {
                System.out.println("Received response with status code " + response1.statusCode());

                System.out.println("Response time: " +  sdfDate.format(new Date()));
                response.end("Sum:");
                response.close();
            });
//            eventBus.send("ABC", procNumber,new DeliveryOptions().setSendTimeout(900 * 1000), handler -> {
//                    if (handler.succeeded()) {
//                        JsonObject resultSet = new JsonObject(handler.result().body().toString());
//                        System.out.println("RESULT:::" + resultSet);
//
//                        response.putHeader("content-type", "text/plain");
//                        response.end("Sum:" + resultSet.getValue("Value").toString());
//                        response.close();
//                    } else {
//                        System.out.println("Error Getting response." + handler.cause());
//                    }
//                });

        });

        httpServer.requestHandler(router::accept).listen(9000);


    }
}
