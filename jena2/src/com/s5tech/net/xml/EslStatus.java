/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslStatus.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EslStatus implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mac.
     */
    private byte[] _mac;

    /**
     * Field _hashCodeActivePrice.
     */
    private long _hashCodeActivePrice;

    /**
     * keeps track of state for field: _hashCodeActivePrice
     */
    private boolean _has_hashCodeActivePrice;

    /**
     * Field _hashCodePendingPrice.
     */
    private long _hashCodePendingPrice;

    /**
     * keeps track of state for field: _hashCodePendingPrice
     */
    private boolean _has_hashCodePendingPrice;

    /**
     * Field _batteryLevel.
     */
    private long _batteryLevel;

    /**
     * keeps track of state for field: _batteryLevel
     */
    private boolean _has_batteryLevel;

    /**
     * Field _txPower.
     */
    private long _txPower;

    /**
     * keeps track of state for field: _txPower
     */
    private boolean _has_txPower;

    /**
     * Field _macAssociatedCoordinator.
     */
    private byte[] _macAssociatedCoordinator;

    /**
     * Field _temperature.
     */
    private float _temperature;

    /**
     * keeps track of state for field: _temperature
     */
    private boolean _has_temperature;

    /**
     * Field _firmwareVersion.
     */
    private java.lang.String _firmwareVersion;

    /**
     * Field _lifetimeHours.
     */
    private long _lifetimeHours;

    /**
     * keeps track of state for field: _lifetimeHours
     */
    private boolean _has_lifetimeHours;

    /**
     * Field _channel.
     */
    private java.lang.String _channel;

    /**
     * Field _railDetected.
     */
    private boolean _railDetected;

    /**
     * keeps track of state for field: _railDetected
     */
    private boolean _has_railDetected;

    /**
     * Field _nightMode.
     */
    private boolean _nightMode;

    /**
     * keeps track of state for field: _nightMode
     */
    private boolean _has_nightMode;

    /**
     * Field _deviceType.
     */
    private long _deviceType;

    /**
     * keeps track of state for field: _deviceType
     */
    private boolean _has_deviceType;

    /**
     * Field _state.
     */
    private com.s5tech.net.xml.types.StateType _state;

    /**
     * Field _coordinatorsInRange.
     */
    private com.s5tech.net.xml.CoordinatorsInRange _coordinatorsInRange;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslStatus() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteBatteryLevel(
    ) {
        this._has_batteryLevel= false;
    }

    /**
     */
    public void deleteDeviceType(
    ) {
        this._has_deviceType= false;
    }

    /**
     */
    public void deleteHashCodeActivePrice(
    ) {
        this._has_hashCodeActivePrice= false;
    }

    /**
     */
    public void deleteHashCodePendingPrice(
    ) {
        this._has_hashCodePendingPrice= false;
    }

    /**
     */
    public void deleteLifetimeHours(
    ) {
        this._has_lifetimeHours= false;
    }

    /**
     */
    public void deleteNightMode(
    ) {
        this._has_nightMode= false;
    }

    /**
     */
    public void deleteRailDetected(
    ) {
        this._has_railDetected= false;
    }

    /**
     */
    public void deleteTemperature(
    ) {
        this._has_temperature= false;
    }

    /**
     */
    public void deleteTxPower(
    ) {
        this._has_txPower= false;
    }

    /**
     * Returns the value of field 'batteryLevel'.
     * 
     * @return the value of field 'BatteryLevel'.
     */
    public long getBatteryLevel(
    ) {
        return this._batteryLevel;
    }

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
     * Returns the value of field 'coordinatorsInRange'.
     * 
     * @return the value of field 'CoordinatorsInRange'.
     */
    public com.s5tech.net.xml.CoordinatorsInRange getCoordinatorsInRange(
    ) {
        return this._coordinatorsInRange;
    }

    /**
     * Returns the value of field 'deviceType'.
     * 
     * @return the value of field 'DeviceType'.
     */
    public long getDeviceType(
    ) {
        return this._deviceType;
    }

    /**
     * Returns the value of field 'firmwareVersion'.
     * 
     * @return the value of field 'FirmwareVersion'.
     */
    public java.lang.String getFirmwareVersion(
    ) {
        return this._firmwareVersion;
    }

    /**
     * Returns the value of field 'hashCodeActivePrice'.
     * 
     * @return the value of field 'HashCodeActivePrice'.
     */
    public long getHashCodeActivePrice(
    ) {
        return this._hashCodeActivePrice;
    }

    /**
     * Returns the value of field 'hashCodePendingPrice'.
     * 
     * @return the value of field 'HashCodePendingPrice'.
     */
    public long getHashCodePendingPrice(
    ) {
        return this._hashCodePendingPrice;
    }

    /**
     * Returns the value of field 'lifetimeHours'.
     * 
     * @return the value of field 'LifetimeHours'.
     */
    public long getLifetimeHours(
    ) {
        return this._lifetimeHours;
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
     * Returns the value of field 'macAssociatedCoordinator'.
     * 
     * @return the value of field 'MacAssociatedCoordinator'.
     */
    public byte[] getMacAssociatedCoordinator(
    ) {
        return this._macAssociatedCoordinator;
    }

    /**
     * Returns the value of field 'nightMode'.
     * 
     * @return the value of field 'NightMode'.
     */
    public boolean getNightMode(
    ) {
        return this._nightMode;
    }

    /**
     * Returns the value of field 'railDetected'.
     * 
     * @return the value of field 'RailDetected'.
     */
    public boolean getRailDetected(
    ) {
        return this._railDetected;
    }

    /**
     * Returns the value of field 'state'.
     * 
     * @return the value of field 'State'.
     */
    public com.s5tech.net.xml.types.StateType getState(
    ) {
        return this._state;
    }

    /**
     * Returns the value of field 'temperature'.
     * 
     * @return the value of field 'Temperature'.
     */
    public float getTemperature(
    ) {
        return this._temperature;
    }

    /**
     * Returns the value of field 'txPower'.
     * 
     * @return the value of field 'TxPower'.
     */
    public long getTxPower(
    ) {
        return this._txPower;
    }

    /**
     * Method hasBatteryLevel.
     * 
     * @return true if at least one BatteryLevel has been added
     */
    public boolean hasBatteryLevel(
    ) {
        return this._has_batteryLevel;
    }

    /**
     * Method hasDeviceType.
     * 
     * @return true if at least one DeviceType has been added
     */
    public boolean hasDeviceType(
    ) {
        return this._has_deviceType;
    }

    /**
     * Method hasHashCodeActivePrice.
     * 
     * @return true if at least one HashCodeActivePrice has been
     * added
     */
    public boolean hasHashCodeActivePrice(
    ) {
        return this._has_hashCodeActivePrice;
    }

    /**
     * Method hasHashCodePendingPrice.
     * 
     * @return true if at least one HashCodePendingPrice has been
     * added
     */
    public boolean hasHashCodePendingPrice(
    ) {
        return this._has_hashCodePendingPrice;
    }

    /**
     * Method hasLifetimeHours.
     * 
     * @return true if at least one LifetimeHours has been added
     */
    public boolean hasLifetimeHours(
    ) {
        return this._has_lifetimeHours;
    }

    /**
     * Method hasNightMode.
     * 
     * @return true if at least one NightMode has been added
     */
    public boolean hasNightMode(
    ) {
        return this._has_nightMode;
    }

    /**
     * Method hasRailDetected.
     * 
     * @return true if at least one RailDetected has been added
     */
    public boolean hasRailDetected(
    ) {
        return this._has_railDetected;
    }

    /**
     * Method hasTemperature.
     * 
     * @return true if at least one Temperature has been added
     */
    public boolean hasTemperature(
    ) {
        return this._has_temperature;
    }

    /**
     * Method hasTxPower.
     * 
     * @return true if at least one TxPower has been added
     */
    public boolean hasTxPower(
    ) {
        return this._has_txPower;
    }

    /**
     * Returns the value of field 'nightMode'.
     * 
     * @return the value of field 'NightMode'.
     */
    public boolean isNightMode(
    ) {
        return this._nightMode;
    }

    /**
     * Returns the value of field 'railDetected'.
     * 
     * @return the value of field 'RailDetected'.
     */
    public boolean isRailDetected(
    ) {
        return this._railDetected;
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
     * Sets the value of field 'batteryLevel'.
     * 
     * @param batteryLevel the value of field 'batteryLevel'.
     */
    public void setBatteryLevel(
            final long batteryLevel) {
        this._batteryLevel = batteryLevel;
        this._has_batteryLevel = true;
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
     * Sets the value of field 'coordinatorsInRange'.
     * 
     * @param coordinatorsInRange the value of field
     * 'coordinatorsInRange'.
     */
    public void setCoordinatorsInRange(
            final com.s5tech.net.xml.CoordinatorsInRange coordinatorsInRange) {
        this._coordinatorsInRange = coordinatorsInRange;
    }

    /**
     * Sets the value of field 'deviceType'.
     * 
     * @param deviceType the value of field 'deviceType'.
     */
    public void setDeviceType(
            final long deviceType) {
        this._deviceType = deviceType;
        this._has_deviceType = true;
    }

    /**
     * Sets the value of field 'firmwareVersion'.
     * 
     * @param firmwareVersion the value of field 'firmwareVersion'.
     */
    public void setFirmwareVersion(
            final java.lang.String firmwareVersion) {
        this._firmwareVersion = firmwareVersion;
    }

    /**
     * Sets the value of field 'hashCodeActivePrice'.
     * 
     * @param hashCodeActivePrice the value of field
     * 'hashCodeActivePrice'.
     */
    public void setHashCodeActivePrice(
            final long hashCodeActivePrice) {
        this._hashCodeActivePrice = hashCodeActivePrice;
        this._has_hashCodeActivePrice = true;
    }

    /**
     * Sets the value of field 'hashCodePendingPrice'.
     * 
     * @param hashCodePendingPrice the value of field
     * 'hashCodePendingPrice'.
     */
    public void setHashCodePendingPrice(
            final long hashCodePendingPrice) {
        this._hashCodePendingPrice = hashCodePendingPrice;
        this._has_hashCodePendingPrice = true;
    }

    /**
     * Sets the value of field 'lifetimeHours'.
     * 
     * @param lifetimeHours the value of field 'lifetimeHours'.
     */
    public void setLifetimeHours(
            final long lifetimeHours) {
        this._lifetimeHours = lifetimeHours;
        this._has_lifetimeHours = true;
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
     * Sets the value of field 'macAssociatedCoordinator'.
     * 
     * @param macAssociatedCoordinator the value of field
     * 'macAssociatedCoordinator'.
     */
    public void setMacAssociatedCoordinator(
            final byte[] macAssociatedCoordinator) {
        this._macAssociatedCoordinator = macAssociatedCoordinator;
    }

    /**
     * Sets the value of field 'nightMode'.
     * 
     * @param nightMode the value of field 'nightMode'.
     */
    public void setNightMode(
            final boolean nightMode) {
        this._nightMode = nightMode;
        this._has_nightMode = true;
    }

    /**
     * Sets the value of field 'railDetected'.
     * 
     * @param railDetected the value of field 'railDetected'.
     */
    public void setRailDetected(
            final boolean railDetected) {
        this._railDetected = railDetected;
        this._has_railDetected = true;
    }

    /**
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
     */
    public void setState(
            final com.s5tech.net.xml.types.StateType state) {
        this._state = state;
    }

    /**
     * Sets the value of field 'temperature'.
     * 
     * @param temperature the value of field 'temperature'.
     */
    public void setTemperature(
            final float temperature) {
        this._temperature = temperature;
        this._has_temperature = true;
    }

    /**
     * Sets the value of field 'txPower'.
     * 
     * @param txPower the value of field 'txPower'.
     */
    public void setTxPower(
            final long txPower) {
        this._txPower = txPower;
        this._has_txPower = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.EslStatus
     */
    public static com.s5tech.net.xml.EslStatus unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.EslStatus) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.EslStatus.class, reader);
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
