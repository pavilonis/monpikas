package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.domain.Message;
import lt.pavilonis.monpikas.server.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

   @RequestMapping(value = "messages", method = RequestMethod.POST)
   public String saveMessage(@ModelAttribute Message msg) {
      messageRepository.save(msg);
      return "redirect:/accepted";
   }

   @ResponseBody
   @RequestMapping(value="messages", produces = "application/json")
   public List<Message> showMessages(Model m) {
      System.out.println("got message request");
      return messageRepository.findAll();
   }

   @RequestMapping("/messages/{id}")
   public String deleteMessage(@PathVariable("id") Long id) {
      messageRepository.delete(messageRepository.findOne(id));
      return "redirect:/messages";
   }

   @Autowired
   MessageRepository messageRepository;
}