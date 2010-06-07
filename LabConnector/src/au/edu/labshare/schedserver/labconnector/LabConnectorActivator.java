/**
 * SAHARA Scheduling Server - LabConnector Activator
 * Activator class for the OSGi Bundle.
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
 * @author Herbert Yeung
 * @date 18th May 2010
 */
package au.edu.labshare.schedserver.labconnector;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
//import org.osgi.framework.Constants;
//import org.osgi.framework.ServiceEvent;
//import org.osgi.framework.ServiceReference;

//Needed for Sahara
import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

public class LabConnectorActivator implements BundleActivator 
{
    /** Servlet container service registration. */
    private ServiceRegistration serverReg;
    private Logger		logger;
    
    /** Configuration confService tracker. */
    private ServiceTracker confService;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
        @Override
	public void start(BundleContext context) throws Exception 
	{
		/* Used to debug whether Servlet service is started or not */
		this.logger = LoggerActivator.getLogger();
	        this.logger.info("Starting " + context.getBundle().getSymbolicName() + " bundle.");
		
	        /*Get the Configuration for the Properties*/
	        ServiceReference ref = context.getServiceReference(Config.class.getName());
	        
	        if (ref == null)
	        {
	            /* Configuration confService not running, so attempt to start it. */
	            Bundle[] bundles = context.getBundles();
	            for (Bundle b : bundles)
	            {
	                if (b.getSymbolicName().equals("SchedulingServer-Configuration") && b.getState() == Bundle.INSTALLED
	                        || b.getState() == Bundle.RESOLVED) 
	                {
	                    b.start();
	                }
	            }
	            ref = context.getServiceReference(Config.class.getName());
	        }
	        
	        if (ref != null)
	        {
	            /* Load all configuration properties. */
	            this.confService = new ServiceTracker(context, ref, null);
	            this.confService.open();
	            Config conf = (Config)this.confService.getService();
	            
	            /* Common configuration. */
	            conf.getProperty("Syslog_Local_Facility_Num", "1");
	        }
	        else
	        {
	            System.err.println("Unable to load LabConnector configuration, so using defaults.");
	        }
	        
		/* Service to host the LabConnector interface. */
	        ServletContainerService service = new ServletContainerService();
	        service.addServlet(new ServletContainer(new AxisServlet(), true));
	        this.serverReg = context.registerService(ServletContainerService.class.getName(), service, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
    @Override
	public void stop(BundleContext context) throws Exception 
	{        
		/* Stop the servlet container service and log it */
		this.logger.info("Stopping " + context.getBundle().getSymbolicName() + " bundle.");
		this.serverReg.unregister();
	}

}