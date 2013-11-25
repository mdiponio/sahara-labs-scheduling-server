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
 * @date 7th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigproxy;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.IdentityToken;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.RigClientServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.Allocate;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectable;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.NullType;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.Release;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.UserType;

/**
 * Proxies calls to the rig client using a synchronous message.
 * <b />
 * All calls using this proxy are provided with an identity token
 * the rig client is registered with.
 */
public class RigClientService
{
    /** The proxy implementation. */
    private RigClientServiceImpl service;
    
    /** The rig clients name. */
    private String rig;
    
    /** Identity token register. */
    private IdentityToken tok;
    
    /** Logger. */
    private Logger logger;
    
    /**
     * Creates a service client for the rig.
     *
     * @param rig 
     * @throws RigProxyException
     */
    public RigClientService(Rig rig) throws RigProxyException
    {
        this(rig.getName());
    }
    
    /**
     * Creates a service client for the rig and with the timeout period
     * given in seconds.
     * 
     * @param rig
     * @param tm timeout in seconds
     * @throws RigProxyException
     */
    public RigClientService(Rig rig, int tm) throws RigProxyException
    {
        this(rig.getName(), tm);
    }
    
    /**
     * Creates a service client for the rig with the specified name.
     *
     * @param rig rig name
     * @throws RigProxyException
     */
    public RigClientService(String rig) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        RigDao dao = new RigDao();
        Rig record = dao.findByName(rig);
        dao.closeSession();
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientServiceImpl(record.getContactUrl());
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
        }
    }
    
    /**
     * Creates a service client for the rig with the specified name and with the timeout period
     * given in seconds.
     * 
     * @param rig rig name
     * @param tm timeout in seconds
     * @throws RigProxyException
     */
    public RigClientService(String rig, int tm) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        RigDao dao = new RigDao();
        Rig record = dao.findByName(rig);
        dao.closeSession();
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientServiceImpl(record.getContactUrl(), tm);
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
        }
    }
    
    /**
     * Creates a service client for the rig and with the timeout period
     * given in seconds.  The rig will be loaded from the provided session.
     * 
     * @param rig
     * @param db database session
     * @throws RigProxyException
     */
    public RigClientService(Rig rig, Session db) throws RigProxyException
    {
        this(rig.getName(), db);
    }
    
    /**
     * Creates a service client for the rig and with the timeout period
     * given in seconds. The rig will be loaded from the provided session.
     * 
     * @param rig
     * @param db database session
     * @param tm timeout in seconds
     * @throws RigProxyException
     */
    public RigClientService(Rig rig, Session db, int tm) throws RigProxyException
    {
        this(rig.getName(), db, tm);
    }
    
    /**
     * Creates a service client for the rig and with the timeout period
     * given in seconds.  The rig will be loaded from the provided session.
     * 
     * @param rig
     * @param db database session
     * @throws RigProxyException
     */
    public RigClientService(String rig, Session db) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        Rig record = new RigDao(db).findByName(rig);
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientServiceImpl(record.getContactUrl());
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
        }
    }
    
    /**
     * Creates a service client for the rig and with the timeout period
     * given in seconds. The rig will be loaded from the provided session.
     * 
     * @param rig
     * @param db database session
     * @param tm timeout in seconds
     * @throws RigProxyException
     */
    public RigClientService(String rig, Session db, int tm) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        Rig record = new RigDao(db).findByName(rig);
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientServiceImpl(record.getContactUrl(), tm);
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
        }
    }
    
    /**
     * Request to allocate a specified user to the rig.
     * 
     * @param name user to allocate
     * @response response
     * @throws RemoteException 
     */
    public AllocateResponse allocate(String name) throws RemoteException 
    {        
        Allocate request = new Allocate();
        UserType user = new UserType();
        request.setAllocate(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));

        return this.service.allocate(request);
    }
    
    /**
     * Request to release the specified user from the rig.
     * 
     * @param name user to release
     * @return response
     * @throws RemoteException
     */
    public ReleaseResponse release(String name) throws RemoteException
    {
        Release request = new Release();
        UserType user = new UserType();
        request.setRelease(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));
        
        return this.service.release(request);
    }
    
    /**
     * Request to detect session activity.
     * 
     * @return response
     * @throws RemoteException
     */
    public IsActivityDetectableResponse isActivityDetectable() throws RemoteException
    {
        IsActivityDetectable request = new IsActivityDetectable();
        NullType nt = new NullType();
        request.setIsActivityDetectable(nt);
        nt.set_void("The world wonders...");
        
        return this.service.isActivityDetectable(request);
    }
}
