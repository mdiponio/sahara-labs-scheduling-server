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
 * @date 6th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

// Generated 06/01/2010 5:09:20 PM by Hibernate Tools 3.2.5.Beta

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite primary key for user_association table.
 */
@Embeddable
public class UserAssociationId implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = 2763184757998150139L;
    
    /** User primary key. */
    private long usersId;
    
    /** User class primary key. */
    private long userClassId;

    public UserAssociationId()
    {
        /* Bean style primary key. */
    }

    public UserAssociationId(final long usersId, final long userClassId)
    {
        this.usersId = usersId;
        this.userClassId = userClassId;
    }

    @Column(name = "users_id", nullable = false)
    public long getUsersId()
    {
        return this.usersId;
    }

    public void setUsersId(final long usersId)
    {
        this.usersId = usersId;
    }

    @Column(name = "user_class_id", nullable = false)
    public long getUserClassId()
    {
        return this.userClassId;
    }

    public void setUserClassId(final long userClassId)
    {
        this.userClassId = userClassId;
    }

    @Override
    public boolean equals(final Object other)
    {
        if ((this == other))
        {
            return true;
        }
        if ((other == null))
        {
            return false;
        }
        if (!(other instanceof UserAssociationId))
        {
            return false;
        }
        final UserAssociationId castOther = (UserAssociationId) other;

        return (this.getUsersId() == castOther.getUsersId())
                && (this.getUserClassId() == castOther.getUserClassId());
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (int) this.getUsersId();
        result = 37 * result + (int) this.getUserClassId();
        return result;
    }

}