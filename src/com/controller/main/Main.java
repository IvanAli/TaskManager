package com.controller.main;

import com.controller.task.TaskManager;
import com.model.timer.CountdownTimerStrategy;
import com.model.timer.StopWatchStrategy;
import com.model.timer.TimeCounter;
import com.view.Application;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        TaskManager taskManager = new TaskManager();
        TimeCounter stopWatch = new TimeCounter(new StopWatchStrategy());
        TimeCounter countdownTimer = new TimeCounter(new CountdownTimerStrategy());
        Application gui = new Application(taskManager, stopWatch, countdownTimer);
        JFrame frame = new JFrame("Task Manager");
        frame.setPreferredSize(new Dimension(400, 440));;
        frame.setContentPane(gui.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
