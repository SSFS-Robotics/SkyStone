/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package beestbot.mode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import beestbot.mode.AbsurdMode.BeestAbsurdMode;
import beestbot.state.SensorSignals;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Task;
import beestbot.state.Team;
import beestbot.util.Configuration;
import beestbot.util.Util;
import beestbot.vision.SkyStoneVsionManager;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "HankesCheatingRedMode", group = "Autonomous")
// TODO: WARNING - When there is an issue updating the opMode, try: Build->Clean Project
public class HankesCheatingRedMode extends BeestAbsurdMode {

    private long startTime = 0;

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    }
    @Override
    public void setSide() {
        Configuration.setSide(Side.FOUNDATION);
    }
    @Override
    public void setState() {
        Configuration.setState(State.HARDCODE);
    }
    @Override
    public void setVisionManager() {
        Configuration.visionManager = new SkyStoneVsionManager(hardwareMap, telemetry, true, false, false);
    }

    @Override
    public void sub_init() {
        this.startTime = System.nanoTime();
    }

    @Override
    public void sub_init_loop() {
        int estimatedTime = (int)((double)(System.nanoTime() - startTime) / Math.pow(10, 9));
        int positionNow = estimatedTime % 6;

        switch (positionNow) {
            case 0:
                Configuration.signal = SensorSignals.SKYSTONE_AT_ONE;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            case 1:
                Configuration.signal = SensorSignals.SKYSTONE_AT_TWO;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            case 2:
                Configuration.signal = SensorSignals.SKYSTONE_AT_THREE;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            case 3:
                Configuration.signal = SensorSignals.SKYSTONE_AT_FOUR;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            case 4:
                Configuration.signal = SensorSignals.SKYSTONE_AT_FIVE;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            case 5:
                Configuration.signal = SensorSignals.SKYSTONE_AT_SIX;
                telemetry.addData("CHEATING", "Finished Setting SensorSignals: " + Configuration.signal.toString());
                break;
            default:
                throw new IllegalArgumentException("What? YOU DON'T KNOW HOW TO CHEAT? STUPID!");
        }
    }

    @Override
    public void sub_start() {
        try {
            // park
            Configuration.tasks.push(new Task(4.5, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // move foundation

            // turning and push foundation
            Configuration.tasks.push(new Task(0.1, null, Task.getMethod("releaseFoundation"), Task.getMethod("stop"), telemetry)); // grab
            Configuration.tasks.push(new Task(2, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // move foundation
            Configuration.tasks.push(new Task(0.05, null, Task.getMethod("turnLeft"), Task.getMethod("stop"), telemetry)); // 90 back
            Configuration.tasks.push(new Task(1.6, null, Task.getMethod("turnRight"), Task.getMethod("stop"), telemetry)); // right 90 degree

            // grab and move back
            Configuration.tasks.push(new Task(2.5, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // move foundation
            Configuration.tasks.push(new Task(0.1, null, Task.getMethod("grabFoundation"), Task.getMethod("stop"), telemetry)); // grab

            // left and push
            Configuration.tasks.push(new Task(2.5, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // go to foundation
            Configuration.tasks.push(new Task(0.05, null, Task.getMethod("turnRight"), Task.getMethod("stop"), telemetry)); // 90 back
            Configuration.tasks.push(new Task(1.5, null, Task.getMethod("turnLeft"), Task.getMethod("stop"), telemetry)); // left 90 degree

            // to foundation
            Configuration.tasks.push(new Task(0.1, null, Task.getMethod("dropBlock"), Task.getMethod("stop"), telemetry)); // drop block
            Configuration.tasks.push(new Task(7, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // go to foundation

            // go back
            Configuration.tasks.push(new Task(2, null, Task.getMethod("moveRight"), Task.getMethod("stop"), telemetry)); // move back little bit

            // grab block
            Configuration.tasks.push(new Task(1, null, Task.getMethod("grabBlock"), Task.getMethod("stop"), telemetry)); // grabBlock

            // go to correct position
            switch (Configuration.signal) {
                case SKYSTONE_AT_SIX:
                    Configuration.tasks.push(new Task(0.35*0 + 0.20, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case SKYSTONE_AT_FIVE:
                    Configuration.tasks.push(new Task(0.35*1 + 0.20, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case SKYSTONE_AT_FOUR:
                    Configuration.tasks.push(new Task(0.35*0 + 0.20, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case SKYSTONE_AT_THREE:
                    Configuration.tasks.push(new Task(0.35*1 + 0.20, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case SKYSTONE_AT_TWO:
                    Configuration.tasks.push(new Task(0.35*2 + 0.20, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case SKYSTONE_AT_ONE:
                    Configuration.tasks.push(new Task(0.35*3 + 0.20, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // grabBlock
                    break;
                case UNKNOWN:
                    break;
                case NOTHING:
                    break;
                default:
            }

            // go to skystones
            Configuration.tasks.push(new Task(0.05, null, Task.getMethod("turnLeft"), Task.getMethod("stop"), telemetry)); // 90 back
            Configuration.tasks.push(new Task(1.4, null, Task.getMethod("turnRight"), Task.getMethod("stop"), telemetry)); // right 90 degree
            Configuration.tasks.push(new Task(0.1, null, Task.getMethod("moveBack"), Task.getMethod("stop"), telemetry)); // go to skystone back
            Configuration.tasks.push(new Task(2.2, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry)); // go to skystone
            Configuration.tasks.push(new Task(0.05, null, Task.getMethod("turnRight"), Task.getMethod("stop"), telemetry)); // 90 back
            Configuration.tasks.push(new Task(1.5, null, Task.getMethod("turnLeft"), Task.getMethod("stop"), telemetry)); // left 90 degree
            Configuration.tasks.push(new Task(0.1, null, Task.getMethod("moveLeft"), Task.getMethod("stop"), telemetry)); // just get out
        } catch (NoSuchMethodException e) {
            telemetry.addData("CRASH", "There is a crash when you tries to add tasks");
            telemetry.update();
            e.printStackTrace();
        }
    }

    @Override
    public void sub_loop() {
        // TASK MANAGER
        if (Configuration.currentTask == null) {
            if (!Configuration.tasks.empty()) {
                // if there is no task: get next task and invoke init method
                Task task = Configuration.tasks.pop(); // TODO: assume it is not Null
                telemetry.addData("TASK", "No Current Task; Next: " + task.getLoopMethod().getName());
                task.setStartTime(System.nanoTime()/Math.pow(10, 9));
                task.setEndTime(System.nanoTime()/Math.pow(10, 9) + task.getLastTime());
                Configuration.currentTask = task;
                try {
                    // ref: https://www.math.uni-hamburg.de/doc/java/tutorial/reflect/object/invoke.html
                    // invoke(FatherClass, new Object[] {inputData}
                    if (task.getInitMethod() != null) {task.getInitMethod().invoke(task);}
                } catch (IllegalAccessException | InvocationTargetException e) {
                    telemetry.addData("CRASH", "There is a crash when you tries to getInitMethod()");
                    telemetry.update();
                    e.printStackTrace();
                }
            } else {
                telemetry.addData("DEBUG", "ALL TASKS FINISHED! CHEER!");
                telemetry.addData("DEBUG", "ALL TASKS FINISHED! CHEER!");
                telemetry.addData("DEBUG", "ALL TASKS FINISHED! CHEER!");
                telemetry.addData("DEBUG", "ALL TASKS FINISHED! CHEER!");
            }
        } else {
            // if there is a task, if the task should end: invoke end method
            Task task = Configuration.currentTask;
            telemetry.addData("TASK", "Current Task: " + task.getLoopMethod().getName());
            if (System.nanoTime()/Math.pow(10, 9) > task.getEndTime()) {
                telemetry.addData("TASK", "time("+ String.valueOf(System.nanoTime()/Math.pow(10, 9)) + ") > task.getEndTime(" + String.valueOf(task.getEndTime()) + ")");
                try {
                    if (task.getEndMethod() != null) {task.getEndMethod().invoke(task);}
                } catch (IllegalAccessException | InvocationTargetException e) {
                    telemetry.addData("CRASH", "There is a crash when you tries to getEndMethod()");
                    telemetry.update();
                    e.printStackTrace();
                }
                Configuration.currentTask = null;
                // if there is a task, if the task should continue: invoke loop method
            } else {
                telemetry.addData("TASK", "time("+ String.valueOf(System.nanoTime()/Math.pow(10, 9)) + ") < task.getEndTime(" + String.valueOf(task.getEndTime()) + ")");
                try {
                    if (task.getLoopMethod() != null) {task.getLoopMethod().invoke(task);}
                } catch (IllegalAccessException | InvocationTargetException e) {
                    telemetry.addData("CRASH", "There is a crash when you tries to getLoopMethod()");
                    telemetry.update();
                    e.printStackTrace();
                }
            }

        }
        motionManager.updateWithException(Configuration.gamepadManager);
        if (Configuration.currentTask != null) {
            telemetry.addData("TASK", "loop: " + Configuration.currentTask.getLoopMethod().getName());
        } else {
            telemetry.addData("TASK", "loop: null");
        }
    }

    @Override
    public void sub_stop() {

    }
}