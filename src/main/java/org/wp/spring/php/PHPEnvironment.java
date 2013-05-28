/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.env.Env;

/**
 *
 * @author Paul Shishakov
 * @e-mail: paulandweb@gmail.com
 */
public class PHPEnvironment {
    private Env _env;
    private PHPContext _context;
 
   public void setContext(PHPContext php){
       this._context=php;
   }
   public PHPContext getContext(){
       return _context;
   }
   public Env getEnvironment(){
       return _env;
   }
   public void init(){
       if(this._context==null){
           throw new NullPointerException("Don't set context!");
       }
       _env=new Env(_context.getContext());
      _env.setRuntimeEncoding("UTF-8");
       _env.start();
   }
   public void destroy(){
       if(_env!=null){
           _env.close();
       }
   }
}
