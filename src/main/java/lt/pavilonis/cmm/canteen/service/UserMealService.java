package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.TimeUtils;
import lt.pavilonis.cmm.canteen.domain.MealData;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.repository.PupilDataRepository;
import lt.pavilonis.cmm.canteen.repository.UserRepository;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.domain.UserRepresentation;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserMealService implements EntityRepository<UserMeal, String> {

   private static final Logger LOG = LoggerFactory.getLogger(UserMealService.class.getSimpleName());

   @Autowired
   private PupilDataRepository pupilDataRepository;

   @Autowired
   private UserRepository usersRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   @Override
   public Optional<UserMeal> load(String cardCode) {
      Optional<UserRepresentation> optionalUser = usersRepository.load(cardCode);
      if (!optionalUser.isPresent()) {
         return Optional.empty();
      }

      MealData mealData = pupilDataRepository.load(cardCode)
            .orElseGet(() -> new MealData(cardCode));

      return Optional.of(
            new UserMeal(optionalUser.get(), mealData)
      );
   }

   public boolean canHaveMeal(String cardCode, Date day, MealType type) {
      int mealsThatDay = eventsRepository.numOfMealEvents(cardCode, beginning(day), end(day), type);
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(String cardCode, MealType type) {
      Optional<MealData> pupil = pupilDataRepository.load(cardCode);

      return pupil.orElseThrow(IllegalArgumentException::new)
            .getMeals().stream()
            .anyMatch(portion -> portion.getType() == type);
   }

   private Date beginning(Date date) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
   }

   private Date end(Date date) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.set(Calendar.HOUR_OF_DAY, 23);
      c.set(Calendar.MINUTE, 59);
      c.set(Calendar.SECOND, 59);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
   }

   @Override
   public List<UserMeal> loadAll() {
      LocalDateTime opStart = LocalDateTime.now();
      Collection<MealData> pupilDataCollection = pupilDataRepository.loadAll(false);
      LOG.info("Loaded pupil meal data [duration={}]", TimeUtils.duration(opStart));

      opStart = LocalDateTime.now();
      List<UserRepresentation> users = usersRepository.loadAll();
      LOG.info("Loaded user data [duration={}]", TimeUtils.duration(opStart));

      opStart = LocalDateTime.now();
      List<UserMeal> result = users.stream()
            .map(user -> {
               Optional<MealData> mealData = pupilDataCollection.stream()
                     .filter(data -> data.getCardCode().equals(user.getCardCode()))
                     .findFirst();

               return new UserMeal(
                     user,
                     mealData.orElseGet(() -> new MealData(user.getCardCode()))
               );
            })
            .collect(toList());
      LOG.info("Composed dtos [duration={}]", TimeUtils.duration(opStart));

      return result;
   }

   public List<UserMeal> loadWithMealAssigned() {
      Collection<MealData> pupilData = pupilDataRepository.loadAll(true);
      return loadAndMerge(pupilData);
   }

   public List<UserMeal> loadByMeal(Long mealId) {
      Collection<MealData> mealData = pupilDataRepository.loadByMeal(mealId);
      return loadAndMerge(mealData);
   }

   private List<UserMeal> loadAndMerge(Collection<MealData> pupilData) {
      List<UserRepresentation> users = usersRepository.loadAll();
      return pupilData.stream()
            .map(data -> {
               Optional<UserRepresentation> correspondingUser = users.stream()
                     .filter(user -> user.getCardCode().equals(data.getCardCode()))
                     .findFirst();

               if (correspondingUser.isPresent()) {
                  UserRepresentation user = correspondingUser.get();
                  UserMeal userMeal = new UserMeal(user, data);
                  return Optional.of(userMeal);
               } else {
                  return Optional.<UserMeal>empty();
               }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
   }

   @Override
   public UserMeal saveOrUpdate(UserMeal entity) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public void delete(String cardCode) {
      throw new NotImplementedException("not needed");
   }
}