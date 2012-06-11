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
package container.fractal.benchmark;

import static java.lang.System.getProperties;
import static org.objectweb.fractal.api.Fractal.getBootstrapComponent;
import static org.objectweb.fractal.util.Fractal.getBindingController;
import static org.objectweb.fractal.util.Fractal.getLifeCycleController;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.julia.Julia;

import com.google.caliper.Param;
import com.google.caliper.Runner;

import container.fractal.applications.fibo.api.FibonacciType;
import container.fractal.applications.fibo.lib.FcFibonacciDelegate;
import container.java.applications.fibo.api.IFibonacci;
import container.java.applications.fibo.lib.Fibonacci;
import container.java.applications.fibo.lib.FibonacciIterative;
import container.java.benchmark.FibonacciBenchmark;

/**
 * Measures sorting on different distributions of integers.
 */
public class FractalFibonacciBenchmark extends FibonacciBenchmark {
	@Param({ "0", "1", "2", "4", "8", "16" })
	protected int n;

	@Param
	private Implementation implementation;

	@Param({ "default", "merge-controllers",
			"merge-controllers-and-interceptors",
			"merge-controllers-and-content",
			"merge-controllers-interceptors-and-content",
			"no-lifecycle-interceptors" })
	private String backend;

	@Override
	protected void setUp() throws Exception {
		fib = implementation.create(backend);
	}

	public void timeFibonacciCompute(int reps) {
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

		public IFibonacci create(String conf) throws Exception {
			getProperties().put("fractal.provider", Julia.class.getName());
			getProperties().put("julia.config", conf+".cfg");
			Component boot = getBootstrapComponent();
			Component comp = this.delegate ? FibonacciType.FIBONACCI_DELEGATE
					.createPrimitive(boot, this.cls) : FibonacciType.FIBONACCI
					.createPrimitive(boot, this.cls);
			Component root = this.composite ? FibonacciType.FIBONACCI
					.createComposite(boot, comp) : comp;
			if (this.delegate) {
				getBindingController(comp).bindFc("delegate",
						FibonacciType.FIBONACCI_DELEGATE.getInterface(comp));
			}
			getLifeCycleController(root).startFc();
			return FibonacciType.FIBONACCI.getInterface(root);
		}
	}

	public static void main(String[] args) throws Exception {
		Runner.main(FractalFibonacciBenchmark.class, args);
	}
}
