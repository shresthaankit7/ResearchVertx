package io.research.vertx;

import io.research.vertx.io.research.vertx.utils.DatabaseService;
import io.research.vertx.io.research.vertx.utils.Holder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class WorkerVerticle extends AbstractVerticle {

    int count = 0;

    public void start(){
        EventBus eventBus = Holder.getInstance().getVertx().eventBus();

        DatabaseService databaseService = new DatabaseService();

        System.out.println("WORKER THRED::" + Thread.currentThread() + "WORKER CONTEXT::" + vertx.getOrCreateContext());

        eventBus.consumer("ABC",message -> {
            System.out.println("Worker Thread:::" + Thread.currentThread() + "\nWorker Context::" + vertx.getOrCreateContext());

                System.out.println("FOR LOOP THREAD Worker::" + Thread.currentThread().toString() + "\n"
                                + "LOOP CONTEXT Worker:::" + vertx.getOrCreateContext().toString());
                String query = "call Proc_1();";
                databaseService.query(h -> {
                    if (h != null) {
                        System.out.println("Executing Thread worker::" + Thread.currentThread().toString() + "\n"
                                + "CONTEXT:::" + vertx.getOrCreateContext());
                        System.out.println("RESULT GOT ::" + h);
                        message.reply(h);
                    }
                }, query);
        });

    }
}
