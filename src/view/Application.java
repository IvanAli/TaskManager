package view;

import com.task.Priority;
import com.task.Task;
import com.task.TaskManager;
import com.timer.TimeCounter;
import com.timer.TimerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
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
    public Application(TaskManager taskManager, TimeCounter stopWatch, TimeCounter countdownTimer) {
        this.taskManager = taskManager;
        this.stopWatch = stopWatch;
        this.countdownTimer = countdownTimer;
        int rows = 3, cols = 2;
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
        gridPanel[2][1].add(saveTasks);

        setTaskListeners();
        setTimerListeners();

        updateTaskJList();

        timerThread = new TimerThread(stopWatch, countdownTimer, stopWatchLabel, countdownLabel);
        timerThread.start();
    }

    private void setTimerListeners() {
        startTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.startTimers();
                } catch (Exception e) {};

            }
        });
        stopTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.stopTimers();
                } catch (Exception e) {};

            }
        });
        resetTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.resetTimers();
                } catch (Exception e) {};

            }
        });
    }
    private void setTaskListeners() {
//        MouseListener mouseListener = new MouseAdapter() {
//            public void mouseClicked(MouseEvent mouseEvent) {
//                JList theList = (JList) mouseEvent.getSource();
//                if (mouseEvent.getClickCount() == 2) {
//                    int index = theList.locationToIndex(mouseEvent.getPoint());
//                    if (index >= 0) {
//                        Object o = theList.getModel().getElementAt(index);
//                        System.out.println("Double-clicked on: " + o.toString());
//                    }
//                }
//            }
//        };
//        taskJList.addMouseListener(mouseListener);

        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            boolean ok = false;
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
//                if (adjust) return;
                System.out.println("OK: " + ok);
                if (ok) {
                    ok = false;
                    return;
                }
                JList list = (JList) listSelectionEvent.getSource();
                int selected = list.getSelectedIndex();
                int previous = selected == listSelectionEvent.getFirstIndex() ? listSelectionEvent.getLastIndex() : listSelectionEvent.getFirstIndex();
                if (!adjust) {
                    if (!stopWatch.getIsStopped()) {
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
//                        timerThread.setCurrentTask(currentTask);
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
                    }
                }
            }
        });

        modifyTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String taskName;
                int taskTime;
                Priority taskPriority = Priority.HIGH;
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();

                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
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
                int option = JOptionPane.showConfirmDialog(null, message, "Modify task", JOptionPane.OK_CANCEL_OPTION);

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
//                                currentTask = taskManager.modifyTask(id, taskName, taskPriority, taskTime);
//                                timerThread.setCurrentTask(currentTask);
                                System.out.println("Task successfully modified");
                                updateTaskJList();
                            } catch (Exception e) {

                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(mainPanel, "Task time is not a valid number", "Invalid time", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
            }
        });

        deleteTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }

                String message = "Are you sure you want to delete this task?";
                int option = JOptionPane.showConfirmDialog(null, message, "Delete task", JOptionPane.OK_CANCEL_OPTION);

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
//                            timerThread.setCurrentTask(currentTask);
                        }
                        taskManager.deleteTask(id);
                        System.out.println("Task successfully deleted");
                        updateTaskJList();
                    } catch (Exception e) {

                    }
                }
            }
        });

        saveTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                taskManager.saveTasks();
                JOptionPane.showMessageDialog(null, "Tasks have been successfully saved");
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
