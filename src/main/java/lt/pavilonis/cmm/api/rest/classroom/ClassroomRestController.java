package lt.pavilonis.cmm.api.rest.classroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RequestMapping("/rest/classrooms")
@RestController
public class ClassroomRestController {

   private static final Logger LOG = LoggerFactory.getLogger(ClassroomRestController.class.getSimpleName());

   @Autowired
   private ClassroomRepository repository;

   @GetMapping
   public ResponseEntity<List<ClassroomOccupancy>> load(@RequestParam(required = false) List<Integer> levels) {

      if (levels == null) {
         LOG.warn("'levels' param was NULL");
         levels = Collections.emptyList();
      }

      List<ClassroomOccupancy> result = repository.loadActive(levels);

      return ResponseEntity.ok().body(result);
   }
}
