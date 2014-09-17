package lt.pavilonis.monpikas.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

   @RequestMapping("/login")
   public String showLoginScreen() {
      return "login";
   }
}