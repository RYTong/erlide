package org.erlide.core.internal.model.erlang;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.erlide.core.ErlangPlugin;
import org.erlide.core.internal.model.root.Openable;
import org.erlide.core.model.root.ErlModelException;
import org.erlide.core.model.root.IErlExternal;
import org.erlide.core.model.root.IParent;
import org.erlide.core.model.util.CoreUtil;
import org.erlide.core.services.search.ErlideOpen;
import org.erlide.jinterface.rpc.IRpcCallSite;


/**
 * @author lu.jingbo
 *
 */
public class ErlYawsExternalReferenceEntryList extends Openable implements
        IErlExternal {

    public ErlYawsExternalReferenceEntryList(IParent parent, String name) {
        super(parent, name);
    }

    /* (non-Javadoc)
     * @see org.erlide.core.model.root.IErlElement#getKind()
     */
    @Override
    public Kind getKind() {
        return Kind.EXTERNAL;
    }

    /* (non-Javadoc)
     * @see org.erlide.core.model.root.IErlExternal#isOTP()
     */
    @Override
    public boolean isOTP() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.erlide.core.model.root.IErlExternal#hasIncludes()
     */
    @Override
    public boolean hasIncludes() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.erlide.core.internal.model.root.Openable#buildStructure(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected boolean buildStructure(IProgressMonitor pm)
            throws ErlModelException {
        
        final IRpcCallSite backend = CoreUtil.getBuildOrIdeBackend(getProject()
                .getWorkspaceProject());
        String libDir = ErlangPlugin.yawsPath;
        final List<String> srcInclude = ErlideOpen.getLibSrcInclude(
                backend, libDir);
        boolean hasHeaders = false;
        for (final String path : srcInclude) {
            if (includePath(path)) {
                hasHeaders = true;
                break;
            }
        }
        final IErlExternal external = new ErlExternalReferenceEntry(this,
                "yaws", libDir, true, hasHeaders);
        addChild(external);
        for (final String i : srcInclude) {
            external.addChild(new ErlExternalReferenceEntry(external,
                    "include", i, false, includePath(i)));
        }
        
        return true;
    }
    
    private final boolean includePath(final String path) {
        final IPath p = new Path(path);
        return p.lastSegment().equals("include");
    }
    
    @Override
    public boolean isOpen() {
        return super.isOpen();
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public String getLabelString() {
        return getName();
    }

    public String getExternalName() {
        return getName();
    }

    public boolean hasModuleWithPath(final String path) {
        return false;
    }

    @Override
    public IResource getResource() {
        return null;
    }

}
