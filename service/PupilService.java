package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.domain.PupilLocalData;
import lt.pavilonis.monpikas.server.domain.UserRepresentation;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.PupilDataRepository;
import lt.pavilonis.monpikas.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class PupilService {

   private static final Logger LOG = LoggerFactory.getLogger(PupilService.class.getSimpleName());

   @Autowired
   private PupilDataRepository pupilDataRepository;

   @Autowired
   private UserRepository usersRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   public Optional<Pupil> find(String cardCode) {
      Optional<UserRepresentation> optionalUser = usersRepository.load(cardCode);
      if (!optionalUser.isPresent()) {
         return Optional.empty();
      }

      UserRepresentation user = optionalUser.get();
      Optional<PupilLocalData> data = pupilDataRepository.load(cardCode);
      Pupil result = new Pupil(
            user.cardCode,
            user.firstName,
            user.lastName,
            user.description,
            user.birthDate,
            data.isPresent() ? data.get().getType() : null,
            data.isPresent() ? data.get().getMeals() : new HashSet<>(),
            data.isPresent() ? data.get().getComment() : null,
            user.photoUrl
      );
      return Optional.of(result);
   }

   public boolean canHaveMeal(String cardCode, Date day, MealType type) {
      int mealsThatDay = eventsRepository.numOfMealEvents(cardCode, beginning(day), end(day), type);
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(String cardCode, MealType type) {
      Optional<PupilLocalData> pupil = pupilDataRepository.load(cardCode);

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

   public Collection<Pupil> loadAll() {
      Collection<PupilLocalData> pupilDataCollection = pupilDataRepository.loadAll(false);
      List<UserRepresentation> users = usersRepository.loadAll();
      return users.stream()
            .map(user -> {
               Optional<PupilLocalData> pupilData = pupilDataCollection.stream()
                     .filter(data -> data.getCardCode().equals(user.cardCode))
                     .findFirst();
               return new Pupil(
                     user.cardCode,
                     user.firstName,
                     user.lastName,
                     user.description,
                     user.birthDate,
                     pupilData.isPresent() ? pupilData.get().getType() : null,
                     pupilData.isPresent() ? pupilData.get().getMeals() : new HashSet<>(),
                     pupilData.isPresent() ? pupilData.get().getComment() : null,
                     user.photoUrl
               );
            })
            .collect(toList());
   }

   public List<Pupil> loadWithMealAssigned() {
      Collection<PupilLocalData> pupilData = pupilDataRepository.loadAll(true);
      return loadAndMerge(pupilData);
   }

   public List<Pupil> loadByMeal(Long mealId) {
      Collection<PupilLocalData> pupilLocalData = pupilDataRepository.loadByMeal(mealId);
      return loadAndMerge(pupilLocalData);
   }

   private List<Pupil> loadAndMerge(Collection<PupilLocalData> pupilData) {
      List<UserRepresentation> users = usersRepository.loadAll();
      return pupilData.stream()
            .map(data -> {
               Optional<UserRepresentation> correspondingUser = users.stream()
                     .filter(user -> user.cardCode.equals(data.getCardCode()))
                     .findFirst();
               if (!correspondingUser.isPresent()) {
                  return Optional.<Pupil>empty();
               } else {
                  UserRepresentation user = correspondingUser.get();
                  Pupil pupil = new Pupil(
                        user.cardCode,
                        user.firstName,
                        user.lastName,
                        user.description,
                        user.birthDate,
                        data.getType(),
                        data.getMeals(),
                        data.getComment(),
                        user.photoUrl
                  );
                  return Optional.of(pupil);
               }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
   }
}