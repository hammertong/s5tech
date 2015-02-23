/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration EslEventType.
 * 
 * @version $Revision$ $Date$
 */
public enum EslEventType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant JOINED
     */
    JOINED("Joined"),
    /**
     * Constant LOST
     */
    LOST("Lost"),
    /**
     * Constant REJOINED
     */
    REJOINED("Rejoined"),
    /**
     * Constant UNAUTHORIZEDJOINATTEMPT
     */
    UNAUTHORIZEDJOINATTEMPT("UnauthorizedJoinAttempt"),
    /**
     * Constant KEYCHANGE
     */
    KEYCHANGE("KeyChange"),
    /**
     * Constant PRICEUNKNOWN
     */
    PRICEUNKNOWN("PriceUnknown"),
    /**
     * Constant SCANPROBE
     */
    SCANPROBE("Scanprobe"),
    /**
     * Constant RAILALARM
     */
    RAILALARM("RailAlarm");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, EslEventType> enumConstants = new java.util.HashMap<java.lang.String, EslEventType>();


    static {
        for (EslEventType c: EslEventType.values()) {
            EslEventType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private EslEventType(final java.lang.String value) {
        this.value = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.s5tech.net.xml.types.EslEventType fromValue(
            final java.lang.String value) {
        EslEventType c = EslEventType.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * 
     * 
     * @param value
     */
    public void setValue(
            final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString(
    ) {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value(
    ) {
        return this.value;
    }

}
