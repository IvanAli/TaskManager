package com.main;

import com.task.Priority;
import com.task.Task;
import com.task.TaskManager;
import com.timer.CountdownTimerStrategy;
import com.timer.StopWatchStrategy;
import com.timer.TimeCounter;

/**
 * Created by ivan on 25/04/17.
 */
public class Tester {
    public void test() {
//        TaskManager taskManager = new TaskManager();
//        Task testTask = taskManager.addTask("Code all night", Priority.HIGH, 5);
//        TimeCounter stopWatch = new TimeCounter(new StopWatchStrategy(testTask));
//        TimeCounter countdownTimer = new TimeCounter(new CountdownTimerStrategy(testTask));
//        try {
//            stopWatch.start();
//            long prv = stopWatch.getCurrentTime();
//            while (true) {
//                boolean res = stopWatch.run();
//                long cur = stopWatch.getCurrentTime();
//                if (prv < cur) System.out.println(cur);
//                prv = cur;
//                if (res) break;
//            }
//        } catch(Exception e) {
//
//        }
    }
    public void go() {
        TaskManager taskManager = new TaskManager();
        System.out.println("Actions:");
        System.out.println("1) add new task");
        System.out.println("2) modify existing task");
        System.out.println("3) delete existing task");
    }
}
