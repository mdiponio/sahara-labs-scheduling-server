/**
 * OperationRequestTypeSequence_type3.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

/**
 * OperationRequestTypeSequence_type3 bean class
 */

public class OperationRequestTypeSequence_type3 implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = OperationRequestTypeSequence_type3
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = 6914035797619579439L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for UserID
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType localUserID;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType getUserID()
    {
        return this.localUserID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserID
     */
    public void setUserID(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType param)
    {

        this.localUserID = param;

    }

    /**
     * field for PermissionID
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType localPermissionID;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType getPermissionID()
    {
        return this.localPermissionID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            PermissionID
     */
    public void setPermissionID(
            final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType param)
    {

        this.localPermissionID = param;

    }

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(final javax.xml.stream.XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;

        try
        {
            isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader
                    .getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (final java.lang.IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
    {

        final org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
                parentQName)
        {

            @Override
            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                OperationRequestTypeSequence_type3.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {

        if (serializeType)
        {

            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":OperationRequestTypeSequence_type3", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "OperationRequestTypeSequence_type3", xmlWriter);
            }

        }

        if (this.localUserID == null)
        {
            throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");
        }
        this.localUserID.serialize(new javax.xml.namespace.QName("", "userID"), factory, xmlWriter);

        if (this.localPermissionID == null)
        {
            throw new org.apache.axis2.databinding.ADBException("permissionID cannot be null!!");
        }
        this.localPermissionID.serialize(new javax.xml.namespace.QName("", "permissionID"), factory, xmlWriter);

    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(final java.lang.String prefix, final java.lang.String namespace,
            final java.lang.String attName, final java.lang.String attValue,
            final javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
    {
        if (xmlWriter.getPrefix(namespace) == null)
        {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);

        }

        xmlWriter.writeAttribute(namespace, attName, attValue);

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(final javax.xml.stream.XMLStreamWriter xmlWriter,
            final java.lang.String namespace) throws javax.xml.stream.XMLStreamException
    {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null)
        {
            prefix = OperationRequestTypeSequence_type3.generatePrefix(namespace);

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     */
    public javax.xml.stream.XMLStreamReader getPullParser(final javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException
    {

        final java.util.ArrayList elementList = new java.util.ArrayList();
        final java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new javax.xml.namespace.QName("", "userID"));

        if (this.localUserID == null)
        {
            throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");
        }
        elementList.add(this.localUserID);

        elementList.add(new javax.xml.namespace.QName("", "permissionID"));

        if (this.localPermissionID == null)
        {
            throw new org.apache.axis2.databinding.ADBException("permissionID cannot be null!!");
        }
        elementList.add(this.localPermissionID);

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
                attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        /**
         * static method to create the object
         * Precondition: If this object is an element, the current or next start element starts this object and any
         * intervening reader events are ignorable
         * If this object is not an element, it is a complex type and the reader is at the event just after the outer
         * start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         * If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static OperationRequestTypeSequence_type3 parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            final OperationRequestTypeSequence_type3 object = new OperationRequestTypeSequence_type3();

            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userID").equals(reader.getName()))
                {

                    object.setUserID(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType.Factory
                            .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "permissionID").equals(reader.getName()))
                {

                    object
                            .setPermissionID(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType.Factory
                                    .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

            }
            catch (final javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }//end of factory class

}
