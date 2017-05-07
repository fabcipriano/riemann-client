/*
 */
package com.facio.riemann.client;

import io.riemann.riemann.Proto;
import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.List;
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
        try (RiemannClient c = RiemannClient.tcp("localhost", 5555)) {
            LOG.info("Send to riemann ...");
            
            c.connect();
            c.event().
                    service("console-java-client").
                    state("running").
                    metric(99.9).
                    tags("test", "java").
                    send().
                    deref(5000, java.util.concurrent.TimeUnit.MILLISECONDS);
            
            LOG.info("Event sent to Riemann.");

            List<Proto.Event> events = c.query("tagged \"test\" and metric > 0").deref(); // => List<Event>;
            
            LOG.info("Event found, events={}", events);
            
        } catch (Exception ex) {
            LOG.error("failed to execute client", ex);
        } finally {
            LOG.info("Riemann client closed.");        
        }
    }
}
