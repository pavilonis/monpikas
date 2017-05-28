CREATE TABLE mm_ClassroomOccupancy (
   classroomNumber SMALLINT                    NOT NULL,
   dateTime        DATETIME2 DEFAULT getdate() NOT NULL,
   occupied        BIT                         NOT NULL
);

CREATE UNIQUE INDEX INDEX_UNIQUE_classroomNumber_dateTime
ON mm_ClassroomOccupancy (classroomNumber, dateTime);