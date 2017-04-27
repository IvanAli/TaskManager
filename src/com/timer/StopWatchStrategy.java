package com.timer;

import com.task.Task;

import static com.timer.Mode.LIMITED;
import static com.timer.Mode.UNLIMITED;

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
    private boolean isReady;
    private boolean isStopped;
    public StopWatchStrategy(Task task) {
        this.task = task;
        this.mode = LIMITED;
        this.isStopped = true;
        this.isReady = false;
        initialTime = 0;
        currentTime = 0;
        targetTime = task.getTaskTime();
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
        isReady = true;
    }
    @Override
    public boolean run() {
        if (isStopped || !isReady) return false;
        long currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - previousSystemTime >= SEC) {
            if (mode == UNLIMITED || currentTime < targetTime) {
                currentTime++;
                task.incrementRunningTime(1);
                previousSystemTime = currentSystemTime;
            }
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
    }
    @Override
    public boolean getIsStopped() { return isStopped; }
}