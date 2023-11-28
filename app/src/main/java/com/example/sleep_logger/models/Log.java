package com.example.sleep_logger.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "logs")
public class Log {
    @PrimaryKey(autoGenerate = true)
    public Date date;
    public double sleepHours, weight;
    public int exerciseMinutes, quality;

    public Log() {
        this.date = new Date(System.currentTimeMillis());
        this.sleepHours = 0;
        this.quality = 1;
        this.exerciseMinutes = 0;
        this.weight = 0;
    }

    public Log(Date date, double sleepHours, int quality, int exerciseMinutes, double weight) {
        this.date = date;
        this.sleepHours = sleepHours;
        this.quality = quality;
        this.exerciseMinutes = exerciseMinutes;
        this.weight = weight;
    }

    public Log(double sleepHours, int quality, int exerciseMinutes, double weight) {
        this.date = new Date(System.currentTimeMillis());
        this.sleepHours = sleepHours;
        this.quality = quality;
        this.exerciseMinutes = exerciseMinutes;
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public double getExerciseMinutes() {
        return exerciseMinutes;
    }

    public void setExerciseMinutes(int exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
