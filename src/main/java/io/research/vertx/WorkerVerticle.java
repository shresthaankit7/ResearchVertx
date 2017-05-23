package io.research.vertx;

import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.research.vertx.io.research.vertx.utils.DatabaseService;
import io.research.vertx.io.research.vertx.utils.Holder;
import io.research.vertx.io.research.vertx.utils.SumClass;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class WorkerVerticle extends AbstractVerticle {
    public void start(){
        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        DatabaseService databaseService = new DatabaseService();


        eventBus.consumer("ABC",message -> {
            System.out.println("Executing Thread:::" + Thread.currentThread());

            JsonObject inputBody = new JsonObject(message.body().toString());
            String input = inputBody.getString("procID");

            SumClass sum = new SumClass();

            List<String> params = new ArrayList<String>();

            for(int i = 1; i <= Integer.parseInt(input); i++){
                params.add(Integer.toString(i));


                String query =  "call " + "Proc_" +  Integer.toString(i) + "();";
                databaseService.query(h->{
                    if(h != null){
                        System.out.println("RSULT GOT ::" + h);
                        sum.add( Integer.parseInt( h.getValue("Value").toString() ) );
                    }
                },query);
            }

            message.reply(new JsonObject().put("sum",sum.getSum()));

        });

    }
}
