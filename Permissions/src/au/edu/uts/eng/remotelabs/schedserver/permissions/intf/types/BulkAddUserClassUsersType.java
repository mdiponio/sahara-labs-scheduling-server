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
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * BulkAddUserClassUsersType bean class
 */

public class BulkAddUserClassUsersType extends OperationRequestType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = BulkAddUserClassUsersType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -677551764132617813L;

    protected UserClassIDType userClass;
    protected UserListType userList;
    protected boolean lockUsers;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public UserClassIDType getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(final UserClassIDType param)
    {

        this.userClass = param;

    }

    public UserListType getUserList()
    {
        return this.userList;
    }

    public void setUserList(final UserListType param)
    {
        this.userList = param;
    }

    public boolean getLockUsers()
    {
        return this.lockUsers;
    }

    public void setLockUsers(final boolean param)
    {
        this.lockUsers = param;
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

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName)
        {
            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter) throws XMLStreamException
            {
                BulkAddUserClassUsersType.this.serialize(this.parentQName, factory, xmlWriter);
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
        String prefix = parentQName.getPrefix();
        String namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0))
        {
            final String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null)
            {
                xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            }
            else
            {
                if (prefix == null)
                {
                    prefix = BulkAddUserClassUsersType.generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        else
        {
            xmlWriter.writeStartElement(parentQName.getLocalPart());
        }

        final String namespacePrefix = this.registerPrefix(xmlWriter,
                "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":BulkAddUserClassUsersType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "BulkAddUserClassUsersType", xmlWriter);
        }

        if (this.requestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = BulkAddUserClassUsersType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "requestorID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("requestorID");
            }

            if (this.requestorID == Integer.MIN_VALUE)
            {
                throw new ADBException("requestorID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.requestorID));
            }

            xmlWriter.writeEndElement();
        }
        
        if (this.nameNameSpaceSequenceTracker)
        {
            if (this.nameNameSpaceSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            this.nameNameSpaceSequence.serialize(null, factory, xmlWriter);
        }
        
        if (this.requestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = BulkAddUserClassUsersType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "requestorQName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorQName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("requestorQName");
            }

            if (this.requestorQName == null)
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.requestorQName);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.userClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        this.userClass.serialize(new QName("", "userClass"), factory, xmlWriter);

        if (this.userList == null)
        {
            throw new ADBException("userList cannot be null!!");
        }
        this.userList.serialize(new QName("", "userList"), factory, xmlWriter);

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = BulkAddUserClassUsersType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "lockUsers", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "lockUsers");
            }
        }
        else
        {
            xmlWriter.writeStartElement("lockUsers");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.lockUsers));
        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();
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
            prefix = BulkAddUserClassUsersType.generatePrefix(namespace);
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
        final ArrayList<QName> attribList = new ArrayList<QName>();

        attribList.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                "BulkAddUserClassUsersType"));
        if (this.requestorIDTracker)
        {
            elementList.add(new QName("", "requestorID"));
            elementList.add(ConverterUtil.convertToString(this.requestorID));
        }
        
        if (this.nameNameSpaceSequenceTracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                    "OperationRequestTypeSequence_type0"));
            if (this.nameNameSpaceSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.nameNameSpaceSequence);
        }
        if (this.requestorQNameTracker)
        {
            elementList.add(new QName("", "requestorQName"));
            if (this.requestorQName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.requestorQName));
            }
            else
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
        }

        elementList.add(new QName("", "userClass"));
        if (this.userClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        elementList.add(this.userClass);

        elementList.add(new QName("", "userList"));
        if (this.userList == null)
        {
            throw new ADBException("userList cannot be null!!");
        }
        elementList.add(this.userList);

        elementList.add(new QName("", "lockUsers"));
        elementList.add(ConverterUtil.convertToString(this.lockUsers));

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static BulkAddUserClassUsersType parse(final XMLStreamReader reader) throws Exception
        {
            final BulkAddUserClassUsersType object = new BulkAddUserClassUsersType();
            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    final String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");
                    if (fullTypeName != null)
                    {
                        String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;
                        final String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);
                        if (!"BulkAddUserClassUsersType".equals(type))
                        {
                            //find namespace for the prefix
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (BulkAddUserClassUsersType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "requestorID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRequestorID(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setRequestorID(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                try
                {
                    if (reader.isStartElement())
                    {
                        object.setOperationRequestTypeSequence(OperationRequestTypeSequence.Factory
                                .parse(reader));
                    }
                }
                catch (final Exception e)
                {
                    /* Optional. */
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "requestorQName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRequestorQName(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClass").equals(reader.getName()))
                {
                    object.setUserClass(UserClassIDType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "userList").equals(reader.getName()))
                {
                    object.setUserList(UserListType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "lockUsers").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setLockUsers(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement())
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
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
