/*
 *    Copyright (c) [2019] Payara Foundation and/or its affiliates.
 */

package org.javaee8.cdi.events.async.startup;

import org.awaitility.Awaitility;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@RunWith(Arquillian.class)
public class AsyncAtStartupTest {

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(ProducerAtStartup.class.getPackage())
                .addPackages(true, Awaitility.class.getPackage());
    }

    @Inject
    ObserverAtStartup observer;

    @Test
    public void startupEventIsEventuallyObserved() {
        await().atMost(5, TimeUnit.SECONDS).until(observer::isReceived);
    }
}
