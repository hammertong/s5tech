/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml.types;

/**
 * Enumeration MessageCommand.
 * 
 * @version $Revision$ $Date$
 */
public enum MessageCommand {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant SETSTOREKEY
     */
    SETSTOREKEY("SetStoreKey"),
    /**
     * Constant ADDESLLIST
     */
    ADDESLLIST("AddEslList"),
    /**
     * Constant REMOVEESLLIST
     */
    REMOVEESLLIST("RemoveEslList"),
    /**
     * Constant ESLPRICEUPDATE
     */
    ESLPRICEUPDATE("EslPriceUpdate"),
    /**
     * Constant ESLENTERNIGHTMODE
     */
    ESLENTERNIGHTMODE("EslEnterNightMode"),
    /**
     * Constant ESLSETACTIVESERVICEPAGE
     */
    ESLSETACTIVESERVICEPAGE("EslSetActiveServicePage"),
    /**
     * Constant ESLFIRMWAREUPDATE
     */
    ESLFIRMWAREUPDATE("EslFirmwareUpdate"),
    /**
     * Constant ESLSETALARMMODE
     */
    ESLSETALARMMODE("EslSetAlarmMode"),
    /**
     * Constant ESLEVENT
     */
    ESLEVENT("EslEvent"),
    /**
     * Constant ESLSTATUS
     */
    ESLSTATUS("EslStatus"),
    /**
     * Constant ESLSTATISTICS
     */
    ESLSTATISTICS("EslStatistics"),
    /**
     * Constant ESLSETCHANNELTOJOIN
     */
    ESLSETCHANNELTOJOIN("EslSetChannelToJoin"),
    /**
     * Constant ESLSTATISTICSREQUEST
     */
    ESLSTATISTICSREQUEST("EslStatisticsRequest"),
    /**
     * Constant ESLKILL
     */
    ESLKILL("EslKill"),
    /**
     * Constant ESLLEAVE
     */
    ESLLEAVE("EslLeave"),
    /**
     * Constant ESLSTATUSREQUEST
     */
    ESLSTATUSREQUEST("EslStatusRequest"),
    /**
     * Constant COORDINATOREVENT
     */
    COORDINATOREVENT("CoordinatorEvent"),
    /**
     * Constant HUBEVENT
     */
    HUBEVENT("HubEvent");

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
    private static final java.util.Map<java.lang.String, MessageCommand> enumConstants = new java.util.HashMap<java.lang.String, MessageCommand>();


    static {
        for (MessageCommand c: MessageCommand.values()) {
            MessageCommand.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private MessageCommand(final java.lang.String value) {
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
    public static com.s5tech.net.xml.types.MessageCommand fromValue(
            final java.lang.String value) {
        MessageCommand c = MessageCommand.enumConstants.get(value);
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
