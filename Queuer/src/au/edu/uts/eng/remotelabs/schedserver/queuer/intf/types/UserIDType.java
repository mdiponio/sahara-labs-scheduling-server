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
 * @date 28th March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types;

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
 * UserIDType bean class.
 */
public class UserIDType extends OperationRequestType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserIDType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/queuer
     * Namespace Prefix = ns1
     */
    
    public static final String QNAME_DELIM = ":";
    
    private static final long serialVersionUID = 8637939230347195152L;
    
    protected String userID;
    protected boolean userIDTracker;
    
    protected UserNSNameSequence userNSNameSeq;
    protected boolean userNSNameSeqTracker;
    
    protected String userQName;
    protected boolean userQNameTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/queuer"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public String getUserID()
    {
        return this.userID;
    }

    public void setUserID(final String param)
    {
        if (param != null)
        {
            this.userIDTracker = true;
        }
        else
        {
            this.userIDTracker = false;
        }

        this.userID = param;
    }
    
    public String getUserNamespace()
    {
        if (this.userNSNameSeq != null && this.userNSNameSeq.getUserNamespace() != null)
        {
            return this.userNSNameSeq.getUserNamespace();
        }
        
        if (this.userQName != null)
        {
            String idParts[] = this.userQName.split(UserIDType.QNAME_DELIM, 2);
            return idParts[0];
        }
        
        return null;
    }
    
    public String getUserName()
    {
        if (this.userNSNameSeq != null && this.userNSNameSeq.getUserName() != null)
        {
            return this.userNSNameSeq.getUserName();
        }
        
        if (this.userQName != null)
        {
            String idParts[] = this.userQName.split(UserIDType.QNAME_DELIM, 2);
            if (idParts.length == 2)
            {
                return idParts[1];
            }
        }
        
        return null;
    }

    public UserNSNameSequence getUserNSNameSequence()
    {
        return this.userNSNameSeq;
    }

    public void setUserNSNameSequence(final UserNSNameSequence param)
    {
        if (param != null)
        {
            this.userNSNameSeqTracker = true;
        }
        else
        {
            this.userNSNameSeqTracker = false;
        }

        this.userNSNameSeq = param;
    }

    public String getUserQName()
    {
        return this.userQName;
    }

    public void setUserQName(final String param)
    {
        if (param != null)
        {
            this.userQNameTracker = true;
        }
        else
        {
            this.userQNameTracker = false;
        }
        this.userQName = param;
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
                UserIDType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = UserIDType.generatePrefix(namespace);
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
                "http://remotelabs.eng.uts.edu.au/schedserver/queuer");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":UserIDType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserIDType", xmlWriter);
        }

        if (this.requestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserIDType.generatePrefix(namespace);
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
        
        if (this.requestorNSNameSequenceTracker)
        {
            if (this.requestorNSNameSequence == null)
            {
                throw new ADBException("RequestorNSNameSequence cannot be null!!");
            }
            this.requestorNSNameSequence.serialize(null, factory, xmlWriter);
        }
        
        if (this.requestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserIDType.generatePrefix(namespace);
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
        
        if (this.userIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserIDType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userID");
            }

            if (this.userID == null)
            {
                throw new ADBException("userID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.userID);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.userNSNameSeqTracker)
        {
            if (this.userNSNameSeq == null)
            {
                throw new ADBException("UserNSNameSequence cannot be null!!");
            }
            this.userNSNameSeq.serialize(null, factory, xmlWriter);
        }
        
        if (this.userQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserIDType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userQName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userQName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userQName");
            }

            if (this.userQName == null)
            {
                throw new ADBException("userQName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.userQName);
            }
            xmlWriter.writeEndElement();
        }
        
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
            prefix = UserIDType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/queuer", "UserIDType"));
        if (this.requestorIDTracker)
        {
            elementList.add(new QName("", "requestorID"));
            elementList.add(ConverterUtil.convertToString(this.requestorID));
        }
        
        if (this.requestorNSNameSequenceTracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/queuer", "RequestorNSNameSequence"));
            if (this.requestorNSNameSequence == null)
            {
                throw new ADBException("RequestorNSNameSequence cannot be null!!");
            }
            elementList.add(this.requestorNSNameSequence);
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
        
        if (this.userIDTracker)
        {
            elementList.add(new QName("", "userID"));
            if (this.userID != null)
            {
                elementList.add(ConverterUtil.convertToString(this.userID));
            }
            else
            {
                throw new ADBException("userID cannot be null!!");
            }
        }
        
        if (this.userNSNameSeqTracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/queuer", "UserNSNameSequence"));
            if (this.userNSNameSeq == null)
            {
                throw new ADBException("UserNSNameSequence cannot be null!!");
            }
            elementList.add(this.userNSNameSeq);
        }
        
        if (this.userQNameTracker)
        {
            elementList.add(new QName("", "userQName"));
            if (this.userQName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.userQName));
            }
            else
            {
                throw new ADBException("userQName cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static UserIDType parse(final XMLStreamReader reader) throws Exception
        {
            final UserIDType object = new UserIDType();
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
                        if (!"UserIDType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (UserIDType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                        object.setRequestorNSNameSequence(RequestorNSNameSequence.Factory.parse(reader));
                    }
                }
                catch (final Exception e)
                { /* Optional. */ }

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
                if (reader.isStartElement() && new QName("", "userID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserID(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                try
                {
                    if (reader.isStartElement())
                    {
                        object.setUserNSNameSequence(UserNSNameSequence.Factory.parse(reader));
                    }
                }
                catch (final Exception e)
                { /* Optional. */ }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "userQName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserQName(ConverterUtil.convertToString(content));
                    reader.next();
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
