/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration CommandResult.
 * 
 * @version $Revision$ $Date$
 */
public enum CommandResult {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant SUCCESS
     */
    SUCCESS("Success"),
    /**
     * Constant FAILURE
     */
    FAILURE("Failure"),
    /**
     * Constant TIMEOUT
     */
    TIMEOUT("Timeout"),
    /**
     * Constant NOROUTE
     */
    NOROUTE("NoRoute");

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
    private static final java.util.Map<java.lang.String, CommandResult> enumConstants = new java.util.HashMap<java.lang.String, CommandResult>();


    static {
        for (CommandResult c: CommandResult.values()) {
            CommandResult.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private CommandResult(final java.lang.String value) {
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
    public static com.s5tech.net.xml.types.CommandResult fromValue(
            final java.lang.String value) {
        CommandResult c = CommandResult.enumConstants.get(value);
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
