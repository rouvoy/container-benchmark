/**
 * Copyright (C) 2011 University Lille 1, Inria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 *
 * Contact: romain.rouvoy@univ-lille1.fr
 */
package container.ipojo.applications.fibo;

import container.java.applications.fibo.api.IDelegate;

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
