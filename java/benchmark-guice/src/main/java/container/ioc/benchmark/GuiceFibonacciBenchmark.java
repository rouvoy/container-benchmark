/**
 * Copyright (C) 2012 University Lille 1, Inria
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
 * Contact: nicolas.petitprez@inria.fr
 */
package container.ioc.benchmark;

import java.util.Arrays;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.inject.Guice;
import com.google.inject.Module;

import container.ioc.applications.fibo.DelegateModule;
import container.ioc.applications.fibo.IterativeModule;
import container.ioc.applications.fibo.RecursiveModule;
import container.ioc.applications.fibo.SimpleModule;
import container.java.applications.fibo.api.IFibonacci;
import container.java.benchmark.FibonacciBenchmark;

/**
 * Measures sorting on different distributions of integers.
 */
public class GuiceFibonacciBenchmark extends FibonacciBenchmark {
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
		Naive(new SimpleModule()),
		Recursive(new RecursiveModule()),
		Iterative(new IterativeModule()),
		Delegate(new DelegateModule());

		private final Iterable<Module> modules;

		Implementation(Module... modules) {
			this.modules = Arrays.asList(modules);
		}

		public IFibonacci create() throws Exception {
			return Guice.createInjector(modules).getInstance(IFibonacci.class);
		}
	}

	public static void main(String[] args) throws Exception {
		Runner.main(GuiceFibonacciBenchmark.class, args);
	}
}
