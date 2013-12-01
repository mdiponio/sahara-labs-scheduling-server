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
 * @date 6th April 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.session.intf;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientService;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.InSessionType;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.impl.SessionServiceImpl;

/**
 * Session SOAP interface implementation.
 */
public class SessionSOAPImpl implements SessionSOAP
{
    /** Logger. */
    private Logger logger;
    
    /** Flag for unit testing to disable rig client communication. */ 
    private boolean notTest = true;
    
    public SessionSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public FinishSessionResponse finishSession(final FinishSession request)
    {
        /* Request parameters. */
        UserIDType uID = request.getFinishSession();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#finishSession request for user with id=" 
                + uID.getUserID() + ", namespace=" + uID.getUserNamespace() + ", name=" + uID.getUserName() + '.');
        
        /* Response parameters. */
        FinishSessionResponse resp = new FinishSessionResponse();
        InSessionType inSes = new InSessionType();
        resp.setFinishSessionResponse(inSes);
        
        SessionDao dao = new SessionDao();
        try
        {
            Session ses;
            User user = this.getUserFromUserID(uID, new UserDao(dao.getSession()));
            
            if (user != null && (ses = dao.findActiveSession(user)) != null)
            {
                inSes.setIsInSession(!new SessionServiceImpl().finishSession(ses, "User request", dao.getSession()));
            }
        }
        finally
        {
            dao.closeSession();
        }
        return resp;
    }

    @Override
    public GetSessionInformationResponse getSessionInformation(final GetSessionInformation request)
    {
        /* Request parameters. */
        UserIDType uID = request.getGetSessionInformation();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getSessionInformation request for user " +
        		"with id=" + uID.getUserID() + ", namespace=" + uID.getUserNamespace() + ", name=" + 
        		uID.getUserName() + '.');
        
        /* Response parameters. */
        GetSessionInformationResponse resp = new GetSessionInformationResponse();
        SessionType info = new SessionType();
        resp.setGetSessionInformationResponse(info);
        info.setIsInSession(false);
        
        SessionDao dao = new SessionDao();
        Session ses;
        User user = this.getUserFromUserID(uID, new UserDao(dao.getSession()));
        if (user != null && (ses = dao.findActiveSession(user)) != null && ses.getRig() != null)
        {
            info.setIsInSession(true);
        
          
            info.setIsCodeAssigned(ses.getCodeReference() != null);
            
            /* Resource. */
            Rig rig = ses.getRig();
            ResourceIDType res = new ResourceIDType();
            info.setResource(res);
            res.setType(ResourcePermission.RIG_PERMISSION);
            res.setResourceID(rig.getId().intValue());
            res.setResourceName(rig.getName());
            info.setContactURL(rig.getContactUrl());
            info.setRigType(rig.getRigType().getName());
            
            /* Session time and remaining time. */
            ResourcePermission perm = ses.getResourcePermission();
            int time = Math.round((System.currentTimeMillis() - ses.getAssignmentTime().getTime()) / 1000);
            info.setTime(time);
            int remainingTime = ses.getDuration() + (perm.getAllowedExtensions() - ses.getExtensions()) *  
                    perm.getExtensionDuration() - time;
            info.setTimeLeft(remainingTime);
            info.setExtensions(ses.getExtensions());
            info.setIsReady(ses.isReady());
            
            /* Warning messages. */
            if (ses.isInGrace())
            {
                info.setWarningMessage(remainingTime > 0 ? 
                        "Your session will expire in " + remainingTime + " seconds." :
                        "Your session is being terminated.");
            }
            else if (ses.isReady() && ses.getResourcePermission().isActivityDetected())
            {
                /* Find out about activity, if not ignored. */
                try
                {
                    if (this.notTest)
                    {
                        IsActivityDetectableResponse detectResponse = new RigClientService(rig, dao.getSession()).isActivityDetectable();
                        if (!detectResponse.getIsActivityDetectableResponse().getActivity())
                        {
                            int rmTime = perm.getSessionActivityTimeout() -  
                                    (ses.getActivityLastUpdated().before(ses.getAssignmentTime()) ? time :
                                     Math.round((System.currentTimeMillis() - ses.getActivityLastUpdated().getTime()) / 1000));
                            info.setWarningMessage(rmTime > 0 ?
                                    "Your session will be terminated if you do not use this rig within " + rmTime + " seconds." :
                                    "Your session is being terminated.");
                        }
                        else
                        {
                            ses.setActivityLastUpdated(new Date());
                            dao.flush();
                        }
                    }
                }
                catch (Exception e)
                {
                    this.logger.warn("Unable to call activity detection on rig client " + rig.getName() + " at " +
                            rig.getContactUrl() + ", error " + e.getMessage() + '.');
                }
            }
            
            /* User class details. */
            info.setUserClass(ses.getResourcePermission().getUserClass().getId().intValue());
            info.setUserClassName(ses.getResourcePermission().getUserClass().getName());
            info.setResourcePermission(ses.getResourcePermission().getId().intValue());
        }
        
        dao.closeSession();
        return resp;
    }
    
    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uID user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uID, UserDao dao)
    {
        User user;
        
        long recordId = 0;
        try
        {
            if (uID.getUserID() != null)
            {
                recordId = Long.parseLong(uID.getUserID());
            }
        }
        catch (NumberFormatException e) { /* Don't use user ID then. */ }
        
        String ns = uID.getUserNamespace(), nm = uID.getUserName();
        
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
}
