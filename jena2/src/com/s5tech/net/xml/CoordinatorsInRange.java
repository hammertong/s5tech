/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.s5tech.net.xml;

/**
 * Class CoordinatorsInRange.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class CoordinatorsInRange implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _coordinatorList.
     */
    private java.util.List<com.s5tech.net.xml.Coordinator> _coordinatorList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordinatorsInRange() {
        super();
        this._coordinatorList = new java.util.ArrayList<com.s5tech.net.xml.Coordinator>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCoordinator
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCoordinator(
            final com.s5tech.net.xml.Coordinator vCoordinator)
    throws java.lang.IndexOutOfBoundsException {
        this._coordinatorList.add(vCoordinator);
    }

    /**
     * 
     * 
     * @param index
     * @param vCoordinator
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCoordinator(
            final int index,
            final com.s5tech.net.xml.Coordinator vCoordinator)
    throws java.lang.IndexOutOfBoundsException {
        this._coordinatorList.add(index, vCoordinator);
    }

    /**
     * Method enumerateCoordinator.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.s5tech.net.xml.Coordinator> enumerateCoordinator(
    ) {
        return java.util.Collections.enumeration(this._coordinatorList);
    }

    /**
     * Method getCoordinator.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.s5tech.net.xml.Coordinator at
     * the given index
     */
    public com.s5tech.net.xml.Coordinator getCoordinator(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._coordinatorList.size()) {
            throw new IndexOutOfBoundsException("getCoordinator: Index value '" + index + "' not in range [0.." + (this._coordinatorList.size() - 1) + "]");
        }

        return (com.s5tech.net.xml.Coordinator) _coordinatorList.get(index);
    }

    /**
     * Method getCoordinator.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.s5tech.net.xml.Coordinator[] getCoordinator(
    ) {
        com.s5tech.net.xml.Coordinator[] array = new com.s5tech.net.xml.Coordinator[0];
        return (com.s5tech.net.xml.Coordinator[]) this._coordinatorList.toArray(array);
    }

    /**
     * Method getCoordinatorCount.
     * 
     * @return the size of this collection
     */
    public int getCoordinatorCount(
    ) {
        return this._coordinatorList.size();
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
     * Method iterateCoordinator.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.s5tech.net.xml.Coordinator> iterateCoordinator(
    ) {
        return this._coordinatorList.iterator();
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
    public void removeAllCoordinator(
    ) {
        this._coordinatorList.clear();
    }

    /**
     * Method removeCoordinator.
     * 
     * @param vCoordinator
     * @return true if the object was removed from the collection.
     */
    public boolean removeCoordinator(
            final com.s5tech.net.xml.Coordinator vCoordinator) {
        boolean removed = _coordinatorList.remove(vCoordinator);
        return removed;
    }

    /**
     * Method removeCoordinatorAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.s5tech.net.xml.Coordinator removeCoordinatorAt(
            final int index) {
        java.lang.Object obj = this._coordinatorList.remove(index);
        return (com.s5tech.net.xml.Coordinator) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCoordinator
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCoordinator(
            final int index,
            final com.s5tech.net.xml.Coordinator vCoordinator)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._coordinatorList.size()) {
            throw new IndexOutOfBoundsException("setCoordinator: Index value '" + index + "' not in range [0.." + (this._coordinatorList.size() - 1) + "]");
        }

        this._coordinatorList.set(index, vCoordinator);
    }

    /**
     * 
     * 
     * @param vCoordinatorArray
     */
    public void setCoordinator(
            final com.s5tech.net.xml.Coordinator[] vCoordinatorArray) {
        //-- copy array
        _coordinatorList.clear();

        for (int i = 0; i < vCoordinatorArray.length; i++) {
                this._coordinatorList.add(vCoordinatorArray[i]);
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
     * @return the unmarshaled com.s5tech.net.xml.CoordinatorsInRang
     */
    public static com.s5tech.net.xml.CoordinatorsInRange unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.s5tech.net.xml.CoordinatorsInRange) org.exolab.castor.xml.Unmarshaller.unmarshal(com.s5tech.net.xml.CoordinatorsInRange.class, reader);
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
