package io.research.vertx.io.research.vertx.utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class Holder {
    public static Holder instance = null;
    private Vertx vertx = null;
    private JsonObject config = null;

    public static Holder getInstance(){
        if( instance == null){
            instance = new Holder();
        }
        return instance;
    }

    public void setVertx(Vertx vertx){
        this.vertx = vertx;
    }

    public Vertx getVertx(){
        return this.vertx;
    }

    public void setConfig(JsonObject configuration){
        this.config = configuration;
    }

    public JsonObject getConfig(){
        return this.config;
    }

}
