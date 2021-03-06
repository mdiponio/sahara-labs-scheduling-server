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
 * @date 22nd February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.tasksched.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Service listener for task scheduler bundle. Adds and removes the service
 * object when service events are received.
 */
public class SchedulingServiceListener implements ServiceListener
{
    /** Task scheduler. */
    private final TaskScheduler scheduler;
    
    /** Task scheduler context. */
    private final BundleContext context;
    
    /** Logger. */
    private final Logger logger;
    
    public SchedulingServiceListener(TaskScheduler sched, BundleContext con)
    {
        this.logger = LoggerActivator.getLogger();
        this.scheduler = sched;
        this.context = con;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void serviceChanged(ServiceEvent event)
    {
        ServiceReference<Runnable> ref;
        switch (event.getType())
        {
            case ServiceEvent.REGISTERED:
                ref = (ServiceReference<Runnable>) event.getServiceReference();
                this.logger.debug("Received a registered service event for a servlet service of bundle " + 
                        ref.getBundle().getSymbolicName() + '.');
                this.scheduler.addTask(this.context.getService(ref),
                        Integer.parseInt((String) ref.getProperty("period")));
                break;
                
            case ServiceEvent.UNREGISTERING:
                ref = (ServiceReference<Runnable>) event.getServiceReference();
                this.logger.debug("Received a unregistering service event for a shutting down servlet service of " +
                        "bundle " + ref.getBundle().getSymbolicName() + '.');
                this.scheduler.removeTask(this.context.getService(ref));
                break;
        }
    }

}
