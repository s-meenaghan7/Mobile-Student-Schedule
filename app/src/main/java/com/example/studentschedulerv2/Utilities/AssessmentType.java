package com.example.studentschedulerv2.Utilities;

import androidx.annotation.NonNull;

public enum AssessmentType {
    OBJECTIVE, PERFORMANCE;

    @NonNull
    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0: return "Objective";
            case 1: return "Performance";
        }
        return "";
    }
}
