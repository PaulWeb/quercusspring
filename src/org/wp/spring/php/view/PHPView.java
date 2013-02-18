/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php.view;

import com.caucho.quercus.QuercusRequestAdapter;
import com.caucho.quercus.QuercusRuntimeException;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.vfs.FilePath;
import com.caucho.vfs.Path;
import com.caucho.vfs.StreamImpl;
import com.caucho.vfs.VfsStream;
import com.caucho.vfs.WriteStream;
import com.caucho.vfs.WriterStreamImpl;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.wp.spring.php.*;
import java.lang.instrument.Instrumentation;


/**
 *
 * @author Paul Shishakov
 * @e-mail paulandweb@gmail.com
 */
public class PHPView extends AbstractTemplateView {

    private Logger _log = Logger.getLogger(PHPView.class);
    private PHPContext _context;
    
  
    
    public void setContext(PHPContext context){
        this._context=context;
    }
    
    public PHPContext getContext(){
        return this._context;
    }
    protected void checkServletAPIVersion(ServletContext servletContext) {
        int major = servletContext.getMajorVersion();
        int minor = servletContext.getMinorVersion();

        if ((major < 2) || ((major == 2) && (minor < 4))) {
            throw new QuercusRuntimeException("Quercus requires Servlet API 2.4+.");
        }
    }

    @Override
    protected void initServletContext(ServletContext servletContext) {
        checkServletAPIVersion(servletContext);
        if(_context==null){
            _context=BeanFactoryUtils.beanOfTypeIncludingAncestors(
					getApplicationContext(), PHPContext.class, true, false);
        }
       // _context.getContext().setPwd(new FilePath(servletContext.getRealPath("/")));
        _context.getContext().init();
        _context.getContext().setServletContext(servletContext);
    }

    
    @Override
    protected void renderMergedTemplateModel(Map<String, Object> map, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        Env env = null;
        WriteStream ws = null;
        String scriptPath = getUrl();
        try {
            Path path = getPath(hsr);
            if (_context.isPageModified(scriptPath)) {
                _context.putPage(scriptPath, path);
            }
            QuercusPage page = _context.getPage(scriptPath);           
            StreamImpl out;
            try {
                out = new VfsStream(null, hsr1.getOutputStream());
            } catch (IllegalStateException e) {
                WriterStreamImpl writer = new WriterStreamImpl();
                writer.setWriter(hsr1.getWriter());
                out = writer;
            }
            ws = new WriteStream(out);
            ws.setNewlineString("\n");
            long t=Runtime.getRuntime().freeMemory();
            env=_context.getContext().createEnv(page, ws, hsr, hsr1);
             for (Iterator i$ = map.entrySet().iterator(); i$.hasNext(); ) { 
                 Object entryObj = i$.next();
                 Map.Entry entry = (Map.Entry)entryObj;       
                env.setGlobalValue((String)entry.getKey(), env.wrapJava(entry.getValue()));
              }             
             env.start();  
             
             env.execute();    
             long t1=Runtime.getRuntime().freeMemory();
             _log.info(t-t1);

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
            hsr1.sendError(404);
        }finally
        {
            if(env!=null){
                env.close();
            }
            if(ws!=null){
                ws.close();
            }
        }

    }

    private Path getPath(HttpServletRequest req) {
        String scriptPath = getUrl();
        return new FilePath(req.getRealPath(scriptPath));
    }
   
}
