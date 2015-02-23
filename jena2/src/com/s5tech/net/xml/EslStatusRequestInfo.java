/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslStatusRequestInfo.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class EslStatusRequestInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _maxBuf.
     */
    private long _maxBuf;

    /**
     * keeps track of state for field: _maxBuf
     */
    private boolean _has_maxBuf;

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

    public EslStatusRequestInfo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteMaxBuf(
    ) {
        this._has_maxBuf= false;
    }

    /**
     */
    public void deleteSecsToWait(
    ) {
        this._has_secsToWait= false;
    }

    /**
     * Returns the value of field 'maxBuf'.
     * 
     * @return the value of field 'MaxBuf'.
     */
    public long getMaxBuf(
    ) {
        return this._maxBuf;
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
     * Method hasMaxBuf.
     * 
     * @return true if at least one MaxBuf has been added
     */
    public boolean hasMaxBuf(
    ) {
        return this._has_maxBuf;
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
     * Sets the value of field 'maxBuf'.
     * 
     * @param maxBuf the value of field 'maxBuf'.
     */
    public void setMaxBuf(
            final long maxBuf) {
        this._maxBuf = maxBuf;
        this._has_maxBuf = true;
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
