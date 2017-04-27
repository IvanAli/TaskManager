package com.controller.timer;

import com.model.task.Task;
import com.model.timer.TimeCounter;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ivan on 26/04/17.
 * Class responsible for running the timers and updating the GUI labels that correspond to time
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

    /**
     * Converts seconds to a formatted time string (hh:mm:ss)
     * @param seconds
     * @return
     */
    private String secondsToString(long seconds) {
        long hour = seconds / 3600;
        long minute = (seconds % 3600) / 60;
        long second = (seconds % 3600) % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Sets the time in the GUI for the stopwatch timer and the countdown timer
     */
    private void setLabels() {
        String formattedDate;
        formattedDate = secondsToString(currentTask.getRunningTime());
        stopWatchLabel.setText(formattedDate);
        formattedDate = secondsToString(countdownTimer.getCurrentTime());
        countdownLabel.setText(formattedDate);
    }

    /**
     * Plays the alarm file when countdown timer hits zero
     */
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

    /**
     * Runs both timers until they are either stopped or the countdown timer hits zero
     */
    @Override
    public void run() {
        while (true) {
            currentTask = stopWatch.getTask();
            synchronized (this) {
                if (currentTask != null) {
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

    /**
     * Starts both timers
     */
    public void startTimers() {
        try {
            stopWatch.start();
            countdownTimer.start();
        } catch (Exception e) {}
    }

    /**
     * Stops both timers
     */
    public void stopTimers() {
        try {
            stopWatch.stop();
            countdownTimer.stop();
        } catch (Exception e) {}
    }

    /**
     * Resets both timers
     */
    public void resetTimers() {
        try {
            stopWatch.reset();
            countdownTimer.reset();
        } catch (Exception e) {}
    }
}
