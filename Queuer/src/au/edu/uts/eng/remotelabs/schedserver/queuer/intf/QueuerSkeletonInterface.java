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
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueueResponse;

/**
 * Interface for the Queuer SOAP interface.
 */
public interface QueuerSkeletonInterface
{
    /**
     * Adds a user to the queue.
     * 
     * @param request 
     * @return response
     */
	public AddUserToQueueResponse addUserToQueue(AddUserToQueue request);
	
    /**
     * Adds a user as a slave
     * 
     * @param request 
     * @return response
     */
	public AddUserAsSlaveResponse addUserAsSlave(AddUserAsSlave request);
    public RemoveUserFromQueueResponse removeUserFromQueue(RemoveUserFromQueue request);

    /**
     * Returns information about the avaliability of a permission. The information
     * returned is:
     * <ul>
     *  <li>viable - Whether this resource is ready to be queued.</li>
     *  <li>hasFree - Whether there are free rigs that match this permission.</li>
     *  <li>isQueueable - Whether this permission is queuable.</li>
     *  <li>isCodeAssignable - Whether code can be assigned during the request
     *  of this resource.</li>
     *  <li>queuedResource - The resource that the permission queues for.</li>
     *  <li>queueTarget - The list of rigs that match this permission, including
     *  whether they are online and in session.</li>
     * </ul>
     * 
     * @param request
     * @return response
     */
    public CheckPermissionAvailabilityResponse checkPermissionAvailability(CheckPermissionAvailability request);

    /**
     * Returns the position the user in the queue. Also returned is the time the
     * user has been in the queue and information about the queue.
     * 
     * @param request 
     * @return response
     */
    public GetUserQueuePositionResponse getUserQueuePosition(GetUserQueuePosition request);

    /**
     * Finds information about the avaliability of a resource,
     * 
     * @param request 
     * @return response
     */
    public CheckResourceAvailabilityResponse checkResourceAvailability(CheckResourceAvailability request);

    /**
     * Checks if a user is either in queue or in session. If in session, 
     * details of the in session rig is provided.
     * 
     * @param request
     * @return response
     */
    public IsUserInQueueResponse isUserInQueue(IsUserInQueue request);
}
