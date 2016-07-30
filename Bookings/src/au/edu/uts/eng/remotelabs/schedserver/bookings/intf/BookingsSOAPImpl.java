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
 * @date 9th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.intf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingResponseType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingSlotListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingsRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CancelBookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.CreateBookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindBookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetTimezoneProfiles;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetTimezoneProfilesResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.SlotState;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.SystemTimezoneType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.TimePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.TimezoneType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.impl.BookingsServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation.BestFit;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod.BookingSlot;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Bookings interface implementation.
 */
public class BookingsSOAPImpl implements BookingsSOAP
{
    /** Logger. */
    private Logger logger;
    
    public BookingsSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public CreateBookingResponse createBooking(CreateBooking createBooking)
    {
        /* --------------------------------------------------------------------
         * -- Read request parameters.                                       --
         * -------------------------------------------------------------------- */
        CreateBookingType request = createBooking.getCreateBooking();
        String debug = "Received " + this.getClass().getSimpleName() + "#createBooking with params: ";
        
        UserIDType uid = request.getUserID();
        debug += " user ID=" + uid.getUserID() + ", user namespace=" + uid.getUserNamespace() + ", user name=" + 
            uid.getUserName() + " user QName=" + uid.getUserQName();
        
        BookingType booking = request.getBooking();
        int pid = booking.getPermissionID().getPermissionID();
        debug += ", permission=" + pid;
        
        Calendar start = booking.getStartTime();
        Calendar end = booking.getEndTime();
        this.dstHack(start);
        this.dstHack(end);
        
        debug += ", start=" + start.getTime() + ", end=" + end.getTime();
        
        debug += ", send notification=" + request.getSendNotification() + ", notification time zone=" + 
                request.getNotificationTimezone() +  '.';
        this.logger.debug(debug);
        
        
        
        /* --------------------------------------------------------------------
         * -- Generate default response parameters.                          --
         * -------------------------------------------------------------------- */
        CreateBookingResponse response = new CreateBookingResponse();
        BookingResponseType status = new BookingResponseType();
        status.setSuccess(false);
        response.setCreateBookingResponse(status);
        
        Session ses = DataAccessActivator.getNewSession();
        try
        {
            /* ----------------------------------------------------------------
             * -- Load the user.                                             --
             * ---------------------------------------------------------------- */
            User user = this.getUserFromUserID(uid, ses);
            if (user == null)
            {
                this.logger.info("Unable to create a booking because the user has not been found. Supplied " +
                        "credentials ID=" + uid.getUserID() + ", namespace=" + uid.getUserNamespace() + ", " +
                        "name=" + uid.getUserName() + '.');
                status.setFailureReason("User not found.");
                return response;
            }
            
            /* ----------------------------------------------------------------
             * -- Load the permission.                                       --
             * ---------------------------------------------------------------- */
            ResourcePermission perm = new ResourcePermissionDao(ses).get(Long.valueOf(pid));
            if (perm == null)
            {
                this.logger.info("Unable to create a booking because the permission has not been found. Supplied " +
                        "permission ID=" + pid + '.');
                status.setFailureReason("Permission not found.");
                return response;
            }
            
            if (!this.checkPermission(user, perm))
            {
                this.logger.info("Unable to create a booking because the user " + user.getNamespace() + ':' + 
                        user.getName() + " does not have permission " + pid + '.');
                status.setFailureReason("Does not have permission.");
                return response;
            }
            
            BookingOperation operation = new BookingsServiceImpl().createBooking(start, end, user, perm, ses);
            status.setSuccess(operation.successful());
            status.setFailureReason(operation.getFailureReason());
            if (operation.successful())
            {
                BookingIDType bid = new BookingIDType();
                bid.setBookingID(operation.getBooking().getId().intValue());
                status.setBookingID(bid);
            }
            else if (operation.getBestFits().size() > 0)
            {
                BookingListType bestFits = new BookingListType();
                status.setBestFits(bestFits);
                
                PermissionIDType pt = new PermissionIDType();
                pt.setPermissionID(perm.getId().intValue());
                
                for (BestFit bf :  operation.getBestFits())
                {
                    BookingType bt = new BookingType();
                    bt.setStartTime(bf.getStart());
                    bt.setEndTime(bf.getEnd());
                    bt.setPermissionID(pt);
                    bestFits.addBookings(bt);
                }
            }            
        }
        finally
        {
            ses.close();
        }

        return response;
    }

