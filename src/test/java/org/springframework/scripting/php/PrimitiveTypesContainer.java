/*
 * Copyright (c) 2013 World Page Company -- all rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.scripting.php;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 * 
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
    List<Long> setArray(int[] x);
    int setCalendar(Calendar x);
    int setDate(Date x);
    URL setURL(URL x);
    List setList(List x);
    Map setMap(Map x);
    Object setObject(Object x);
}
