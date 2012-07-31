
package container.osgi.benchmark;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import container.java.applications.fibo.api.IFibonacci;
import container.java.applications.fibo.lib.FibonacciDelegate;

/**
 * 
 * @author Joao Claudio AMERICO
 * <br><br>
 * OSGi Activator for the Delegate Fibonacci Version
 *
 */

public class Activator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		FibonacciDelegate fib = new FibonacciDelegate();
		fib.setDelegate(fib);
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("name", "fibonacci-delegate");
		context.registerService(IFibonacci.class.getName(), fib, properties);
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

}
