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
 * @date 9th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigoperations;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.RigClientAsyncService;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.RigClientAsyncServiceCallbackHandler;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.NotifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.OperationResponseType;

/**
 * Notifies the in session user on the rig of a message.
 */
public class RigNotifier extends RigClientAsyncServiceCallbackHandler
{
    /** Rig to release. */
    private Rig rig;
    
    /** Logger. */
    private Logger logger;
    
    public RigNotifier()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    public void notify(String message, Session ses, org.hibernate.Session db)
    {
        this.rig = ses.getRig();
        
        try
        {
            RigClientAsyncService service = new RigClientAsyncService(this.rig.getName(), db);
            service.notify(message, this);
        }
        catch (Exception e)
        {
            this.logger.error("Failed calling rig client notify from " + this.rig.getName() + " at " + 
                    this.rig.getContactUrl() + " because of error " + e.getMessage() + ".");

            /* Put the rig offline. */
            this.rig.setInSession(false);
            this.rig.setOnline(false);
            this.rig.setOfflineReason("Notify failed for session " + ses.getId() + ".");
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
            
            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(db);
            rigLogDao.addOfflineLog(this.rig, "Notify failed with error: " + e.getMessage());
            
            /* Fire event the rig is offline. */
            RigEventListener evts[] = RigOperationsActivator.getRigEventListeners();
            for (RigEventListener evt : evts)
            {
                evt.eventOccurred(RigStateChangeEvent.OFFLINE, this.rig, db);
            }
        }
    }
    
    @Override
    public void notifyResponseCallback(final NotifyResponse response)
    {
        RigDao dao = new RigDao();
        this.rig = dao.merge(this.rig);
            
        OperationResponseType op = response.getNotifyResponse();
        if (!op.getSuccess())
        {
            /* Failed release so take rig offline. */
            this.rig.setOnline(false);
            this.rig.setOfflineReason("Notify failed with reason " + op.getError().getReason() + '.');
            dao.flush();
            
            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(dao.getSession());
            rigLogDao.addOfflineLog(this.rig, "Notify failed with reason: " + op.getError().getReason());
            
            /* Fire event the rig is offline. */
            RigEventListener evts[] = RigOperationsActivator.getRigEventListeners();
            for (RigEventListener evt : evts)
            {
                evt.eventOccurred(RigStateChangeEvent.OFFLINE, this.rig, dao.getSession());
            }
        }
        
        dao.closeSession();
    }
    
    @Override
    public void releaseErrorCallback(final Exception e)
    {
        RigDao dao = new RigDao();
        this.rig = dao.merge(this.rig);
        
        this.logger.error("Received error response from notify of rig " + this.rig.getName() + " at " 
                + this.rig.getContactUrl() + ". Error message" + " is " + e.getMessage() + '.');
        
        /* Release failed so take the rig offline. */
        this.rig.setOnline(false);
        this.rig.setOfflineReason("Notify failed with error: " + e.getMessage());
        dao.flush();
        
        /* Log when the rig is offline. */
        RigLogDao rigLogDao = new RigLogDao(dao.getSession());
        rigLogDao.addOfflineLog(this.rig, "Notify failed with error: " + e.getMessage());
        /* Fire event the rig is offline. */
        RigEventListener evts[] = RigOperationsActivator.getRigEventListeners();
        for (RigEventListener evt : evts)
        {
            evt.eventOccurred(RigStateChangeEvent.OFFLINE, this.rig, dao.getSession());
        }
        
        
        dao.closeSession();
    }
}
