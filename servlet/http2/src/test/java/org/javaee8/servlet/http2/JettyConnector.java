package org.javaee8.servlet.http2;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configuration;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData.Request;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FuturePromise;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.spi.AsyncConnectorCallback;
import org.glassfish.jersey.client.spi.Connector;
import org.glassfish.jersey.message.internal.Statuses;

public class JettyConnector implements Connector {

    private HTTP2Client client;
    private SslContextFactory sslContextFactory;

    /**
     * Needed for the JAX-RS connector creation.
     */
    public JettyConnector(final Client jaxrsClient, final Configuration config) {
        this();
    }

    public JettyConnector() {
        this(Level.INFO);
    }

    public JettyConnector(Level logLevel) {
        System.setProperty("org.eclipse.jetty.client.LEVEL", logLevel.getName());
        client = new HTTP2Client();

        // Configure SSL for test. Ignore insecure certificates
        sslContextFactory = new SslContextFactory(true);
        client.addBean(sslContextFactory);

        // Start client
        try {
            client.start();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start client.");
        }
    }

    @Override
    public ClientResponse apply(ClientRequest request) {

        String host = request.getUri().getHost();
        int port = request.getUri().getPort();
        if (port == -1) {
            port = 443;
        }
        boolean secure = request.getUri().getScheme().equals("https");

        // Get the session
        Session session = createSession(host, port, secure);

        // Create HTTP headers
        HttpFields headers = new HttpFields();
        request.getStringHeaders().forEach((key, value) -> {
            headers.put(key, value);
        });
        headers.put("User-Agent", getName());
        headers.put("Host", host + ":" + port);

        // Create the request
        Request jettyRequest = new Request(request.getMethod(), new HttpURI(request.getUri()), HttpVersion.HTTP_2,
                headers);

        // Stored metadata
        int status = 200;
        StringBuilder entityStream = new StringBuilder();
        Map<String, String> responseHeaders = new HashMap<>();

        // Response listener
        CountDownLatch latch = new CountDownLatch(1);
        Stream.Listener responseListener = new Stream.Listener.Adapter() {
            @Override
            public void onHeaders(Stream stream, HeadersFrame frame) {
                frame.getMetaData().getFields().forEach(field -> {
                    responseHeaders.put(field.getName(), field.getValue());
                });
            }

            @Override
            public void onData(Stream stream, DataFrame frame, Callback callback) {
                byte[] bytes = new byte[frame.getData().remaining()];
                frame.getData().get(bytes);
                entityStream.append(new String(bytes));
                if (frame.isEndStream()) {
                    latch.countDown();
                    callback.succeeded();
                }
            }
        };

        // Make the connection
        session.newStream(new HeadersFrame(jettyRequest, null, true), new FuturePromise<>(), responseListener);

        // Wait for response
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("The request timed out. This usually means HTTP/2 isn't supported.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Request interrupted.");
        }

        // Build response from metadata
        ClientResponse response = new ClientResponse(Statuses.from(status), request);
        response.setEntityStream(new ByteArrayInputStream(entityStream.toString().getBytes()));
        responseHeaders.forEach((key, value) -> {
            response.header(key, value);
        });
        return response;
    }

    @Override
    public Future<?> apply(ClientRequest request, AsyncConnectorCallback callback) {
        throw new UnsupportedOperationException("Unimplemented method.");
    }

    @Override
    public String getName() {
        return client.getClass().getName() + "/" + Jetty.VERSION;
    }

    @Override
    public void close() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session createSession(String host, int port, boolean secure) {
        FuturePromise<Session> sessionPromise = new FuturePromise<>();
        if (secure) {
            client.connect(sslContextFactory, new InetSocketAddress(host, port), new ServerSessionListener.Adapter(),
                    sessionPromise);
        } else {
            client.connect(new InetSocketAddress(host, port), new ServerSessionListener.Adapter(), sessionPromise);
        }

        try {
            return sessionPromise.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException("Cloud not get a session.", e);
        }
    }

}
