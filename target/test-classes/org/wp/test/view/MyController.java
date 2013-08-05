package org.wp.test.view;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




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
 * @author Paul Shishakov
 * @e-mail paulandweb@gmail.com
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
        return "test-templ";
    }
    
}
