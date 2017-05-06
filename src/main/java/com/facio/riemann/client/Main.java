/*
 */
package com.facio.riemann.client;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author fabiano
 */
public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {            
            LOG.info("Send to riemann ...");
            
            RiemannClient c = RiemannClient.tcp("localhost", 5555);
            c.connect();
            c.event().
                    service("console-java-client").
                    state("running").
                    metric(66.6).
                    tags("test", "java").
                    send().
                    deref(5000, java.util.concurrent.TimeUnit.MILLISECONDS);
            
            LOG.info("Event sent to Riemann.");

            c.query("tagged \"test\" and metric > 0").deref(); // => List<Event>;
            
            LOG.info("Event found");
            c.close();
            
            LOG.info("Riemann client closed.");        
        } catch (Exception ex) {
            LOG.error("failed to execute client", ex);
        }
    }
}
