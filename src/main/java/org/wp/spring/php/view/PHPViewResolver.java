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

import com.caucho.quercus.QuercusRuntimeException;
import com.caucho.servlets.webdav.FilePath;
import javax.servlet.ServletContext;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 *
 * @author Paul Shishakov <paulandweb@gmail.com>
 *
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
