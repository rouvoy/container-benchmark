package container.osgi.benchmark.fibo.osgi;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;

import com.google.caliper.Param;

import container.java.applications.fibo.api.IFibonacci;
import container.java.benchmark.FibonacciBenchmark;
import container.osgi.benchmark.FwkLauncher;

/**
 * 
 * @author Joao Claudio AMERICO
 *
 *<br><br>
 * This class measures the response time of a service published in OSGi's Service Registry.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OSGiBenchmark extends FibonacciBenchmark{
	

	private static Framework fwk = null;
	
	{
		// First we get the Framework Factory. With this factory, we create a new framework.
		
		URL url = OSGiBenchmark.class.getClassLoader().
				getResource("META-INF/services/org.osgi.framework.launch.FrameworkFactory");
		try {
			
			/* 
			 * These lines demand the framework to export the packages container.java.applications.fibo.api
			 and container.java.applications.fibo.lib. OSGi requires that a service consumer and producer
			 reference the same class object as service interface. 
			 This way we are ensuring that both will reference the class that is exported by the framework
			 and that is in the JVM classpath.
			 *			  
			 */
			
			Map configMap = new HashMap();
			configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, 
					"container.java.applications.fibo.api, container.java.applications.fibo.lib");
			fwk = FwkLauncher.getFrameworkFactory(url).newFramework(configMap);
			fwk.init();
			
			/*
			 * Now we are going to get the user directory in order to install the bundles we need to.
			 * These files correspond to the implementation bundles. They are taken directly from the
			 * project target folder. 
			 * 
			 */
			
			String path = new File(System.getProperty("user.dir")).getParent();
			fwk.start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/FibonacciDelegate/target/FibonacciDelegate-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/FibonacciRecursive/target/FibonacciRecursive-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/FibonacciIterative/target/FibonacciIterative-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/FibonacciSimple/target/FibonacciSimple-1.0-SNAPSHOT.jar").start();
		} catch (BundleException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Param({ "0", "1", "2", "4", "8", "16" })
	protected int n;

	@Param
	private Implementation implementation;

	@Override
	protected void setUp() throws Exception {
		fib = implementation.create();
	}

	@Override
	protected void tearDown() throws Exception {
		fwk.getBundleContext().getBundle(0).stop();
        fwk.waitForStop(0);
        fwk = null;
	}
	
	public void timeFibonacciCompute(int reps) {
		for (int i = 0; i < reps; i++) {
			fib.compute(n);
		}
	}
	
	public enum Implementation {
		
		RecursiveVersion_Bundle("fibonacci-recursive"), 
		IterativeVersion_Bundle("fibonacci-iterative"), 
		NaiveVersion_Bundle("fibonacci-simple"), 
		DelegateVersion_Bundle("fibonacci-delegate");

		private final String name;
		
		Implementation(String name) {
			this.name = name;
		}

		public IFibonacci create() throws Exception {
			BundleContext bc = fwk.getBundleContext();
			
			/* From the bundle context, we get the list of bundles and compare with the implementation
			 * we are looking for
			 */
			
			ServiceReference[] sr = bc.getAllServiceReferences(IFibonacci.class.getName(), null);
			if (sr == null)
				System.out.println("sr is null");
			for (int i = 0, n = sr.length; i < n; i++) {
				if (sr[i].getProperty("name").equals(this.name)) {
					return (IFibonacci) bc.getService(sr[i]);
				}
			}
			
			// There is no such service, sorry
			throw new Exception("Implementation " + this.name + " was not found");
		}
	}
}
