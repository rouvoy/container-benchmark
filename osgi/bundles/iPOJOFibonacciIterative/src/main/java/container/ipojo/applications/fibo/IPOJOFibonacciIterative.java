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

import container.java.applications.fibo.api.IFibonacci;

/**
 * Iterative implementation of the Fibonacci algorithm.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
public class IPOJOFibonacciIterative implements IFibonacci {
    /* (non-Javadoc)
     * @see org.ow2.frascati.benchmark.api.IFibonacci#compute(int)
     */
	
    public long compute(int n) {
        if (n<=0) return 0;
        final long[] values = new long[n+1];
        values[0] = 0;
        values[1] = 1;
        for(int i = 2; i < n+1; i++)
            values[i] = values[i-1] + values[i-2];
        return values[n];
    }
}
