/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 23rd July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.impl;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.QueueActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueEntry;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueuerUtil;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests.RigReleaser;

/**
 * Implementation of the Queuer POJO service.
 */
public class QueuerServiceImpl implements QueuerService
{
    /** Logger. */
    private Logger logger;
    
    /** Flag for unit testing to disable rig client communication. */ 
    private boolean notTest = true;
    
    public QueuerServiceImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public QueueAvailability checkAvailability(ResourcePermission perm, org.hibernate.Session ses)
    {
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#checkAvailability for permission '" + 
                perm.getId() + "'.");
        
        QueueAvailability response = new QueueAvailability();
        response.setType(perm.getType());
        
        boolean free = false;
        if (ResourcePermission.RIG_PERMISSION.equals(perm.getType()))
        {
            /* Rig resource. */
            Rig rig = perm.getRig();
            response.setName(rig.getName());
            response.setHasFree(QueuerUtil.isRigFree(rig, perm, ses));
            response.setViable(rig.isOnline());
            
            /* Code assignable is defined by the rig type of the rig. */
            response.setCodeAssignable(rig.getRigType().isCodeAssignable());
            
            /* Only one resource, the actual rig. */
            response.addTarget(rig, QueuerUtil.isRigFree(rig, perm, ses));          
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(perm.getType()))
        {
            /* Rig type resource. */
            RigType rigType = perm.getRigType();
            response.setName(rigType.getName());
            response.setCodeAssignable(rigType.isCodeAssignable());
            
            /* The targets are the rigs in the rig type. */
            for (Rig rig : rigType.getRigs())
            {
                if (rig.isOnline()) response.setViable(true);
                if (free = QueuerUtil.isRigFree(rig, perm, ses)) response.setHasFree(true);
                
                response.addTarget(rig, free);
            }
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(perm.getType()))
        {
            /* Capabilities resource. */
            RequestCapabilities requestCaps = perm.getRequestCapabilities();
            response.setName(requestCaps.getCapabilities());

            /* For code assignable to be true, all rigs who match the
             * request capabilities, must be code assignable. */
            response.setCodeAssignable(true);
            
            /* Are all the rigs who have match rig capabilities to the
             * request capabilities. */
            for (MatchingCapabilities match : requestCaps.getMatchingCapabilitieses())
            {
                for (Rig capRig : match.getRigCapabilities().getRigs())
                {
                    if (!capRig.getRigType().isCodeAssignable()) response.setCodeAssignable(false);
                    
                    /* To be viable, only one rig needs to be online. */
                    if (capRig.isOnline()) response.setViable(true);
                    
                    /* To be 'has free', only one rig needs to be free. */
                    if (free = QueuerUtil.isRigFree(capRig, perm, ses)) response.setHasFree(true);
                    
                    /* Add target. */
                    response.addTarget(capRig, free);
                }
            }
        }
                
        return response;
    }

    @Override
    public QueueSession addToQueue(User user, ResourcePermission perm, String code, org.hibernate.Session db)
    {
        QueueEntry entry = new QueueEntry(db);
        
        /* If user is already in the queue, they can be added again. */
        if (entry.isInQueue(user))
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because they are already in the queue.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (!entry.hasPermission(perm))
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because they do not have permission to use " +
            		"the ri or the permission is inactive.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (!entry.canUserQueue())
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because the resource may be offline.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (entry.addToQueue(code))
        {
            Session ses = entry.getActiveSession();
            if (ses.getAssignmentTime() == null)
            {
                /* In queue. */
                return new QueueSession(ses, Queue.getInstance().getEntryPosition(ses, db));
            }
            else return new QueueSession(ses); // Assigned session
        }
        else
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because the resource may be offline or the " +
                    "user does not have permission to use the rig.");
            return new QueueSession("User does not have permission or rig is offline.");
        }
    }

    @Override
    public int getQueuePosition(Session ses, org.hibernate.Session db)
    {
        return Queue.getInstance().getEntryPosition(ses, db);
    }

    @Override
    public boolean removeFromQueue(Session ses, String reason, org.hibernate.Session db)
    {
        Queue.getInstance().removeEntry(ses, db);
        
        db.beginTransaction();
        ses.setActive(false);
        ses.setRemovalTime(new Date());
        ses.setRemovalReason("User request.");
        db.getTransaction().commit();
        
        QueueActivator.notifySessionEvent(SessionEvent.FINISHED, ses, db);
        
        /* If the user is assigned to a rig, free the rig. */
        if (ses.getRig() != null && this.notTest) new RigReleaser().release(ses, db);

        return true;
    }
}
