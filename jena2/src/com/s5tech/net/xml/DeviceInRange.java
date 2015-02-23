/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class DeviceInRange.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class DeviceInRange implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mac.
     */
    private byte[] _mac;

    /**
     * Field _signalLevel.
     */
    private long _signalLevel;

    /**
     * keeps track of state for field: _signalLevel
     */
    private boolean _has_signalLevel;


      //----------------/
     //- Constructors -/
    //----------------/

    public DeviceInRange() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteSignalLevel(
    ) {
        this._has_signalLevel= false;
    }

    /**
     * Returns the value of field 'mac'.
     * 
     * @return the value of field 'Mac'.
     */
    public byte[] getMac(
    ) {
        return this._mac;
    }

    /**
     * Returns the value of field 'signalLevel'.
     * 
     * @return the value of field 'SignalLevel'.
     */
    public long getSignalLevel(
    ) {
        return this._signalLevel;
    }

    /**
     * Method hasSignalLevel.
     * 
     * @return true if at least one SignalLevel has been added
     */
    public boolean hasSignalLevel(
    ) {
        return this._has_signalLevel;
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
     * Sets the value of field 'mac'.
     * 
     * @param mac the value of field 'mac'.
     */
    public void setMac(
            final byte[] mac) {
        this._mac = mac;
    }

    /**
     * Sets the value of field 'signalLevel'.
     * 
     * @param signalLevel the value of field 'signalLevel'.
     */
    public void setSignalLevel(
            final long signalLevel) {
        this._signalLevel = signalLevel;
        this._has_signalLevel = true;
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
