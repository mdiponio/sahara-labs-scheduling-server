/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Michael Diponio (mdiponio)
 * @date 4th January 2010
 */

package io.rln.ss.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.JarFile;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.framework.wiring.FrameworkWiring;

/**
 * Starts the Scheduling Server OSGi framework instance and shutdown it down
 * on program termination request.
 */
public class SchedulingServer
{
    /** OSGi framework instance. */
    private static Framework framework;

    /** Ordered list of bundles symbolic names which must be started in the
     *  specified order. */
    private final static List<String> bundles = new ArrayList<String>();
    
    /** Whether the framework has successfully started. */
    private static boolean hasStarted = false;

    /**
     * Runs the scheduling server until the static <code>stop</code> method
     * is invoked.
     */
    public void runSchedulingServer()
    {
        System.out.println("Classpath:" + System.getProperty("java.class.path"));
        try
        {
            /* ----------------------------------------------------------------
             * ---- 1) Load the list of mandatory bundles. The bundles are ----
             * ----    loaded in the order they need to be started in.     ----  
             * -------------------------------------------------------------- */
            final InputStream mf = SchedulingServer.class.getResourceAsStream("/META-INF/BundleManifest.xml");
            if (mf == null)
            {
                System.out.println("Unable to start Scheduling Server because the mandatory bundle manifest has not " +
                		"been found. This should packaged within the Scheduling Server main library in " +
                		"'/META-INF/BundleManifest.xml");
                return;
            }
            
            final Properties manifest = new Properties();
            manifest.loadFromXML(mf);
            
            int n = 1;
            String sym = null;
            while ((sym = manifest.getProperty("B" + n)) != null)
            {
                SchedulingServer.bundles.add(sym);
                System.out.println("Mandatory bundle " + n + ": " + sym);
                ++n;
            }
            
            /* ----------------------------------------------------------------
             * ---- 2) Load the OSGI framework and start it up.            ----
             * -------------------------------------------------------------- */
            final FrameworkFactory frmFactory = this.getFrameworkFactory();
            SchedulingServer.framework = frmFactory.newFramework(new FrameworkProperties().getProperties());
            SchedulingServer.framework.init();
            SchedulingServer.framework.start();

            /* ----------------------------------------------------------------
             * ---- 3) Add a shutdown hook to shutdown the OSGI framework  ----
             * ----    and its installed bundles.                          ----
             * -------------------------------------------------------------- */
            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    if (hasStarted) SchedulingServer.stopFramework();
                }
            });

            /* Install or update bundles. */
            System.out.println("Framework state " + SchedulingServer.framework.getState() + '.');
            final BundleContext context = SchedulingServer.framework.getBundleContext();
            if (context == null)
            {
                System.out.println("Unable to obtain Framework bundle.");
            }
            System.out.println("Framework bundle " + context.getBundle().getSymbolicName() + '.');
            System.out.println();

            final Map<String, File> jars = this.getBundleJars(FrameworkProperties.BUNDLE_DIR);

            /* Install mandatory scheduling server bundles in order. */
            /* First pass installs the bundle. */
            for (int i = 0; i < SchedulingServer.bundles.size(); i++)
            {
                final String symName = SchedulingServer.bundles.get(i);
                final File bundleJar = jars.remove(symName);
                if (bundleJar == null)
                {
                    throw new IllegalStateException("Bundle " + symName + " not found");
                }
                System.out.println("Installing bundle " + symName + " from " + bundleJar.toURI().toString() + ".");
                this.installOrUpdateBundle(context, symName, bundleJar, false);
            }
            /* Start the bundles. */
            for (final Bundle b : context.getBundles())
            {
                if (!(b.getState() == Bundle.ACTIVE || b.getState() == Bundle.STARTING))
                {
                    System.out.println("Starting bundle " + b.getSymbolicName() + " (id " + b.getBundleId() + ").");
                    b.start();
                }
            }


            /* Install the rest of the detected bundles. */
            for (final Entry<String, File> e : jars.entrySet())
            {
                System.out
                        .println("Installing bundle " + e.getKey() + " from " + e.getValue().toURI().toString() + ".");
                this.installOrUpdateBundle(context, e.getKey(), e.getValue(), false);
            }

            /* Start any bundles that haven't already been started. */
            for (final Bundle b : context.getBundles())
            {
                if (!(b.getState() == Bundle.ACTIVE || b.getState() == Bundle.STARTING))
                {
                    System.out.println("Starting bundle " + b.getSymbolicName() + " (id " + b.getBundleId() + ").");
                    b.start();
                }
            }

            SchedulingServer.hasStarted = true;
            SchedulingServer.framework.waitForStop(0);
        }
        catch (final Exception ex)
        {
            System.out.println("Failed... Exception: " + ex.getClass().getCanonicalName() + ", Message: "
                    + ex.getMessage() + ".");
            ex.printStackTrace();
            
            /* Shut down everything. */
            System.exit(-1);
        }
    }

    /**
     * Starts the program running.
     */
    public static void start()
    {
        final SchedulingServer ss = new SchedulingServer();
        ss.runSchedulingServer();
    }

    /**
     * Stops the program running.
     */
    public static void stop()
    {   
       SchedulingServer.stopFramework();
       SchedulingServer.hasStarted = false;
    }

    /**
     * Installs the specified bundle if it is not already installed. If it is,
     * the bundle is updated with the provided bundle jar.
     * 
     * @param context a bundle context
     * @param name  symbolic name of a bundle
     * @param bundleJar  bundle file
     * @param doStart   whether to start the bundle immediately
     * @throws Exception  error installing or updating a bundle
     */
    private void installOrUpdateBundle(final BundleContext context, final String name, final File bundleJar,
            final boolean doStart) throws Exception
    {
        Bundle bundle = null;

        /* Search for the bundle. */
        for (final Bundle b : context.getBundles())
        {
            if (b.getSymbolicName().equals(name))
            {
                bundle = b;
                break;
            }
        }

        if (bundle == null)
        {
            bundle = context.installBundle(bundleJar.toURI().toString());
        }
        else
        {
            /*
             * Bundle previously installed so going to update it. Updating a bundle
             * is a two set process.
             * 1) The bundle is 'updated', replacing the old bundle with the new
             * bundle.
             * 2) The bundle is 'refreshed', ensuring all dependent bundles use
             * the newly updated bundle.
             */
            bundle.update(new FileInputStream(bundleJar));
            
            FrameworkWiring wire = SchedulingServer.framework.adapt(FrameworkWiring.class);
            wire.refreshBundles(Arrays.asList(new Bundle[]{ bundle }));
        }

        if (doStart && !(bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.STARTING))
        {
            System.out.println("Starting bundle " + bundle.getSymbolicName() + " (id " + bundle.getBundleId() + ").");
            bundle.start();
            Thread.sleep(5000);
        }
    }

    /**
     * Gets the JAR files in a specified directory. The JAR files which do not
     * have the <code>Bundle-SymbolicName</code> field in their manifest are
     * ignored as these are not OSGi bundles.
     * 
     * @param bundlePath
     *            bundle directory
     * @return map of jar files in directory keyed by their symbolic name
     * @throws IOException
     *             error opening jar file
     */
    private Map<String, File> getBundleJars(final String bundlePath) throws IOException
    {
        final Map<String, File> jars = new HashMap<String, File>();

        final File path = new File(bundlePath);

        if (!path.exists())
        {
            return jars;
        }

        for (final File file : path.listFiles())
        {
            if (file.getName().endsWith(".jar"))
            {
                final JarFile jar = new JarFile(file);
                String name = jar.getManifest().getMainAttributes().getValue("Bundle-SymbolicName");
                jar.close();

                if (name != null) /* Ignore Jars that aren't OSGi bundles as they cannot be installed. */
                {
                    /* Remove any directives. */
                    if (name.indexOf(';') > 0)
                    {
                        name = name.split(";", 2)[0];
                    }
                    jars.put(name, file);
                }
            }
        }

        return jars;
    }

    /**
     * Instantiates a {@link org.osgi.framework.launch.FrameworkFactory} using the resource
     * <code>META-INF/services/org.osgi.framework.launch.FrameworkFactory</code> to find the qualified name of the
     * FrameworkFactory implementation class.
     * 
     * @return {@link FrameworkFactory} instance
     * @throws Exception
     *             failure to find or create instance
     */
    private FrameworkFactory getFrameworkFactory() throws Exception
    {
        /*
         * Create the Framework factory (this uses instructions from
         * http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html.
         */

        /*
         * The following resource file should be packaged inside the OSGi
         * framework Jar file and should contain the name of the
         * FrameworkFactory implementation class on the first non-blank,
         * non-comment line.
         */
        final InputStream input = SchedulingServer.class.getClassLoader().getResourceAsStream(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (input != null)
        {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            Object obj = null;
            try
            {
                for (String tmp = reader.readLine(); tmp != null; tmp = reader.readLine())
                {
                    tmp = tmp.trim();
                    try
                    {
                        if (tmp.length() > 0 && tmp.charAt(0) != '#'
                                && (obj = Class.forName(tmp).newInstance()) instanceof FrameworkFactory)
                        {
                            return (FrameworkFactory) obj;
                        }
                    }
                    catch (final Exception e)
                    {
                        /*
                         * Just swallowing the class loading exception, to continue
                         * to search the resource for the FrameworkFactory
                         * implementation class.
                         */
                    }
                }
            }
            finally
            {
                reader.close();
            }
        }

        throw new Exception("Unable to find FrameworkFactory class.");
    }

    /**
     * Shutdowns the OSGI framework, so shutdowns the Scheduling Server.
     */
    private static void stopFramework()
    {
        if (SchedulingServer.framework != null)
        {
            Map<String, Bundle> runningBundles = new HashMap<String, Bundle>();
            for (Bundle b : SchedulingServer.framework.getBundleContext().getBundles())
            {
                runningBundles.put(b.getSymbolicName(), b);
            }
            
            /* First shutdown the Server bundle. */
            try
            {
                System.err.println("##### Stopping bundle SchedulingServer-Server ##################################");
                runningBundles.get("SchedulingServer-Server").stop(); // Small sleep to allow the bundle threads to interrupt
                Thread.sleep(4000);  // and stop.
            }
            catch (BundleException ex)
            {
                System.err.println("Bundle SchedulingServer-Server throw exception " + 
                        ex.getCause().getClass().getName() + ", with message " + ex.getMessage() + '.');
            }
            catch (InterruptedException ex)  { /* Swallow, already shutting down. */}
            
         
            /* Stop all the bundles (except the framework) that aren't in the Scheduling 
             * Server bundle list in no particular order. */
            for (Entry<String, Bundle> e : runningBundles.entrySet())
            {
                if (!SchedulingServer.bundles.contains(e.getKey()) && e.getValue().getBundleId() != 0)
                {
                    try
                    {
                        StringBuilder mess = new StringBuilder(80);
                        mess.append("##### Stopping bundle ");
                        mess.append(e.getKey());
                        mess.append(' ');
                        for (int m = 0; m < 63 - e.getKey().length(); m++) mess.append('#');
                        System.err.println(mess.toString());
                        e.getValue().stop(); // Small sleep to allow the bundle threads to interrupt
                        Thread.sleep(1000);  // and stop.
                    }
                    catch (BundleException ex)
                    {
                        System.err.println("Bundle" + e.getKey() + " throw exception " + 
                                ex.getCause().getClass().getName() + ", with message " + ex.getMessage() + '.');
                    }
                    catch (InterruptedException ex)  { /* Swallow, already shutting down. */}
                }
            }
            
            /* Stops the Scheduling Server bundles in the opposite order they are started. */
            int sz = SchedulingServer.bundles.size();
            for (int i = 1; i < sz; i++)
            {
                String key = SchedulingServer.bundles.get(sz - i);
                if (runningBundles.containsKey(key))
                {
                    try
                    {
                        StringBuilder mess = new StringBuilder(80);
                        mess.append("##### Stopping bundle ");
                        mess.append(key);
                        mess.append(' ');
                        for (int m = 0; m < 63 - key.length(); m++) mess.append('#');
         
                        System.err.println(mess.toString());
                        runningBundles.get(key).stop(); // Small sleep to allow the bundle threads to interrupt
                        Thread.sleep(1000);             // and stop.
                    }
                    catch (BundleException ex)
                    {
                        System.err.println("Bundle" + key + " throw exception " + 
                                ex.getCause().getClass().getName() + ", with message " + ex.getMessage() + '.');
                    }
                    catch (InterruptedException ex)  { /* Swallow, already shutting down. */}
                }
            }
            
            /* Stop the framework. */
            try
            {
                System.err.println("Shutting down Scheduling Server...");
                SchedulingServer.framework.stop();
            }
            catch (final Exception e)
            {
                System.err.println("Unable to shutdown framework cleanly.");
            }
        }
    }
}
