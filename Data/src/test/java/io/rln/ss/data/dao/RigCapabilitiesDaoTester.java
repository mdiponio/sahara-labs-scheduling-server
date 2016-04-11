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
 * @date 4th January 2010
 */
package io.rln.ss.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.GenericDao;
import io.rln.ss.data.dao.RigCapabilitiesDao;
import io.rln.ss.data.entities.MatchingCapabilities;
import io.rln.ss.data.entities.MatchingCapabilitiesId;
import io.rln.ss.data.entities.RequestCapabilities;
import io.rln.ss.data.entities.RigCapabilities;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link RigCapabilitiesDao} class.
 */
public class RigCapabilitiesDaoTester extends TestCase
{
    /** Object of class under test. */
    private RigCapabilitiesDao dao;
    
    @Before
    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new RigCapabilitiesDao();
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#findCapabilites(java.lang.String)}.
     */
    @Test
    public void testFindCapabilites()
    {
        Session ses = DataActivator.getNewSession();
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        ses.beginTransaction();
        ses.save(caps);
        ses.getTransaction().commit();
        ses.close();
        
        RigCapabilities ld = this.dao.findCapabilites("a,b,c,d,e,f");
        assertNotNull(ld);
        assertEquals(caps.getId(), ld.getId());
        this.dao.delete(ld);
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#findCapabilites(java.lang.String)}.
     */
    @Test
    public void testFindCapabilitiesNotFound()
    {
        RigCapabilities cap = this.dao.findCapabilites("b,c,d");
        assertNull(cap);
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#addCapabilities(java.lang.String)}.
     */
    @Test
    public void testAddCapabilities()
    {
        /* Add the request capabilities. */
        RequestCapabilities req[] = new RequestCapabilities[10];
        GenericDao<RequestCapabilities> reqDao = new GenericDao<RequestCapabilities>(this.dao.getSession(), 
                RequestCapabilities.class);
        req[0] = new RequestCapabilities("a,b");
        req[1] = new RequestCapabilities("b,c");
        req[2] = new RequestCapabilities("a,c");
        req[3] = new RequestCapabilities("c,f");
        req[4] = new RequestCapabilities("d,f");
        req[5] = new RequestCapabilities("f");
        req[6] = new RequestCapabilities("a,c,f");
        req[7] = new RequestCapabilities("x,y");
        req[8] = new RequestCapabilities("xy,x");
        req[9] = new RequestCapabilities("x,y,z");
        for (int i = 0; i < req.length; i++)
        {
            req[i] = reqDao.persist(req[i]);
        }
        
        String capsStr = "f, d, a, b ";
        RigCapabilities caps = this.dao.addCapabilities(capsStr);
        assertNotNull(caps);
        assertEquals("a,b,d,f", caps.getCapabilities());

        Set<MatchingCapabilities> matches = caps.getMatchingCapabilitieses();
        List<String> matchingReq = new ArrayList<String>();
        for (MatchingCapabilities m : matches)
        {
            matchingReq.add(m.getRequestCapabilities().getCapabilities());
        }
        
        assertEquals(3, matchingReq.size());
        assertTrue(matchingReq.contains("a,b"));
        assertTrue(matchingReq.contains("d,f"));
        assertTrue(matchingReq.contains("f"));
        
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        for (MatchingCapabilities m : matches)
        {
            ses.delete(m);
        }
        ses.getTransaction().commit();
        for (RequestCapabilities r : req)
        {
            reqDao.delete(r);
        }
        this.dao.delete(caps);
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#addCapabilities(java.lang.String)}.
     */
    @Test
    public void testAddOneCapabilities()
    {
        /* Add the request capabilities. */
        RequestCapabilities req = new RequestCapabilities("b");
        GenericDao<RequestCapabilities> reqDao = new GenericDao<RequestCapabilities>(this.dao.getSession(), 
                RequestCapabilities.class);
        reqDao.persist(req);

        String capsStr = "a,b";
        RigCapabilities caps = this.dao.addCapabilities(capsStr);
        assertNotNull(caps);
        assertEquals("a,b", caps.getCapabilities());

        Set<MatchingCapabilities> matches = caps.getMatchingCapabilitieses();
        assertEquals(1, matches.size());
        MatchingCapabilities mc = matches.iterator().next();
        assertNotNull(mc);
        assertEquals(mc.getId().getRequestCapabilities(), req.getId().longValue());
        assertEquals(mc.getId().getRigCapabilities(), caps.getId().longValue());
        
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        for (MatchingCapabilities m : matches)
        {
            ses.delete(m);
        }
        ses.getTransaction().commit();
        ses.delete(req);
        this.dao.delete(caps);
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#addCapabilities(java.lang.String)}.
     */
    @Test
    public void testAddNoReqCapabilities()
    {
        String capsStr = "a,b";
        RigCapabilities caps = this.dao.addCapabilities(capsStr);
        assertNotNull(caps);
        assertEquals("a,b", caps.getCapabilities());

        Set<MatchingCapabilities> matches = caps.getMatchingCapabilitieses();
        assertEquals(0, matches.size());
        this.dao.delete(caps);
    }
    
    @Test
    public void testDelete()
    {
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        RequestCapabilities req1 = new RequestCapabilities("a,b");
        ses.persist(req1);
        RequestCapabilities req2 = new RequestCapabilities("c,d");
        ses.persist(req2);
        RigCapabilities rig = new RigCapabilities("a,b,c,d");
        ses.persist(rig);
        MatchingCapabilities match1 = new MatchingCapabilities(new MatchingCapabilitiesId(rig.getId(), req1.getId()), req1, rig);
        ses.persist(match1);
        MatchingCapabilities match2 = new MatchingCapabilities(new MatchingCapabilitiesId(rig.getId(), req2.getId()), req2, rig);
        ses.persist(match2);
        ses.getTransaction().commit();
        
        this.dao.delete(rig.getId());
        assertNull(this.dao.get(rig.getId()));
        
        ses.refresh(req1);
        assertEquals(0, req1.getMatchingCapabilitieses().size());
        ses.refresh(req2);
        assertEquals(0, req2.getMatchingCapabilitieses().size());
        
        ses.beginTransaction();
        ses.delete(req1);
        ses.delete(req2);
        ses.getTransaction().commit();
    }

}
