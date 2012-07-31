package container.osgi.benchmark;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import container.java.applications.fibo.api.IFibonacci;
import container.java.applications.fibo.lib.FibonacciIterative;

/**
 * 
 * @author Joao Claudio AMERICO
 * <br><br>
 * OSGi Activator for the Iterative Fibonacci Version
 *
 */

public class Activator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		IFibonacci fib = new FibonacciIterative();
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("name", "fibonacci-iterative");
		context.registerService(IFibonacci.class.getName(), fib, properties);
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

}
