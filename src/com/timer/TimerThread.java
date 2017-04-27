package com.timer;

import com.task.Task;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivan on 26/04/17.
 */
public class TimerThread extends Thread {
    private Task currentTask;
    private JLabel stopWatchLabel, countdownLabel;
    public TimerThread(Task task, JLabel stopLabel, JLabel countLabel) {
        this.currentTask = task;
        stopWatchLabel = stopLabel;
        countdownLabel = countLabel;
    }
    private String secondsToString(long seconds) {
        long hour = seconds / 3600;
        long minute = (seconds % 3600) / 60;
        long second = (seconds % 3600) % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    private void setLabels() {
        Date date; String formattedDate;
        date = new Date((long)currentTask.getRunningTime() * 1000);
        formattedDate = secondsToString(currentTask.getRunningTime());
        stopWatchLabel.setText(formattedDate);
        date = new Date((long)currentTask.getCountdownTimer().getCurrentTime() * 1000);
        formattedDate = secondsToString(currentTask.getCountdownTimer().getCurrentTime());
        countdownLabel.setText(formattedDate);
    }
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (currentTask != null) {
                    System.out.println("cur task id: " + currentTask.getId() + " time: " + currentTask.getTaskTime());
                    currentTask.getStopWatch().run();
                    currentTask.getCountdownTimer().run();
                    setLabels();
                }
            }
        }
    }
    public void startTimers() {
        try {
            currentTask.getStopWatch().start();
            currentTask.getCountdownTimer().start();
        } catch (Exception e) {}
    }
    public void stopTimers() {
        try {
            currentTask.getStopWatch().stop();
            currentTask.getCountdownTimer().stop();
        } catch (Exception e) {}
    }
    public void resetTimers() {
        try {
            currentTask.getStopWatch().reset();
            currentTask.getCountdownTimer().reset();
        } catch (Exception e) {}
    }
    public void setCurrentTask(Task task) {
        currentTask = task;
    }
}
