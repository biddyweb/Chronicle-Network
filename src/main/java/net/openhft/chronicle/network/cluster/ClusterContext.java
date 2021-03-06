/*
 * Copyright 2016 higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.chronicle.network.cluster;

import net.openhft.chronicle.core.annotation.UsedViaReflection;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.threads.EventLoop;
import net.openhft.chronicle.core.util.ThrowingFunction;
import net.openhft.chronicle.network.*;
import net.openhft.chronicle.network.connection.WireOutPublisher;
import net.openhft.chronicle.wire.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Rob Austin.
 */
public class ClusterContext implements Demarshallable, WriteMarshallable, Consumer<HostDetails> {

    private ConnectionStrategy connectionStrategy;
    private WireType wireType;
    private BiFunction<ClusterContext, HostDetails, WriteMarshallable> handlerFactory;
    private Function<WireType, WireOutPublisher> wireOutPublisherFactory;
    private Function<ClusterContext, NetworkContext> networkContextFactory;
    private Supplier<ConnectionManager> connectionEventHandler;
    private long heartbeatTimeoutMs = 40_000;
    private long heartbeatIntervalMs = 20_000;
    private String clusterName;
    private EventLoop eventLoop;
    private Function<ClusterContext, WriteMarshallable> heartbeatFactory;
    private byte localIdentifier;
    private Function<ClusterContext, NetworkStatsListener>
            networkStatsListenerFactory;
    private ServerThreadingStrategy serverThreadingStrategy;

    @UsedViaReflection
    protected ClusterContext(@NotNull WireIn wire) throws IORuntimeException {
        defaults();
        while (wire.bytes().readRemaining() > 0)
            wireParser().parseOne(wire, null);
    }

    protected ClusterContext() {
        defaults();
    }

    public Function<ClusterContext, NetworkStatsListener> networkStatsListenerFactory() {
        return networkStatsListenerFactory;
    }

    public ClusterContext networkStatsListenerFactory(Function<ClusterContext, NetworkStatsListener> networkStatsListenerFactory) {
        this.networkStatsListenerFactory = networkStatsListenerFactory;
        return this;
    }

    public long heartbeatIntervalMs() {
        return heartbeatIntervalMs;
    }

    public ThrowingFunction<NetworkContext, TcpEventHandler, IOException> tcpEventHandlerFactory() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    private WireParser<Void> wireParser() {
        WireParser<Void> parser = new VanillaWireParser<>((s, v, $) -> {
        });
        parser.register(() -> "wireType", (s, v, $) -> v.text(this, (o, x) -> this.wireType(WireType.valueOf(x))));
        parser.register(() -> "handlerFactory", (s, v, $) -> this.handlerFactory(v.typedMarshallable()));
        parser.register(() -> "heartbeatTimeoutMs", (s, v, $) -> this.heartbeatTimeoutMs(v.int64()));
        parser.register(() -> "heartbeatIntervalMs", (s, v, $) -> this.heartbeatIntervalMs(v.int64()));
        parser.register(() -> "wireOutPublisherFactory",
                (s, v, $) -> this.wireOutPublisherFactory(v.typedMarshallable()));
        parser.register(() -> "networkContextFactory",
                (s, v, $) -> this.networkContextFactory(v.typedMarshallable()));
        parser.register(() -> "connectionStrategy",
                (s, v, $) -> this.connectionStrategy(v.typedMarshallable()));
        parser.register(() -> "connectionEventHandler",
                (s, v, $) -> this.connectionEventHandler(v.typedMarshallable()));
        parser.register(() -> "heartbeatFactory",
                (s, v, $) -> this.heartbeatFactory(v.typedMarshallable()));
        parser.register(() -> "networkStatsListenerFactory",
                (s, v, $) -> this.networkStatsListenerFactory(v.typedMarshallable()));
        parser.register(() -> "serverThreadingStrategy",
                (s, v, $) -> this.serverThreadingStrategy(v.asEnum(ServerThreadingStrategy.class)));
        return parser;
    }

    public void serverThreadingStrategy(ServerThreadingStrategy serverThreadingStrategy) {
        this.serverThreadingStrategy = serverThreadingStrategy;
    }

