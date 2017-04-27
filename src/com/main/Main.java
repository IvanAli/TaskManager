package com.main;

import com.TaskManagerGUI;
import com.task.Priority;
import com.task.Task;
import com.task.TaskManager;
import com.timer.*;
import view.Application;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
	    // write your code here
//        Tester tester = new Tester();
//        tester.go();
        TaskManager taskManager = new TaskManager();
//        TaskManagerGUI gui = new TaskManagerGUI(taskManager);
        Application gui = new Application(taskManager);
        JFrame frame = new JFrame("Task Manager");
        frame.setPreferredSize(new Dimension(400, 400));;
        frame.setContentPane(gui.getMainPanel());
//        frame.setContentPane(gui.getTaskPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        /*while (true) {
            for (Task t : taskManager.getTaskList()) {
                if (!t.getStopWatch().getIsStopped()) {
                    t.getStopWatch().run();
                }
                if (!t.getCountdownTimer().getIsStopped()) {
                    t.getCountdownTimer().run();
                }
            }
        }*/
    }
}
