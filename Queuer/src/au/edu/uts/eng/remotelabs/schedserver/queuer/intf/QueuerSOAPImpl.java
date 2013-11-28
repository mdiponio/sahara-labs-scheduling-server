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
 * @date 28th March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.queuer.intf;

import java.util.Date;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingsActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SlaveableRigsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.SlaveableRigs;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.PermissionAvailabilityRequest;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.PermissionAvailabilityRequest.QueueTarget;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueEntry;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueuerUtil;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserAsSlave;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserAsSlaveResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.InQueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.OperationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoteLoadType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserQueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.impl.QueuerServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests.RigSlaveAllocator;

/**
 * Queuer SOAP interface implementation.
 */
public class QueuerSOAPImpl implements QueuerSOAP
{   
    /** Logger. */
    private Logger logger;
    
    public QueuerSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public AddUserToQueueResponse addUserToQueue(final AddUserToQueue request)
    {
        /* Request parameters. */
        UserIDType uId = request.getAddUserToQueue().getUserID();
        PermissionIDType pId = request.getAddUserToQueue().getPermissionID();
        ResourceIDType rId = request.getAddUserToQueue().getResourceID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#addUserToQueue from user (id=" + 
                uId.getUserID() + ", namespace=" + uId.getUserNamespace() + ", name=" + uId.getUserName() + ") for " +
                (pId != null ? "permission with identifier " + pId.getPermissionID() : (rId != null ? 
                "resource with identifer " + rId.getResourceID() + " and name " + rId.getResourceName() :
                "neither a permission or a resource (this will invalid and will fail)")) + '.');
        
        /* Response parameters. */
        AddUserToQueueResponse resp = new AddUserToQueueResponse();
        InQueueType inQu = new InQueueType();
        resp.setAddUserToQueueResponse(inQu);
        inQu.setInQueue(false);
        inQu.setInSession(false);
        inQu.setQueueSuccessful(false);
        
        if (!this.checkPermission(request.getAddUserToQueue()))
        {
            this.logger.warn("Cannot queue user because of invalid permission.");
            inQu.setFailureReason("Invalid permission.");
            return resp;
        }
        
