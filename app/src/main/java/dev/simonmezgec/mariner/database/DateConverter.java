package dev.simonmezgec.mariner.database;

import java.util.Date;

import androidx.room.TypeConverter;

/** Utility class for converting dates when reading and saving into databases. */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
