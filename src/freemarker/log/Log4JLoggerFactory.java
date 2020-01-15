/*
 * Copyright (c) 2020, Jonathan Revusky revusky@freemarker.es
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and  the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package freemarker.log;

import org.apache.log4j.Level;

/**
 * @version $Id: Log4JLoggerFactory.java,v 1.8 2005/05/04 12:02:43 ddekany Exp $
 * @author Attila Szegedi
 */
class Log4JLoggerFactory implements LoggerFactory
{
    public Logger getLogger(String category)
    {
        return new Log4JLogger(org.apache.log4j.Logger.getLogger(category));
    }

    private static class Log4JLogger
    extends
        Logger
    {
        private final org.apache.log4j.Logger logger;
        
        Log4JLogger(org.apache.log4j.Logger logger)
        {
            this.logger = logger;
        }
        
        public void debug(String message)
        {
            logger.debug(message);
        }

        public void debug(String message, Throwable t)
        {
            logger.debug(message, t);
        }

        public void error(String message)
        {
            logger.error(message);
        }

        public void error(String message, Throwable t)
        {
            logger.error(message, t);
        }

        public void info(String message)
        {
            logger.info(message);
        }

        public void info(String message, Throwable t)
        {
            logger.info(message, t);
        }

        public void warn(String message)
        {
            logger.warn(message);
        }

        public void warn(String message, Throwable t)
        {
            logger.warn(message, t);
        }

        public boolean isDebugEnabled()
        {
            return logger.isDebugEnabled();
        }

        public boolean isInfoEnabled()
        {
            return logger.isInfoEnabled();
        }

        public boolean isWarnEnabled()
        {
            return logger.isEnabledFor(Level.WARN);
        }

        public boolean isErrorEnabled()
        {
            return logger.isEnabledFor(Level.ERROR);
        }

        public boolean isFatalEnabled()
        {
            return logger.isEnabledFor(Level.FATAL);
        }
    }
}