    @Override
    public CancelBookingResponse cancelBooking(CancelBooking cancelBooking)
    {
        /* --------------------------------------------------------------------
         * -- Read request parameters.                                       --
         * -------------------------------------------------------------------- */
        CancelBookingType request = cancelBooking.getCancelBooking();
        String debug = "Received " + this.getClass().getSimpleName() + "#cancelBooking with params: ";
        
        UserIDType uid = request.getUserID();
        debug += " user ID=" + uid.getUserID() + ", user namespace=" + uid.getUserNamespace() + ", user name=" + 
            uid.getUserName() + " user QName=" + uid.getUserQName();
        
        int bid = request.getBookingID().getBookingID();
        debug += ", booking ID=" + bid + ", reason=" + request.getReason() + '.';
        this.logger.debug(debug);
        
        /* --------------------------------------------------------------------
         * -- Generate valid, blank request parameters.                      --
         * -------------------------------------------------------------------- */
        CancelBookingResponse response = new CancelBookingResponse();
        BookingResponseType status = new BookingResponseType();
        status.setSuccess(false);
        response.setCancelBookingResponse(status);
        
        Session ses = DataAccessActivator.getNewSession();
        try
        {
            /* ----------------------------------------------------------------
             * -- Load the user.                                             --
             * ---------------------------------------------------------------- */
            User user = this.getUserFromUserID(uid, ses);
            if (user == null)
            {
                this.logger.info("Unable to cancel a booking because the user has not been found. Supplied " +
                        "credentials ID=" + uid.getUserID() + ", namespace=" + uid.getUserNamespace() + ", " +
                        "name=" + uid.getUserName() + '.');
                status.setFailureReason("User not found.");
                return response;
            }
            
            /* ----------------------------------------------------------------
             * -- Load the booking.                                          --
             * ---------------------------------------------------------------- */
            BookingsDao dao = new BookingsDao(ses);
            Bookings booking = dao.get(Long.valueOf(bid));
            if (booking == null)
            {
                this.logger.info("Unable to delete a booking because the booking with ID " + bid + " was not " +
                		"been found.");
                status.setFailureReason("Booking not found.");
                return response;
            }
            
            /* ----------------------------------------------------------------
             * -- Check whether the user has permission to cancel it.        --
             * ---------------------------------------------------------------- */
            String persona = user.getPersona();
            if (User.USER.equals(persona) && !user.getId().equals(booking.getUser().getId()))
            {
                /* If the user is a user they can only cancel the booking if it
                 * is for them. */
                this.logger.info("Unable to delete booking because the user " + user.getNamespace() + ':' + 
                        user.getName() + " does not own the booking.");
                status.setFailureReason("User does not own booking.");
                return response;
            }
            else if (User.ACADEMIC.equals(persona) && !user.getId().equals(booking.getUser().getId()))
            {
                /* An academic may cancel bookings for classes they own. */
                boolean hasPerm = false;
                UserClass userClass = booking.getResourcePermission().getUserClass();
                
                Iterator<AcademicPermission> apIt = user.getAcademicPermissions().iterator();
                while (apIt.hasNext())
                {
                    AcademicPermission ap = apIt.next();
                    if (ap.getUserClass().getId().equals(userClass.getId()) && ap.isCanKick())
                    {
                        hasPerm = true;
                        break;
                    }    
                }
                
                if (!hasPerm)
                {
                    this.logger.info("Unable to delete booking because the user " + user.getNamespace() + ':' + 
                            user.getName() + " does not own or have academic permission to cancel it.");
                    status.setFailureReason("User does not own or have academic permission to cancel.");
                    return response;
                }
                
                this.logger.debug("Academic " + user.getNamespace() + ':' + user.getName() + " has permission to " +
                		"cancel booking " + bid + " from user class" + userClass.getName() + '.');
            }
            else if (User.ADMIN.equals(persona))
            {
                this.logger.debug("Admin " + user.getNamespace() + ':' + user.getName() + " canceling booking " + 
                        bid + '.');
            }
            else if (!(User.ACADEMIC.equals(persona) || User.ADMIN.equals(persona) || User.USER.equals(persona)))
            {
                this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " with persona " + 
                        user.getPersona() + " is attempting to cancel booking " + bid + ". They have no permission.");
                status.setFailureReason("No permission.");
                return response;
            }

            /* ----------------------------------------------------------------
             * -- Actually cancel the booking.                               --
             * ---------------------------------------------------------------- */
            BookingOperation opResp = new BookingsServiceImpl().cancelBooking(booking, request.getReason(), 
                    user.getId().equals(booking.getUser().getId()), dao.getSession());
            status.setSuccess(opResp.successful());
            status.setFailureReason(opResp.getFailureReason());
        }
        finally
        {
            ses.close();
        }
        
