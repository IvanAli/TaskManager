package com.controller.task;

import com.model.task.Priority;
import com.model.task.Task;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ivan on 25/04/17.
 * Factory class for creating, modifying, deleting, saving, loading tasks
 */
public class TaskManager implements Serializable {
    private ArrayList<Task> taskList;
    public TaskManager() {
        taskList = new ArrayList<>();
        loadTasks();
    }
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    /**
     * Gets a task through its unique ID
     * @param id
     * @return
     * @throws Exception
     */
    public Task getTask(int id) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) return taskList.get(taskPosition);
        else throw new Exception();
    }

    /**
     * Obtains the ID of a task through the string used in the JList item
     * @param s
     * @return
     */
    public int parseId(String s) {
        String[] tokens = s.split(" ");
        return Integer.parseInt(tokens[0]);
    }

    /**
     * Creates a new task and appends it to the list of available tasks
     * @param name
     * @param priority
     * @param taskTime
     * @return
     */
    public Task addTask(String name, Priority priority, int taskTime) {
        int id;
        if (taskList.isEmpty()) id = 0;
        else id = taskList.get(taskList.size() - 1).getId() + 1;
        System.out.println("new id: " + id);
        Task task = new Task(id, name, priority, taskTime);
        taskList.add(task);
        return task;
    }

    /**
     * Efficiently searches the index of a task in the list through binary search
     * @param id
     * @return
     */
    private int searchTask(int id) {
        int low = 0, high = taskList.size() - 1, mid;
        while (low < high) {
            mid = low + high >> 1;
            if (taskList.get(mid).getId() >= id) high = mid;
            else low = mid + 1;
        }
        if (taskList.get(low).getId() == id) return low;
        return -1;
    }

    /**
     * Modifies an existing task in the list
     * @param id
     * @param name
     * @param priority
     * @param taskTime
     * @return
     * @throws Exception
     */
    public Task modifyTask(int id, String name, Priority priority, int taskTime) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) {
            taskList.get(taskPosition).setName(name);
            taskList.get(taskPosition).setPriority(priority);
            taskList.get(taskPosition).setTaskTime(taskTime * 60);
            return taskList.get(taskPosition);
        } else {
            throw new Exception();
        }
    }
    /**
     * Deletes an existing task in the list
     * @param id
     * @throws Exception
     */
    public void deleteTask(int id) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) taskList.remove(taskPosition);
        else throw new Exception();
    }

    /**
     * Load the tasks into the program from the tasks.data file
     */
    private void loadTasks() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("tasks.data"));
            taskList = (ArrayList<Task>)stream.readObject();
        } catch (Exception e) {}
    }

    /**
     * Saves the tasks into the tasks.data file
     */
    public void saveTasks() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("tasks.data"));
            stream.writeObject(taskList);
        } catch (Exception e) {}
    }
}
