package container.osgi.benchmark.fibo.ipojo;

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
 * IPOJOBenchmark
 * @author Joao Claudio AMERICO
 *<br><br>
 * This class measures the response time of a service published and managed by iPOJO. 
 * The services are retrieved directly in OSGi's Service Registry.
 *  
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IPOJOBenchmark extends FibonacciBenchmark{
	
	private static Framework fwk = null;
	
	{
		// First we get the Framework Factory. With this factory, we create a new framework.
		
		URL url = IPOJOBenchmark.class.getClassLoader().
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
			 * The lines in the launch script which copy files from the Maven 2 repository are due to the
			 * fact that iPOJO instrument the original class files when the mvn install command is executed and
			 * then it installs the modified/"ipojified" classes there, and not in the target folder.
			 * Another solution would be to do that inside the Java code, but I don't know what's better.
			 * In addition, the iPOJO bundle must be installed. So I provided it in a folder in order to make
			 * things easier, but we could take it from the repository as well.
			 */
			
			String path = new File(System.getProperty("user.dir")).getParent();
			fwk.start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/otherbundles/org.apache.felix.ipojo-1.8.0.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/iPOJOFibonacciDelegate-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/iPOJOFibonacciRecursive-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/iPOJOFibonacciIterative-1.0-SNAPSHOT.jar").start();
			fwk.getBundleContext().installBundle("file:" + path + "/bundles/iPOJOFibonacciSimple-1.0-SNAPSHOT.jar").start();
			
			/*
			 * The sleep command is just to assure that the bundles will be properly installed and managed by iPOJO before the
			 * set up command is executed.
			 */
			
			Thread.sleep(3000);
			
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
		// Stopping the OSGi Framework.
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
		
		/*
		 * The Delegate version does not work properly with iPOJO. I did not have the time to investigate why.
		 */
		//DelegateVersion_Prop("fibonacci-delegate"),	
		RecursiveVersion_Prop("fibonacci-recursive"), 
		IterativeVersion_Prop("fibonacci-iterative"), NaiveVersion_Prop("fibonacci-simple"); 

		private final String name;
		
		Implementation(String name) {
			this.name = name;
		}
		
		
		public IFibonacci create() throws Exception {
			
			/*
			 * Since the interface is the same, I decided to publish the implementation name along
			 * with the service. Thus, we can distinguish among the different service implementations.
			 * In this method, we get all the service references whose interface is IFibonacci.
			 * Then we compare the name property with the name we are looking for.
			 * And we get the service instance afterwards. 
			 */
			
			BundleContext bc = fwk.getBundleContext();
			ServiceReference[] sr = bc.getAllServiceReferences(IFibonacci.class.getName(), null);
			if ((sr == null) || (sr.length == 0)) {
				throw new Exception("Implementation " + this.name + " was not found");
			} else {
				for (int i = 0, n = sr.length; i < n; i++) {
					if (sr[i].getProperty("name").equals(this.name))
						return (IFibonacci) bc.getService(sr[i]);
				}
			}
			return null;
		}
	}
}
