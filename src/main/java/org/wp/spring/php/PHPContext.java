/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.vfs.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paul Shishakov
 * @e-mail:paulandweb@gmail.com
 */
public class PHPContext {

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