    public ServerThreadingStrategy serverThreadingStrategy() {
        return serverThreadingStrategy;
    }

    private BiFunction<ClusterContext, HostDetails, WriteMarshallable> handlerFactory() {
        return handlerFactory;
    }

    public void handlerFactory(BiFunction<ClusterContext, HostDetails, WriteMarshallable> handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public void clusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public EventLoop eventLoop() {
        return eventLoop;
    }

    public ClusterContext eventLoop(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
        return this;
    }

    public void defaults() {
    }

    public ClusterContext localIdentifier(byte localIdentifier) {
        this.localIdentifier = localIdentifier;
        return this;
    }

    public ClusterContext wireType(WireType wireType) {
        this.wireType = wireType;
        return this;
    }

    public ClusterContext heartbeatFactory(Function<ClusterContext, WriteMarshallable>
                                                   heartbeatFactor) {
        this.heartbeatFactory = heartbeatFactor;
        return this;
    }

    public ClusterContext heartbeatIntervalMs(long heartbeatIntervalMs) {
        this.heartbeatIntervalMs = heartbeatIntervalMs;
        return this;
    }

    public ClusterContext heartbeatTimeoutMs(long heartbeatTimeoutMs) {
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
        return this;
    }

    public ClusterContext wireOutPublisherFactory(Function<WireType, WireOutPublisher> wireOutPublisherFactory) {
        this.wireOutPublisherFactory = wireOutPublisherFactory;
        return this;
    }

    public ClusterContext networkContextFactory(Function<ClusterContext, NetworkContext> networkContextFactory) {
        this.networkContextFactory = networkContextFactory;
        return this;
    }

    public WireType wireType() {
        return wireType;
    }

    public Function<WireType, WireOutPublisher> wireOutPublisherFactory() {
        return wireOutPublisherFactory;
    }

    public long heartbeatTimeoutMs() {
        return heartbeatTimeoutMs;
    }

    public String clusterName() {
        return clusterName;
    }

    public byte localIdentifier() {
        return localIdentifier;
    }

    public Function<ClusterContext, NetworkContext> networkContextFactory() {
        return networkContextFactory;
    }

    public ClusterContext connectionStrategy(ConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
        return this;
    }

    private ConnectionStrategy connectionStrategy() {
        return this.connectionStrategy;
    }

    private Supplier<ConnectionManager> connectionEventHandler() {
        return connectionEventHandler;
    }

    public ClusterContext connectionEventHandler(Supplier<ConnectionManager> connectionEventHandler) {
        this.connectionEventHandler = connectionEventHandler;
        return this;
    }

    private Function<ClusterContext, WriteMarshallable> heartbeatFactory() {
        return heartbeatFactory;
    }

    @Override
    public void writeMarshallable(@NotNull WireOut wireOut) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public void accept(@NotNull HostDetails hd) {
        if (this.localIdentifier == hd.hostId())
            return;

        final ConnectionStrategy connectionStrategy = this.connectionStrategy();
        hd.connectionStrategy(connectionStrategy);

        final ConnectionManager connectionManager = this
                .connectionEventHandler().get();
        hd.connectionManager(connectionManager);

        final HostConnector hostConnector = new HostConnector(this, new
                RemoteConnector(this.tcpEventHandlerFactory()), hd);

        hd.hostConnector(hostConnector);

        ClusterNotifier clusterNotifier = new ClusterNotifier(connectionManager,
                hostConnector, bootstraps(hd));

        hd.clusterNotifier(clusterNotifier);
        hd.terminationEventHandler(clusterNotifier);

        clusterNotifier.connect();
    }

    private List<WriteMarshallable> bootstraps(HostDetails hd) {
        final BiFunction<ClusterContext, HostDetails, WriteMarshallable> handler = this
                .handlerFactory();
        final Function<ClusterContext, WriteMarshallable> heartbeat = this.heartbeatFactory();

        ArrayList<WriteMarshallable> result = new ArrayList<WriteMarshallable>();
        result.add(handler.apply(this, hd));
        result.add(heartbeat.apply(this));
        return result;
    }
}

