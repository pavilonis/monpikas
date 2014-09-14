package lt.pavilonis.monpikas.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceController {

   @RequestMapping("hello")
   public String go() {
      System.out.println("got request");
      return "hello";
   }
}
