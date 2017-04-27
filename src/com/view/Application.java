package com.view;

import com.model.task.Priority;
import com.model.task.Task;
import com.controller.task.TaskManager;
import com.model.timer.Mode;
import com.model.timer.TimeCounter;
import com.controller.timer.TimerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Created by ivan on 25/04/17.
 */
public class Application {
    private JPanel mainPanel;
    private TaskManager taskManager;
    private TimeCounter stopWatch, countdownTimer;
    private JPanel[][] gridPanel;
    private DefaultListModel model;
    private JButton addTaskBtn, deleteTaskBtn, modifyTaskBtn;
    private JList taskJList;
    private JLabel stopWatchLabel, countdownLabel;
    private JButton startTimer, stopTimer, resetTimer;
    private JButton saveTasks;
    private Task currentTask;
    private TimerThread timerThread;
    private JScrollPane scrollPane;
    private JCheckBox timerMode;
    public Application(TaskManager taskManager, TimeCounter stopWatch, TimeCounter countdownTimer) {
        this.taskManager = taskManager;
        this.stopWatch = stopWatch;
        this.countdownTimer = countdownTimer;
        int rows = 4, cols = 2;
        mainPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel = new JPanel[rows][cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            gridPanel[i][j] = new JPanel();
            mainPanel.add(gridPanel[i][j]);
        }

        addTaskBtn = new JButton("   Add a new task   ");
        modifyTaskBtn = new JButton("Modify selected task");
        deleteTaskBtn = new JButton("Delete selected task");
        startTimer = new JButton("Play");
        stopTimer = new JButton("Stop");
        resetTimer = new JButton("Reset");
        saveTasks = new JButton("Save tasks");
        model = new DefaultListModel();
        taskJList = new JList(model);
        timerMode = new JCheckBox();
        timerMode.setSelected(true);
        stopWatchLabel = new JLabel("00:00:00");
        countdownLabel = new JLabel("--:--:--");

        taskJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskJList.setFixedCellWidth(170);

        gridPanel[0][0].add(taskJList);
        gridPanel[0][1].add(addTaskBtn);
        gridPanel[0][1].add(modifyTaskBtn);
        gridPanel[0][1].add(deleteTaskBtn);
        gridPanel[1][1].add(new JLabel("Total task time: "));
        gridPanel[1][1].add(stopWatchLabel);
        gridPanel[1][1].add(new JLabel("Time remaining: "));
        gridPanel[1][1].add(countdownLabel);
        gridPanel[1][1].add(startTimer);
        gridPanel[1][1].add(stopTimer);
        gridPanel[1][1].add(resetTimer);
        gridPanel[2][1].add(timerMode);
        gridPanel[2][1].add(new JLabel("Stop main timer when"));
        gridPanel[2][1].add(new JLabel("time remaining hits zero"));
        gridPanel[3][1].add(saveTasks);

        setTaskListeners();
        setTimerListeners();

        updateTaskJList();

        timerThread = new TimerThread(mainPanel, stopWatch, countdownTimer, stopWatchLabel, countdownLabel);
        timerThread.start();
    }

