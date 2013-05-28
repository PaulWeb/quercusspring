/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php.view;

import com.caucho.quercus.QuercusRuntimeException;
import com.caucho.servlets.webdav.FilePath;
import javax.servlet.ServletContext;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 *
 * @author Paul Shishakov
 * @e-mail paulandweb@gmail.com
 */
public class PHPViewResolver extends AbstractTemplateViewResolver {
        public PHPViewResolver() {
		setViewClass(requiredViewClass());
	}
       

	/**
	 * Requires {@link PHPView}.
	 */
	@Override
	protected Class requiredViewClass() {
		return PHPView.class;
	}
}
