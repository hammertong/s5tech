/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslPriceData.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EslPriceData implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private byte[] _content;

    /**
     * Field _activationTime.
     */
    private java.util.Date _activationTime;

    /**
     * Field _hashCode.
     */
    private long _hashCode;

    /**
     * keeps track of state for field: _hashCode
     */
    private boolean _has_hashCode;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslPriceData() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteHashCode(
    ) {
        this._has_hashCode= false;
    }

    /**
     * Returns the value of field 'activationTime'.
     * 
     * @return the value of field 'ActivationTime'.
     */
    public java.util.Date getActivationTime(
    ) {
        return this._activationTime;
    }

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public byte[] getContent(
    ) {
        return this._content;
    }

    /**
     * Returns the value of field 'hashCode'.
     * 
     * @return the value of field 'HashCode'.
     */
    public long getHashCode(
    ) {
        return this._hashCode;
    }

    /**
     * Method hasHashCode.
     * 
     * @return true if at least one HashCode has been added
     */
    public boolean hasHashCode(
    ) {
        return this._has_hashCode;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'activationTime'.
     * 
     * @param activationTime the value of field 'activationTime'.
     */
    public void setActivationTime(
            final java.util.Date activationTime) {
        this._activationTime = activationTime;
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(
            final byte[] content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'hashCode'.
     * 
     * @param hashCode the value of field 'hashCode'.
     */
    public void setHashCode(
            final long hashCode) {
        this._hashCode = hashCode;
        this._has_hashCode = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.EslPriceData
     */
    public static com.s5tech.net.xml.EslPriceData unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.EslPriceData) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.EslPriceData.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
