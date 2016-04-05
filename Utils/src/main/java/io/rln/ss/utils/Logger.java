/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @date 28th December 2009
 */

package io.rln.ss.utils;

/**
 * Logger interface.
 */
public interface Logger
{
    /** Fatal messages. */
    public int FATAL = 0;
    
    /** Priority messages. */
    public int PRIORITY = 1;
    
    /** Error messages. */
    public int ERROR = 2;

    /** Warning messages. */
    public int WARN = 3;

    /** Informational messages. */
    public int INFO = 4;

    /** Debugging messages. */
    public int DEBUG = 5;
    
    /**
     * Logs fatal messages.
     * 
     * @param message message to log
     */
    public void fatal(String message);
    
    /**
     * Logs priority messages.
     * 
     * @param message message to log
     */
    public void priority(String message);
    
    /**
     * Logs error messages.
     * 
     * @param message message to log
     */
    public void error(String message);
    
    /**
     * Logs warning messages.
     * 
     * @param message message to log
     */
    public void warn(String message);
    
    /**
     * Logs informational messages.
     * 
     * @param message message to log
     */
    public void info(String message);

    /**
     * Logs debugging messages.
     * 
     * @param message message to log
     */
    public void debug(String message);

    /**
     * Logs a message to the specified level.
     * 
     * @param level logging level
     * @param message message to log
     */
    public void log(final int level, String message);
}
