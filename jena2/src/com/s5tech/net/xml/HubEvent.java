/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class HubEvent.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class HubEvent implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hubMac.
     */
    private byte[] _hubMac;

    /**
     * Field _type.
     */
    private com.s5tech.net.xml.types.HubEventType _type;

    /**
     * Field _time.
     */
    private java.util.Date _time;

    /**
     * Field _ipAddress.
     */
    private java.lang.String _ipAddress;


      //----------------/
     //- Constructors -/
    //----------------/

    public HubEvent() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'hubMac'.
     * 
     * @return the value of field 'HubMac'.
     */
    public byte[] getHubMac(
    ) {
        return this._hubMac;
    }

    /**
     * Returns the value of field 'ipAddress'.
     * 
     * @return the value of field 'IpAddress'.
     */
    public java.lang.String getIpAddress(
    ) {
        return this._ipAddress;
    }

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'Time'.
     */
    public java.util.Date getTime(
    ) {
        return this._time;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public com.s5tech.net.xml.types.HubEventType getType(
    ) {
        return this._type;
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
     * Sets the value of field 'hubMac'.
     * 
     * @param hubMac the value of field 'hubMac'.
     */
    public void setHubMac(
            final byte[] hubMac) {
        this._hubMac = hubMac;
    }

    /**
     * Sets the value of field 'ipAddress'.
     * 
     * @param ipAddress the value of field 'ipAddress'.
     */
    public void setIpAddress(
            final java.lang.String ipAddress) {
        this._ipAddress = ipAddress;
    }

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(
            final java.util.Date time) {
        this._time = time;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final com.s5tech.net.xml.types.HubEventType type) {
        this._type = type;
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
