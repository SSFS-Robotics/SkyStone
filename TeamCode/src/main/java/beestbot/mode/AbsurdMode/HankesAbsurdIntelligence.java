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

package beestbot.mode.AbsurdMode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;

import beestbot.io.FileSerialization;
import beestbot.io.GamepadManager;
import beestbot.motion.MotionManager;
import beestbot.state.Inverse;
import beestbot.state.SensorSignals;
import beestbot.state.State;
import beestbot.util.Configuration;
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
@TeleOp(name = "HankesAbsurdIntelligence", group = "OpMode")
@Disabled
// TODO: WARNING - When there is an issue updating the opMode, try: Build->Clean Project
public class HankesAbsurdIntelligence extends BeestAbsurdMode {

    private MotionManager motionManager;
    private ArrayList<SensorSignals> positionStream = new ArrayList<>();


    @Override
    public void init() {
        super.msStuckDetectInit     = 5000;
        super.msStuckDetectInitLoop = 5000;
        super.msStuckDetectStart    = 5000;
        super.msStuckDetectLoop     = 5000;
        super.msStuckDetectStop     = 10000;

        // initiate modes based on specific settings
        setTeam();
        setSide();
        setState();
        setVisionManager();

        motionManager = new MotionManager(telemetry, hardwareMap);
        Configuration.gamepadManager = new GamepadManager();
        Configuration.visionManager.enable();
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.addData("Recorded Files:", FileSerialization.getInternalStorageList(hardwareMap.appContext));
        telemetry.update();
    }

    @Override
    public void init_loop() {
        long lStartTime = System.currentTimeMillis();

        // update [SensorSignals laskKnown]
        SensorSignals positions = Configuration.visionManager.fetch();
        if (positionStream.size() > Configuration.maximumStream)
            positionStream.remove(0);
        positionStream.add(positions);
        SensorSignals _ = calculateLastKnown(positionStream);
        if (_ != SensorSignals.UNKNOWN) {
            Configuration.setSensorSignal(_);
            telemetry.addData("Sensor", "randomPosition = %s", Configuration.getSensorSignal());
        }

        long timeElapsed = System.currentTimeMillis() - lStartTime;
        telemetry.addData("Record", "timeElapsed = %d", timeElapsed);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.addData("Recorded Files:", FileSerialization.getInternalStorageList(hardwareMap.appContext));
        telemetry.update();
    }

    @Override
    public void start() {
//        if (Configuration.getSensorSignal() == SensorSignals.UNKNOWN) throw new InvalidParameterException("SensorSignals cannot be 'UNKNOWN'");
        Configuration.visionManager.disable();
        resetStartTime();
        if (Configuration.getState() == State.AUTONOMOUS) {
            Configuration.debugMessage = Configuration.debugMessage + "Getting file in " + Configuration.getFileName() + "; ";

            Object _ = FileSerialization.loadInternal(hardwareMap.appContext, Configuration.getFileName(), telemetry);
            if (_ != null) {
                Configuration.gamepadsTimeStream = (ArrayList<GamepadManager>) _;
                Configuration.debugMessage = Configuration.debugMessage + "File != null; ";
            } else {
                Configuration.debugMessage = Configuration.debugMessage + "File == null; Object is null; ";
            }
        }
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void loop() {

        telemetry.addData("Record", "timeElapsed = %f", time);
        if (Configuration.getState() == State.CONTROL) {
            Configuration.gamepadManager.update(gamepad1, gamepad2);
        } else if (Configuration.getState() == State.RECORDING) {

            telemetry.addData("Reverse Mode Running:", String.valueOf(Configuration.inverse));
            // this line must be in front since Configuration.gamepadManager will later be altered if inverse mode is activated
            if (Configuration.inverse != Inverse.NO_INVERSE) {
                Configuration.gamepadManager.inverselyUpdate(gamepad1, gamepad2);
                Configuration.inversedGamepadsTimeStream.add(Configuration.gamepadManager.clone());
            }

            Configuration.gamepadManager.update(gamepad1, gamepad2);
            Configuration.gamepadsTimeStream.add(Configuration.gamepadManager.clone());

        } else if (Configuration.getState() == State.AUTONOMOUS && Configuration.gamepadsTimeStream.size() >0) {
            telemetry.addData("Running Record:", Configuration.getFileName());
            Configuration.gamepadManager = Configuration.gamepadsTimeStream.get(0);
            Configuration.gamepadsTimeStream.remove(0);
        }
        motionManager.updateWithException(Configuration.gamepadManager);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (Configuration.getState() == State.RECORDING) {
            boolean successful = FileSerialization.saveInternal(hardwareMap.appContext, Configuration.getFileName(), Configuration.gamepadsTimeStream, telemetry);
            Configuration.debugMessage = Configuration.debugMessage + "Configuration saved in " + Configuration.getFileName() + "; Successful = " + Boolean.toString(successful) + "; ";
//            String s = FileSerialization.readInternal(hardwareMap.appContext, Configuration.getFileName(), telemetry);
//            FileSerialization.setClipboard(hardwareMap.appContext, s);

            if (Configuration.inverse != Inverse.NO_INVERSE) {
                successful = FileSerialization.saveInternal(hardwareMap.appContext, Configuration.getInverseFileName(), Configuration.inversedGamepadsTimeStream, telemetry);
                Configuration.debugMessage = Configuration.debugMessage + "Inversed Configuration saved in " + Configuration.getFileName() + "; Successful = " + Boolean.toString(successful) + "; ";
            }
        }
        telemetry.addData("Time", "time = %f", time);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    private static SensorSignals calculateLastKnown(ArrayList<SensorSignals> positionStream) {
        for (SensorSignals pos: positionStream) {
            if (positionStream.get(0) != pos) {
                return SensorSignals.UNKNOWN;
            }
        }
        return positionStream.get(0);
    }
}