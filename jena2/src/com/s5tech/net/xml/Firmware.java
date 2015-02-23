/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class Firmware.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Firmware implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private byte[] _content;

    /**
     * Field _startNow.
     */
    private boolean _startNow;

    /**
     * keeps track of state for field: _startNow
     */
    private boolean _has_startNow;


      //----------------/
     //- Constructors -/
    //----------------/

    public Firmware() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteStartNow(
    ) {
        this._has_startNow= false;
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
     * Returns the value of field 'startNow'.
     * 
     * @return the value of field 'StartNow'.
     */
    public boolean getStartNow(
    ) {
        return this._startNow;
    }

    /**
     * Method hasStartNow.
     * 
     * @return true if at least one StartNow has been added
     */
    public boolean hasStartNow(
    ) {
        return this._has_startNow;
    }

    /**
     * Returns the value of field 'startNow'.
     * 
     * @return the value of field 'StartNow'.
     */
    public boolean isStartNow(
    ) {
        return this._startNow;
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
     * Sets the value of field 'startNow'.
     * 
     * @param startNow the value of field 'startNow'.
     */
    public void setStartNow(
            final boolean startNow) {
        this._startNow = startNow;
        this._has_startNow = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.Firmware
     */
    public static com.s5tech.net.xml.Firmware unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.Firmware) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.Firmware.class, reader);
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
