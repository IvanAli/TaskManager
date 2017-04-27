package com.timer;

import com.task.Task;

/**
 * Created by ivan on 25/04/17.
 */
public class CountdownTimerStrategy implements TimerStrategy {
    final static int SEC = 1000;
    private long initialTime;
    private long currentTime;
    private long targetTime;
    private long previousSystemTime;
    private Task task;
    private boolean isStopped;
    private boolean isReady;
    public CountdownTimerStrategy() {
        this.isStopped = true;
        this.isReady = false;
    }
    @Override
    public long getInitialTime() {
        return initialTime;
    }
    @Override
    public long getCurrentTime() {
        return currentTime;
    }
    @Override
    public long getTargetTime() {
        return targetTime;
    }
    @Override
    public void start() throws Exception {
        if (task == null) throw new Exception("A task hasn't been associated to this timer yet");
        previousSystemTime = System.currentTimeMillis();
        isStopped = false;
        isReady = true;
    }
    @Override
    public boolean run() {
        if (isStopped || !isReady) return false;
        if (currentTime == targetTime) return false;
        long currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - previousSystemTime >= SEC) {
            currentTime--;
            previousSystemTime = currentSystemTime;
        }
        if (currentTime == targetTime) return true;
        return false;
    }
    @Override
    public void stop() {
        isStopped = true;
    }
    @Override
    public void reset() {
        currentTime = initialTime;
    }
    @Override
    public void associate(Task task) {
        this.task = task;
        initialTime = task.getTaskTime();
        targetTime = 0;
        currentTime = initialTime;
    }
    @Override
    public boolean getIsStopped() { return isStopped; }
    @Override
    public Task getTask() { return task; }
}
