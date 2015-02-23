/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class Message.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Message implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mac.
     */
    private byte[] _mac;

    /**
     * Field _msgId.
     */
    private java.lang.String _msgId;

    /**
     * Field _msgCommand.
     */
    private com.s5tech.net.xml.types.MessageCommand _msgCommand;

    /**
     * Field _creationTime.
     */
    private java.util.Date _creationTime;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _anyObject.
     */
    private java.util.List<java.lang.Object> _anyObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public Message() {
        super();
        setContent("");
        this._anyObject = new java.util.ArrayList<java.lang.Object>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAnyObject
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAnyObject(
            final java.lang.Object vAnyObject)
    throws java.lang.IndexOutOfBoundsException {
        this._anyObject.add(vAnyObject);
    }

    /**
     * 
     * 
     * @param index
     * @param vAnyObject
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAnyObject(
            final int index,
            final java.lang.Object vAnyObject)
    throws java.lang.IndexOutOfBoundsException {
        this._anyObject.add(index, vAnyObject);
    }

    /**
     * Method enumerateAnyObject.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends java.lang.Object> enumerateAnyObject(
    ) {
        return java.util.Collections.enumeration(this._anyObject);
    }

    /**
     * Method getAnyObject.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.Object at the given index
     */
    public java.lang.Object getAnyObject(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._anyObject.size()) {
            throw new IndexOutOfBoundsException("getAnyObject: Index value '" + index + "' not in range [0.." + (this._anyObject.size() - 1) + "]");
        }

        return (java.lang.Object) _anyObject.get(index);
    }

    /**
     * Method getAnyObject.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.Object[] getAnyObject(
    ) {
        java.lang.Object[] array = new java.lang.Object[0];
        return (java.lang.Object[]) this._anyObject.toArray(array);
    }

    /**
     * Method getAnyObjectCount.
     * 
     * @return the size of this collection
     */
    public int getAnyObjectCount(
    ) {
        return this._anyObject.size();
    }

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public java.lang.String getContent(
    ) {
        return this._content;
    }

    /**
     * Returns the value of field 'creationTime'.
     * 
     * @return the value of field 'CreationTime'.
     */
    public java.util.Date getCreationTime(
    ) {
        return this._creationTime;
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
     * Returns the value of field 'msgCommand'.
     * 
     * @return the value of field 'MsgCommand'.
     */
    public com.s5tech.net.xml.types.MessageCommand getMsgCommand(
    ) {
        return this._msgCommand;
    }

    /**
     * Returns the value of field 'msgId'.
     * 
     * @return the value of field 'MsgId'.
     */
    public java.lang.String getMsgId(
    ) {
        return this._msgId;
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
     * Method iterateAnyObject.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends java.lang.Object> iterateAnyObject(
    ) {
        return this._anyObject.iterator();
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
     */
    public void removeAllAnyObject(
    ) {
        this._anyObject.clear();
    }

    /**
     * Method removeAnyObject.
     * 
     * @param vAnyObject
     * @return true if the object was removed from the collection.
     */
    public boolean removeAnyObject(
            final java.lang.Object vAnyObject) {
        boolean removed = _anyObject.remove(vAnyObject);
        return removed;
    }

    /**
     * Method removeAnyObjectAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.Object removeAnyObjectAt(
            final int index) {
        java.lang.Object obj = this._anyObject.remove(index);
        return (java.lang.Object) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAnyObject
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAnyObject(
            final int index,
            final java.lang.Object vAnyObject)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._anyObject.size()) {
            throw new IndexOutOfBoundsException("setAnyObject: Index value '" + index + "' not in range [0.." + (this._anyObject.size() - 1) + "]");
        }

        this._anyObject.set(index, vAnyObject);
    }

    /**
     * 
     * 
     * @param vAnyObjectArray
     */
    public void setAnyObject(
            final java.lang.Object[] vAnyObjectArray) {
        //-- copy array
        _anyObject.clear();

        for (int i = 0; i < vAnyObjectArray.length; i++) {
                this._anyObject.add(vAnyObjectArray[i]);
        }
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(
            final java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'creationTime'.
     * 
     * @param creationTime the value of field 'creationTime'.
     */
    public void setCreationTime(
            final java.util.Date creationTime) {
        this._creationTime = creationTime;
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
     * Sets the value of field 'msgCommand'.
     * 
     * @param msgCommand the value of field 'msgCommand'.
     */
    public void setMsgCommand(
            final com.s5tech.net.xml.types.MessageCommand msgCommand) {
        this._msgCommand = msgCommand;
    }

    /**
     * Sets the value of field 'msgId'.
     * 
     * @param msgId the value of field 'msgId'.
     */
    public void setMsgId(
            final java.lang.String msgId) {
        this._msgId = msgId;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.Message
     */
    public static com.s5tech.net.xml.Message unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.Message) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.Message.class, reader);
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
