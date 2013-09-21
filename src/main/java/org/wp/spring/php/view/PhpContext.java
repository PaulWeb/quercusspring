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
package org.wp.spring.php.view;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.vfs.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paul Shishakov <paulandweb@gmail.com>
 *
 */
public class PhpContext {

    private QuercusContext _context;
    private String _scriptPath="";
    private Map<String,QuercusPage> _pages=new HashMap<String,QuercusPage>();
    public QuercusPage getPage(String name){
        return _pages.get(name);        
    }
    public boolean hasPage(String name){
        return _pages.containsKey(name);
    }
    public synchronized  void putPage(String name,Path path) throws IOException{
       if(isPageModified(name)){
        QuercusPage page=_context.parse(path);
        if(_pages.containsKey(name)){
            _pages.remove(name);
        }
        _pages.put(name, page);
       }
    }

    public String getScriptPath() {
        return _scriptPath;
    }

    public void setScriptPath(String _scriptPath) {
        this._scriptPath = _scriptPath;
    }
    
    
    public boolean isPageModified(String name){
        boolean result=true;
        if(_pages.containsKey(name)){
            result=_pages.get(name).isModified();
        }
        return result;
    }
    public void init() {
        _context = new QuercusContext();
        _context.init();
        _context.start();
    }

    public QuercusContext getContext() {
        return this._context;
    }

    public void destroy() {
        if (_context != null) {
            _context.close();           
        }
         _pages.clear();
    }
}
