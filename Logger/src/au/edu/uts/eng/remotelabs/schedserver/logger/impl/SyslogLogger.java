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

package au.edu.uts.eng.remotelabs.schedserver.logger.impl;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.net.SyslogAppender;

import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Syslog logger which appends log messages to a remote syslog host.
 */
public class SyslogLogger extends AbstractLog4JLogger
{
    @Override
    protected void setAppeneder()
    {
       final String host = LoggerActivator.getProperty("Syslog_Host");
       final int localNum = this.getConfigInt(LoggerActivator.getProperty("Syslog_Local_Facility_Num"), 1);
       
       SyslogAppender appender = new SyslogAppender();
       appender.setLayout(new PatternLayout(AbstractLog4JLogger.PATTERN_LAYOUT));
       appender.setSyslogHost(host);
       if (localNum >= 0 && localNum <= 7)
       {
           appender.setFacility("LOCAL" + localNum);
       }
       else
       {
           appender.setFacility("LOCAL1");
       }
       
       this.logger.addAppender(appender);
    }
}
