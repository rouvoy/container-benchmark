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
package container.sca.applications.fibo.lib;

import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

import container.sca.applications.fibo.api.IFibonacci;

/**
 * SCA-based implementation of the Fibonacci algorithm.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
@Service(value = IFibonacci.class) // REQUIRED BY TUSCANY
public class FibonacciDelegate extends container.java.applications.fibo.lib.FibonacciDelegate implements IFibonacci {
	@Reference
	public IFibonacci delegate;

	@Override
	protected long delegate(int n) {
		 return this.delegate.compute(n);
	}
}
