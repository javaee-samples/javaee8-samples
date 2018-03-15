package org.javaee8.servlet.http2;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
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

public class WebClient {

    private HTTP2Client client;
    private SslContextFactory sslContextFactory;

    public WebClient() {
        this(Level.INFO);
    }

    public WebClient(Level logLevel) {
        System.setProperty("org.eclipse.jetty.client.LEVEL", logLevel.getName());
    }

    //private Pattern urlMatcher = Pattern.compile("^(?<protocol>http[s]?):\\/\\/(?<host>[\\w.]+)(?:(?=:):(?<port>9000)|\\/)");

    public String getResponse(String url)
            throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        URI uri = new URI(url);

        String host = uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            port = 443;
        }
        String scheme = uri.getScheme();

        FuturePromise<Session> sessionPromise = new FuturePromise<>();
        if (scheme.contains("https")) {
            client.connect(sslContextFactory, new InetSocketAddress(host, port), new ServerSessionListener.Adapter(),
                    sessionPromise);
        } else {
            client.connect(new InetSocketAddress(host, port), new ServerSessionListener.Adapter(), sessionPromise);
        }

        Session session = sessionPromise.get(5, TimeUnit.SECONDS);

        HttpFields requestFields = new HttpFields();
        requestFields.put("User-Agent", client.getClass().getName() + "/" + Jetty.VERSION);
        requestFields.put("Host", host + ":" + port);

        MetaData.Request request = new MetaData.Request("GET", new HttpURI(url), HttpVersion.HTTP_2, requestFields);

        HeadersFrame headersFrame = new HeadersFrame(request, null, true);

        CountDownLatch latch = new CountDownLatch(1);
        final StringBuilder response = new StringBuilder();
        Stream.Listener responseListener = new Stream.Listener.Adapter() {
            @Override
            public void onData(Stream stream, DataFrame frame, Callback callback) {
                byte[] bytes = new byte[frame.getData().remaining()];
                frame.getData().get(bytes);
                response.append(new String(bytes));
                latch.countDown();
                callback.succeeded();
            }
        };

        session.newStream(headersFrame, new FuturePromise<>(), responseListener);

        if (!latch.await(1, TimeUnit.SECONDS)) {
            throw new RuntimeException("The request timed out.");
        }
        return response.toString();
    }

    public void start() throws Exception {
        client = new HTTP2Client();

        // Configure SSL for test
        sslContextFactory = new SslContextFactory(true);
        client.addBean(sslContextFactory);

        // Start client
        client.start();
    }

    public void stop() throws Exception {
        client.stop();
    }

}