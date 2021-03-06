/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 3rd January 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Store redemption of users.
 */
@Entity
@Table(name = "user_class_key_redemption")
public class UserClassKeyRedemption implements Serializable
{
    private static final long serialVersionUID = -9010792260048736914L;

    /** Record primary key. */
    private Long id;
    
    /** The key that redeemed the user. */
    private UserClassKey classKey;
    
    /** The user that was redeemed. */
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @JoinColumn(name = "user_class_key_id", nullable = false)
    @ManyToOne(optional = false)
    public UserClassKey getClassKey()
    {
        return this.classKey;
    }

    public void setClassKey(UserClassKey classKey)
    {
        this.classKey = classKey;
    }

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false)
    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
