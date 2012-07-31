package container.osgi.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.launch.FrameworkFactory;

/**
 * 
 * @author Joao Claudio AMERICO
 * <br><br>
 * FwkLauncher class
 * This class is responsible for getting the Framework Factory instance from the classpath.
 * This code is based on the code available at Apache Felix's homepage:
 * http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html
 * 
 *
 */
public abstract class FwkLauncher {
	public static FrameworkFactory getFrameworkFactory(URL url) throws Exception
    {
        
		if (url != null)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            try
            {
                for (String s = br.readLine(); s != null; s = br.readLine())
                {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.
                    if ((s.length() > 0) && (s.charAt(0) != '#'))
                    {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            }
            finally
            {
                if (br != null) br.close();
            }
        }
        throw new Exception("Could not find framework factory.");
    }
}
