/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration CoordinatorEventType.
 * 
 * @version $Revision$ $Date$
 */
public enum CoordinatorEventType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant ONLINE
     */
    ONLINE("Online"),
    /**
     * Constant OFFLINE
     */
    OFFLINE("Offline"),
    /**
     * Constant TIMEOUT
     */
    TIMEOUT("Timeout"),
    /**
     * Constant CHANNELCHANGED
     */
    CHANNELCHANGED("ChannelChanged");

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
    private static final java.util.Map<java.lang.String, CoordinatorEventType> enumConstants = new java.util.HashMap<java.lang.String, CoordinatorEventType>();


    static {
        for (CoordinatorEventType c: CoordinatorEventType.values()) {
            CoordinatorEventType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private CoordinatorEventType(final java.lang.String value) {
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
    public static com.s5tech.net.xml.types.CoordinatorEventType fromValue(
            final java.lang.String value) {
        CoordinatorEventType c = CoordinatorEventType.enumConstants.get(value);
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