        org.hibernate.Session db = new UserDao().getSession();
        try
        {
            QueueEntry entry = new QueueEntry(db);
            User user;
            Bookings booking;
            /**********************************************************************
             ** 1) Load user and continue if they exist.                         **
             **********************************************************************/
            if ((user = this.getUserFromUserID(uId, db)) == null)
            {
                this.logger.warn("Cannot queue user with with identifier=" + uId.getUserID() + " and name=" + 
                        uId.getUserNamespace() + ':' + uId.getUserName() + " because not found.");
                inQu.setFailureReason("User not found.");
            }
            /*********************************************************************
             ** 2) Check the user isn't in queue and continue if they aren't    **
             **    already in the queue.                                        **
             *********************************************************************/
            else if (entry.isInQueue(user))
            {
                this.logger.warn("Cannot queue user " + user.qName() + " because already in queue.");
                inQu.setFailureReason(entry.getErrorMessage());
            }
            /**********************************************************************
             ** 3) Check the user has permission to use either the requested     **
             **    resource permission or requested resource.                    **
             **********************************************************************/
            else if ((pId == null || !entry.hasPermission(pId.getPermissionID())) &&
                     (rId == null || !entry.hasPermission(rId.getType(), rId.getResourceID(), rId.getResourceName())))
            {            
                this.logger.warn("Cannot queue user " + user.qName()  + " because does not have permission to access " +
                		"requested resource permission or resource.");
                inQu.setFailureReason(entry.getErrorMessage());
            }
            /**********************************************************************
             ** 4) Check the user doesn't have a bookingService starting before  **
             **    the queued session would finish.                              **
             **********************************************************************/
            else if ((booking = new BookingsDao(db).getNextBookingWithin(user, entry.getResourcePermission().getSessionDuration())) != null)
            {
                this.logger.info("Cannot queue user " + user.qName() + " because has a booking starting before queued " +
                		"session would end.");
                inQu.setFailureReason("Booking: " + ((booking.getStartTime().getTime() - System.currentTimeMillis()) / 1000) + 
                        " seconds until booking starts.");
                inQu.setInBooking(true);
                inQu.setBookingID(booking.getId().intValue());
            }
            /**********************************************************************
             ** 5) Check the user can queue.                                     **
             **********************************************************************/
            else if (!entry.canUserQueue())
            {
                this.logger.warn("Cannot queue user " + user.qName() + " because a queue condition fails. This may be " +
                		"because the requested resource is offline or the user does not have the queue permission " +
                		"for in use resources.");
                inQu.setFailureReason(entry.getErrorMessage());
            }
            /**********************************************************************
             ** 6) Every pre-queue predicate is satisfied so add the user to the **
             *     queue.                                                        **
             **********************************************************************/
            else
            {
                // TODO Queue entry - Upload batch code.
                inQu.setQueueSuccessful(entry.addToQueue(null));
            }
            
            /* Populate queue return details if successful. */
            Session activeSes = entry.getActiveSession();
            if (activeSes == null)
            {
                inQu.setFailureReason(entry.getErrorMessage());
            }
            else
            {
                ResourceIDType resource = new ResourceIDType();
                resource.setType(activeSes.getResourceType());
                if (activeSes.getRig() == null)
                {
                    inQu.setInQueue(true);
                    resource.setResourceID(activeSes.getRequestedResourceId().intValue());
                    resource.setResourceName(activeSes.getRequestedResourceName());
                    inQu.setQueuedResouce(resource);
                }
                else
                {
                    inQu.setInSession(true);
                    resource.setResourceID(activeSes.getRig().getId().intValue());
                    resource.setResourceName(activeSes.getRig().getName());
                    inQu.setAssignedResource(resource);
                }
            }
        }
        finally
        {
            db.close();
        }
        return resp;
    }
    
    @Override
    public AddUserAsSlaveResponse addUserAsSlave(AddUserAsSlave request)
    {
        // FIXME Overlook this
        
        
        /* Request parameters. */
        UserIDType uId = request.getAddUserAsSlave().getUserID();
        PermissionIDType pId = request.getAddUserAsSlave().getPermissionID();

        this.logger.debug("Received " + this.getClass().getSimpleName() + "#addUserAsSlave from user (id=" + 
                uId.getUserID() + ", namespace=" + uId.getUserNamespace() + ", name=" + uId.getUserName() + ") for " +
                (pId != null ? "permission with identifier " + pId.getPermissionID() :
                "neither a permission or a resource (this will invalid and will fail)") + '.');
        
        String password = request.getAddUserAsSlave().getPassword();
        String rigID = request.getAddUserAsSlave().getRigID();

        /* Response parameters. */
        AddUserAsSlaveResponse resp = new AddUserAsSlaveResponse();
        InQueueType inQu = new InQueueType();
        resp.setAddUserAsSlaveResponse(inQu);
        inQu.setInQueue(false);
        inQu.setInSession(false);
        inQu.setQueueSuccessful(false);

        if (!this.checkPermission(request.getAddUserAsSlave()))
        {
            this.logger.warn("Cannot enslave user because of invalid permission.");
            inQu.setFailureReason("Invalid permission.");
            return resp;
        }
        
        org.hibernate.Session db = new UserDao().getSession();
        
        ResourcePermissionDao rpd = new ResourcePermissionDao();
        ResourcePermission rp = rpd.get(Long.valueOf(pId.getPermissionID()));
        rpd.closeSession();
       	RigDao rd = new RigDao();
    	Rig r = rd.get(Long.parseLong(rigID));
    	rd.closeSession();
        
    	if (rp.getRig() != null && rp.getRig().getId().equals(r.getId()))
        {
            this.logger.debug("matching Rig ID found in permission");
        }
        else if (rp.getRigType() != null && r.getRigType().getId().equals(rp.getRigType().getId()))
        {
        	this.logger.debug("matching Rig Type found in permission");
        }
        else{
            this.logger.warn("Cannot enslave user because of invalid permission.");
            inQu.setFailureReason("Invalid permission.");
            return resp;
        }
        
        List<Session> activeSessions = new SessionDao().findAllActiveSessions();
        Session mainSession = null;
        for (Session items:activeSessions){
        	if (items.getCodeReference() == null && items.getRig().getId().equals(Long.parseLong(rigID))){
        		mainSession = items;
        	}
        }
        
        if (mainSession == null)
        {
        	this.logger.warn("The master session cannot be found.");
        	inQu.setFailureReason("Invalid permission.");
        	return resp;
        }
        
        this.logger.info("Found session data: " + mainSession.getId() + " " +
        		mainSession.getRequestedResourceName() + " " + mainSession.getUserName());
        
        SlaveableRigsDao sdao = new SlaveableRigsDao();
        SlaveableRigs slav = sdao.getInfo(mainSession.getRig());
        if (slav == null){
        	this.logger.warn("The master session cannot be found.");
        	inQu.setFailureReason("Invalid permission.");
        	return resp;
        }
        
        if (!slav.getPassword().equals(password) || slav.getRig().getId()!=Integer.parseInt(rigID)){
        	this.logger.warn("Wrong parameters.");
        	inQu.setFailureReason("Incorrect credentials.");
        	return resp;
        }

        sdao.closeSession();
        try
        {
            QueueEntry entry = new QueueEntry(db);
            User user;
            Bookings booking;
            /**********************************************************************
             ** 1) Load user and continue if they exist.                         **
             **********************************************************************/
            if ((user = this.getUserFromUserID(uId, db)) == null)
            {
                this.logger.warn("Cannot queue user with with identifier=" + uId.getUserID() + " and name=" + 
                        uId.getUserNamespace() + ':' + uId.getUserName() + " because not found.");
                inQu.setFailureReason("User not found.");
            }
            /*********************************************************************
             ** 2) Check the user isn't in queue and continue if they aren't    **
             **    already in the queue.                                        **
             *********************************************************************/
            else if (entry.isInQueue(user))
            {
                this.logger.warn("Cannot queue user " + user.qName() + " because already in queue.");
                inQu.setFailureReason("Already in queue.");
            }
            /**********************************************************************
             ** 3) Check the user has permission to use either the requested     **
             **    resource permission or requested resource.                    **
             **********************************************************************/
            else if ((pId == null || !entry.hasPermission(pId.getPermissionID())))
            {            
                this.logger.warn("Cannot queue user " + user.qName()  + " because does not have permission to access " +
                		"requested resource permission or resource.");
                inQu.setFailureReason("No permission.");
            }
            /**********************************************************************
             ** 4) Check the user doesn't have a bookingService starting before  **
             **    the queued session would finish.                              **
             **********************************************************************/
//            else if ((booking = this.getNextBooking(user, entry.getResourcePermission().getSessionDuration(), db)) != null)
//            {
//                this.logger.info("Cannot queue user " + user.qName() + " because has a booking starting before queued " +
//                		"session would end.");
//                inQu.setFailureReason("Booking: " + ((booking.getStartTime().getTime() - System.currentTimeMillis()) / 1000) + 
//                        " seconds until booking starts.");
//                inQu.setInBooking(true);
//                inQu.setBookingID(booking.getId().intValue());
//            }
            /**********************************************************************
             ** 5) Check the user can queue.                                     **
             **********************************************************************/
            else if (!entry.canUserQueue())
            {
                this.logger.warn("Cannot queue user " + user.qName() + " because the user cannot queue. This may be " +
                		"because the requested resource is offline or the user does not have the queue permission " +
                		"for in use resources.");
                inQu.setFailureReason("Cannot queue.");
            }
            /**********************************************************************
             ** 6) Every pre-queue predicate is satisfied so add the user to the **
             *     queue.                                                        **
             **********************************************************************/
            else
            {
            	this.logger.info("Made it to the slave allocation stage, for user " + user.qName());
                // TODO Queue entry - Upload batch code.

                //mainSession.setUser(entry.getUser());
                //mainSession.setUserNamespace(entry.getUser().getNamespace());
                //mainSession.setUserName(entry.getUser().getName());
                
                Session n = new Session();
                n.setActive(mainSession.isActive());
                n.setActivityLastUpdated(mainSession.getActivityLastUpdated());
                n.setAssignedRigName(mainSession.getAssignedRigName());
                n.setAssignmentTime(mainSession.getAssignmentTime());
                n.setDuration(mainSession.getDuration());
                n.setExtensions(mainSession.getExtensions());
                n.setInGrace(mainSession.isInGrace());
                n.setPriority(mainSession.getPriority());
                n.setReady(mainSession.isReady());
                n.setRemovalReason(mainSession.getRemovalReason());
                n.setRemovalTime(mainSession.getRemovalTime());
                n.setRequestedResourceId(mainSession.getRequestedResourceId());
                n.setRequestedResourceName(mainSession.getRequestedResourceName());
                n.setRequestTime(mainSession.getRequestTime());
                n.setResourcePermission(mainSession.getResourcePermission());
                n.setResourceType(mainSession.getResourceType());
                n.setRig(mainSession.getRig());
                n.setUser(entry.getUser());
                n.setUserNamespace(entry.getUser().getNamespace());
                n.setUserName(entry.getUser().getName());
                n.setCodeReference(mainSession.getCodeReference());
                n.setIsSlave(true);
                
                this.logger.info("About to call RigSlaveAllocator for " + user.qName());
                new RigSlaveAllocator().slaveAllocate(n, db);
                this.logger.info("Called RigSlaveAllocator for " + user.qName());
                inQu.setQueueSuccessful(true);
            }

            /* Populate slave return details if successful. */
            Session activeSes = entry.getActiveSession();
            if (activeSes != null)
            {
                ResourceIDType resource = new ResourceIDType();
                resource.setType(activeSes.getResourceType());
                if (activeSes.getRig() == null)
                {
                    inQu.setInQueue(true);
                    resource.setResourceID(activeSes.getRequestedResourceId().intValue());
                    resource.setResourceName(activeSes.getRequestedResourceName());
                    inQu.setQueuedResouce(resource);
                }
                else
                {
                    inQu.setInSession(true);
                    resource.setResourceID(activeSes.getRig().getId().intValue());
                    resource.setResourceName(activeSes.getRig().getName());
                    inQu.setAssignedResource(resource);
                }
            }
        }
        finally
        {
            db.close();
        }
        return resp;
    }

    @Override
    public RemoveUserFromQueueResponse removeUserFromQueue(final RemoveUserFromQueue request)
    {
        /* Request parameters. */
        UserIDType uId = request.getRemoveUserFromQueue();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#removeUserFromQueue with params user id=" + 
                uId.getUserID() + ", user namespace " + uId.getUserNamespace() + ", user name=" + uId.getUserName() + '.');
        
        /* Response parameters. */
        RemoveUserFromQueueResponse resp = new RemoveUserFromQueueResponse();
        InQueueType inQueue = new InQueueType();
        resp.setRemoveUserFromQueueResponse(inQueue);
        inQueue.setInQueue(false);
        inQueue.setInSession(false);
        inQueue.setQueueSuccessful(false);
        
        if (!this.checkPermission(uId))
        {
            this.logger.warn("Unable to remove user from queue because of a lack of permission.");
            return resp;
        }
        
        SessionDao dao = new SessionDao();
        try
        {
            Session ses;
            User user;
            
            if ((user = this.getUserFromUserID(uId, dao.getSession())) == null)
            {
                this.logger.warn("Unable to terminate user session because the user was not found.");
                inQueue.setFailureReason("User not found.");
            }
            else if ((ses = dao.findActiveSession(user)) == null)   
            {
                this.logger.warn("Unable to terminate user session because the user does not have an active session.");
                inQueue.setFailureReason("Not in queue.");
            }
            else
            {
                /* User has an active session so invalidate it. */
                inQueue.setQueueSuccessful(new QueuerServiceImpl().removeFromQueue(ses, "User request", dao.getSession()));                
            }
        }
        finally
        {
            dao.closeSession();
        }
        return resp; 
    }
    
    @Override
    public GetUserQueuePositionResponse getUserQueuePosition(final GetUserQueuePosition request)
    {
        /* Request parameters. */
        UserIDType uid = request.getGetUserQueuePosition();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getUserQueuePosition " +
        		"with params user id=" + uid.getUserID() + ", user namespace=" + uid.getUserNamespace() + 
        		", user name=" + uid.getUserName() + '.');
        
        /* Response parameters. */
        GetUserQueuePositionResponse resp = new GetUserQueuePositionResponse();
        UserQueueType queue = new UserQueueType();
        queue.setInQueue(false);
        queue.setInSession(false);
        queue.setPosition(-1);
        queue.setTime(0);
        resp.setGetUserQueuePositionResponse(queue);
        
        SessionDao dao = new SessionDao();
        try
        {
            Session ses;
            User user;
            if (!this.checkPermission(uid))
            {
                this.logger.warn("Unable to check if user is in queue because of invalid permission.");
            }
            else if ((user = this.getUserFromUserID(uid, dao.getSession())) != null &&
                     (ses = dao.findActiveSession(user)) != null)
            {
                /* Update the last contact timestamp. */
                ses.setActivityLastUpdated(new Date());
                dao.flush();
                
                if (ses.getAssignmentTime() == null)
                {
                    /* User is currently in queue. */
                    queue.setInQueue(true);
                    queue.setPosition(new QueuerServiceImpl().getQueuePosition(ses, dao.getSession()));
                    queue.setTime(Math.round((System.currentTimeMillis() - ses.getRequestTime().getTime()) / 1000));
                    
                    /* Add requested resource. */
                    ResourceIDType res = new ResourceIDType();
                    queue.setQueuedResouce(res);
                    res.setType(ses.getResourceType());
                    res.setResourceID(ses.getRequestedResourceId().intValue());
                    res.setResourceName(ses.getRequestedResourceName());
                
                    /* Disabled for MultiSite because the this will require 
                     * another call to the provider. */
                    if (!ResourcePermission.CONSUMER_PERMISSION.equals(ses.getResourceType()))
                    {
                        queue.setQueue(this.getQueueForPermission(ses.getResourcePermission(), dao.getSession()));
                    }
                }
                else
                {
                    /* User is currently in session. */
                    queue.setInSession(true);
                    queue.setPosition(0);
                    Rig rig = ses.getRig();
                    ResourceIDType res = new ResourceIDType();
                    queue.setAssignedResource(res);
                    res.setType("RIG");
                    res.setResourceID(rig.getId().intValue());
                    res.setResourceName(rig.getName());
                    queue.setTime((Math.round(System.currentTimeMillis() - ses.getAssignmentTime().getTime()) / 1000));
                }
            }
        }
        finally
        {
            dao.closeSession();
        }
        return resp;
    }
    
    @Override
    public IsUserInQueueResponse isUserInQueue(final IsUserInQueue request)
    {
        /* Request parameters. */
        UserIDType uid = request.getIsUserInQueue();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#isUserInQueue with params user id=" + 
                uid.getUserID() + ", user namespace=" + uid.getUserNamespace() + ", user name=" + uid.getUserName() + 
                '.');
        
        /* Response parameters. */
        IsUserInQueueResponse resp = new IsUserInQueueResponse();
        InQueueType inQueue = new InQueueType();
        resp.setIsUserInQueueResponse(inQueue);
        inQueue.setInQueue(false);
        inQueue.setInSession(false);
        inQueue.setInBooking(false);
        
        SessionDao dao = new SessionDao();
        try
        {
            User user;
            Session ses;
            Bookings booking;
            
            if (!this.checkPermission(uid))
            {
                this.logger.warn("Unable to check if user is in queue because of invalid permission.");
                inQueue.setFailureReason("Invalid permission.");
            }
            else if ((user = this.getUserFromUserID(uid, dao.getSession())) == null)
            {
                this.logger.debug("Unable to check if user is in queue because user was not found.");
                inQueue.setFailureReason("User not found.");
            }
            else if ((ses = dao.findActiveSession(user)) != null)
            {
                if (ses.getAssignmentTime() == null)
                {
                    /* Update the last contact timestamp. */
                    ses.setActivityLastUpdated(new Date());
                    dao.flush();
                    
                    /* User is currently in queue. */
                    inQueue.setInQueue(true);
                }
                else
                {
                    /* User is currently in session. */
                    inQueue.setInSession(true);
                    Rig rig = ses.getRig();
                    ResourceIDType res = new ResourceIDType();
                    inQueue.setAssignedResource(res);
                    res.setType("RIG");
                    res.setResourceID(rig.getId().intValue());
                    res.setResourceName(rig.getName());
                }
                
                /* Add requested resource. */
                ResourceIDType res = new ResourceIDType();
                inQueue.setQueuedResouce(res);
                res.setType(ses.getResourceType());
                res.setResourceID(ses.getRequestedResourceId().intValue());
                res.setResourceName(ses.getRequestedResourceName());
            }
            else if ((booking = new BookingsDao(
                    dao.getSession()).getNextBookingWithin(user, BookingsActivator.BOOKING_STANDOFF)) != null)
            {
                // DODGY This uses half an hour limit probably should be a 
                // resource permission session duration limit
                inQueue.setInBooking(true);
                inQueue.setBookingID(booking.getId().intValue());
            }
        }
        finally
        {
            dao.closeSession();
        }
        return resp;
    }

    @Override
    public CheckPermissionAvailabilityResponse checkPermissionAvailability(final CheckPermissionAvailability request)
    {
        /* Request parameters. */
        PermissionIDType permResp = request.getCheckPermissionAvailability();
        long pId = permResp.getPermissionID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getCheckPermissionAvailability with params " +
        		"with permission identifier=" + pId + '.');
         
        /* Response parameters. */
        CheckPermissionAvailabilityResponse resp = new CheckPermissionAvailabilityResponse();
        
        if (!this.checkPermission(permResp))
        {
            this.logger.warn("Unable to check the resource permission because of invalid permission.");
            return resp;
        }
        
        ResourcePermissionDao dao = new ResourcePermissionDao();
        try
        {
            ResourcePermission perm = dao.get(pId);
            if (perm == null)
            {
                this.logger.warn("Permission with id=" + pId + " not found, unable to provide its avaliablity.");
            }
            resp.setCheckPermissionAvailabilityResponse(this.getQueueForPermission(perm, dao.getSession()));
        }
        finally
        {
            dao.closeSession();
        }
        return resp;
    }

    /**
     * Gets queue information for the specified resource permission.
     * 
     * @param perm resource permission
     * @param ses database session
     * @return queue information
     */
    private QueueType getQueueForPermission(ResourcePermission perm, org.hibernate.Session ses)
    {
        /* Default values. */
        QueueType queue = new QueueType();
        queue.setViable(false);
        queue.setHasFree(false);
        queue.setIsQueuable(false);
        queue.setIsCodeAssignable(false);
        
        ResourceIDType resource = new ResourceIDType();
        queue.setQueuedResource(resource);
        resource.setType("NOTFOUND");
        
        if (perm == null) return queue;
        
        /* Queuable is based on the resource class. */
        queue.setIsQueuable(perm.getUserClass().isQueuable());
        queue.setIsBookable(perm.getUserClass().isBookable());
               
        String type = perm.getType();
        resource.setType(type);
        boolean free = false;
        if (ResourcePermission.RIG_PERMISSION.equals(type))
        {
            /* Rig resource. */
            Rig rig = perm.getRig();
            resource.setResourceID(rig.getId().intValue());
            resource.setResourceName(rig.getName());

            queue.setHasFree(QueuerUtil.isRigFree(rig, perm, ses));
            queue.setViable(rig.isOnline());
            
            /* Code assignable is defined by the rig type of the rig. */
            queue.setIsCodeAssignable(rig.getRigType().isCodeAssignable());
            
            /* Only one resource, the actual rig. */
            QueueTargetType target = new QueueTargetType();
            target.setViable(rig.isOnline());
            target.setIsFree(QueuerUtil.isRigFree(rig, perm, ses));
            target.setResource(resource);
            queue.addQueueTarget(target);
            
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(type))
        {
            /* Rig type resource. */
            RigType rigType = perm.getRigType();
            resource.setResourceID(rigType.getId().intValue());
            resource.setResourceName(rigType.getName());
            queue.setIsCodeAssignable(rigType.isCodeAssignable());
            
            /* The targets are the rigs in the rig type. */
            for (Rig rig : rigType.getRigs())
            {
                if (rig.isOnline()) queue.setViable(true);
                if (free = QueuerUtil.isRigFree(rig, perm, ses)) queue.setHasFree(true);
                
                QueueTargetType target = new QueueTargetType();
                target.setViable(rig.isOnline());
                target.setIsFree(free);
                ResourceIDType resourceRig = new ResourceIDType();
                resourceRig.setType(ResourcePermission.RIG_PERMISSION);
                resourceRig.setResourceID(rig.getId().intValue());
                resourceRig.setResourceName(rig.getName());
                target.setResource(resourceRig);
                queue.addQueueTarget(target);
            }
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(type))
        {
            /* Capabilities resource. */
            RequestCapabilities requestCaps = perm.getRequestCapabilities();
            resource.setResourceID(requestCaps.getId().intValue());
            resource.setResourceName(requestCaps.getCapabilities());
            
            /* For code assignable to be true, all rigs who match the
             * request capabilities, must be code assignable. */
            queue.setIsCodeAssignable(true);
            
            /* Are all the rigs who have match rig capabilities to the
             * request capabilities. */
            for (MatchingCapabilities match : requestCaps.getMatchingCapabilitieses())
            {
                for (Rig capRig : match.getRigCapabilities().getRigs())
                {
                    if (!capRig.getRigType().isCodeAssignable()) queue.setIsCodeAssignable(false);
                    
                    /* To be viable, only one rig needs to be online. */
                    if (capRig.isOnline()) queue.setViable(true);
                    
                    /* To be 'has free', only one rig needs to be free. */
                    if (free = QueuerUtil.isRigFree(capRig, perm, ses)) queue.setHasFree(true);
                    
                    /* Add target. */
                    QueueTargetType target = new QueueTargetType();
                    target.setViable(capRig.isOnline());
                    target.setIsFree(free);
                    queue.addQueueTarget(target);
                    ResourceIDType resTarget = new ResourceIDType();
                    resTarget.setType(ResourcePermission.RIG_PERMISSION);
                    resTarget.setResourceID(capRig.getId().intValue());
                    resTarget.setResourceName(capRig.getName());
                    target.setResource(resTarget);
                }
            }
        }
        else if (ResourcePermission.CONSUMER_PERMISSION.equals(type))
        {
            /* Sanity check to make sure the permission actually has a mapping. */
            RemotePermission remotePerm = perm.getRemotePermission();
            if (remotePerm == null)
            {
                this.logger.warn("Consumer type permission does not have a mapping to a remote permission so cannot " +
                		"make remote request to determine availability.");
                return queue;
            }
            
            RemoteLoadType remoteLoad = new RemoteLoadType();
            remoteLoad.setSite(remotePerm.getSite().getName());
            queue.setRemoteLoad(remoteLoad);
            
            /* Make the remote call and populate the results. */
            PermissionAvailabilityRequest remoteCheck = new PermissionAvailabilityRequest();
            if (remoteCheck.checkAvailability(remotePerm, ses))
            {
                remoteLoad.setFailed(false);
                
                queue.setViable(remoteCheck.isViable());
                queue.setHasFree(remoteCheck.hasFree());
                queue.setIsBookable(remoteCheck.isBookable());
                queue.setIsQueuable(remoteCheck.isQueueable());
                queue.setIsCodeAssignable(remoteCheck.isCodeAssignable());
                
                ResourceIDType res = new ResourceIDType();
                res.setResourceName(remoteCheck.getResourceName());
                res.setType(remoteCheck.getResourceType());
                queue.setQueuedResource(res);
                
                for (QueueTarget qt : remoteCheck.getQueueTargets())
                {
                    QueueTargetType target = new QueueTargetType();
                    target.setViable(qt.isVaiable());
                    target.setIsFree(qt.isFree());
                    queue.addQueueTarget(target);
                    ResourceIDType resTarget = new ResourceIDType();
                    resTarget.setType(qt.getType());
                    resTarget.setResourceName(qt.getName());
                    target.setResource(resTarget);
                }
            }
            else
            {
                remoteLoad.setFailed(true);
                remoteLoad.setReason(remoteCheck.getFailureReason());
            }
        }

        return queue;
    }

    @Override
    public CheckResourceAvailabilityResponse checkResourceAvailability(final CheckResourceAvailability request)
    {
        /* Request parameters. */
        ResourceIDType resReq = request.getCheckResourceAvailability();
        long rId = resReq.getResourceID();
        String type = resReq.getType(), name = resReq.getResourceName();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#checkResourceAvailability with params " +
        		"resource type=" + type +", resource identifier=" + rId + ", resource name=" + name + '.');
        
        /* Response parameters. */
        CheckResourceAvailabilityResponse resp = new CheckResourceAvailabilityResponse();
        QueueType queue = new QueueType();
        resp.setCheckResourceAvailabilityResponse(queue);
        queue.setViable(false);
        queue.setHasFree(false);
        queue.setIsCodeAssignable(false);
        
        /* This is always true because queueable/bookab;e is stored as a user 
         * class permission. There isn't enough information to determine this
         * so the best case is assumed. */
        queue.setIsQueuable(true);
        queue.setIsBookable(true);
        
        ResourceIDType resource = new ResourceIDType();
        queue.setQueuedResource(resource);
        resource.setType(type);
        
        if (!this.checkPermission(resReq))
        {
            this.logger.warn("Unable to provide resource information because of insufficient permission.");
            return resp;
        }
        
        RigDao rigDao = new RigDao();
        try
        {
            Rig rig;
            RigTypeDao typeDao = new RigTypeDao(rigDao.getSession());
            RigType rigType;
            RequestCapabilitiesDao capsDao = new RequestCapabilitiesDao(rigDao.getSession());
            RequestCapabilities requestCaps;
            if (ResourcePermission.RIG_PERMISSION.equals(type) &&
                    ((rId > 0 && (rig = rigDao.get(rId)) != null) || (name != null && (rig = rigDao.findByName(name)) != null)))
            {
                /* Rig resource. */
                resource.setResourceID(rig.getId().intValue());
                resource.setResourceName(rig.getName());
                
                queue.setHasFree(rig.isOnline() && !rig.isInSession());
                queue.setViable(rig.isOnline());
    
                /* Code assignable is defined by the rig type of the rig. */
                queue.setIsCodeAssignable(rig.getRigType().isCodeAssignable());
    
                /* Only one resource, the actual rig. */
                QueueTargetType target = new QueueTargetType();
                target.setViable(rig.isOnline());
                target.setIsFree(rig.isOnline() && !rig.isInSession());
                target.setResource(resource);
                queue.addQueueTarget(target);
    
            }
            else if (ResourcePermission.TYPE_PERMISSION.equals(type) && 
                    ((rId > 0 && (rigType = typeDao.get(rId)) != null) || 
                     (name != null && (rigType = typeDao.findByName(name)) != null)))
            {
                /* Rig type resource. */
                resource.setResourceID(rigType.getId().intValue());
                resource.setResourceName(rigType.getName());
                
                queue.setIsCodeAssignable(rigType.isCodeAssignable());
    
                /* The targets are the rigs in the rig type. */
                for (Rig r: rigType.getRigs())
                {
                    if (r.isOnline()) queue.setViable(true);
                    if (r.isOnline() && !r.isInSession()) queue.setHasFree(true);
    
                    QueueTargetType target = new QueueTargetType();
                    target.setViable(r.isOnline());
                    target.setIsFree(r.isOnline() && !r.isInSession());
                    ResourceIDType resourceRig = new ResourceIDType();
                    resourceRig.setType(ResourcePermission.RIG_PERMISSION);
                    resourceRig.setResourceID(r.getId().intValue());
                    resourceRig.setResourceName(r.getName());
                    target.setResource(resourceRig);
                    queue.addQueueTarget(target);
                }
            }
            else if (ResourcePermission.CAPS_PERMISSION.equals(type) &&
                    ((rId > 0 && (requestCaps = capsDao.get(rId)) != null) || 
                     (name != null && (requestCaps = capsDao.findCapabilites(name)) != null)))
            {
                /* Capabilities resource. */
                resource.setResourceID(requestCaps.getId().intValue());
                resource.setResourceName(requestCaps.getCapabilities());
    
                /* For code assignable to be true, all rigs who match the
                 * request capabilities, must be code assignable. */
                queue.setIsCodeAssignable(true);
    
                /* Are all the rigs who have match rig capabilities to the
                 * request capabilities. */
                for (MatchingCapabilities match : requestCaps.getMatchingCapabilitieses())
                {
                    for (Rig capRig : match.getRigCapabilities().getRigs())
                    {
                        if (!capRig.getRigType().isCodeAssignable()) queue.setIsCodeAssignable(false);
    
                        /* To be viable, only one rig needs to be online. */
                        if (capRig.isOnline()) queue.setViable(true);
    
                        /* To be 'has free', only one rig needs to be free. */
                        if (capRig.isOnline() && !capRig.isInSession()) queue.setHasFree(true);
    
                        /* Add target. */
                        QueueTargetType target = new QueueTargetType();
                        target.setViable(capRig.isOnline());
                        target.setIsFree(capRig.isOnline() && !capRig.isInSession());
                        queue.addQueueTarget(target);
                        ResourceIDType resTarget = new ResourceIDType();
                        resTarget.setType(ResourcePermission.RIG_PERMISSION);
                        resTarget.setResourceID(capRig.getId().intValue());
                        resTarget.setResourceName(capRig.getName());
                        target.setResource(resTarget);
                    }
                }
            }
            else
            {
                this.logger.info("Unable to find resource of type " + type + " with ID " + rId + " and name " + name + '.');
                resource.setType("NOTFOUND");
            }
        }
        finally
        {
            rigDao.closeSession();
        }

        return resp;
    }
    
    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uid, org.hibernate.Session ses)
    {
        UserDao dao = new UserDao(ses);
        User user;
        long recordId = this.getIdentifier(uid.getUserID());
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        
        if (recordId > 0 && (user = dao.get(recordId)) != null)
        {
            return user;
        }
        else if (ns != null && nm != null && (user = dao.findByName(ns, nm)) != null)
        {
            return user;
        }
        
        return null;
    }
    
    /**
     * Converts string identifiers to a long.
     * 
     * @param idStr string containing a long  
     * @return long or 0 if identifier not valid
     */
    private long getIdentifier(String idStr)
    {
        if (idStr == null) return 0;
        
        try
        {
            return Long.parseLong(idStr);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }    
    
    /**
     * Checks whether the request has the specified permission. Currently this
     * is a stub and always return, irrespective of the provided user.
     * 
     * @return true if the request has the appropriate permission
     */
    private boolean checkPermission(OperationRequestType req)
    {
        // TODO Check request permissions for queuer
        return true;
    }

    
}
