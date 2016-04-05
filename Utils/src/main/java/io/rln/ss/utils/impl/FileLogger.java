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
package io.rln.ss.utils.logger.impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

import io.rln.ss.utils.logger.LoggerActivator;

/**
 *  Appends messages to a file using Log4j. This does not roll the
 *  file, so be wary of log file bloat. 
 */
public class FileLogger extends AbstractLog4JLogger
{
    @Override
    protected void setAppeneder()
    {
        /* File logger. */
        final String fileName = LoggerActivator.getProperty("Log_File_Name");
        
        if (fileName == null)
        {
            System.err.println("FATAL - Failed to load logging file name from configuration.");
            System.err.println("Check the configuration file is present, readable by the sheduling server");
            System.err.println("and the 'Log_File_Name' field is present.");
            System.err.println();
            throw new RuntimeException("Unable to load file logger file name.");
        }

        try
        {
            this.logger.addAppender(new FileAppender(new PatternLayout(AbstractLog4JLogger.PATTERN_LAYOUT), fileName));
        }
        catch (IOException ioe)
        {
            /* Check the file. */
            final File file = new File(fileName);
            if (file.isFile())
            {
                System.err.println("FATAL - Failed to add a Log4J file appender.");
                throw new RuntimeException("Failed adding file appender.");
            }
            else
            {
                System.err.println("FATAL - The loaded configuration file name (" + fileName + ") is not a valid file.");
                System.err.println("Change the configuration file property 'Log_File_Name' in");
                throw new RuntimeException("Log file does not exist.");
            }
        }
    }
}
