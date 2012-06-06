package org.ow2.frascati.benchmark.lib;

import org.ow2.frascati.benchmark.api.IDelegate;

public class Delegate<T> implements IDelegate<T> {
    protected T delegate;

    /* (non-Javadoc)
     * @see org.ow2.frascati.benchmark.api.IDelegate#setDelegate(java.lang.Object)
     */
    public void setDelegate(T del) {
        this.delegate = del;
    }
    
    /* (non-Javadoc)
     * @see org.ow2.frascati.benchmark.api.IDelegate#getDelegate()
     */
    public T getDelegate() {
        return this.delegate;
    }
}
