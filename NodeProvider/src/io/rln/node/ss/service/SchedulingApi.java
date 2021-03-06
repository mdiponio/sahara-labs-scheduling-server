/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 8th February 2017
 */

package io.rln.node.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import io.rln.node.ss.NodeProviderActivator;

/**
 * API for Scheduling managing operations.
 */
public class SchedulingApi extends ApiBase
{
    private static final long serialVersionUID = 1L;
    
    /** Path this API is accessible on. */
    public static final String PATH = "/scheduling";
    
    public SchedulingApi(List<String> hosts)
    {
        super(hosts);
    }

    /**
     * The HEAD method reloads the booking engine.
     */
    @Override
    public void doHead(HttpServletRequest req, HttpServletResponse resp)
    {
        this.logger.info("Reloading booking engine.");
        NodeProviderActivator.getBookingEngine().reloadEngine();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    
    /**
     * The POST method changes node associations in the booking engine.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
        String name = req.getParameter("node");
        if (name == null)
        {
            this.logger.warn("Not accepting node association request, name parameter not specified.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        RigDao dao = null;
        try
        {
            dao = new RigDao();
                
            Rig node = dao.findByName(name);
            if (node == null)
            {
                this.logger.info("Cannot reload node associations, node with ID " + name + " not found.");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (node.isInSession() && node.getSession() != null)
            {
                this.logger.info("Node " + node.getName() + " is changing assocation but it is currently in session, finishing session for user " +
                        node.getSession().getUserName() + ".");
                NodeProviderActivator.getSession().finishSession(node.getSession(), "Node is changing type association.", dao.getSession());
            }
            
            this.logger.info("Changing association for node with ID " + node.getName());
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.REMOVED, node, dao.getSession());
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.REGISTERED, node, dao.getSession());
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }
}
