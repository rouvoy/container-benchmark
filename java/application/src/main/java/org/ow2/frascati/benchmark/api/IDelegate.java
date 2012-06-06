/**
 * OW2 FraSCAti SCA Benchmark
 * Copyright (C) 2011 INRIA, University Lille 1
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: frascati@ow2.org
 *
 * Author: Romain Rouvoy
 *
 * Contributor(s): 
 *
 */
package org.ow2.frascati.benchmark.api;

/**
 * Interface to configure a delegate.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
public interface IDelegate<T> {
    /**
     * Getter method
     * 
     * @return the current delegate
     */
    T getDelegate();

    /**
     * Setter method
     * 
     * @param d
     *            the new delegate
     */
    void setDelegate(T d);
}
