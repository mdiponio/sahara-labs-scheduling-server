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
 * @date 11th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Contains the bookings for a rig on a single day in terms of slots.
 */
public class RigBookings
{
    /** The day these booking refer to. */
    private String dayKey;
    
    /** Transient rig whose bookings these belong to. */
    private Rig rig;
    
    /** The list of booking slots of this rig. */
    private MBooking[] slots;
    
    /** The position of the first in use slot. */
    private int startSlot;
    
    /** The position of the last in use slot. */
    private int endSlot;
    
    /** The number of bookings this rig has. */
    private int numBookings;
    
    /** Next matching type bookings in resource loop. */
    private RigBookings typeNext;
    
    /** Next mapping caps bookings in resource loop. */
    private Map<RequestCapabilities, RigBookings> capsNext;
    
    /** Logger. */
    private Logger logger;
    
    public RigBookings(Rig rig, String day)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.rig = rig;
        this.dayKey = day;
        
        this.slots = new MBooking[(24 * 60 * 60) / BookingActivator.TIME_QUANTUM];
        this.startSlot = 0;
        this.endSlot = 0;
        this.numBookings = 0;
    }

    /** 
     * Returns true if the number of rig slots free.
     * 
     * @param start the start slot
     * @param num number of slots
     * @return true if slots are free
     */
    public boolean areSlotsFree(int start, int end)
    {
        if (this.numBookings == 0) return true;
        
        start = start > this.startSlot ? start : this.startSlot;
        end = end < this.endSlot ? end : this.endSlot;
        
        for ( ; start <= end; start++)
        {
            if (this.slots[start] != null) return false;
        }
        
        return true;
    }
    
    /**
     * Returns true if the range is free.
     * 
     * @param range slots range
     * @return true if free
     */
    public boolean areSlotsFree(MRange range)
    {
        return this.areSlotsFree(range.getStartSlot(), range.getEndSlot());
    }
    
    /**
     * Gets the free slots of this rig. The response is in the form of 2D
     * array containing [slot start][number free].
     * 
     * @return free slots
     */
    public List<MRange> getFreeSlots()
    {
        return this.getFreeSlots(1);
    }
    
    /**
     * Gets the free booking holes for this rig. A hole is the threshold number of 
     * free slots. The minimum threshold is 1 (i.e. just free slots).
     * 
     * @return thres threshold for a hole
     * @return free slots
     */
    public List<MRange> getFreeSlots(int thres)
    {
        List<MRange> free = new ArrayList<MRange>();
        
        if (this.numBookings == 0)
        {
            free.add(new MRange(0, this.slots.length - 1, this.dayKey));
            return free;
        }
        
        if (thres < 1) thres = 1;
        
        if (this.startSlot - 1 >= thres)
        {
            free.add(new MRange(0, this.startSlot - 1, this.dayKey));
        }
        
        int num = this.numBookings - 1;
        int fs = this.slots[this.startSlot].getEndSlot() + 1;
        int ef;
        while (num > 0)
        {
            ef = fs;
            while (ef < this.endSlot && this.slots[++ef] == null);
            
            /* We have reached the end of the slots. */
            if (ef == this.slots.length) break;
 
            num--;
            if (ef - fs >= thres) free.add(new MRange(fs, ef - 1, this.dayKey));
            fs = this.slots[ef].getEndSlot() + 1;
        }
        
        if (this.slots.length - this.endSlot > thres)
        {
            free.add(new MRange(this.endSlot + 1, this.slots.length - 1, this.dayKey));
        }

        return free;
    }
    
    /**
     * Commits a booking to this rig. 
     * 
     * @param booking booking to commit
     * @return true if successfully committed
     */
    public boolean commitBooking(MBooking booking)
    {
        /* Sanity check to make sure the rig is free for this time. */
        if (!this.areSlotsFree(booking.getStartSlot(), booking.getEndSlot()))
        {
            this.logger.error("Tried to commit a booking to a non-free rig.");
            return false;
        }
        
        int s = booking.getStartSlot();
        if (this.numBookings == 0)
        {
            this.startSlot = s;
            this.endSlot = booking.getEndSlot();
        }
        else
        {
            if (this.startSlot > s) this.startSlot = s;
            if (this.endSlot < booking.getEndSlot()) this.endSlot = booking.getEndSlot();
        }
        this.numBookings++;
        
        for ( ; s <= booking.getEndSlot(); s++)
        {
            this.slots[s] = booking;
        }
       
        return true;
    }
    
    /**
     * Returns true if this rig has the supplied booking.
     * 
     * @param booking booking to find
     * @return true if has booking
     */
    public boolean hasBooking(MBooking booking)
    {
        if (this.slots[booking.getStartSlot()] == null) return false;
        return this.slots[booking.getStartSlot()].equals(booking);
    }
    
    /**
     * Removes a booking from a rig.
     * 
     * @param booking to remove
     * @return true if booking removed
     */
    public boolean removeBooking(MBooking booking)
    {
        /* Sanity check to make sure this rig has the booking. */
        if (!this.hasBooking(booking))
        {
            this.logger.error("Tried to remove a booking from a rig that doesn't have the booking.");
            return false;
        }
        for (int start = booking.getStartSlot(); start <= booking.getEndSlot(); start++)
        {
            this.slots[start] = null;
        }
        
        if (--this.numBookings != 0)
        {
            /* Find the index of the first booking. */
            int s;
            for (s = 0; s < this.slots.length; s++)
            {
                if (this.slots[s] != null) break;
            }
            this.startSlot = s;
            
            /* Find the index of the last booking. */
            for (s = this.slots.length - 1; s >= 0; s--)
            {
                if (this.slots[s] != null) break;
            }
            this.endSlot = s;
        }
        
        return true;
    }
    
    public int getNumBookings()
    {
        return this.numBookings;
    }
     
    public Rig getRig()
    {
        return this.rig;
    }
    
    public String getDayKey()
    {
        return this.dayKey;
    }

    public RigBookings getTypeLoopNext()
    {
        return this.typeNext;
    }

    public void setTypeLoopNext(RigBookings next)
    {
        this.typeNext = next;
    }

    public RigBookings getCapsLoopNext(RequestCapabilities caps)
    {
        return this.capsNext.get(caps);
    }

    public void setCapsLoopNext(RequestCapabilities caps, RigBookings next)
    {
        this.capsNext.put(caps, next);
    }
}   