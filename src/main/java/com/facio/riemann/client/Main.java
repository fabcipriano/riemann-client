/*
 */
package com.facio.riemann.client;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;

/**
 *
 * @author fabiano
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Send to riemann ...");
        RiemannClient c = RiemannClient.tcp("localhost", 5555);
        c.connect();
        c.event().
                service("console-java-client").
                state("running").
                metric(66.6).
                tags("test", "java").
                send().
                deref(5000, java.util.concurrent.TimeUnit.MILLISECONDS);

        c.query("tagged \"test\" and metric > 0").deref(); // => List<Event>;
        c.close();     
        
        System.out.println("Event sent to Riemann.");
    }
}
