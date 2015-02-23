/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class CoordinatorEvent.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class CoordinatorEvent implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _coordinatorMac.
     */
    private byte[] _coordinatorMac;

    /**
     * Field _type.
     */
    private com.s5tech.net.xml.types.CoordinatorEventType _type;

    /**
     * Field _time.
     */
    private java.util.Date _time;

    /**
     * Field _port.
     */
    private java.lang.String _port;

    /**
     * Field _channel.
     */
    private java.lang.String _channel;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordinatorEvent() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'channel'.
     * 
     * @return the value of field 'Channel'.
     */
    public java.lang.String getChannel(
    ) {
        return this._channel;
    }

    /**
     * Returns the value of field 'coordinatorMac'.
     * 
     * @return the value of field 'CoordinatorMac'.
     */
    public byte[] getCoordinatorMac(
    ) {
        return this._coordinatorMac;
    }

    /**
     * Returns the value of field 'port'.
     * 
     * @return the value of field 'Port'.
     */
    public java.lang.String getPort(
    ) {
        return this._port;
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
    public com.s5tech.net.xml.types.CoordinatorEventType getType(
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
     * Sets the value of field 'channel'.
     * 
     * @param channel the value of field 'channel'.
     */
    public void setChannel(
            final java.lang.String channel) {
        this._channel = channel;
    }

    /**
     * Sets the value of field 'coordinatorMac'.
     * 
     * @param coordinatorMac the value of field 'coordinatorMac'.
     */
    public void setCoordinatorMac(
            final byte[] coordinatorMac) {
        this._coordinatorMac = coordinatorMac;
    }

    /**
     * Sets the value of field 'port'.
     * 
     * @param port the value of field 'port'.
     */
    public void setPort(
            final java.lang.String port) {
        this._port = port;
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
            final com.s5tech.net.xml.types.CoordinatorEventType type) {
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
