package lt.pavilonis.cmm.canteen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

   @RequestMapping("/login")
   public void showLoginScreen(@RequestParam(defaultValue = "false", required = false) boolean error, Model m) {
      m.addAttribute("error", error);
   }

   @RequestMapping()
   public String redirect() {
      return "redirect:app";
   }
}