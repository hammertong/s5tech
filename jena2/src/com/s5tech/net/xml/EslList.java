/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class EslList.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EslList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _all.
     */
    private boolean _all;

    /**
     * keeps track of state for field: _all
     */
    private boolean _has_all;

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _macList.
     */
    private java.util.List<byte[]> _macList;

    /**
     * Field _eslItemList.
     */
    private java.util.List<com.s5tech.net.xml.EslItem> _eslItemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EslList() {
        super();
        this._macList = new java.util.ArrayList<byte[]>();
        this._eslItemList = new java.util.ArrayList<com.s5tech.net.xml.EslItem>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vEslItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEslItem(
            final com.s5tech.net.xml.EslItem vEslItem)
    throws java.lang.IndexOutOfBoundsException {
        this._eslItemList.add(vEslItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vEslItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEslItem(
            final int index,
            final com.s5tech.net.xml.EslItem vEslItem)
    throws java.lang.IndexOutOfBoundsException {
        this._eslItemList.add(index, vEslItem);
    }

    /**
     * 
     * 
     * @param vMac
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMac(
            final byte[] vMac)
    throws java.lang.IndexOutOfBoundsException {
        this._macList.add(vMac);
    }

    /**
     * 
     * 
     * @param index
     * @param vMac
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMac(
            final int index,
            final byte[] vMac)
    throws java.lang.IndexOutOfBoundsException {
        this._macList.add(index, vMac);
    }

    /**
     */
    public void deleteAll(
    ) {
        this._has_all= false;
    }

    /**
     * Method enumerateEslItem.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.s5tech.net.xml.EslItem> enumerateEslItem(
    ) {
        return java.util.Collections.enumeration(this._eslItemList);
    }

    /**
     * Method enumerateMac.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends byte[]> enumerateMac(
    ) {
        return java.util.Collections.enumeration(this._macList);
    }

    /**
     * Returns the value of field 'all'.
     * 
     * @return the value of field 'All'.
     */
    public boolean getAll(
    ) {
        return this._all;
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Method getEslItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.s5tech.net.xml.EslItem at the
     * given index
     */
    public com.s5tech.net.xml.EslItem getEslItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eslItemList.size()) {
            throw new IndexOutOfBoundsException("getEslItem: Index value '" + index + "' not in range [0.." + (this._eslItemList.size() - 1) + "]");
        }

        return (com.s5tech.net.xml.EslItem) _eslItemList.get(index);
    }

    /**
     * Method getEslItem.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.s5tech.net.xml.EslItem[] getEslItem(
    ) {
        com.s5tech.net.xml.EslItem[] array = new com.s5tech.net.xml.EslItem[0];
        return (com.s5tech.net.xml.EslItem[]) this._eslItemList.toArray(array);
    }

    /**
     * Method getEslItemCount.
     * 
     * @return the size of this collection
     */
    public int getEslItemCount(
    ) {
        return this._eslItemList.size();
    }

    /**
     * Method getMac.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the byte[] at the given index
     */
    public byte[] getMac(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._macList.size()) {
            throw new IndexOutOfBoundsException("getMac: Index value '" + index + "' not in range [0.." + (this._macList.size() - 1) + "]");
        }

        return (byte[]) _macList.get(index);
    }

    /**
     * Method getMac.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public byte[][] getMac(
    ) {
        byte[][] array = new byte[0][];
        return (byte[][]) this._macList.toArray(array);
    }

    /**
     * Method getMacCount.
     * 
     * @return the size of this collection
     */
    public int getMacCount(
    ) {
        return this._macList.size();
    }

    /**
     * Method hasAll.
     * 
     * @return true if at least one All has been added
     */
    public boolean hasAll(
    ) {
        return this._has_all;
    }

    /**
     * Returns the value of field 'all'.
     * 
     * @return the value of field 'All'.
     */
    public boolean isAll(
    ) {
        return this._all;
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
     * Method iterateEslItem.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.s5tech.net.xml.EslItem> iterateEslItem(
    ) {
        return this._eslItemList.iterator();
    }

    /**
     * Method iterateMac.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends byte[]> iterateMac(
    ) {
        return this._macList.iterator();
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
    public void removeAllEslItem(
    ) {
        this._eslItemList.clear();
    }

    /**
     */
    public void removeAllMac(
    ) {
        this._macList.clear();
    }

    /**
     * Method removeEslItem.
     * 
     * @param vEslItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeEslItem(
            final com.s5tech.net.xml.EslItem vEslItem) {
        boolean removed = _eslItemList.remove(vEslItem);
        return removed;
    }

    /**
     * Method removeEslItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.s5tech.net.xml.EslItem removeEslItemAt(
            final int index) {
        java.lang.Object obj = this._eslItemList.remove(index);
        return (com.s5tech.net.xml.EslItem) obj;
    }

    /**
     * Method removeMac.
     * 
     * @param vMac
     * @return true if the object was removed from the collection.
     */
    public boolean removeMac(
            final byte[] vMac) {
        boolean removed = _macList.remove(vMac);
        return removed;
    }

    /**
     * Method removeMacAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public byte[] removeMacAt(
            final int index) {
        java.lang.Object obj = this._macList.remove(index);
        return (byte[]) obj;
    }

    /**
     * Sets the value of field 'all'.
     * 
     * @param all the value of field 'all'.
     */
    public void setAll(
            final boolean all) {
        this._all = all;
        this._has_all = true;
    }

    /**
     * 
     * 
     * @param index
     * @param vEslItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEslItem(
            final int index,
            final com.s5tech.net.xml.EslItem vEslItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eslItemList.size()) {
            throw new IndexOutOfBoundsException("setEslItem: Index value '" + index + "' not in range [0.." + (this._eslItemList.size() - 1) + "]");
        }

        this._eslItemList.set(index, vEslItem);
    }

    /**
     * 
     * 
     * @param vEslItemArray
     */
    public void setEslItem(
            final com.s5tech.net.xml.EslItem[] vEslItemArray) {
        //-- copy array
        _eslItemList.clear();

        for (int i = 0; i < vEslItemArray.length; i++) {
                this._eslItemList.add(vEslItemArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vMac
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setMac(
            final int index,
            final byte[] vMac)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._macList.size()) {
            throw new IndexOutOfBoundsException("setMac: Index value '" + index + "' not in range [0.." + (this._macList.size() - 1) + "]");
        }

        this._macList.set(index, vMac);
    }

    /**
     * 
     * 
     * @param vMacArray
     */
    public void setMac(
            final byte[][] vMacArray) {
        //-- copy array
        _macList.clear();

        for (int i = 0; i < vMacArray.length; i++) {
                this._macList.add(vMacArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.s5tech.net.xml.EslList
     */
    public static com.s5tech.net.xml.EslList unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.EslList) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.EslList.class, reader);
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