    private void setTimerListeners() {
        startTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not selected a task yet");
                    return;
                }
                try {
                    timerThread.startTimers();
                } catch (Exception e) {};

            }
        });
        stopTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not selected a task yet");
                    return;
                }
                try {
                    timerThread.stopTimers();
                } catch (Exception e) {};

            }
        });
        resetTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not selected a task yet");
                    return;
                }
                try {
                    timerThread.resetTimers();
                } catch (Exception e) {};

            }
        });
        timerMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == 1) stopWatch.setMode(Mode.LIMITED);
                else stopWatch.setMode(Mode.UNLIMITED);
            }
        });
    }
    private void setTaskListeners() {
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            boolean ok = false;
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                if (ok) {
                    ok = false;
                    return;
                }
                JList list = (JList) listSelectionEvent.getSource();
                int selected = list.getSelectedIndex();
                int previous = selected == listSelectionEvent.getFirstIndex() ? listSelectionEvent.getLastIndex() : listSelectionEvent.getFirstIndex();
                if (!adjust) {
                    if (!stopWatch.getIsStopped() && previous != -1) {
                        ok = true;
                        System.out.println("ok now: " + ok);
                        JOptionPane.showMessageDialog(mainPanel, "You have not stopped the clock");
                        taskJList.setSelectedIndex(previous);
                        return;
                    }
                    try {
                        synchronized (this) {
                            int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                            currentTask = taskManager.getTask(id);
                            stopWatch.associate(currentTask);
                            countdownTimer.associate(currentTask);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        };
        taskJList.addListSelectionListener(listSelectionListener);

        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!stopWatch.getIsStopped()) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not stopped the clock");
                    return;
                }
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();
                Object[] message = {
                        "Task name:", jTaskName,
                        "Priority:", jTaskPriority,
                        "Task Time (in minutes): ", jTaskTime
                };
                int option = JOptionPane.showConfirmDialog(mainPanel, message, "New task", JOptionPane.OK_CANCEL_OPTION);

                String taskName = jTaskName.getText();

                String selectedPriority = jTaskPriority.getSelectedItem().toString();
                Priority taskPriority = Priority.HIGH;
                if (selectedPriority.equals("High")) taskPriority = Priority.HIGH;
                else if (selectedPriority.equals("Medium")) taskPriority = Priority.MEDIUM;
                else if (selectedPriority.equals("Low")) taskPriority = Priority.LOW;

                if (option == JOptionPane.OK_OPTION) {
                    if (!taskName.equals("")) {
                        try {
                            int taskTime = Integer.parseInt(jTaskTime.getText());
                            taskManager.addTask(taskName, taskPriority, taskTime);
                            System.out.println("Task successfully added");
                            updateTaskJList();
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(mainPanel, "Task time is not a valid number", "Invalid time", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Task name is empty", "Empty name", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        modifyTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!stopWatch.getIsStopped()) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not stopped the clock");
                    return;
                }
                String taskName;
                int taskTime;
                Priority taskPriority = Priority.HIGH;
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();

                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not selected a task yet");
                    return;
                }
                try {
                    int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                    taskName = taskManager.getTask(id).getName();
                    jTaskName.setText(taskName);
                    taskTime = taskManager.getTask(id).getTaskTime() / 60;
                    jTaskTime.setText(String.valueOf(taskTime));
                    taskPriority = taskManager.getTask(id).getPriority();
                    jTaskPriority.setSelectedIndex(taskPriority.ordinal());
                } catch (Exception e) {};
                Object[] message = {
                        "Task name:", jTaskName,
                        "Priority:", jTaskPriority,
                        "Task Time (in minutes): ", jTaskTime
                };
                int option = JOptionPane.showConfirmDialog(mainPanel, message, "Modify task", JOptionPane.OK_CANCEL_OPTION);

                taskName = jTaskName.getText();

                String selectedPriority = jTaskPriority.getSelectedItem().toString();

                if (selectedPriority.equals("High")) taskPriority = Priority.HIGH;
                else if (selectedPriority.equals("Medium")) taskPriority = Priority.MEDIUM;
                else if (selectedPriority.equals("Low")) taskPriority = Priority.LOW;

                if (option == JOptionPane.OK_OPTION) {
                    if (!taskName.equals("")) {
                        try {
                            taskTime = Integer.parseInt(jTaskTime.getText());
                            try {
                                int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                                currentTask = taskManager.modifyTask(id, taskName, taskPriority, taskTime);
                                stopWatch.associate(currentTask);
                                countdownTimer.associate(currentTask);
                                System.out.println("Task successfully modified");
                                updateTaskJList();
                            } catch (Exception e) {

                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(mainPanel, "Task time is not a valid number", "Invalid time", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Task name is empty", "Empty name", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!stopWatch.getIsStopped()) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not stopped the clock");
                    return;
                }
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "You have not selected a task yet");
                    return;
                }

                String message = "Are you sure you want to delete this task?";
                int option = JOptionPane.showConfirmDialog(mainPanel, message, "Delete task", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                        Task deletedTask = taskManager.getTask(id);
                        if (deletedTask == currentTask) {
                            stopWatchLabel.setText("00:00:00");
                            countdownLabel.setText("--:--:--");
                            currentTask = null;
                            stopWatch.associate(currentTask);
                            countdownTimer.associate(currentTask);
                        }
                        taskManager.deleteTask(id);
                        System.out.println("Task successfully deleted");
                        updateTaskJList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        saveTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                taskManager.saveTasks();
                JOptionPane.showMessageDialog(mainPanel, "Tasks have been successfully saved");
            }
        });
    }

    private void updateTaskJList() {
        model.removeAllElements();
        ArrayList<Task> taskList = taskManager.getTaskList();
        ArrayList<Task> clonedTaskList = new ArrayList<Task>();
        for (Task t : taskList) clonedTaskList.add(new Task(t));
        Collections.sort(clonedTaskList);
        for (int i = 0; i < clonedTaskList.size(); i++) {
            Task t = clonedTaskList.get(i);
            model.add(i, String.valueOf(t.getId()) + ' ' + t.getName());
        }
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
