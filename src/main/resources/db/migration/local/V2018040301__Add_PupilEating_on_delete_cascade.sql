ALTER TABLE PupilEating DROP FOREIGN KEY FK_eating_id;
ALTER TABLE PupilEating DROP FOREIGN KEY FK_pupil_cardCode;

ALTER TABLE PupilEating ADD CONSTRAINT `FK_PupilEating_Eating`
FOREIGN KEY (`eating_id`) REFERENCES `Eating` (`id`)
   ON DELETE CASCADE;

ALTER TABLE PupilEating ADD CONSTRAINT `FK_PupilEating_Pupil`
FOREIGN KEY (`pupil_cardCode`) REFERENCES `Pupil` (`cardCode`)
   ON DELETE CASCADE;