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

import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMConstants;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.MTOMConstants;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axiom.om.impl.llom.OMStAXWrapper;
import org.apache.axiom.om.util.ElementHelper;
import org.apache.axiom.soap.impl.builder.MTOMStAXSOAPModelBuilder;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * QueueRequestType bean class
 */

public class SlaveRequestType extends OperationRequestType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SlaveRequestType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/queuer
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -2783235333651250152L;

    protected UserIDType userID;

    protected PermissionIDType permissionID;
    protected boolean permissionIDTracker = false;

    protected String rigID;
    protected boolean rigIDTracker = false;
    
    protected String password;
    protected boolean passwordTracker = false;
    
    protected DataHandler code;
    protected boolean codeTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/queuer"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

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
        if (param != null)
        {
            this.permissionIDTracker = true;
        }
        else
        {
            this.permissionIDTracker = false;
        }

        this.permissionID = param;
    }

    public String getRigID()
    {
        return this.rigID;
    }

    public void setRigID(final String param)
    {
        if (param != null)
        {
            //update the setting tracker
            this.rigIDTracker = true;
        }
        else
        {
            this.rigIDTracker = false;
        }

        this.rigID = param;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(final String param)
    {
        if (param != null)
        {
            //update the setting tracker
            this.passwordTracker = true;
        }
        else
        {
            this.passwordTracker = false;
        }

        this.password = param;
    }

    
    public DataHandler getCode()
    {
        return this.code;
    }

    public void setCode(final DataHandler param)
    {
        if (param != null)
        {
            this.codeTracker = true;
        }
        else
        {
            this.codeTracker = false;
        }

        this.code = param;
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
                SlaveRequestType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = SlaveRequestType.generatePrefix(namespace);
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
                    + ":QueueRequestType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "QueueRequestType",
                    xmlWriter);
        }

        if (this.requestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SlaveRequestType.generatePrefix(namespace);
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
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
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
                    prefix = SlaveRequestType.generatePrefix(namespace);
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

        if (this.userID == null)
        {
            throw new ADBException("userID cannot be null!!");
        }
        this.userID.serialize(new QName("", "userID"), factory, xmlWriter);

        if (this.permissionIDTracker)
        {
            if (this.permissionID == null)
            {
                throw new ADBException("permissionID cannot be null!!");
            }
            this.permissionID.serialize(new QName("", "permissionID"), factory, xmlWriter);
        }

        if (this.rigIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SlaveRequestType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "rigID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("rigID");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.rigID));
            xmlWriter.writeEndElement();
        }
        
        if (this.passwordTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SlaveRequestType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "password", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "password");
                }
            }
            else
            {
                xmlWriter.writeStartElement("password");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.password));
            xmlWriter.writeEndElement();
        }

        if (this.codeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SlaveRequestType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "code", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "code");
                }
            }
            else
            {
                xmlWriter.writeStartElement("code");
            }

            if (this.code != null)
            {
                xmlWriter.writeDataHandler(this.code);
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
            prefix = SlaveRequestType.generatePrefix(namespace);
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

        final ArrayList<Object> elementList = new ArrayList<Object>();
        final ArrayList<QName> attribList = new ArrayList<QName>();

        attribList.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/queuer", "QueueRequestType"));
        
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
        
        elementList.add(new QName("", "userID"));
        if (this.userID == null)
        {
            throw new ADBException("userID cannot be null!!");
        }
        elementList.add(this.userID);
        
        if (this.permissionIDTracker)
        {
            elementList.add(new QName("", "permissionID"));
            if (this.permissionID == null)
            {
                throw new ADBException("permissionID cannot be null!!");
            }
            elementList.add(this.permissionID);
        }
        
        if (this.rigIDTracker)
        {
            elementList.add(new QName("", "rigID"));
            elementList.add(ConverterUtil.convertToString(this.rigID));
        }
        
        if (this.passwordTracker)
        {
            elementList.add(new QName("", "password"));
            elementList.add(ConverterUtil.convertToString(this.password));
        }
        
        if (this.codeTracker)
        {
            elementList.add(new QName("", "code"));
            elementList.add(this.code);
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        @SuppressWarnings("deprecation")
        public static SlaveRequestType parse(final XMLStreamReader reader) throws Exception
        {
            final SlaveRequestType object = new SlaveRequestType();
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

                        if (!"QueueRequestType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SlaveRequestType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigID(ConverterUtil.convertToString(content));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "password").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setPassword(ConverterUtil.convertToString(content));
                    reader.next();
                }
                
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "code").equals(reader.getName()))
                {
                    reader.next();
                    if (SlaveRequestType.isReaderMTOMAware(reader)
                            && Boolean.TRUE.equals(reader.getProperty(OMConstants.IS_BINARY)))
                    {
                        object.setCode((DataHandler) reader.getProperty(OMConstants.DATA_HANDLER));
                    }
                    else
                    {
                        if (reader.getEventType() == XMLStreamConstants.START_ELEMENT
                                && reader.getName().equals(new QName(MTOMConstants.XOP_NAMESPACE_URI, MTOMConstants.XOP_INCLUDE)))
                        {
                            final String id = ElementHelper.getContentID(reader, "UTF-8");
                            object.setCode(((MTOMStAXSOAPModelBuilder) ((OMStAXWrapper) reader).getBuilder())
                                    .getDataHandler(id));
                            reader.next();
                            reader.next();
                        }
                        else if (reader.hasText())
                        {
                            final String content = reader.getText();
                            object.setCode(ConverterUtil.convertToBase64Binary(content));
                            reader.next();
                        }
                    }

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
