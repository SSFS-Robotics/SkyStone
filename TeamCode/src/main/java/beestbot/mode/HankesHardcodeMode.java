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

import beestbot.io.GamepadManager;
import beestbot.mode.AbsurdMode.BeestAbsurdMode;
import beestbot.motion.MotionManager;
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
@TeleOp(name = "HankesHardcodeMode", group = "Autonomous")
// TODO: WARNING - When there is an issue updating the opMode, try: Build->Clean Project
public class HankesHardcodeMode extends BeestAbsurdMode {

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
        Configuration.setState(State.RECORDING);
    }
    @Override
    public void setVisionManager() {
        Configuration.visionManager = new SkyStoneVsionManager(hardwareMap, telemetry);
    }

    @Override
    public void sub_init() {

    }

    @Override
    public void sub_init_loop() {
    }

    @Override
    public void sub_start() {
        try { // TODO: CAREFUL The stack will execute backward
            Configuration.tasks.push(new Task(2, null, Task.getMethod("dropBlock"), Task.getMethod("stop"), telemetry));
            telemetry.addData("DEBUG", "added dropBlock");
            Configuration.tasks.push(new Task(2, null, Task.getMethod("moveFront"), Task.getMethod("stop"), telemetry));
            telemetry.addData("DEBUG", "added moveFront");
            Configuration.tasks.push(new Task(2, null, Task.getMethod("grabBlock"), Task.getMethod("stop"), telemetry));
            telemetry.addData("DEBUG", "added grabBlock");
            Configuration.tasks.push(new Task(60, null, Task.getMethod("goToSkyStone"), Task.getMethod("stop"), telemetry));
            telemetry.addData("DEBUG", "added goToSkyStone");
        } catch (NoSuchMethodException e) {
            telemetry.addData("CRASH", "There is a crash when you tries to add tasks");
            telemetry.update();
            e.printStackTrace();
        }
    }

    @Override
    public void sub_loop() {
        // update sensor signal and refinement
        if (Configuration.signals.size() > 10) {SensorSignals _ = Configuration.signals.poll();}
        Configuration.signals.offer(Configuration.visionManager.fetch());
        ArrayList<Integer> i = new ArrayList<>();
        for (SensorSignals ss : Configuration.signals) {
            i.add(ss.getValue());
        }
        Configuration.signal = SensorSignals.getSensorSignals(Util.mode(Util.convertIntegers(i)));
        telemetry.addData("DEBUG", "Finished Setting SensorSignals");

        // TASK MANAGER
        if (Configuration.currentTask == null) {
            if (!Configuration.tasks.empty()) {
                telemetry.addData("DEBUG", "No Current Task");
                // if there is no task: get next task and invoke init method
                Task task = Configuration.tasks.pop(); // TODO: assume it is not Null
                task.setStartTime(time);
                task.setEndTime(time + task.getLastTime());
                Configuration.currentTask = task;
                telemetry.addData("DEBUG", "Setting a Task");
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
            telemetry.addData("DEBUG", "There is a Task");
            // if there is a task, if the task should end: invoke end method
            Task task = Configuration.currentTask;
            if (time > task.getEndTime()) {
                telemetry.addData("DEBUG", "time > task.getEndTime()");
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
                telemetry.addData("DEBUG", "time < task.getEndTime()");
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