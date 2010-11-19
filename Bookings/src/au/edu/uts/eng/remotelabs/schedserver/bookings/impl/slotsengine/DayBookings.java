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
 * @date 15th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * The bookings for a day.
 */
public class DayBookings
{
    /** Loaded rig bookings. */
    private Map<Rig, RigBookings> rigBookings;
    
    private String day;
    
    /** Logger. */
    private Logger logger;
    
    public DayBookings(String day)
    {
        this.logger = LoggerActivator.getLogger();
        this.rigBookings = new HashMap<Rig, RigBookings>();
        
        this.day = day;
    }
    
    /**
     * Gets the free slots for the rig. 
     * 
     * @param rig the rig to find free slots of
     * @param start start slot
     * @param end end slot
     * @return list of free slots
     */
    public List<MRange> getFreeSlots(Rig rig, int start, int end, Session ses)
    {
        RigBookings rb = this.getRigBookings(rig, ses);
        
        return null;
    }
    
    /**
     * Gets the rig bookings for the rig.
     * 
     * @param rig rig to find bookings of
     * @return rig bookings
     */
    private RigBookings getRigBookings(Rig rig, Session ses)
    {
        if (!this.rigBookings.containsKey(rig))
        {
            this.logger.debug("Loaded day bookings for rig '" + rig.getName() + "' on day " + this.day + ".");
            synchronized (this.rigBookings)
            {
                RigBookings bookings = new RigBookings(rig, this.day);
                this.rigBookings.put(rig, bookings);
                
                /* Load rig bookings for rig. */
                Criteria cri = ses.createCriteria(Bookings.class);
                cri.add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION))
                   .add(Restrictions.eq("rig", rig));

                

                RigBookings prev = bookings;
                Set<Rig> rigs = rig.getRigType().getRigs();
                for (Rig r : rigs)
                {
                    /* Load the rig bookings for rig. */
                    
                    
                    if (r.equals(rig)) continue;
                    RigBookings next = new RigBookings(r, this.day);
                    prev.setTypeLoopNext(bookings);
                    prev = next;
                }
                prev.setTypeLoopNext(bookings);
            }
        }
        
        return this.rigBookings.get(rig);
    }
}
