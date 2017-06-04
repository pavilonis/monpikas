package lt.pavilonis.cmm.api.rest.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rest/classrooms")
@RestController
public class ClassroomController {

   @Autowired
   private ClassroomRepository repository;

   @GetMapping
   public ResponseEntity<List<ClassroomOccupancy>> load() {

      List<ClassroomOccupancy> result = repository.loadActive();

      return ResponseEntity.ok().body(result);
   }
}
