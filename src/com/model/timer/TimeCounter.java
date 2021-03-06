package com.model.timer;

import com.model.task.Task;

/**
 * Created by ivan on 25/04/17.
 * Context class that picks the correct timer strategy depending on how it was instantiated
 */
public class TimeCounter {
    private TimerStrategy strategy;
    public TimeCounter(TimerStrategy strategy) {
        this.strategy = strategy;
    }
    public void start() throws Exception {
        strategy.start();
    }
    public boolean run() {
        return strategy.run();
    }
    public void stop() {
        strategy.stop();
    }
    public void reset() {
        strategy.reset();
    }
    public void associate(Task task) {
        strategy.associate(task);
    }
    public void setStrategy(TimerStrategy strategy) {
        this.strategy = strategy;
    }
    public long getCurrentTime() {
        return strategy.getCurrentTime();
    }
    public long getInitialTime() {
        return strategy.getInitialTime();
    }
    public long getTargetTime() {
        return strategy.getTargetTime();
    }
    public boolean getIsStopped() {
        return strategy.getIsStopped();
    }
    public Task getTask() {
        return strategy.getTask();
    }
    public void setMode(Mode mode) {
        strategy.setMode(mode);
    }
}
