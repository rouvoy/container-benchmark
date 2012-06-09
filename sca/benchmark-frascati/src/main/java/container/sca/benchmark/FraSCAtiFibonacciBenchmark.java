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
package container.sca.benchmark;

import static org.objectweb.fractal.util.Fractal.getContentController;
import static org.ow2.frascati.FraSCAti.newFraSCAti;

import org.objectweb.fractal.api.Component;
import org.ow2.frascati.FraSCAti;
import org.ow2.frascati.util.FrascatiException;

import com.google.caliper.Param;
import com.google.caliper.Runner;

import container.java.applications.fibo.api.IFibonacci;
import container.java.benchmark.FibonacciBenchmark;

/**
 * Measures sorting on different distributions of integers.
 */
public class FraSCAtiFibonacciBenchmark extends FibonacciBenchmark {
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
		/*
		 * FRASCATI_SIMPLE_COMPOSITE("fibonacci-simple", false),
		 * FRASCATI_DELEGATE_COMPOSITE( "fibonacci-delegate", false),
		 * FRASCATI_SIMPLE_REQUEST( "fibonacci-simple-request", false),
		 * FRASCATI_DELEGATE_REQUEST( "fibonacci-delegate-request", false),
		 */
		RecursiveVersion_CompositeScope("fibonacci-recursive", true), IterativeVersion_CompositeScope(
				"fibonacci-iterative", true), RecursiveVersion_RequestScope(
				"fibonacci-recursive-request", true), IterativeVersion_RequestScope(
				"fibonacci-iterative-request", true), NaiveVersion_CompositeScope(
				"fibonacci-simple", true), DelegateVersion_CompositeScope(
				"fibonacci-delegate", true), NaiveVersion_RequestScope(
				"fibonacci-simple-request", true), DelegateVersion_RequestScope(
				"fibonacci-delegate-request", true);

		private final String descriptor;

		private Component scaComposite;

		private FraSCAti frascati;

		private final boolean primitive;

		Implementation(String descriptor, boolean primitive) {
			this.descriptor = descriptor;
			this.primitive = primitive;
			try {
				this.frascati = newFraSCAti();
				this.scaComposite = this.frascati.getComposite(this.descriptor);
			} catch (FrascatiException e) {
				e.printStackTrace();
			}
		}

		// public void finalize() {
		// if (this.scaComposite != null)
		// this.frascati.close(this.scaComposite);
		// }

		public IFibonacci create() throws Exception {
			if (primitive)
				return (IFibonacci) getContentController(this.scaComposite)
						.getFcSubComponents()[0].getFcInterface("fibonacci");
			else
				return this.frascati.getService(this.scaComposite, "fibonacci",
						IFibonacci.class);
		}
	}

	public static void main(String[] args) throws Exception {
		Runner.main(FraSCAtiFibonacciBenchmark.class, args);
	}
}
