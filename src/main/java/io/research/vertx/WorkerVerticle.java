package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.DatabaseService;
import io.research.vertx.io.research.vertx.utils.Holder;
import io.research.vertx.io.research.vertx.utils.SumClass;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class WorkerVerticle extends AbstractVerticle {

    int count = 0;

    public void start(){
        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        DatabaseService databaseService = new DatabaseService();


        eventBus.consumer("ABC",message -> {
            System.out.println("EventBUS Thread:::" + Thread.currentThread());

            JsonObject inputBody = new JsonObject(message.body().toString());
            String input = inputBody.getString("procID");

            SumClass sum = new SumClass();
            for(int i = 1; i <= Integer.parseInt(input); i++) {
                System.out.println("FOR LOOP THREAD::" + Thread.currentThread().toString());
                String query = "call " + "Proc_" + Integer.toString(i) + "();";
                databaseService.query(h -> {
                    if (h != null) {
                        System.out.println("Executing Thread::" + Thread.currentThread().toString());
                        System.out.println("RESULT GOT ::" + h);
                        sum.add(Integer.parseInt(h.getValue("Value").toString()));
                        count++;

                        if (count == Integer.parseInt(input))
                            message.reply(new JsonObject().put("sum", sum.getSum()));

                    }
                }, query);
            }
        });

    }
}
