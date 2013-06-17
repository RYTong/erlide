/*******************************************************************************
 * Copyright (c) 2010 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.backend.internal;

import java.io.IOException;

import org.erlide.backend.IErlRuntime;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.IRpcCallback;
import org.erlide.jinterface.rpc.IRpcFuture;
import org.erlide.jinterface.rpc.IRpcResultCallback;
import org.erlide.jinterface.rpc.RpcException;
import org.erlide.jinterface.rpc.RpcHelper;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import com.ericsson.otp.erlang.OtpNodeStatus;
import com.ericsson.otp.erlang.SignatureException;

public class ErlRuntime extends OtpNodeStatus implements IErlRuntime {
    private static final int MAX_RETRIES = 20;
    public static final int RETRY_DELAY = Integer.parseInt(System.getProperty(
            "erlide.connect.delay", "300"));
    private static final Object connectLock = new Object();
    private static final RpcHelper rpcHelper = RpcHelper.getInstance();

    public enum State {
        CONNECTED, DISCONNECTED, DOWN
    }

    private final String peerName;
    private State state;
    private OtpNode localNode;
    private final Object localNodeLock = new Object();
    private final String cookie;

    public ErlRuntime(final String name, final String cookie) {
        this(name, cookie, true);
        // if (epmdWatcher.isRunningNode(name)) {
        // connect();
        // }
    }
    
    public ErlRuntime(final String name, final String cookie, boolean async) {
        state = State.DISCONNECTED;
        peerName = name;
        this.cookie = cookie;
        startLocalNode(async);
    }
    
    public void startLocalNode() {
        startLocalNode(true);
    }
    
    public void startLocalNode(boolean async) {
        boolean nodeCreated = false;
        synchronized (localNodeLock) {
            int i = 0;
            do {
                try {
                    i++;
                    localNode = ErlRuntime.createOtpNode(cookie);
                    if (async) {
                        localNode.registerStatusHandler(this);
                    } else {
                        if(connectRetry()){
                            state = State.CONNECTED;
                        }
                    }                
                    nodeCreated = true;
                } catch (final IOException e) {
                    ErlLogger
                            .error("ErlRuntime could not be created (%s), retrying %d",
                                    e.getMessage(), i);
                    try {
                        localNodeLock.wait(300);
                    } catch (final InterruptedException e1) {
                    }
                }
            } while (!nodeCreated && i < 10);

        }
    }

    @Override
    public String getNodeName() {
        return peerName;
    }

    private boolean connectRetry() {
        int tries = MAX_RETRIES;
        boolean ok = false;
        while (!ok && tries > 0) {
            ErlLogger.debug("# ping..." + getNodeName() + " "
                    + Thread.currentThread().getName());
            ok = localNode.ping(getNodeName(), RETRY_DELAY
                    + (MAX_RETRIES - tries) * RETRY_DELAY % 3);
            tries--;
        }
        return ok;
    }

    @Override
    public void remoteStatus(final String node, final boolean up,
            final Object info) {
        if (node.equals(peerName)) {
            if (up) {
                ErlLogger.debug("Node %s is up", peerName);
                connectRetry();
            } else {
                ErlLogger.debug("Node %s is down: %s", peerName, info);
                state = State.DOWN;
            }
        }
    }

    @Override
    public void makeAsyncResultCall(final IRpcResultCallback cb,
            final String m, final String f, final String signature,
            final Object[] args) throws SignatureException {
        final OtpErlangAtom gleader = new OtpErlangAtom("user");
        rpcHelper.rpcCastWithProgress(cb, localNode, peerName, false, gleader,
                m, f, signature, args);
    }

    @Override
    public IRpcFuture makeAsyncCall(final OtpErlangObject gleader,
            final String module, final String fun, final String signature,
            final Object... args0) throws RpcException, SignatureException {
        tryConnect();
        return rpcHelper.sendRpcCall(localNode, peerName, false, gleader,
                module, fun, signature, args0);
    }

    @Override
    public IRpcFuture makeAsyncCall(final String module, final String fun,
            final String signature, final Object... args0) throws RpcException,
            SignatureException {
        return makeAsyncCall(new OtpErlangAtom("user"), module, fun, signature,
                args0);
    }

    @Override
    public void makeAsyncCbCall(final IRpcCallback cb, final int timeout,
            final String module, final String fun, final String signature,
            final Object... args) throws RpcException, SignatureException {
        makeAsyncCbCall(cb, timeout, new OtpErlangAtom("user"), module, fun,
                signature, args);
    }

    @Override
    public void makeAsyncCbCall(final IRpcCallback cb, final int timeout,
            final OtpErlangObject gleader, final String module,
            final String fun, final String signature, final Object... args)
            throws RpcException, SignatureException {
        tryConnect();
        rpcHelper.makeAsyncCbCall(localNode, peerName, cb, timeout, gleader,
                module, fun, signature, args);
    }

    @Override
    public OtpErlangObject makeCall(final int timeout,
            final OtpErlangObject gleader, final String module,
            final String fun, final String signature, final Object... args0)
            throws RpcException, SignatureException {
        tryConnect();
        final OtpErlangObject result = rpcHelper.rpcCall(localNode, peerName,
                false, gleader, module, fun, timeout, signature, args0);
        return result;
    }

    @Override
    public OtpErlangObject makeCall(final int timeout, final String module,
            final String fun, final String signature, final Object... args0)
            throws RpcException, SignatureException {
        return makeCall(timeout, new OtpErlangAtom("user"), module, fun,
                signature, args0);
    }

    @Override
    public void makeCast(final OtpErlangObject gleader, final String module,
            final String fun, final String signature, final Object... args0)
            throws SignatureException, RpcException {
        tryConnect();
        rpcHelper.rpcCast(localNode, peerName, false, gleader, module, fun,
                signature, args0);
    }

    @Override
    public void makeCast(final String module, final String fun,
            final String signature, final Object... args0)
            throws SignatureException, RpcException {
        makeCast(new OtpErlangAtom("user"), module, fun, signature, args0);
    }

    private void tryConnect() throws RpcException {
        synchronized (connectLock) {
            switch (state) {
            case DISCONNECTED:
                if (connectRetry()) {
                    state = State.CONNECTED;
                } else {
                    state = State.DISCONNECTED;
                }
                break;
            case CONNECTED:
                break;
            case DOWN:
                final String msg = "Backend '%s' is down";
                // XXX restart it??
                throw new RpcException(String.format(msg, peerName));
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return state == State.CONNECTED;
    }

    public static String createJavaNodeName() {
        final String fUniqueId = ErlRuntime.getTimeSuffix();
        return "jerlide_" + fUniqueId;
    }

    static String getTimeSuffix() {
        String fUniqueId;
        fUniqueId = Long.toHexString(System.currentTimeMillis() & 0xFFFFFFF);
        return fUniqueId;
    }

    public static OtpNode createOtpNode(final String cookie) throws IOException {
        OtpNode node;
        if (cookie == null) {
            node = new OtpNode(createJavaNodeName());
        } else {
            node = new OtpNode(createJavaNodeName(), cookie);
        }
        final String nodeCookie = node.cookie();
        final int len = nodeCookie.length();
        final String trimmed = len > 7 ? nodeCookie.substring(0, 7)
                : nodeCookie;
        ErlLogger.debug("using cookie '%s...'%d (info: '%s')", trimmed, len,
                cookie);
        return node;
    }

    @Override
    public void send(final OtpErlangPid pid, final Object msg)
            throws RpcException, SignatureException {
        tryConnect();
        rpcHelper.send(localNode, pid, msg);
    }

    @Override
    public void send(final String fullNodeName, final String name,
            final Object msg) throws SignatureException, RpcException {
        tryConnect();
        rpcHelper.send(localNode, fullNodeName, name, msg);
    }

    @Override
    public OtpMbox createMbox(final String name) {
        return localNode.createMbox(name);
    }

    @Override
    public OtpMbox createMbox() {
        return localNode.createMbox();
    }

    @Override
    public void stop() {
        // close peer too?
        localNode.close();
    }
}
