package lt.pavilonis.cmm.api.rest.classroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

   private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomRestController.class);
   private final ClassroomRepository repository;

   public ClassroomRestController(ClassroomRepository repository) {
      this.repository = repository;
   }

   @GetMapping
   public ResponseEntity<List<ClassroomOccupancy>> load(@RequestParam(required = false) List<Integer> levels,
                                                        @RequestParam(required = false) String building) {
      if (levels == null) {
         LOGGER.warn("'levels' param was NULL");
         return ResponseEntity.ok().body(Collections.emptyList());
      }

      List<ClassroomOccupancy> result = repository.loadActive(levels, building);
      return ResponseEntity.ok().body(result);
   }
}
