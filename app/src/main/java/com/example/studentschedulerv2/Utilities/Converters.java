package com.example.studentschedulerv2.Utilities;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String statusAsString(Status status) {
        return status.toString();
    }

    @TypeConverter
    public static Status stringToStatus(String string) {
        switch (string) {
            case "Dropped": return Status.DROPPED;
            case "Plan to Take": return Status.PLAN_TO_TAKE;
            case "In Progress": return Status.IN_PROGRESS;
            case "Completed": return Status.COMPLETED;
        }
        return null;
    }

    @TypeConverter
    public static String assessmentTypeAsString(AssessmentType a) {
        return a.toString();
    }

    @TypeConverter
    public static AssessmentType stringToAssessmentType(String string) {
        switch (string) {
            case "Objective": return AssessmentType.OBJECTIVE;
            case "Performance": return AssessmentType.PERFORMANCE;
        }
        return null;
    }
}
