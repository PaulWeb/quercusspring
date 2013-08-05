/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.test.spring.php;

import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface PrimitiveTypesContainer {
    // getters
    int getInt();
    double getFloat();
    boolean getBool();
    String getString();
    List getArray();
    Object getNull();
    Object getObject();
    Map getMap();
    // setters
    Object setNull(Object x);
    boolean setBoolean(boolean x);
    long setByte(byte x);
    long setShort(short x);
    long setInt(int x);
    long setLong(long x);
    double setFloat(float x);
    double setDouble(double x);
    String setString(String x);
    String setChar(char x);
    String setCharArray(char[] x);
    String setByteArray(byte[] x);
    long[] setArray(int[] x);
    List setList(List x);
    Map setMap(Map x);
    Object setObject(Object x);
}
