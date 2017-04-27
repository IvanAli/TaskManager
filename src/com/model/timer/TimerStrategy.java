package com.model.timer;

import com.model.task.Task;

/**
 * Created by ivan on 25/04/17.
 */
public interface TimerStrategy {
    public long getInitialTime();
    public long getCurrentTime();
    public long getTargetTime();
    public void start() throws Exception;
    public boolean run();
    public void stop();
    public void reset();
    public void associate(Task task);
    public boolean getIsStopped();
    public Task getTask();
    public void setMode(Mode mode);
}