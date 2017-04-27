package com.controller.task;

import com.model.task.Priority;
import com.model.task.Task;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ivan on 25/04/17.
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
    public Task getTask(int id) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) return taskList.get(taskPosition);
        else throw new Exception();
    }
    public int parseId(String s) {
        String[] tokens = s.split(" ");
        return Integer.parseInt(tokens[0]);
    }
    public Task addTask(String name, Priority priority, int taskTime) {
        int id;
        if (taskList.isEmpty()) id = 0;
        else id = taskList.get(taskList.size() - 1).getId() + 1;
        System.out.println("new id: " + id);
        Task task = new Task(id, name, priority, taskTime);
        taskList.add(task);
        return task;
    }
    public int searchTask(int id) {
        int low = 0, high = taskList.size() - 1, mid;
        while (low < high) {
            mid = low + high >> 1;
            if (taskList.get(mid).getId() >= id) high = mid;
            else low = mid + 1;
        }
        if (taskList.get(low).getId() == id) return low;
        return -1;
    }
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
    public void modifyTask(int id, Priority priority) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) taskList.get(taskPosition).setPriority(priority);
        else throw new Exception();
    }
    public void modifyTask(int id, int taskTime) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) taskList.get(taskPosition).setTaskTime(taskTime);
        else throw new Exception();
    }
    public void deleteTask(int id) throws Exception {
        int taskPosition = searchTask(id);
        if (taskPosition > -1) taskList.remove(taskPosition);
        else throw new Exception();
    }
    private void loadTasks() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("tasks.data"));
            taskList = (ArrayList<Task>)stream.readObject();
        } catch (Exception e) {}
    }
    public void saveTasks() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("tasks.data"));
            stream.writeObject(taskList);
        } catch (Exception e) {}
    }
}
