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
 * @date 8th February 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.server;

import java.util.Collection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.impl.ServerImpl;
import au.edu.uts.eng.remotelabs.schedserver.server.impl.ServerServiceListener;

/**
 * Activator for the Server bundle which is a Jetty server to host the Axis2
 * SOAP interface.
 */
public class ServerActivator implements BundleActivator 
{
    /** The server implementation. */
    private ServerImpl server;
    
    /** Logger. */
    private Logger logger;

    @Override
    public void start(final BundleContext context) throws Exception
    {
        this.logger = LoggerActivator.getLogger();
        
        this.server = new ServerImpl(context);
        this.server.init();
        
        /* Register a service listener for servlet services. */
        this.logger.debug("Adding a service listener for the object class " + ServletContainerService.class.getName() + '.');
        final ServerServiceListener listener = new ServerServiceListener(this.server);
        context.addServiceListener(listener, '(' + Constants.OBJECTCLASS + '=' + ServletContainerService.class.getName() + ')');
        
        /* Fire pseudo-events for already registered servers. */
        final Collection<ServiceReference<ServletContainerService>> refs = 
                context.getServiceReferences(ServletContainerService.class, null);
        
        for (ServiceReference<ServletContainerService> ref : refs)
        {
            this.logger.debug("Firing registered event for servlet service from bundle " + 
                    ref.getBundle().getSymbolicName() + '.');
            listener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Start the server. */
        this.server.start();
    }

    @Override
    public void stop(final BundleContext context) throws Exception
    {
        /* The framework will cleanup any services in use and remove the service listener. */
        this.logger.warn("Stopping the server bundle.");
        this.server.stop();
    } 
}
