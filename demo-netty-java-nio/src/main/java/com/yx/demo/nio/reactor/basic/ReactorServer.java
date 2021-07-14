package com.yx.demo.nio.reactor.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class ReactorServer {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public void startup() {
        try {
            Processor processor = new Processor();
            Acceptor acceptor = new Acceptor( 9092, processor);
            executor.execute(acceptor);
            executor.execute(processor);
            acceptor.await();
            processor.await();
            System.out.println("acceptor thread started....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}