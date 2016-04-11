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
 * @date 19th September 2011
 */
package io.rln.ss.data.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.rln.ss.data.entities.RequestablePermissionPeriod;

/**
 * Data access object for the {@link RequestablePermissionPeriod} entity.
 */
public class RequestablePermissionPeriodDao extends GenericDao<RequestablePermissionPeriod>
{
    public RequestablePermissionPeriodDao()
    {
        super(RequestablePermissionPeriod.class);
    }
    
    public RequestablePermissionPeriodDao(Session db)
    {
        super(db, RequestablePermissionPeriod.class);
    }
    
    /**
     * Gets the list of active requestable periods.
     * 
     * @return list of requestable periods
     */
    @SuppressWarnings("unchecked")
    public List<RequestablePermissionPeriod> getActivePeriods()
    {
        return this.session.createCriteria(RequestablePermissionPeriod.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.gt("end", new Date()))
                .addOrder(Order.desc("type"))
                .addOrder(Order.desc("rigType"))
                .addOrder(Order.desc("rig"))
                .addOrder(Order.desc("requestCapabilities"))
                .addOrder(Order.asc("start"))
                .list();
    }
}
