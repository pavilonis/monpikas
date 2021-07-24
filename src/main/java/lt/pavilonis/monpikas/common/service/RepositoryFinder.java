package lt.pavilonis.monpikas.common.service;

import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.EntityRepository;

import java.util.Map;

public class RepositoryFinder {

   private static final Class<EntityRepository> ENTITY_REPOSITORY_CLASS = EntityRepository.class;

   public static <T, ID, FILTER> EntityRepository<T, ID, FILTER> find(Class<T> type) {

      Map<String, EntityRepository> beans = App.context.getBeansOfType(ENTITY_REPOSITORY_CLASS);

      return beans.values().stream()
            .filter(bean -> bean.entityClass() == type)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find repository for " + type.getSimpleName()));
   }
}
