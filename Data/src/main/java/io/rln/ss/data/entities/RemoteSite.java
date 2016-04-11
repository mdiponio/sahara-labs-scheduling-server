/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 18th May 2011
 */
package io.rln.ss.data.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Remote site entity which maps to a remote site list.
 */
@Entity
@Table(name = "remote_site")
public class RemoteSite implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -4281815890358921326L;

    /** Record Identifier. */
    private long id;
    
    /** Identification GUID. */
    private String guid;

    /** The name of the remote site institution. */
    private String name;
    
    /** The namespace of users who originate from this remote site. */
    private String userNamespace;
    
    /** The multi-service address. */
    private String serviceAddress;
    
    /** Whether the site is expected to be online. */ 
    private boolean online;
    
    /** The reason the site is offline. */
    private String offlineReason;
    
    /** The time the site sent a status update. */
    private Date lastPush;
    
    /** Whether a site can view this sites free ranges. */
    private boolean viewer;
    
    /** Whether a site can request permissions for this site. */
    private boolean requestor;
    
    /** Whether this site can authorize permissions for this site. */
    private boolean authorizer;
    
    /** Whether this site is a redirectee of permission requests. */
    private boolean redirectee;
    
    /** The rig types that are from this site. */
    private Set<RigType> rigTypes = new HashSet<RigType>(0);
    
    /** The rigs that are from this site. */
    private Set<Rig> rigs = new HashSet<Rig>(0);
    
    /** The remote permissions that relate to this site. */
    private Set<RemotePermission> remotePermissions = new HashSet<RemotePermission>(0);
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
    
    @Column(name = "guid", unique = true, nullable = false)
    public String getGuid()
    {
        return this.guid;
    }
    
    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    @Column(name = "name", unique = true, nullable = false, length = 255)
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "user_namespace", unique = true, nullable = false)
    public String getUserNamespace()
    {
        return this.userNamespace;
    }

    public void setUserNamespace(String userNamespace)
    {
        this.userNamespace = userNamespace;
    }

    @Column(name = "service_address", nullable = false, length = 1024)
    public String getServiceAddress()
    {
        return this.serviceAddress;
    }
    
    public void setServiceAddress(String serviceAddress)
    {
        this.serviceAddress = serviceAddress;
    }

    @Column(name = "online", nullable = false)
    public boolean isOnline()
    {
        return this.online;
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    @Column(name = "offline_reason", nullable = true, length = 255)
    public String getOfflineReason()
    {
        return this.offlineReason;
    }

    public void setOfflineReason(String offlineReason)
    {
        this.offlineReason = offlineReason;
    }

    @Column(name = "last_push", nullable = false)
    public Date getLastPush()
    {
        return this.lastPush;
    }

    public void setLastPush(Date lastPush)
    {
        this.lastPush = lastPush;
    }
    @Column(name = "is_viewer", nullable = false)
    public boolean isViewer()
    {
        return this.viewer;
    }

    public void setViewer(boolean viewer)
    {
        this.viewer = viewer;
    }

    @Column(name = "is_requestor", nullable = false)
    public boolean isRequestor()
    {
        return this.requestor;
    }

    public void setRequestor(boolean requestor)
    {
        this.requestor = requestor;
    }

    @Column(name = "is_authorizer", nullable = false)
    public boolean isAuthorizer()
    {
        return this.authorizer;
    }

    public void setAuthorizer(boolean authorizer)
    {
        this.authorizer = authorizer;
    }
    
    @Column(name = "is_redirectee", nullable = false)
    public boolean isRedirectee()
    {
        return this.redirectee;
    }
    
    public void setRedirectee(boolean redirectee)
    {
        this.redirectee = redirectee;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    public Set<RigType> getRigTypes()
    {
        return this.rigTypes;
    }
    
    public void setRigTypes(Set<RigType> rigTypes)
    {
        this.rigTypes = rigTypes;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    public Set<Rig> getRigs()
    {
        return this.rigs;
    }
    
    public void setRigs(Set<Rig> rigs)
    {
        this.rigs = rigs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    public Set<RemotePermission> getRemotePermissions()
    {
        return this.remotePermissions;
    }

    public void setRemotePermissions(Set<RemotePermission> remotePermissions)
    {
        this.remotePermissions = remotePermissions;
    }
}
