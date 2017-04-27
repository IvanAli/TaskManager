package com.model.timer;

import com.model.task.Task;

/**
 * Created by ivan on 25/04/17.
 * Class built under the Strategy pattern: both the stopwatch timer and the countdown timer have the same actions but
 * they perform them differently. Therefore, they both implement this interface
 */
public interface TimerStrategy {
    /**
     * Gets the initial time of the timer
     * @return
     */
    public long getInitialTime();

    /**
     * Gets the current time of the timer
     * @return
     */
    public long getCurrentTime();

    /**
     * Gets the target time of the timer
     * @return
     */
    public long getTargetTime();

    /**
     * Starts the timer
     * @throws Exception
     */
    public void start() throws Exception;

    /**
     * Increments or decrements the current time of the timer until it reaches the target time
     * @return
     */
    public boolean run();

    /**
     * Stops the timer
     */
    public void stop();

    /**
     * Resets the timer
     */
    public void reset();

    /**
     * Associates a task to a timer from which it gets the initial time, target time and mode
     * @param task
     */
    public void associate(Task task);

    /**
     * Returns whether the timer is stopped or not
     * @return
     */
    public boolean getIsStopped();

    /**
     * Gets the task associated to this timer
     * @return
     */
    public Task getTask();

    /**
     * Sets the timer mode (it can either be UNLIMITED -- keeps counting even if the other timer hits zero or
     * LIMITED -- stops when the other timer hits zero
     * @param mode
     */
    public void setMode(Mode mode);
}
