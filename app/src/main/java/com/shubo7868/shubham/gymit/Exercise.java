package com.shubo7868.shubham.gymit;

public class Exercise {
    private String exerciseName;
    private int exerciseDuration;

    Exercise() {}
    Exercise(String name, int duration) {
        this.exerciseName = name;
        this.exerciseDuration = duration;
    }


    public String getExerciseName() {
        return exerciseName;
    }

    public int getExerciseDuration() {
        return exerciseDuration;
    }
}
