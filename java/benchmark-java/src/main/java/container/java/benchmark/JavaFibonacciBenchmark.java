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
package container.java.benchmark;

import com.google.caliper.Param;
import com.google.caliper.Runner;

import container.java.applications.fibo.api.IFibonacci;
import container.java.applications.fibo.lib.Fibonacci;
import container.java.applications.fibo.lib.FibonacciDelegate;
import container.java.applications.fibo.lib.FibonacciInterceptor;
import container.java.applications.fibo.lib.FibonacciIterative;
import container.java.applications.fibo.lib.FibonacciProxy;
import container.java.applications.fibo.lib.FibonacciRecursive;

/**
 * Measures sorting on different distributions of integers.
 */
public class JavaFibonacciBenchmark extends FibonacciBenchmark {
    @Param({ "0", "1", "2", "4", "8", "16" })
    protected int n;

    @Param
    private Implementation implementation;

    @Override
    protected void setUp() throws Exception {
        fib = implementation.create();
    }

    public void timeFibonacciCompute(int reps) {
        for (int i = 0; i < reps; i++) {
            fib.compute(n);
        }
    }

    public enum Implementation {
        NaiveVersion {
            @Override
            IFibonacci create() {
                return new Fibonacci();
            }
        },
        NaiveVersion_StaticProxy {
            @Override
            IFibonacci create() {
                return new FibonacciInterceptor(new Fibonacci());
            }
        },
        NaiveVersion_ReflectiveProxy {
            @Override
            IFibonacci create() {
                return FibonacciProxy.newProxy(new Fibonacci());
            }
        },
        DelegateVersion {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                fib.setDelegate(fib);
                return fib;
            }
        },
        DelegateVersion_ReflectiveProxy {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                IFibonacci proxy = FibonacciProxy.newProxy(fib);
                fib.setDelegate(proxy);
                return proxy;
            }
        },
        DelegateVersion_StaticProxy {
            @Override
            IFibonacci create() {
                final FibonacciDelegate fib = new FibonacciDelegate();
                IFibonacci intercept = new FibonacciInterceptor(fib);
                fib.setDelegate(intercept);
                return intercept;
            }
        },
        IterativeVersion {
            @Override
            IFibonacci create() {
                return new FibonacciIterative();
            }
        },
        RecursiveVersion {
            @Override
            IFibonacci create() {
                return new FibonacciRecursive();
            }
        };

        abstract IFibonacci create();
    }

    public static void main(String[] args) throws Exception {
        Runner.main(JavaFibonacciBenchmark.class, args);
    }
}
