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
 * @date 3rd March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMConstants;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * UserLockIDUserPermSequence bean class.
 */
public class UserLockIDUserPermSequence implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserLockIDUserPermSequence
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 6914035797619579439L;

    protected UserIDType userID;
    protected PermissionIDType permissionID;

    public UserIDType getUserID()
    {
        return this.userID;
    }

    public void setUserID(final UserIDType param)
    {
        this.userID = param;
    }

    public PermissionIDType getPermissionID()
    {
        return this.permissionID;
    }

    public void setPermissionID(final PermissionIDType param)
    {
        this.permissionID = param;
    }
    
    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public static boolean isReaderMTOMAware(final XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;
        try
        {
            isReaderMTOMAware = Boolean.TRUE.equals(reader.getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (final IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName)
        {

            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter) throws XMLStreamException
            {
                UserLockIDUserPermSequence.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new OMSourcedElementImpl(parentQName, factory, dataSource);
    }

    @Override
    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    @Override
    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws XMLStreamException, ADBException
    {
        if (serializeType)
        {
            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":UserLockIDUserPermSequence", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "UserLockIDUserPermSequence", xmlWriter);
            }
        }

        if (this.userID == null)
        {
            throw new ADBException("userID cannot be null!!");
        }
        this.userID.serialize(new QName("", "userID"), factory, xmlWriter);

        if (this.permissionID == null)
        {
            throw new ADBException("permissionID cannot be null!!");
        }
        this.permissionID.serialize(new QName("", "permissionID"), factory, xmlWriter);
    }

    private void writeAttribute(final String prefix, final String namespace, final String attName,
            final String attValue, final XMLStreamWriter xmlWriter) throws XMLStreamException
    {
        if (xmlWriter.getPrefix(namespace) == null)
        {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null)
        {
            prefix = UserLockIDUserPermSequence.generatePrefix(namespace);
            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {

        final ArrayList<Serializable> elementList = new ArrayList<Serializable>();

        elementList.add(new QName("", "userID"));
        if (this.userID == null)
        {
            throw new ADBException("userID cannot be null!!");
        }
        elementList.add(this.userID);

        elementList.add(new QName("", "permissionID"));
        if (this.permissionID == null)
        {
            throw new ADBException("permissionID cannot be null!!");
        }
        elementList.add(this.permissionID);

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static UserLockIDUserPermSequence parse(final XMLStreamReader reader) throws Exception
        {
            final UserLockIDUserPermSequence object = new UserLockIDUserPermSequence();

            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "userID").equals(reader.getName()))
                {
                    object.setUserID(UserIDType.Factory.parse(reader));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "permissionID").equals(reader.getName()))   
                {
                    object.setPermissionID(PermissionIDType.Factory.parse(reader));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
            }
            catch (final XMLStreamException e)
            {
                throw new Exception(e);
            }

            return object;
        }
    }
}
