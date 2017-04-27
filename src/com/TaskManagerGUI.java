package com;

import com.task.Priority;
import com.task.TaskManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ivan on 25/04/17.
 */
public class TaskManagerGUI {
    private JButton addNewTaskButton;
    private JPanel taskPanel;
    private JList jTaskList;
    private TaskManager taskManager;
    public TaskManagerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        addNewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();
                Object[] message = {
                        "Task name:", jTaskName,
                        "Priority:", jTaskPriority,
                        "Task Time (in minutes): ", jTaskTime
                };
                int option = JOptionPane.showConfirmDialog(null, message, "New task", JOptionPane.OK_CANCEL_OPTION);

                String taskName = jTaskName.getText();

                String selectedPriority = jTaskPriority.getSelectedItem().toString();
                Priority taskPriority;
                if (selectedPriority.equals("High")) taskPriority = Priority.HIGH;
                else if (selectedPriority.equals("Medium")) taskPriority = Priority.MEDIUM;
                else if (selectedPriority.equals("Low")) taskPriority = Priority.LOW;

                int taskTime = Integer.parseInt(jTaskTime.getText());

                if (option == JOptionPane.OK_OPTION) {
                    if (!taskName.equals("")) {
                        taskManager.addTask(taskName, Priority.HIGH, taskTime);
                        System.out.println("Task successfully added");
                    }
                }
            }
        });
    }
    public JPanel getTaskPanel() {
        return taskPanel;
    }
}
