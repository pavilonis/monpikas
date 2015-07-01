package lt.pavilonis.monpikas.server.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalTime;

import static java.time.LocalDate.now;

@Converter(autoApply = true)
public class LocalTimePersistenceConverter implements AttributeConverter<LocalTime, Timestamp> {

   @Override
   public Timestamp convertToDatabaseColumn(LocalTime time) {
      return Timestamp.valueOf(time.atDate(now()));
   }

   @Override
   public LocalTime convertToEntityAttribute(Timestamp dbData) {
      return dbData.toLocalDateTime().toLocalTime();
   }
}
