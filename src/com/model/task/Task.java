package com.model.task;

import java.io.Serializable;

/**
 * Created by ivan on 25/04/17.
 */

public class Task implements Comparable<Task>, Serializable {
    private int id;
    private Priority priority;
    private String name;
    private int taskTime;
    private int runningTime;
    public Task(int id, String name, Priority priority, int taskTime) {
        this.id = id;
        this.name = name;
        this.setPriority(priority);
        taskTime *= 60;
        this.setTaskTime(taskTime);
        this.setRunningTime(0);
    }
    public Task(Task task) {
        this.id = task.id;
        this.priority = task.priority;
        this.name = task.name;
        this.taskTime = task.taskTime;
        this.runningTime = task.runningTime;
    }

    /**
     * Increments the running time of the task
     * @param amount
     */
    public void incrementRunningTime(int amount) {
        runningTime += amount;
    }

    /**
     * Gets the name of the task
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the task
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the priority of the task
     * @return
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the task
     * @param priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Gets the task time
     * @return
     */
    public int getTaskTime() {
        return taskTime;
    }

    /**
     * Sets the task time
     * @param taskTime
     */
    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }

    /**
     * Gets the running time of the task
     * @return
     */
    public int getRunningTime() {
        return runningTime;
    }

    /**
     * Sets the running time of the task
     * @param runningTime
     */
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    /**
     * Comparison function to sort tasks by priority
     * @param task
     * @return
     */
    @Override
    public int compareTo(Task task) {
        return this.getPriority().ordinal() - task.getPriority().ordinal();
    }
}
