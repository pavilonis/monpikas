package lt.pavilonis.cmm.school.user;

import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.SizeConsumingBackendDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class UserListRepository implements EntityRepository<User, Long, UserFilter> {

   @Autowired
   private UserRepository userRepository;

   @Override
   public User saveOrUpdate(User entity) {
      return entity.getId() == null
            ? userRepository.create(entity)
            : userRepository.update(entity);
   }

   @Override
   public Optional<User> find(Long id) {
      User result = userRepository.load(id, true);
      return Optional.ofNullable(result);
   }

   @Override
   public List<User> load() {
      return load(new UserFilter(null, null, null, false));
   }

   @Override
   public List<User> load(UserFilter filter) {
      return userRepository.load(filter);
   }

   @Override
   public Optional<SizeConsumingBackendDataProvider<User, UserFilter>> lazyDataProvider(UserFilter filter) {
      SizeConsumingBackendDataProvider<User, UserFilter> provider = new SizeConsumingBackendDataProvider<User, UserFilter>() {
         @Override
         protected Stream<User> fetchFromBackEnd(Query<User, UserFilter> query) {
            List<User> result = load(
                  filter
                        .withOffset(query.getOffset())
                        .withLimit(query.getLimit())
            );
            return result.stream();
         }

         @Override
         protected int sizeInBackEnd() {
            return userRepository.size(filter);
         }
      };
      return Optional.of(provider);
   }

   @Override
   public void delete(Long id) {
      userRepository.delete(id);
   }

   @Override
   public Class<User> entityClass() {
      return User.class;
   }
}
