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
package container.java.applications.fibo.lib;

import static java.lang.Thread.currentThread;
import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import container.java.applications.fibo.api.IFibonacci;

/**
 * Proxy implementation of the Fibonacci algorithm.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
public class FibonacciProxy implements InvocationHandler {
    private IFibonacci delegate;

    public FibonacciProxy(IFibonacci fib) {
        this.delegate = fib;
    }

    @Override
    public Object invoke(Object target, Method m, Object[] params)
            throws Throwable {
        return m.invoke(delegate, params);
    }

    
    /**
     * @param delegate
     * @return
     */
    public static IFibonacci newProxy(IFibonacci delegate) {
        return (IFibonacci) newProxyInstance(currentThread()
                .getContextClassLoader(), new Class[] { IFibonacci.class },
                new FibonacciProxy(delegate));
    }
}
