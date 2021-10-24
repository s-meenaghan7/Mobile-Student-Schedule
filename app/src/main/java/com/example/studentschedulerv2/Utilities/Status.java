package com.example.studentschedulerv2.Utilities;

import androidx.annotation.NonNull;

public enum Status {
    DROPPED, PLAN_TO_TAKE, IN_PROGRESS, COMPLETED;

    @NonNull
    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0: return "Dropped";
            case 1: return "Plan to Take";
            case 2: return "In Progress";
            case 3: return "Completed";
        }
        return "";
    }
}
