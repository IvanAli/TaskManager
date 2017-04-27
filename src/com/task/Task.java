package com.task;

import com.timer.CountdownTimerStrategy;
import com.timer.StopWatchStrategy;
import com.timer.TimeCounter;

/**
 * Created by ivan on 25/04/17.
 */

public class Task implements Comparable<Task> {
    private int id;
    private Priority priority;
    private String name;
    private int taskTime;
    private int runningTime;
    private TimeCounter stopWatch, countdownTimer;
    public Task(int id, String name, Priority priority, int taskTime) {
        this.id = id;
        this.name = name;
        this.setPriority(priority);
        taskTime *= 60;
        this.setTaskTime(taskTime);
        this.setRunningTime(0);
        this.stopWatch = new TimeCounter(new StopWatchStrategy(this));
        this.countdownTimer = new TimeCounter(new CountdownTimerStrategy(this));
    }
    public Task(Task task) {
        this.id = task.id;
        this.priority = task.priority;
        this.name = task.name;
        this.taskTime = task.taskTime;
        this.runningTime = task.runningTime;
    }
    public TimeCounter getStopWatch() {
        return stopWatch;
    }
    public TimeCounter getCountdownTimer() {
        return countdownTimer;
    }
    public void incrementRunningTime(int amount) {
        runningTime += amount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return this.id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public int compareTo(Task task) {
        return this.getPriority().ordinal() - task.getPriority().ordinal();
    }
}
