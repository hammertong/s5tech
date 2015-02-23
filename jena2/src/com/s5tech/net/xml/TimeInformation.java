/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class TimeInformation.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class TimeInformation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _activation.
     */
    private java.lang.String _activation;

    /**
     * Field _durationSecs.
     */
    private long _durationSecs;

    /**
     * keeps track of state for field: _durationSecs
     */
    private boolean _has_durationSecs;

    /**
     * Field _intervalSecs.
     */
    private long _intervalSecs;

    /**
     * keeps track of state for field: _intervalSecs
     */
    private boolean _has_intervalSecs;


      //----------------/
     //- Constructors -/
    //----------------/

    public TimeInformation() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteDurationSecs(
    ) {
        this._has_durationSecs= false;
    }

    /**
     */
    public void deleteIntervalSecs(
    ) {
        this._has_intervalSecs= false;
    }

    /**
     * Returns the value of field 'activation'.
     * 
     * @return the value of field 'Activation'.
     */
    public java.lang.String getActivation(
    ) {
        return this._activation;
    }

    /**
     * Returns the value of field 'durationSecs'.
     * 
     * @return the value of field 'DurationSecs'.
     */
    public long getDurationSecs(
    ) {
        return this._durationSecs;
    }

    /**
     * Returns the value of field 'intervalSecs'.
     * 
     * @return the value of field 'IntervalSecs'.
     */
    public long getIntervalSecs(
    ) {
        return this._intervalSecs;
    }

    /**
     * Method hasDurationSecs.
     * 
     * @return true if at least one DurationSecs has been added
     */
    public boolean hasDurationSecs(
    ) {
        return this._has_durationSecs;
    }

    /**
     * Method hasIntervalSecs.
     * 
     * @return true if at least one IntervalSecs has been added
     */
    public boolean hasIntervalSecs(
    ) {
        return this._has_intervalSecs;
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
     * Sets the value of field 'activation'.
     * 
     * @param activation the value of field 'activation'.
     */
    public void setActivation(
            final java.lang.String activation) {
        this._activation = activation;
    }

    /**
     * Sets the value of field 'durationSecs'.
     * 
     * @param durationSecs the value of field 'durationSecs'.
     */
    public void setDurationSecs(
            final long durationSecs) {
        this._durationSecs = durationSecs;
        this._has_durationSecs = true;
    }

    /**
     * Sets the value of field 'intervalSecs'.
     * 
     * @param intervalSecs the value of field 'intervalSecs'.
     */
    public void setIntervalSecs(
            final long intervalSecs) {
        this._intervalSecs = intervalSecs;
        this._has_intervalSecs = true;
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
