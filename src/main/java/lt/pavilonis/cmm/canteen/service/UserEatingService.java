package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.canteen.domain.EatingData;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.canteen.repository.EatingEventRepository;
import lt.pavilonis.cmm.canteen.repository.PupilDataRepository;
import lt.pavilonis.cmm.canteen.ui.user.UserEatingFilter;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.school.user.UserFilter;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserEatingService implements EntityRepository<UserEating, String, UserEatingFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(UserEatingService.class.getSimpleName());
   private static final String ROLE_PUPIL = "Mokinys";

   @Autowired
   private PupilDataRepository pupilDataRepository;

   @Autowired
   private UserRepository usersRepository;

   @Autowired
   private EatingEventRepository eventsRepository;

   @Override
   public List<UserEating> load() {
      return load(new UserEatingFilter(null, null, false));
   }

   @Override
   public List<UserEating> load(UserEatingFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();
      Collection<EatingData> pupilDataCollection = pupilDataRepository.loadAll(
            filter.isWithEatingAssigned(),
            filter.getEatingType()
      );
      LOG.info("Loaded pupil eating data [duration={}]", TimeUtils.duration(opStart));

      opStart = LocalDateTime.now();
      List<User> users = usersRepository.load(new UserFilter(filter.getText(), ROLE_PUPIL, null, false));
      LOG.info("Loaded user data [duration={}]", TimeUtils.duration(opStart));

      return filter.isWithEatingAssigned()
            ? mergeByEatings(pupilDataCollection, users)
            : mergeByUsers(pupilDataCollection, users);
   }

   @Override
   public Optional<UserEating> find(String cardCode) {
      User user = usersRepository.load(cardCode, true);
      if (user == null) {
         return Optional.empty();
      }

      EatingData eatingData = pupilDataRepository.load(cardCode)
            .orElseGet(() -> new EatingData(cardCode));

      return Optional.of(new UserEating(user, eatingData));
   }

   public boolean canEat(String cardCode, Date day, EatingType type) {

      Date dayStart = LocalDate.fromDateFields(day)
            .toDateTimeAtStartOfDay()
            .toDate();
      Date dayEnd = org.joda.time.LocalDateTime.fromDateFields(day)
            .withTime(23, 59, 59, 999)
            .toDate();

      return eventsRepository.numOfEatingEvents(cardCode, dayStart, dayEnd, type) == 0;
   }

   public boolean portionAssigned(String cardCode, EatingType type) {
      Optional<EatingData> pupil = pupilDataRepository.load(cardCode);

      return pupil.orElseThrow(IllegalArgumentException::new)
            .getEatings().stream()
            .anyMatch(portion -> portion.getType() == type);
   }

   private List<UserEating> mergeByUsers(Collection<EatingData> pupilDataCollection, List<User> users) {
      LocalDateTime opStart;
      opStart = LocalDateTime.now();
      List<UserEating> result = users.stream()
            .map(user -> {
               EatingData eatingData = pupilDataCollection.stream()
                     .filter(data -> data.getCardCode().equals(user.getCardCode()))
                     .findFirst()
                     .orElseGet(() -> new EatingData(user.getCardCode()));

               return new UserEating(user, eatingData);
            })
            .collect(toList());
      LOG.info("Composed dtos [duration={}]", TimeUtils.duration(opStart));
      return result;
   }

   //TODO check
   private List<UserEating> mergeByEatings(Collection<EatingData> pupilData, List<User> users) {
      return pupilData.stream()
            .map(data -> {
               Optional<User> correspondingUser = users.stream()
                     .filter(user -> user.getCardCode().equals(data.getCardCode()))
                     .findFirst();

               if (correspondingUser.isPresent()) {
                  User user = correspondingUser.get();
                  UserEating userEating = new UserEating(user, data);
                  return Optional.of(userEating);
               } else {
                  return Optional.<UserEating>empty();
               }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
   }

   @Override
   public UserEating saveOrUpdate(UserEating entity) {
      // Here, only eating data is managed.
      // User representation is only used for viewing.
      // So, only eatingData is saved.
      EatingData eatingData = pupilDataRepository.saveOrUpdate(entity.getEatingData());
      return this.find(eatingData.getCardCode())
            .orElseThrow(() -> new RuntimeException("Could not load recently saved eatingData for user"));
   }

   @Override
   public void delete(String cardCode) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public Class<UserEating> entityClass() {
      return UserEating.class;
   }
}