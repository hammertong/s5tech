/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslEvent.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class EslEvent implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _eslMac.
     */
    private byte[] _eslMac;

    /**
     * Field _type.
     */
    private com.s5tech.net.xml.types.EslEventType _type;

    /**
     * Field _time.
     */
    private java.util.Date _time;

    /**
     * Field _coordinatorMac.
     */
    private java.lang.String _coordinatorMac;

    /**
     * Field _eslShortAddress.
     */
    private long _eslShortAddress;

    /**
     * keeps track of state for field: _eslShortAddress
     */
    private boolean _has_eslShortAddress;

    /**
     * Field _eslType.
     */
    private java.lang.String _eslType;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslEvent() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteEslShortAddress(
    ) {
        this._has_eslShortAddress= false;
    }

    /**
     * Returns the value of field 'coordinatorMac'.
     * 
     * @return the value of field 'CoordinatorMac'.
     */
    public java.lang.String getCoordinatorMac(
    ) {
        return this._coordinatorMac;
    }

    /**
     * Returns the value of field 'eslMac'.
     * 
     * @return the value of field 'EslMac'.
     */
    public byte[] getEslMac(
    ) {
        return this._eslMac;
    }

    /**
     * Returns the value of field 'eslShortAddress'.
     * 
     * @return the value of field 'EslShortAddress'.
     */
    public long getEslShortAddress(
    ) {
        return this._eslShortAddress;
    }

    /**
     * Returns the value of field 'eslType'.
     * 
     * @return the value of field 'EslType'.
     */
    public java.lang.String getEslType(
    ) {
        return this._eslType;
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
    public com.s5tech.net.xml.types.EslEventType getType(
    ) {
        return this._type;
    }

    /**
     * Method hasEslShortAddress.
     * 
     * @return true if at least one EslShortAddress has been added
     */
    public boolean hasEslShortAddress(
    ) {
        return this._has_eslShortAddress;
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
     * Sets the value of field 'coordinatorMac'.
     * 
     * @param coordinatorMac the value of field 'coordinatorMac'.
     */
    public void setCoordinatorMac(
            final java.lang.String coordinatorMac) {
        this._coordinatorMac = coordinatorMac;
    }

    /**
     * Sets the value of field 'eslMac'.
     * 
     * @param eslMac the value of field 'eslMac'.
     */
    public void setEslMac(
            final byte[] eslMac) {
        this._eslMac = eslMac;
    }

    /**
     * Sets the value of field 'eslShortAddress'.
     * 
     * @param eslShortAddress the value of field 'eslShortAddress'.
     */
    public void setEslShortAddress(
            final long eslShortAddress) {
        this._eslShortAddress = eslShortAddress;
        this._has_eslShortAddress = true;
    }

    /**
     * Sets the value of field 'eslType'.
     * 
     * @param eslType the value of field 'eslType'.
     */
    public void setEslType(
            final java.lang.String eslType) {
        this._eslType = eslType;
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
            final com.s5tech.net.xml.types.EslEventType type) {
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
