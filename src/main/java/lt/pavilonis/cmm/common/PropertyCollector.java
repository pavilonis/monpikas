package lt.pavilonis.cmm.common;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyCollector {
   public static List<String> collect(Class<?> type) {
      return Stream.of(type.getMethods())
            .map(Method::getName)
            .filter(name -> name.startsWith("get") || name.startsWith("is"))
            .map(name -> name.startsWith("is") ? name.substring(2) : name.substring(3))
            .map(String::toLowerCase)
            .collect(Collectors.toList());
   }
}
