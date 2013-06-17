package org.erlide.backend;

import org.erlide.backend.runtimeinfo.RuntimeInfo;

public interface IBackendFactory {

    IBackend createIdeBackend();

    IBackend createBuildBackend(final RuntimeInfo info);

    IBackend createBackend(final BackendData data);
    
    /**
     * @since 1.0
     */
    IBackend registerEWPBackend(final RuntimeInfo info);

}
