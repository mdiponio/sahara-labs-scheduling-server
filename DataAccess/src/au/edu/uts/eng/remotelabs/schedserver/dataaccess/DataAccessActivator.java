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
 * @date 5th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.dataaccess;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.impl.DataAccessConfiguration;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Data Access bundle activator. The data access bundle is used to load and 
 * persist entity classes into a databases.
 * <br />
 * The activator is used to set up the Hibernate session factory.
 */
public class DataAccessActivator implements BundleActivator 
{
    /** Session factory to obtains sessions from. */
    private static SessionFactory sessionFactory;
    
    /** Logger. */
    private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
	{
	    this.logger = LoggerActivator.getLogger();
	    this.logger.info("Starting the Data Access bundle.");
	    
	    /* Configure Hibernate for use with annotations. */
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.setProperties(new DataAccessConfiguration(context).getProperties());
		
		this.logger.debug("Hibernate properties: " + cfg.getProperties().toString());
		
		DataAccessActivator.sessionFactory = cfg.buildSessionFactory();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
		System.out.println("Goodbye World!!");
	}

	/**
	 * Returns a Hibernate session. Returns null if a session cannot be 
	 * opened.
	 * 
	 * @return session or null
	 */
	public Session getSession()
	{
	    if (sessionFactory == null) return null;
	    
	    return DataAccessActivator.sessionFactory.openSession();
	}
}
