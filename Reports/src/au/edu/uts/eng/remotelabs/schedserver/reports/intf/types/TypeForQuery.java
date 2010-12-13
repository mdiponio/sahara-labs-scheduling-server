/**
 * TypeForQuery.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * TypeForQuery bean class
 */

public class TypeForQuery implements org.apache.axis2.databinding.ADBBean
{

    private static final long serialVersionUID = -8554310463465175661L;

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
            "http://remotelabs.eng.uts.edu.au/schedserver/reports", "typeForQuery_type1", "ns1");

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for TypeForQuery_type0
     */

    protected java.lang.String localTypeForQuery_type0;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor

    protected TypeForQuery(final java.lang.String value, final boolean isRegisterValue)
    {
        this.localTypeForQuery_type0 = value;
        if (isRegisterValue)
        {

            TypeForQuery._table_.put(this.localTypeForQuery_type0, this);

        }

    }

    public static final java.lang.String _USER_CLASS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("USER_CLASS");
    public static final java.lang.String _RIG_TYPE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("RIG_TYPE");
    public static final java.lang.String _RIG = org.apache.axis2.databinding.utils.ConverterUtil.convertToString("RIG");
    public static final java.lang.String _USER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("USER");
    public static final java.lang.String _REQUEST_CAPABILITIES = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("REQUEST_CAPABILITIES");

    public static final TypeForQuery USER_CLASS = new TypeForQuery(TypeForQuery._USER_CLASS, true);
    public static final TypeForQuery RIG_TYPE = new TypeForQuery(TypeForQuery._RIG_TYPE, true);
    public static final TypeForQuery RIG = new TypeForQuery(TypeForQuery._RIG, true);
    public static final TypeForQuery USER = new TypeForQuery(TypeForQuery._USER, true);
    public static final TypeForQuery REQUEST_CAPABILITIES = new TypeForQuery(
            TypeForQuery._REQUEST_CAPABILITIES, true);

    public java.lang.String getValue()
    {
        return this.localTypeForQuery_type0;
    }

    @Override
    public boolean equals(final java.lang.Object obj)
    {
        return (obj == this);
    }

    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    @Override
    public java.lang.String toString()
    {

        return this.localTypeForQuery_type0.toString();

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
                TypeForQuery.MY_QNAME)
        {

            @Override
            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                TypeForQuery.this.serialize(TypeForQuery.MY_QNAME, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(TypeForQuery.MY_QNAME, factory, dataSource);

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

        //We can safely assume an element has only one type associated with it

        final java.lang.String namespace = parentQName.getNamespaceURI();
        final java.lang.String localName = parentQName.getLocalPart();

        if (!namespace.equals(""))
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = TypeForQuery.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, localName, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, localName);
            }

        }
        else
        {
            xmlWriter.writeStartElement(localName);
        }

        // add the type details if this is used in a simple type
        if (serializeType)
        {
            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":typeForQuery_type1", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "typeForQuery_type1",
                        xmlWriter);
            }
        }

        if (this.localTypeForQuery_type0 == null)
        {

            throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");

        }
        else
        {

            xmlWriter.writeCharacters(this.localTypeForQuery_type0);

        }

        xmlWriter.writeEndElement();

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
            prefix = TypeForQuery.generatePrefix(namespace);

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

        //We can safely assume an element has only one type associated with it
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
                TypeForQuery.MY_QNAME,
                new java.lang.Object[] { org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localTypeForQuery_type0) },
                null);

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        public static TypeForQuery fromValue(final java.lang.String value)
                throws java.lang.IllegalArgumentException
        {
            final TypeForQuery enumeration = (TypeForQuery)

            TypeForQuery._table_.get(value);

            if (enumeration == null)
            {
                throw new java.lang.IllegalArgumentException();
            }
            return enumeration;
        }

        public static TypeForQuery fromString(final java.lang.String value, final java.lang.String namespaceURI)
                throws java.lang.IllegalArgumentException
        {
            try
            {

                return Factory.fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));

            }
            catch (final java.lang.Exception e)
            {
                throw new java.lang.IllegalArgumentException();
            }
        }

        public static TypeForQuery fromString(final javax.xml.stream.XMLStreamReader xmlStreamReader,
                final java.lang.String content)
        {
            if (content.indexOf(":") > -1)
            {
                final java.lang.String prefix = content.substring(0, content.indexOf(":"));
                final java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                return TypeForQuery.Factory.fromString(content, namespaceUri);
            }
            else
            {
                return TypeForQuery.Factory.fromString(content, "");
            }
        }

        /**
         * static method to create the object
         * Precondition: If this object is an element, the current or next start element starts this object and any
         * intervening reader events are ignorable
         * If this object is not an element, it is a complex type and the reader is at the event just after the outer
         * start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         * If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static TypeForQuery parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            TypeForQuery object = null;
            new java.util.HashMap();
            new java.util.ArrayList();

            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                while (!reader.isEndElement())
                {
                    if (reader.isStartElement() || reader.hasText())
                    {

                        final java.lang.String content = reader.getElementText();

                        if (content.indexOf(":") > 0)
                        {
                            // this seems to be a Qname so find the namespace and send
                            prefix = content.substring(0, content.indexOf(":"));
                            namespaceuri = reader.getNamespaceURI(prefix);
                            object = TypeForQuery.Factory.fromString(content, namespaceuri);
                        }
                        else
                        {
                            // this seems to be not a qname send and empty namespace incase of it is
                            // check is done in fromString method
                            object = TypeForQuery.Factory.fromString(content, "");
                        }

                    }
                    else
                    {
                        reader.next();
                    }
                } // end of while loop

            }
            catch (final javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }//end of factory class

}
