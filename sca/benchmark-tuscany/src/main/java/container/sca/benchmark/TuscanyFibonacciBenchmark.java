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

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;

import com.google.caliper.Param;
import com.google.caliper.Runner;

import container.java.applications.fibo.api.IFibonacci;
import container.java.benchmark.FibonacciBenchmark;

/**
 * Measures sorting on different distributions of integers.
 */
public class TuscanyFibonacciBenchmark extends FibonacciBenchmark {
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
		NaiveVersion_CompositeScope("fibonacci-simple"), DelegateVersion_CompositeScope(
				"fibonacci-delegate"), RecursiveVersion_CompositeScope(
				"fibonacci-recursive"), IterativeVersion_CompositeScope(
				"fibonacci-iterative"), NaiveVersion_RequestScope(
				"fibonacci-simple-request"), DelegateVersion_RequestScope(
				"fibonacci-delegate-request"), RecursiveVersion_RequestScope(
				"fibonacci-recursive-request"), IterativeVersion_RequestScope(
				"fibonacci-iterative-request");

		private Node node;

		Implementation(String descriptor) {
			try {
				this.node = TuscanyRuntime
						.runComposite(
								descriptor + ".composite",
								"../../sca/applications-oasis/target/container-sca-applications-oasis-1.0-SNAPSHOT.jar",
								"../../java/applications/target/container-java-applications-1.0-SNAPSHOT.jar");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// public void finalize() {
		// if (this.node != null)
		// this.node.stop();
		// }

		public IFibonacci create() throws Exception {
			return node.getService(IFibonacci.class, "fibonacci-impl");
		}
	}

	public static void main(String[] args) throws Exception {
		Runner.main(TuscanyFibonacciBenchmark.class, args);
	}
}
