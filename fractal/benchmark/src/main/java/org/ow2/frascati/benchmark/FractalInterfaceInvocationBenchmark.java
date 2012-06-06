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

import static java.lang.System.getProperties;
import static org.objectweb.fractal.api.Fractal.getBootstrapComponent;
import static org.objectweb.fractal.util.Fractal.getBindingController;
import static org.objectweb.fractal.util.Fractal.getLifeCycleController;
import static org.ow2.frascati.benchmark.FibonacciType.FIBONACCI;
import static org.ow2.frascati.benchmark.FibonacciType.FIBONACCI_DELEGATE;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.julia.Julia;
import org.ow2.frascati.benchmark.api.IFibonacci;
import org.ow2.frascati.benchmark.lib.FcFibonacciDelegate;
import org.ow2.frascati.benchmark.lib.Fibonacci;
import org.ow2.frascati.benchmark.lib.FibonacciIterative;

import com.google.caliper.Param;
import com.google.caliper.Runner;

/**
 * Measures sorting on different distributions of integers.
 */
public class FractalInterfaceInvocationBenchmark extends
        StaticInterfaceInvocation {
    @Param({ "0", "1", "2", "4", "8", "16" })
    protected int n;

    @Param
    private Implementation implementation;

    @Override
    protected void setUp() throws Exception {
        fib = implementation.create();
    }

    public void timeStaticInvoke(int reps) {
        for (int i = 0; i < reps; i++) {
            fib.compute(n);
        }
    }

    public enum Implementation {
        FC_PRIMITIVE_SIMPLE(Fibonacci.class, false, false), FC_PRIMITIVE_OPTIMIZED(
                FibonacciIterative.class, false, false), FC_COMPOSITE_SIMPLE(
                Fibonacci.class, false, true), FC_COMPOSITE_DELEGATE(
                FcFibonacciDelegate.class, true, true), FC_COMPOSITE_OPTIMIZED(
                FibonacciIterative.class, false, true);

        private final boolean delegate, composite;

        private final Class<? extends IFibonacci> cls;

        Implementation(Class<? extends IFibonacci> cls, boolean delegate,
                boolean composite) {
            this.delegate = delegate;
            this.composite = composite;
            this.cls = cls;
        }

        public IFibonacci create() throws Exception {
            getProperties().put("fractal.provider", Julia.class.getName());
            Component boot = getBootstrapComponent();
            Component comp = this.delegate ? FIBONACCI_DELEGATE
                    .createPrimitive(boot, this.cls) : FIBONACCI
                    .createPrimitive(boot, this.cls);
            Component root = this.composite ? FIBONACCI.createComposite(boot,
                    comp) : comp;
            if (this.delegate) {
                getBindingController(comp).bindFc("delegate",
                        FIBONACCI_DELEGATE.getInterface(comp));
            }
            getLifeCycleController(root).startFc();
            return FIBONACCI.getInterface(root);
        }
    }

    public static void main(String[] args) throws Exception {
        Runner.main(FractalInterfaceInvocationBenchmark.class, args);
    }
}
