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
package org.ow2.frascati.benchmark;

import static org.ow2.frascati.benchmark.lib.FibonacciProxy.newProxy;

import java.lang.reflect.Method;

import org.ow2.frascati.benchmark.api.IFibonacci;
import org.ow2.frascati.benchmark.lib.Fibonacci;
import org.ow2.frascati.benchmark.lib.FibonacciDelegate;
import org.ow2.frascati.benchmark.lib.FibonacciInterceptor;
import org.ow2.frascati.benchmark.lib.FibonacciIterative;
import org.ow2.frascati.benchmark.lib.FibonacciRecursive;

import com.google.caliper.Param;
import com.google.caliper.Runner;

/**
 * Measures sorting on different distributions of integers.
 */
public class JavaInterfaceInvocationBenchmark extends StaticInterfaceInvocation {
    @Param({ "0", "1", "2", "4", "8", "16" })
    protected int n;

    @Param
    private Implementation implementation;

//    private Method m;

    @Override
    protected void setUp() throws Exception {
        fib = implementation.create();
//        m = IFibonacci.class.getMethod("compute", int.class);
    }

    public void timeStaticInvoke(int reps) {
        for (int i = 0; i < reps; i++) {
            fib.compute(n);
        }
    }

//    public void timeReflectInvoke(int reps) {
//        try {
//            for (int i = 0; i < reps; i++) {
//                m.invoke(fib, n);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public enum Implementation {
        SIMPLE {
            @Override
            IFibonacci create() {
                return new Fibonacci();
            }
        },
        SIMPLE_PROXY_STATIC {
            @Override
            IFibonacci create() {
                return new FibonacciInterceptor(new Fibonacci());
            }
        },
        SIMPLE_PROXY_REFLECT {
            @Override
            IFibonacci create() {
                return newProxy(new Fibonacci());
            }
        },
        DELEGATE {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                fib.setDelegate(fib);
                return fib;
            }
        },
        DELEGATE_PROXY_REFLECT {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                IFibonacci proxy = newProxy(fib);
                fib.setDelegate(proxy);
                return proxy;
            }
        },
        DELEGATE_PROXY_STATIC {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                IFibonacci intercept = new FibonacciInterceptor(fib);
                fib.setDelegate(intercept);
                return intercept;
            }
        },
        OPTIMIZED_ITERATIVE {
            @Override
            IFibonacci create() {
                return new FibonacciIterative();
            }
        },
        OPTIMIZED_RECURSIVE {
            @Override
            IFibonacci create() {
                return new FibonacciRecursive();
            }
        };

        abstract IFibonacci create();
    }

    public static void main(String[] args) throws Exception {
        Runner.main(JavaInterfaceInvocationBenchmark.class, args);
    }
}
