/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslChannelToJoinInfo.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class EslChannelToJoinInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _channel.
     */
    private long _channel;

    /**
     * keeps track of state for field: _channel
     */
    private boolean _has_channel;

    /**
     * Field _secsToWait.
     */
    private long _secsToWait;

    /**
     * keeps track of state for field: _secsToWait
     */
    private boolean _has_secsToWait;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslChannelToJoinInfo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteChannel(
    ) {
        this._has_channel= false;
    }

    /**
     */
    public void deleteSecsToWait(
    ) {
        this._has_secsToWait= false;
    }

    /**
     * Returns the value of field 'channel'.
     * 
     * @return the value of field 'Channel'.
     */
    public long getChannel(
    ) {
        return this._channel;
    }

    /**
     * Returns the value of field 'secsToWait'.
     * 
     * @return the value of field 'SecsToWait'.
     */
    public long getSecsToWait(
    ) {
        return this._secsToWait;
    }

    /**
     * Method hasChannel.
     * 
     * @return true if at least one Channel has been added
     */
    public boolean hasChannel(
    ) {
        return this._has_channel;
    }

    /**
     * Method hasSecsToWait.
     * 
     * @return true if at least one SecsToWait has been added
     */
    public boolean hasSecsToWait(
    ) {
        return this._has_secsToWait;
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
            final long channel) {
        this._channel = channel;
        this._has_channel = true;
    }

    /**
     * Sets the value of field 'secsToWait'.
     * 
     * @param secsToWait the value of field 'secsToWait'.
     */
    public void setSecsToWait(
            final long secsToWait) {
        this._secsToWait = secsToWait;
        this._has_secsToWait = true;
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
