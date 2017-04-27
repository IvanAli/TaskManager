package com.controller.timer;

import com.model.task.Task;
import com.model.timer.TimeCounter;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ivan on 26/04/17.
 */
public class TimerThread extends Thread {
    private JPanel mainPanel;
    private TimeCounter stopWatch, countdownTimer;
    private JLabel stopWatchLabel, countdownLabel;
    private Task currentTask;
    public TimerThread(JPanel mainPanel, TimeCounter stopWatch, TimeCounter countdownTimer, JLabel stopLabel, JLabel countLabel) {
        this.mainPanel = mainPanel;
        this.stopWatch = stopWatch;
        this.countdownTimer = countdownTimer;
        currentTask = stopWatch.getTask();
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
        String formattedDate;
        formattedDate = secondsToString(currentTask.getRunningTime());
        stopWatchLabel.setText(formattedDate);
        formattedDate = secondsToString(countdownTimer.getCurrentTime());
        countdownLabel.setText(formattedDate);
    }
    private void playAlarm() {
        String soundName = "alarm.wav";
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundName));
            AudioFormat format = inputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true) {
            currentTask = stopWatch.getTask();
            synchronized (this) {
                if (currentTask != null) {
//                    System.out.println("cur task id: " + currentTask.getId() + " time: " + currentTask.getTaskTime());
                    stopWatch.run();
                    boolean isZero = countdownTimer.run();
                    if (isZero) {
                        JOptionPane.showMessageDialog(mainPanel, "Task time is up", "Time's up", JOptionPane.INFORMATION_MESSAGE);
                        playAlarm();
                    }
                    setLabels();
                }
            }
        }
    }
    public void startTimers() {
        try {
            stopWatch.start();
            countdownTimer.start();
        } catch (Exception e) {}
    }
    public void stopTimers() {
        try {
            stopWatch.stop();
            countdownTimer.stop();
        } catch (Exception e) {}
    }
    public void resetTimers() {
        try {
            stopWatch.reset();
            countdownTimer.reset();
        } catch (Exception e) {}
    }
    public void setCurrentTask(Task task) {
        currentTask = task;
    }
}
