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
 * @date 8th November 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.bookings.intf;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetTimezoneProfiles;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetTimezoneProfilesResponse;

/**
 * Bookings skeleton interface.
 */
public class BookingsSkeleton implements BookingsInterface
{   
    @Override
    public CancelBookingResponse cancelBooking(CancelBooking cancelBooking)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#cancelBooking");
    }

    @Override
    public FindFreeBookingsResponse findFreeBookings(FindFreeBookings findFreeBookings)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#findFreeBookings");
    }

    @Override
    public GetBookingResponse getBooking(GetBooking getBooking)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getBooking");
    }

    @Override
    public CreateBookingResponse createBooking(CreateBooking createBooking)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#createBooking");
    }

    @Override
    public GetBookingsResponse getBookings(GetBookings getBookings)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getBookings");
    }

    @Override
    public GetTimezoneProfilesResponse getTimezoneProfiles(GetTimezoneProfiles profiles)
    {
        throw new UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getTimeProfiles");
    }
}
