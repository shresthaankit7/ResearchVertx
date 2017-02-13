package io.research.vertx.io.research.vertx.utils;

/**
 * Created by ankshrestha on 2/10/17.
 */
public class SumClass {
    public int sum = 0;
    public int count = 0;

    public void add(int a){
        sum = sum + a;
        count++;
    }

    public int getSum(){
        return sum;
    }

    public int getCount(){
        return count;
    }
}
