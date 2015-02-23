/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration StateType.
 * 
 * @version $Revision$ $Date$
 */
public enum StateType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant UNKNOWN
     */
    UNKNOWN("Unknown"),
    /**
     * Constant STANDBY
     */
    STANDBY("Standby"),
    /**
     * Constant OPERATIVE
     */
    OPERATIVE("Operative"),
    /**
     * Constant ALARM
     */
    ALARM("Alarm");

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
    private static final java.util.Map<java.lang.String, StateType> enumConstants = new java.util.HashMap<java.lang.String, StateType>();


    static {
        for (StateType c: StateType.values()) {
            StateType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private StateType(final java.lang.String value) {
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
    public static com.s5tech.net.xml.types.StateType fromValue(
            final java.lang.String value) {
        StateType c = StateType.enumConstants.get(value);
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
