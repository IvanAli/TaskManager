package com.model.timer;

import com.model.task.Task;

/**
 * Created by ivan on 25/04/17.
 */
public class StopWatchStrategy implements TimerStrategy {
    final static int SEC = 1000;
    private Mode mode;
    private long initialTime;
    private long currentTime;
    private long targetTime;
    private long previousSystemTime;
    private Task task;
    private boolean isStopped;
    public StopWatchStrategy() {
        this.mode = Mode.LIMITED;
        this.isStopped = true;
    }
    public StopWatchStrategy(Mode mode) {
        this.mode = mode;
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
    }
    @Override
    public boolean run() {
        if (isStopped) return false;
        long currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - previousSystemTime >= SEC) {
            if (mode == Mode.UNLIMITED || currentTime < targetTime) {
                currentTime++;
                task.incrementRunningTime(1);
                previousSystemTime = currentSystemTime;
            }
        }
        if (currentTime == targetTime) {
            if (mode == Mode.LIMITED) isStopped = true;
            return true;
        }
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
        if (task == null) return;
        initialTime = 0;
        targetTime = task.getTaskTime();
        currentTime = initialTime;
    }
    @Override
    public boolean getIsStopped() { return isStopped; }
    @Override
    public Task getTask() { return task; }
    public void setMode(Mode mode) { this.mode = mode; }

}
