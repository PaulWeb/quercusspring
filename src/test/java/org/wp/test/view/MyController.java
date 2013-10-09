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
package org.wp.test.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author Paul Shishakov <paulandweb@gmail.com>
 *
 */
@Controller
public class MyController  {

    //@Inject
    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String handleRequestInternal( ModelMap model) throws Exception {
      //  ModelAndView model = new ModelAndView("test-templ");
        model.addAttribute("msg", "PHP and SPRING");
         model.addAttribute("msg1", "PHP");
        //Provider<Object> obj=null;
        return "org/wp/test/view/test-templ";
    }
    
}
