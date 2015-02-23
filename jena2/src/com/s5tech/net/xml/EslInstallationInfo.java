/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslInstallationInfo.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public abstract class EslInstallationInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mac.
     */
    private byte[] _mac;

    /**
     * Field _eslShortAddress.
     */
    private long _eslShortAddress;

    /**
     * keeps track of state for field: _eslShortAddress
     */
    private boolean _has_eslShortAddress;

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Field _installationKey.
     */
    private byte[] _installationKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslInstallationInfo() {
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
     * Returns the value of field 'eslShortAddress'.
     * 
     * @return the value of field 'EslShortAddress'.
     */
    public long getEslShortAddress(
    ) {
        return this._eslShortAddress;
    }

    /**
     * Returns the value of field 'installationKey'.
     * 
     * @return the value of field 'InstallationKey'.
     */
    public byte[] getInstallationKey(
    ) {
        return this._installationKey;
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
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
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
     * Sets the value of field 'installationKey'.
     * 
     * @param installationKey the value of field 'installationKey'.
     */
    public void setInstallationKey(
            final byte[] installationKey) {
        this._installationKey = installationKey;
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
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
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
