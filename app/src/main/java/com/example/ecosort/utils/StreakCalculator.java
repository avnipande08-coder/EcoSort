package com.example.ecosort.utils;

import com.example.ecosort.data.WasteLogEntity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreakCalculator {

    /**
     * Computes the current consecutive-day logging streak from a list of waste logs.
     * A streak stays "alive" if the most recent log was today or yesterday
     * (so it doesn't reset to 0 the moment the clock passes midnight before
     * the user has had a chance to log today).
     */
    public static int computeCurrentStreak(List<WasteLogEntity> logs) {
        if (logs == null || logs.isEmpty()) return 0;

        Set<Long> loggedDayKeys = new HashSet<>();
        for (WasteLogEntity log : logs) {
            loggedDayKeys.add(dayKey(log.timestamp));
        }

        Calendar cursor = Calendar.getInstance();
        long todayKey = dayKey(cursor.getTimeInMillis());

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        long yesterdayKey = dayKey(yesterday.getTimeInMillis());

        boolean loggedToday = loggedDayKeys.contains(todayKey);
        boolean loggedYesterday = loggedDayKeys.contains(yesterdayKey);

        if (!loggedToday && !loggedYesterday) {
            return 0;
        }

        // Start counting from today if logged today, otherwise from yesterday.
        Calendar walker = Calendar.getInstance();
        if (!loggedToday) {
            walker.add(Calendar.DAY_OF_YEAR, -1);
        }

        int streak = 0;
        while (loggedDayKeys.contains(dayKey(walker.getTimeInMillis()))) {
            streak++;
            walker.add(Calendar.DAY_OF_YEAR, -1);
        }
        return streak;
    }

    private static long dayKey(long timestampMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestampMillis);
        return cal.get(Calendar.YEAR) * 1000L + cal.get(Calendar.DAY_OF_YEAR);
    }
}