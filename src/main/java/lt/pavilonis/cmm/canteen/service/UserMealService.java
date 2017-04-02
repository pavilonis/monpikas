package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.MealData;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.repository.PupilDataRepository;
import lt.pavilonis.cmm.canteen.repository.UserRepository;
import lt.pavilonis.cmm.canteen.views.user.UserMealFilter;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.domain.UserRepresentation;
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
public class UserMealService implements EntityRepository<UserMeal, String, UserMealFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(UserMealService.class.getSimpleName());

   @Autowired
   private PupilDataRepository pupilDataRepository;

   @Autowired
   private UserRepository usersRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   @Override
   public List<UserMeal> load(UserMealFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();
      Collection<MealData> pupilDataCollection = pupilDataRepository.loadAll(
            filter.isWithMealAssigned(),
            filter.getMealType()
      );
      LOG.info("Loaded pupil meal data [duration={}]", TimeUtils.duration(opStart));

      opStart = LocalDateTime.now();
      List<UserRepresentation> users = usersRepository.loadAllPupils(filter.getText());
      LOG.info("Loaded user data [duration={}]", TimeUtils.duration(opStart));

      return filter.isWithMealAssigned()
            ? mergeByMeals(pupilDataCollection, users)
            : mergeByUsers(pupilDataCollection, users);
   }

   @Override
   public Optional<UserMeal> find(String cardCode) {
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
      Date dayStart = LocalDate.fromDateFields(day)
            .toDateTimeAtStartOfDay()
            .toDate();
      Date dayEnd = org.joda.time.LocalDateTime.fromDateFields(day)
            .withTime(23, 59, 59, 999)
            .toDate();

      int mealsThatDay = eventsRepository.numOfMealEvents(cardCode, dayStart, dayEnd, type);
      return mealsThatDay == 0;
   }
//
//   public boolean portionAssigned(String cardCode, MealType type) {
//      Optional<MealData> pupil = pupilDataRepository.load(cardCode);
//
//      return pupil.orElseThrow(IllegalArgumentException::new)
//            .getMeals().stream()
//            .anyMatch(portion -> portion.getType() == type);
//   }

   private List<UserMeal> mergeByUsers(Collection<MealData> pupilDataCollection, List<UserRepresentation> users) {
      LocalDateTime opStart;
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

   //TODO check
   private List<UserMeal> mergeByMeals(Collection<MealData> pupilData, List<UserRepresentation> users) {
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
      // Here, only meal data is managed.
      // User representation is only used for viewing.
      // So, only mealData is saved.
      MealData mealData = pupilDataRepository.saveOrUpdate(entity.getMealData());
      return this.find(mealData.getCardCode())
            .orElseThrow(() -> new RuntimeException("Could not load recently saved mealData for user"));
   }

   @Override
   public void delete(String cardCode) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public Class<UserMeal> getEntityClass() {
      return UserMeal.class;
   }
}