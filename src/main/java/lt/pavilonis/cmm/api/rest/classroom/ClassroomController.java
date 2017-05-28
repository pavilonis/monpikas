package lt.pavilonis.cmm.api.rest.classroom;

import lt.pavilonis.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/classrooms")
@RestController
public class ClassroomController {
   private static final Logger LOG = LoggerFactory.getLogger(ClassroomController.class.getSimpleName());

   @Autowired
   private ClassroomRepository repository;

   @GetMapping
   public ResponseEntity<List<ClassroomOccupancy>> load() {

      LocalDateTime opStart = LocalDateTime.now();
      List<ClassroomOccupancy> result = repository.loadActive();
      LOG.info("GET [number={}, duration={}]", result.size(), TimeUtils.duration(opStart));

      return ResponseEntity.ok().body(result);
   }
}
