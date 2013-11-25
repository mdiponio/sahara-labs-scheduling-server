/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @date 3rd March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.transport.http.AxisServlet;
import org.apache.velocity.app.Velocity;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.permissions.pages.KeysPage;
import au.edu.uts.eng.remotelabs.schedserver.permissions.pages.UserClassesPage;
import au.edu.uts.eng.remotelabs.schedserver.permissions.pages.UsersPage;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the Permissions bundle. The Permissions bundle provides a
 * SOAP service to allow scheduling server permissions to created, read, edited, 
 * deleted and read.
 */
public class PermissionActivator implements BundleActivator 
{
    /** Service registration for the Permission SOAP interface. */
    private ServiceRegistration<ServletContainerService> soapRegistration;
    
    /** Hosted page registrations. */
    private List<ServiceRegistration<HostedPage>> pageRegistrations;
    
    /** Messenger service tracker. */
    private static ServiceTracker<MessengerService, MessengerService> messengerTracker;
    
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> configTracker;
    
    /** Logger. */
    private Logger logger;

    @Override
	public void start(BundleContext context) throws Exception 
	{
	    this.logger = LoggerActivator.getLogger();
	    this.logger.info("Starting the " + context.getBundle().getSymbolicName() + " bundle...");
	    
	    PermissionActivator.configTracker = new ServiceTracker<Config, Config>(context, Config.class, null);
	    PermissionActivator.configTracker.open();
	    
	    PermissionActivator.messengerTracker =
	            new ServiceTracker<MessengerService, MessengerService>(context, MessengerService.class, null);
	    PermissionActivator.messengerTracker.open();
	    
	    /* Register the Permissions service. */
	    this.logger.debug("Registering the Permissions SOAP interface service.");
	    ServletContainerService soapService = new ServletContainerService();
	    soapService.addServlet(new ServletContainer(new AxisServlet(), true));
	    this.soapRegistration = context.registerService(ServletContainerService.class, soapService, null);
	    
	    /* Setup the permissions web interface. */
	    Properties velProps = new Properties();
	    velProps.setProperty("resource.loader", "class");
	    velProps.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
	    velProps.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	    Velocity.init(velProps);

	    this.pageRegistrations = new ArrayList<ServiceRegistration<HostedPage>>(3);
	    this.pageRegistrations.add(context.registerService(HostedPage.class, UsersPage.getHostedPage(), null));
	    this.pageRegistrations.add(context.registerService(HostedPage.class, UserClassesPage.getHostedPage(), null));
	    this.pageRegistrations.add(context.registerService(HostedPage.class, KeysPage.getHostedPage(), null));
	}

    @Override
	public void stop(BundleContext context) throws Exception 
	{
        this.logger.info("Stopping the " + context.getBundle().getSymbolicName() + " bundle...");
        
	    this.soapRegistration.unregister();
	    
	    for (ServiceRegistration<HostedPage> pageReg : this.pageRegistrations) pageReg.unregister();
	    this.pageRegistrations.clear();
	    
	    PermissionActivator.messengerTracker.close();
	    PermissionActivator.messengerTracker = null;
	    
	    PermissionActivator.configTracker.close();
	    PermissionActivator.configTracker = null;
	}
    
    /**
     * Returns a messenger service tracker service. If the service has not been found, 
     * null is returned.
     * 
     * @return messenger service or null
     */
    public static MessengerService getMessenger()
    {
        return PermissionActivator.messengerTracker == null ? null : PermissionActivator.messengerTracker.getService();
    }
    
    /**
     * Returns the configured value or null if the property does not exist.
     * 
     * @param prop property
     * @param def default property
     * @return value or null
     */
    public static String getConfig(String prop, String def)
    {
        if (PermissionActivator.configTracker == null) return def;
        
        Config conf = PermissionActivator.configTracker.getService();
        return conf == null ? def : conf.getProperty(prop);
    }
}
