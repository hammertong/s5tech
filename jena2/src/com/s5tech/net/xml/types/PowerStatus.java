/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration PowerStatus.
 * 
 * @version $Revision$ $Date$
 */
public enum PowerStatus {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant OFF
     */
    OFF("Off"),
    /**
     * Constant AC
     */
    AC("Ac"),
    /**
     * Constant BATTERY
     */
    BATTERY("Battery"),
    /**
     * Constant LOWBATTERY
     */
    LOWBATTERY("LowBattery");

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
    private static final java.util.Map<java.lang.String, PowerStatus> enumConstants = new java.util.HashMap<java.lang.String, PowerStatus>();


    static {
        for (PowerStatus c: PowerStatus.values()) {
            PowerStatus.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private PowerStatus(final java.lang.String value) {
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
    public static com.s5tech.net.xml.types.PowerStatus fromValue(
            final java.lang.String value) {
        PowerStatus c = PowerStatus.enumConstants.get(value);
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
