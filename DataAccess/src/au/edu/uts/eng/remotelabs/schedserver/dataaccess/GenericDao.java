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
 * @date 7th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Generic data access object. Implements generic CRUD operations
 * for using database entity classes.
 * <p />
 * NOTE 1: All the generic DAO methods may throw an unchecked 
 * {@link HibernateException} if they fail. It is probably safe to not
 * catch these to implement normal unchecked exception fail-fast 
 * behaviour.
 * <p />
 * Note 2: This class can not be assumed to be thread safe as the Hibernate 
 * documentation specifically states sessions are not thread safe.
 */
public class GenericDao<T>
{
    /** Database session. */
    protected Session session;
    
    /** Logger. */
    protected Logger logger;

    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public GenericDao()
    {
        this(DataAccessActivator.getNewSession());
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public GenericDao(Session ses) throws IllegalStateException
    {
       this.logger = LoggerActivator.getLogger();
        
        if (ses == null || !ses.isOpen())
        {
            this.logger.error("A closed or null session was provided to the GenericDao.");
            throw new IllegalStateException("Provided a closed or null session.");
        }

        this.session = ses;
    }
    
    /**
     * Persist the provided object into the database.
     * 
     * @param obj transient object
     * @return persistent object
     */
    public T persist(T obj)
    {        
        this.begin();
        this.session.saveOrUpdate(obj);
        this.commit();
        
        return obj;
    }
    
    /**
     * Loads a persistent instance of the record with supplied idenitifer.
     * 
     * @param clazz class object of the entity type
     * @param id record identifier 
     * @return instance of record
     */
    @SuppressWarnings("unchecked")
    public T load(Class<T> clazz, Serializable id)
    {
        return (T) this.session.load(clazz, id);
    }
    
    /**
     * Returns a persistent instance of a detached object.
     * 
     * @param obj transitent instance
     * @return persistent instance
     */
    @SuppressWarnings("unchecked")
    public T merge(T obj)
    {
        this.begin();
        T persistent = (T) this.session.merge(obj);
        this.commit();
        
        return persistent;
    }
    
    /**
     * Flushes any changes to the database.
     */
    public void flush()
    {
        this.begin();
        this.session.flush();
        this.commit();
    }
    
    /**
     * Delete the provided object from the database. The object
     * may either be transient or persistent.
     * 
     * @param obj persistent object to delete
     */
    public void delete(T obj)
    {
        this.begin();
        this.session.delete(obj);
        this.commit();
    }

    /**
     * Begins a transaction.
     */
    protected void begin()
    {
        this.session.beginTransaction();
    }

    /**
     * Commits the current transaction.
     */
    protected void commit()
    {
        try
        {
            this.session.getTransaction().commit();
        }
        catch (HibernateException ex)
        {
            this.logger.error("Failed to commit database changes because of error " + ex.getMessage() + 
                    ". Going to close database session.");
            this.rollBack();
            this.session.close();
            throw ex;
        }
    }

    /**
     * Rolls back the current transaction.
     */
    protected void rollBack()
    {
        try
        {
            this.session.getTransaction().rollback();
        }
        catch (HibernateException ex)
        {
            this.logger.error("Failed to roll back changes because of error " + ex.getMessage() +
                    ". Going to close database session.");
            this.session.close();
            throw ex;
        }
    }

    /**
     * Closes the session. If the session is dirty, any changes are 
     * flushed to the database.
     */
    public void closeSession()
    {
        if (this.session.isConnected())
        {
            if (this.session.isDirty())
            {
                this.flush();
            }
            
            this.session.close();
        }
    }

    /**
     * Gets the session.
     * 
     * @return session
     */
    public Session getSession()
    {
        return this.session;
    }
}
