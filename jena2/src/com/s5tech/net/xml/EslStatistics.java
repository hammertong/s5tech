/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslStatistics.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EslStatistics implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mac.
     */
    private byte[] _mac;

    /**
     * Field _nColdReset.
     */
    private long _nColdReset;

    /**
     * keeps track of state for field: _nColdReset
     */
    private boolean _has_nColdReset;

    /**
     * Field _nHotReset.
     */
    private long _nHotReset;

    /**
     * keeps track of state for field: _nHotReset
     */
    private boolean _has_nHotReset;

    /**
     * Field _nPushReset.
     */
    private long _nPushReset;

    /**
     * keeps track of state for field: _nPushReset
     */
    private boolean _has_nPushReset;

    /**
     * Field _nOtaReset.
     */
    private long _nOtaReset;

    /**
     * keeps track of state for field: _nOtaReset
     */
    private boolean _has_nOtaReset;

    /**
     * Field _nAssertReset.
     */
    private long _nAssertReset;

    /**
     * keeps track of state for field: _nAssertReset
     */
    private boolean _has_nAssertReset;

    /**
     * Field _nPushSleep.
     */
    private long _nPushSleep;

    /**
     * keeps track of state for field: _nPushSleep
     */
    private boolean _has_nPushSleep;

    /**
     * Field _nNetSleep.
     */
    private long _nNetSleep;

    /**
     * keeps track of state for field: _nNetSleep
     */
    private boolean _has_nNetSleep;

    /**
     * Field _nScanSleep.
     */
    private long _nScanSleep;

    /**
     * keeps track of state for field: _nScanSleep
     */
    private boolean _has_nScanSleep;

    /**
     * Field _nPowerupSleep.
     */
    private long _nPowerupSleep;

    /**
     * keeps track of state for field: _nPowerupSleep
     */
    private boolean _has_nPowerupSleep;

    /**
     * Field _nStatusRetry.
     */
    private long _nStatusRetry;

    /**
     * keeps track of state for field: _nStatusRetry
     */
    private boolean _has_nStatusRetry;

    /**
     * Field _nScan.
     */
    private long _nScan;

    /**
     * keeps track of state for field: _nScan
     */
    private boolean _has_nScan;

    /**
     * Field _time.
     */
    private long _time;

    /**
     * keeps track of state for field: _time
     */
    private boolean _has_time;

    /**
     * Field _nJoinWDT.
     */
    private long _nJoinWDT;

    /**
     * keeps track of state for field: _nJoinWDT
     */
    private boolean _has_nJoinWDT;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslStatistics() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteNAssertReset(
    ) {
        this._has_nAssertReset= false;
    }

    /**
     */
    public void deleteNColdReset(
    ) {
        this._has_nColdReset= false;
    }

    /**
     */
    public void deleteNHotReset(
    ) {
        this._has_nHotReset= false;
    }

    /**
     */
    public void deleteNJoinWDT(
    ) {
        this._has_nJoinWDT= false;
    }

    /**
     */
    public void deleteNNetSleep(
    ) {
        this._has_nNetSleep= false;
    }

    /**
     */
    public void deleteNOtaReset(
    ) {
        this._has_nOtaReset= false;
    }

    /**
     */
    public void deleteNPowerupSleep(
    ) {
        this._has_nPowerupSleep= false;
    }

    /**
     */
    public void deleteNPushReset(
    ) {
        this._has_nPushReset= false;
    }

    /**
     */
    public void deleteNPushSleep(
    ) {
        this._has_nPushSleep= false;
    }

    /**
     */
    public void deleteNScan(
    ) {
        this._has_nScan= false;
    }

    /**
     */
    public void deleteNScanSleep(
    ) {
        this._has_nScanSleep= false;
    }

    /**
     */
    public void deleteNStatusRetry(
    ) {
        this._has_nStatusRetry= false;
    }

    /**
     */
    public void deleteTime(
    ) {
        this._has_time= false;
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
     * Returns the value of field 'nAssertReset'.
     * 
     * @return the value of field 'NAssertReset'.
     */
    public long getNAssertReset(
    ) {
        return this._nAssertReset;
    }

    /**
     * Returns the value of field 'nColdReset'.
     * 
     * @return the value of field 'NColdReset'.
     */
    public long getNColdReset(
    ) {
        return this._nColdReset;
    }

    /**
     * Returns the value of field 'nHotReset'.
     * 
     * @return the value of field 'NHotReset'.
     */
    public long getNHotReset(
    ) {
        return this._nHotReset;
    }

    /**
     * Returns the value of field 'nJoinWDT'.
     * 
     * @return the value of field 'NJoinWDT'.
     */
    public long getNJoinWDT(
    ) {
        return this._nJoinWDT;
    }

    /**
     * Returns the value of field 'nNetSleep'.
     * 
     * @return the value of field 'NNetSleep'.
     */
    public long getNNetSleep(
    ) {
        return this._nNetSleep;
    }

    /**
     * Returns the value of field 'nOtaReset'.
     * 
     * @return the value of field 'NOtaReset'.
     */
    public long getNOtaReset(
    ) {
        return this._nOtaReset;
    }

    /**
     * Returns the value of field 'nPowerupSleep'.
     * 
     * @return the value of field 'NPowerupSleep'.
     */
    public long getNPowerupSleep(
    ) {
        return this._nPowerupSleep;
    }

    /**
     * Returns the value of field 'nPushReset'.
     * 
     * @return the value of field 'NPushReset'.
     */
    public long getNPushReset(
    ) {
        return this._nPushReset;
    }

    /**
     * Returns the value of field 'nPushSleep'.
     * 
     * @return the value of field 'NPushSleep'.
     */
    public long getNPushSleep(
    ) {
        return this._nPushSleep;
    }

    /**
     * Returns the value of field 'nScan'.
     * 
     * @return the value of field 'NScan'.
     */
    public long getNScan(
    ) {
        return this._nScan;
    }

    /**
     * Returns the value of field 'nScanSleep'.
     * 
     * @return the value of field 'NScanSleep'.
     */
    public long getNScanSleep(
    ) {
        return this._nScanSleep;
    }

    /**
     * Returns the value of field 'nStatusRetry'.
     * 
     * @return the value of field 'NStatusRetry'.
     */
    public long getNStatusRetry(
    ) {
        return this._nStatusRetry;
    }

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'Time'.
     */
    public long getTime(
    ) {
        return this._time;
    }

    /**
     * Method hasNAssertReset.
     * 
     * @return true if at least one NAssertReset has been added
     */
    public boolean hasNAssertReset(
    ) {
        return this._has_nAssertReset;
    }

    /**
     * Method hasNColdReset.
     * 
     * @return true if at least one NColdReset has been added
     */
    public boolean hasNColdReset(
    ) {
        return this._has_nColdReset;
    }

    /**
     * Method hasNHotReset.
     * 
     * @return true if at least one NHotReset has been added
     */
    public boolean hasNHotReset(
    ) {
        return this._has_nHotReset;
    }

    /**
     * Method hasNJoinWDT.
     * 
     * @return true if at least one NJoinWDT has been added
     */
    public boolean hasNJoinWDT(
    ) {
        return this._has_nJoinWDT;
    }

    /**
     * Method hasNNetSleep.
     * 
     * @return true if at least one NNetSleep has been added
     */
    public boolean hasNNetSleep(
    ) {
        return this._has_nNetSleep;
    }

    /**
     * Method hasNOtaReset.
     * 
     * @return true if at least one NOtaReset has been added
     */
    public boolean hasNOtaReset(
    ) {
        return this._has_nOtaReset;
    }

    /**
     * Method hasNPowerupSleep.
     * 
     * @return true if at least one NPowerupSleep has been added
     */
    public boolean hasNPowerupSleep(
    ) {
        return this._has_nPowerupSleep;
    }

    /**
     * Method hasNPushReset.
     * 
     * @return true if at least one NPushReset has been added
     */
    public boolean hasNPushReset(
    ) {
        return this._has_nPushReset;
    }

    /**
     * Method hasNPushSleep.
     * 
     * @return true if at least one NPushSleep has been added
     */
    public boolean hasNPushSleep(
    ) {
        return this._has_nPushSleep;
    }

    /**
     * Method hasNScan.
     * 
     * @return true if at least one NScan has been added
     */
    public boolean hasNScan(
    ) {
        return this._has_nScan;
    }

    /**
     * Method hasNScanSleep.
     * 
     * @return true if at least one NScanSleep has been added
     */
    public boolean hasNScanSleep(
    ) {
        return this._has_nScanSleep;
    }

    /**
     * Method hasNStatusRetry.
     * 
     * @return true if at least one NStatusRetry has been added
     */
    public boolean hasNStatusRetry(
    ) {
        return this._has_nStatusRetry;
    }

    /**
     * Method hasTime.
     * 
     * @return true if at least one Time has been added
     */
    public boolean hasTime(
    ) {
        return this._has_time;
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
     * Sets the value of field 'mac'.
     * 
     * @param mac the value of field 'mac'.
     */
    public void setMac(
            final byte[] mac) {
        this._mac = mac;
    }

    /**
     * Sets the value of field 'nAssertReset'.
     * 
     * @param nAssertReset the value of field 'nAssertReset'.
     */
    public void setNAssertReset(
            final long nAssertReset) {
        this._nAssertReset = nAssertReset;
        this._has_nAssertReset = true;
    }

    /**
     * Sets the value of field 'nColdReset'.
     * 
     * @param nColdReset the value of field 'nColdReset'.
     */
    public void setNColdReset(
            final long nColdReset) {
        this._nColdReset = nColdReset;
        this._has_nColdReset = true;
    }

    /**
     * Sets the value of field 'nHotReset'.
     * 
     * @param nHotReset the value of field 'nHotReset'.
     */
    public void setNHotReset(
            final long nHotReset) {
        this._nHotReset = nHotReset;
        this._has_nHotReset = true;
    }

    /**
     * Sets the value of field 'nJoinWDT'.
     * 
     * @param nJoinWDT the value of field 'nJoinWDT'.
     */
    public void setNJoinWDT(
            final long nJoinWDT) {
        this._nJoinWDT = nJoinWDT;
        this._has_nJoinWDT = true;
    }

    /**
     * Sets the value of field 'nNetSleep'.
     * 
     * @param nNetSleep the value of field 'nNetSleep'.
     */
    public void setNNetSleep(
            final long nNetSleep) {
        this._nNetSleep = nNetSleep;
        this._has_nNetSleep = true;
    }

    /**
     * Sets the value of field 'nOtaReset'.
     * 
     * @param nOtaReset the value of field 'nOtaReset'.
     */
    public void setNOtaReset(
            final long nOtaReset) {
        this._nOtaReset = nOtaReset;
        this._has_nOtaReset = true;
    }

    /**
     * Sets the value of field 'nPowerupSleep'.
     * 
     * @param nPowerupSleep the value of field 'nPowerupSleep'.
     */
    public void setNPowerupSleep(
            final long nPowerupSleep) {
        this._nPowerupSleep = nPowerupSleep;
        this._has_nPowerupSleep = true;
    }

    /**
     * Sets the value of field 'nPushReset'.
     * 
     * @param nPushReset the value of field 'nPushReset'.
     */
    public void setNPushReset(
            final long nPushReset) {
        this._nPushReset = nPushReset;
        this._has_nPushReset = true;
    }

    /**
     * Sets the value of field 'nPushSleep'.
     * 
     * @param nPushSleep the value of field 'nPushSleep'.
     */
    public void setNPushSleep(
            final long nPushSleep) {
        this._nPushSleep = nPushSleep;
        this._has_nPushSleep = true;
    }

    /**
     * Sets the value of field 'nScan'.
     * 
     * @param nScan the value of field 'nScan'.
     */
    public void setNScan(
            final long nScan) {
        this._nScan = nScan;
        this._has_nScan = true;
    }

    /**
     * Sets the value of field 'nScanSleep'.
     * 
     * @param nScanSleep the value of field 'nScanSleep'.
     */
    public void setNScanSleep(
            final long nScanSleep) {
        this._nScanSleep = nScanSleep;
        this._has_nScanSleep = true;
    }

    /**
     * Sets the value of field 'nStatusRetry'.
     * 
     * @param nStatusRetry the value of field 'nStatusRetry'.
     */
    public void setNStatusRetry(
            final long nStatusRetry) {
        this._nStatusRetry = nStatusRetry;
        this._has_nStatusRetry = true;
    }

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(
            final long time) {
        this._time = time;
        this._has_time = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.EslStatistics
     */
    public static com.s5tech.net.xml.EslStatistics unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.EslStatistics) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.EslStatistics.class, reader);
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