        return response;
    }

    @Override
    public FindFreeBookingsResponse findFreeBookings(FindFreeBookings findFreeBookings)
    {
        /* --------------------------------------------------------------------
         * -- Read request parameters.                                       --
         * -------------------------------------------------------------------- */
        FindBookingSlotType request = findFreeBookings.getFindBookingSlots();
        String debug = "Received " + this.getClass().getSimpleName() + "#findFreeBookings with params: ";
        
        UserIDType uid = request.getUserID();
        debug += " user ID=" + uid.getUserID() + ", user namespace=" + uid.getUserNamespace() + ", user name=" + 
                uid.getUserName() + " user QName=" + uid.getUserQName();
        
        PermissionIDType reqPermission = request.getPermissionID();
        if (reqPermission != null) debug += " permission ID=" + reqPermission.getPermissionID();
        
        ResourceIDType reqResource = request.getResourceID();
        if (reqResource != null) debug += " resource type= " + reqResource.getType() + ", ID=" + 
                request.getResourceID().getResourceID() + ", name=" + reqResource.getResourceName();
        
        Calendar reqStart = request.getPeriod().getStartTime();
        Calendar reqEnd = request.getPeriod().getEndTime();
        this.dstHack(reqStart);
        this.dstHack(reqEnd);
        
        debug += " period start=" + reqStart.getTime() + ", period end=" + reqEnd.getTime();
        this.logger.debug(debug);
        
        /* --------------------------------------------------------------------
         * -- Generate valid, blank request parameters.                      --
         * -------------------------------------------------------------------- */
        FindFreeBookingsResponse response = new FindFreeBookingsResponse();
        BookingSlotListType slots = new BookingSlotListType();
        response.setFindFreeBookingsResponse(slots);
        
        PermissionIDType permission = new PermissionIDType();
        slots.setPermissionID(permission);
        
        ResourceIDType resource = new ResourceIDType();
        resource.setType("TYPE");
        slots.setResourceID(resource);

        Session db = DataAccessActivator.getNewSession();
        try
        {
            /* ----------------------------------------------------------------
             * -- Load the user.                                             --
             * ---------------------------------------------------------------- */
            User user = this.getUserFromUserID(uid, db);
            if (user == null)
            {
                this.logger.info("Unable to provide free times because the user has not been found. Supplied " +
                        "credentials ID=" + uid.getUserID() + ", namespace=" + uid.getUserNamespace() + ", " +
                        "name=" + uid.getUserName() + '.');
                return response;
            }
            
            /* ----------------------------------------------------------------
             * -- Load the permission.                                             --
             * ---------------------------------------------------------------- */
            ResourcePermission perm = null;
            if (reqPermission != null)
            {
                ResourcePermissionDao resPermissionDao = new ResourcePermissionDao(db);
                perm = resPermissionDao.get(Long.valueOf(reqPermission.getPermissionID()));
            }
            else if (reqResource != null)
            {
                Criteria qu = db.createCriteria(ResourcePermission.class);
                
                /* Add resource restrictions. */
                qu.add(Restrictions.eq("type", reqResource.getType()));
                if (ResourcePermission.TYPE_PERMISSION.equals(reqResource.getType()))
                {
                    if (reqResource.getResourceID() > 0)
                    {
                        qu.add(Restrictions.eq("rigType.id", Long.valueOf(reqResource.getResourceID())));
                    }
                    if (reqResource.getResourceName() != null)
                    {
                        qu.createCriteria("rigType")
                          .add(Restrictions.eq("name", reqResource.getResourceName()));
                    }
                }
                else if (ResourcePermission.RIG_PERMISSION.equals(reqResource.getType()))
                {
                    if (reqResource.getResourceID() > 0)
                    {
                        qu.add(Restrictions.eq("rig.id", Long.valueOf(reqResource.getResourceID())));
                    }
                    if (reqResource.getResourceName() != null)
                    {
                        qu.createCriteria("rig")
                          .add(Restrictions.eq("name", reqResource.getResourceName()));
                    }
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(reqResource.getType()))
                {
                    if (reqResource.getResourceID() > 0)
                    {
                        qu.add(Restrictions.eq("requestCapabilities.id", Long.valueOf(reqResource.getResourceID())));
                    }
                    if (reqResource.getResourceName() != null)
                    {
                        qu.createCriteria("requestCapabilities")
                          .add(Restrictions.eq("capabilities", reqResource.getResourceName()));
                    }
                }
                else
                {
                    this.logger.warn("Unable to provide free times because resource type " + reqResource.getType() +
                            " is not understood.");
                    return response;
                }
                
                List<UserClass> uc = new ArrayList<UserClass>();
                for (UserAssociation assoc : user.getUserAssociations()) uc.add(assoc.getUserClass());

                /* The permission user class must be active and bookable. */
                qu.createCriteria("userClass")
                  .add(Restrictions.eq("active", Boolean.TRUE))
                  .add(Restrictions.eq("bookable", Boolean.TRUE));
                qu.add(Restrictions.in("userClass",  uc));
                
                /* Add order in case we need to count in range, latest first. */
                qu.addOrder(Order.desc("startTime"));

                @SuppressWarnings("unchecked")
                List <ResourcePermission> rpList = qu.list();
                if (rpList.size() == 1)
                {
                    /* One permission so good to go. */
                    perm = rpList.get(0);
                }
                else if (rpList.size() > 1)
                {
                    Date rsd = reqStart.getTime();
                    Date red = reqEnd.getTime();
                    /* Multiple permissions so we take the permission in time range. */
                    for (ResourcePermission rp : rpList)
                    {
                        if (rp.getStartTime().before(rsd) && rp.getExpiryTime().after(rsd) ||
                            rp.getStartTime().before(red) && rp.getExpiryTime().after(red) ||
                            rp.getStartTime().after(rsd)  && rp.getExpiryTime().before(red))
                        {
                            perm = rp;
                            break;
                        }
                    }
                    
                    /* Nothing in range so it doesn't matter which resource we give. */
                    if (perm == null) perm = rpList.get(0);
                }
            }
            
            /* If no permission is specified, either it doesn't exist or it wasn't
             * specified. Either way, we can't provide any information. */
            if (perm == null)
            {
                this.logger.warn("Unable to provide free times because no permission or resource has been specified " +
                		"or found to provide the free times of.");
                return response;
            }
            
            
            /* There is a permission, but the user doesn't have it. */
            if (!this.checkPermission(user, perm))
            {
                this.logger.warn("Unable to provide free times to user " + user.getNamespace() + ':' + user.getName() +
                        " because they do not have permission " + perm.getId() + ".");
                return response;
            }
            
            /* ----------------------------------------------------------------
             * -- Populate the response with permission parameters.          --
             * ---------------------------------------------------------------- */
            permission.setPermissionID(perm.getId().intValue());
            resource.setType(perm.getType());
            if (ResourcePermission.RIG_PERMISSION.equals(perm.getType()))
            {
                Rig rig = perm.getRig();
                if (rig == null)
                {
                    this.logger.warn("Unable to provide free times because the rig permission with ID=" + perm.getId() +
                            " is not set with a rig.");
                    return response;
                }
                resource.setResourceID(rig.getId().intValue());
                resource.setResourceName(rig.getName());
            }
            else if (ResourcePermission.TYPE_PERMISSION.equals(perm.getType()))
            {
                RigType rigType = perm.getRigType();
                if (rigType == null)
                {
                    this.logger.warn("Unable to provide free times because the rig type permission with ID=" + perm.getId() +
                        " is not set with a rig type.");
                    return response;
                }
                resource.setResourceID(rigType.getId().intValue());
                resource.setResourceName(rigType.getName());
            }
            else if (ResourcePermission.CAPS_PERMISSION.equals(perm.getType()))
            {
                RequestCapabilities caps = perm.getRequestCapabilities();
                if (caps == null)
                {
                    this.logger.warn("Unable to provide free times because the request capabilities permission with ID=" 
                            + perm.getId() + " is not set with a request capabilities.");
                    return response;
                }
                resource.setResourceID(caps.getId().intValue());
                resource.setResourceName(caps.getCapabilities());
            }
            else
            {
                this.logger.warn("Unable to provide free times because the permission with ID=" + perm.getId() + 
                        " has type '" + perm.getType() + "' which is not understood.");
                return response;
            }
            
            BookingsPeriod period = new BookingsServiceImpl().getFreeBookings(reqStart, reqEnd, perm, db);
            for (BookingSlot slot : period.getSlots())
            {
                TimePeriodType tp = new TimePeriodType();
                tp.setStartTime(slot.getStart());
                tp.setEndTime(slot.getEnd());
                
                BookingSlotType bst = new BookingSlotType();
                bst.setSlot(tp);
                bst.setState(SlotState.Factory.fromValue(slot.getState()));
                
                slots.addBookingSlot(bst);
            }
        }
        finally
        {
            db.close();
        }
        
        return response;
    }

    @Override
    public GetBookingResponse getBooking(GetBooking getBooking)
    {
        BookingRequestType request = getBooking.getGetBooking();
        
        int bid = request.getBookingID().getBookingID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getBooking with params: booking ID=" + bid);

        GetBookingResponse response = new GetBookingResponse();
        BookingType booking = new BookingType();
        booking.setBookingID(-1);
        ResourceIDType resource = new ResourceIDType();
        resource.setType(ResourcePermission.RIG_PERMISSION);
        booking.setBookedResource(resource);
        PermissionIDType permission = new PermissionIDType();
        booking.setPermissionID(permission);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        booking.setStartTime(cal);
        booking.setEndTime(cal);
        response.setGetBookingResponse(booking);
        
        BookingsDao dao = new BookingsDao();
        try
        {
            Bookings b = dao.get(Long.valueOf(bid));
            if (b == null)
            {
                this.logger.warn("Booking with identifier '" + bid + "' was not found.");
                return response;
            }
            
            this.populateBookingType(booking, b);
        }
        finally
        {
            dao.closeSession();
        }
        
        
        return response;
    } 

    @Override
    public GetBookingsResponse getBookings(GetBookings getBookings)
    {
        BookingsRequestType request = getBookings.getGetBookings();
        String debug = "Received " + this.getClass().getSimpleName() + "#getBookings with params:";
        if (request.getUserID() != null) debug += " user ID=" + request.getUserID().getUserID() + ", user name=" +
                request.getUserID().getUserName() + ", user namespace=" + request.getUserID().getUserNamespace() +
                " user QName=" + request.getUserID().getUserQName();
        if (request.getPermissionID() != null) debug += " permission ID=" + request.getPermissionID().getPermissionID();
        if (request.getResourceID() != null) debug += "resource type=" + request.getResourceID().getType() + 
                ", resource ID=" + request.getResourceID().getResourceID() + ", resource name=" + 
                request.getResourceID().getResourceName();
        debug += " show cancelled=" + request.showCancelled() + " show finished=" + request.showFinished();
        this.logger.debug(debug);
        
        GetBookingsResponse response = new GetBookingsResponse();
        BookingListType bookings = new BookingListType();
        response.setGetBookingsResponse(bookings);
        
        BookingsDao dao = new BookingsDao();
        try
        {
            Session ses = dao.getSession();
            Criteria cri = ses.createCriteria(Bookings.class);
            
            /* If user specificed, add that to query. */
            User user = null;
            if (request.getUserID() != null && (user = this.getUserFromUserID(request.getUserID(), ses)) != null)
            {
                cri.add(Restrictions.eq("user", user));
            }
            
            /* If permission was specified, add that to query. */
            if (request.getPermissionID() != null)
            {
                cri.add(Restrictions.eq("resourcePermission.id", Long.valueOf(request.getPermissionID().getPermissionID())));
            }
            
            /* If resource was specified, add that to query. */
            ResourceIDType rid = request.getResourceID();
            if (rid != null)
            {
                if (ResourcePermission.RIG_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION));
                    if (rid.getResourceID() > 0) cri.add(Restrictions.eq("rig.id", Long.valueOf(rid.getResourceID())));
                    if (rid.getResourceName() != null)
                    {
                        cri.createCriteria("rig").add(Restrictions.eq("name", rid.getResourceName()));
                    }
                }
                else if (ResourcePermission.TYPE_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.TYPE_PERMISSION));
                    if (rid.getResourceID() > 0) cri.add(Restrictions.eq("rigType.id", Long.valueOf(rid.getResourceID())));
                    if (rid.getResourceName() != null)
                    {
                        cri.createCriteria("rigType").add(Restrictions.eq("name", rid.getResourceName()));
                    }
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(rid.getType()))
                {
                    cri.add(Restrictions.eq("resourceType", ResourcePermission.CAPS_PERMISSION));
                    if (rid.getResourceID() > 0)
                    {
                        cri.add(Restrictions.eq("requestCapabilities.id", Long.valueOf(rid.getResourceID())));
                    }
                    if (rid.getResourceName() != null)
                    {
                        cri.createCriteria("requestCapabilities")
                            .add(Restrictions.eq("capabilities", rid.getResourceName()));
                    }
                }
                else
                {
                    this.logger.warn("Not added a resource restriction to existing booking search because the " +
                    		"resourece type '" + rid.getType() + "' is not one of '" + 
                    		ResourcePermission.RIG_PERMISSION + "' '" + ResourcePermission.TYPE_PERMISSION + "' '" + 
                    		ResourcePermission.CAPS_PERMISSION + "'.");
                }
            }
            
            /* Other constraints specified. */
            if (!request.showCancelled() && !request.showFinished())
            {
                cri.add(Restrictions.eq("active", Boolean.TRUE));
            }
            else if (!request.showFinished())
            {
                cri.add(Restrictions.or(
                        Restrictions.isNotNull("cancelReason"),
                        Restrictions.eq("active", Boolean.TRUE)));
            }
            else if (!request.showCancelled())
            {
                cri.add(Restrictions.isNull("cancelReason"));
            }
            
            /* Order the results be booking start time. */
            cri.addOrder(Order.asc("startTime"));
            
            @SuppressWarnings("unchecked")
            List<Bookings> list = cri.list();
            for (Bookings booking : list)
            {
                bookings.addBookings(this.populateBookingType(new BookingType(), booking));
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    @Override
    public GetTimezoneProfilesResponse getTimezoneProfiles(GetTimezoneProfiles profiles)
    {
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getTimezoneProfiles.");
        
        /* Response parameters. */
        GetTimezoneProfilesResponse response = new GetTimezoneProfilesResponse();
        SystemTimezoneType sysTz = new SystemTimezoneType();
        response.setGetTimezoneProfilesResponse(sysTz);
        
        /* Populate the default time zone information. */
        TimeZone defaultTz = TimeZone.getDefault();
        sysTz.setSystemTimezone(defaultTz.getID());
        
        int off = defaultTz.getOffset(System.currentTimeMillis()) / 1000;
        sysTz.setOffsetFromUTC(off);
        
        Map<String, TimezoneType> tzList = new TreeMap<String, TimezoneType>();
        for (String tzId : TimeZone.getAvailableIDs())
        {
            /* Remove time zones that won't make sense to *most* users. */
            if (tzId.startsWith("Etc")) continue;
            if (!tzId.contains("/")) continue;
            if (tzId.startsWith("SystemV")) continue;
            if (tzId.startsWith("Antarctica")) continue;
            if (tzId.startsWith("Arctic")) continue;
            if (tzId.startsWith("Chile")) continue;
            if (tzId.startsWith("Mideast")) continue;
            if (tzId.startsWith("Mexico")) continue;

            TimeZone tz = TimeZone.getTimeZone(tzId);
            TimezoneType tzt = new TimezoneType();
            tzt.setTimezone(tzId);

            tzt.setOffsetFromSystem(tz.getOffset(System.currentTimeMillis()) / 1000 - off);
            tzList.put(tzId, tzt);
        }
        
        for (TimezoneType tzt : tzList.values()) sysTz.addSupportedTimezones(tzt);
        return response;
    }
    
    /**
     * Checks whether the request has the specified permission. 
     * 
     * @param user the user to check
     * @param perm the permission to ensure the user has
     * @return true if the request has the appropriate permission
     */
    private boolean checkPermission(User user, ResourcePermission perm)
    {
        UserClass userClass = perm.getUserClass();
        for (UserAssociation assoc : user.getUserAssociations())
        {
            if (assoc.getUserClass().getId().equals(userClass.getId()))
            {
                return true;
            }
        }
        
        this.logger.info("User " + user.getNamespace() + ':' + user.getName() + " is not a member of user class " +
        		userClass.getName() + '.');
        return false;
    }
    
    /**
     * Populates an interface booking type with information from a booking.
     * 
     * @param bookingType booking type
     * @param bookingRecord booking record
     */
    private BookingType populateBookingType(BookingType bookingType, Bookings bookingRecord)
    {
        bookingType.setBookingID(bookingRecord.getId().intValue());
        
        ResourceIDType resource = bookingType.getBookedResource();
        if (resource == null)
        {
            resource = new ResourceIDType();
            bookingType.setBookedResource(resource);
        }
        
        resource.setType(bookingRecord.getResourceType());
        if (ResourcePermission.RIG_PERMISSION.equals(bookingRecord.getResourceType()))
        {
            resource.setResourceID(bookingRecord.getRig().getId().intValue());
            resource.setResourceName(bookingRecord.getRig().getName());
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(bookingRecord.getResourceType()))
        {
            resource.setResourceID(bookingRecord.getRigType().getId().intValue());
            resource.setResourceName(bookingRecord.getRigType().getName());
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(bookingRecord.getResourceType()))
        {
            resource.setResourceID(bookingRecord.getRequestCapabilities().getId().intValue());
            resource.setResourceName(bookingRecord.getRequestCapabilities().getCapabilities());
        }
        
        PermissionIDType permission = bookingType.getPermissionID();
        if (permission == null) 
        {
            permission = new PermissionIDType();
            bookingType.setPermissionID(permission);
        }
        permission.setPermissionID(bookingRecord.getResourcePermission().getId().intValue());
        
        Calendar start = Calendar.getInstance();
        start.setTime(bookingRecord.getStartTime());
        bookingType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.setTime(bookingRecord.getEndTime());
        bookingType.setEndTime(end);
        bookingType.setDuration(bookingRecord.getDuration());
        
        if (bookingRecord.getResourcePermission().getDisplayName() == null)
        {
            bookingType.setDisplayName(resource.getResourceName());
        }
        else
        {
            bookingType.setDisplayName(bookingRecord.getResourcePermission().getDisplayName());
        }
        
        bookingType.setIsFinished(!bookingRecord.isActive());
        if (bookingRecord.getCancelReason() == null)
        {
            bookingType.setIsCancelled(false);
        }
        else
        {
            bookingType.setIsCancelled(true);
            bookingType.setCancelReason(bookingRecord.getCancelReason());
        }
        
        if (bookingRecord.getCodeReference() != null)
        {
            bookingType.setCodeReference(bookingRecord.getCodeReference());
        }
        
        return bookingType;
    }

    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uid, Session ses)
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
     * Hack to account for day light savings time.
     * 
     * @param time time to modify
     */
    private void dstHack(Calendar time)
    {
        TimeZone tz = TimeZone.getDefault();
        
        if (tz.inDaylightTime(new Date()) && !tz.inDaylightTime(time.getTime()))
        {
            time.add(Calendar.MILLISECOND, tz.getDSTSavings());
        }
        else if (!tz.inDaylightTime(new Date()) && tz.inDaylightTime(time.getTime()))
        {
            time.add(Calendar.MILLISECOND, -tz.getDSTSavings());

        }
    }
}
