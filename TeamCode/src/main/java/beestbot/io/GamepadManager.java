package beestbot.io;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import beestbot.state.Inverse;
import beestbot.util.Configuration;

public class GamepadManager implements Serializable, Cloneable {

    /*
        MOTOR
     */
    public double forceFrontLeftMotor = 0;
    public double forceFrontRightMotor = 0;
    public double forceBackLeftMotor = 0;
    public double forceBackRightMotor = 0;

    public double forceArmLeftMotor; // deprecated
    public double forceArmRightMotor; // deprecated

    public double forceLiftMotor = 0;

//    private int positionArmMotor;
//    private boolean ifArmEncoder = false;

    public static Integer LIFTING_REVOLUTION = 4; //TODO: adjust lifting revolution
    /*
        SERVO
     */
    public float forceFrontLeftServo = 0f;
    public float forceFrontRightServo = 0f;
    public float forceClipServo = -1.0f;
    public float forceTouchServo; // deprecated

    public GamepadManager() {
        init();
    }

    private void init() {
        if (Configuration.inverse == Inverse.X_AXIS) {
            forceFrontLeftMotor = Range.clip(0 , -1f, 1f);
            forceFrontRightMotor = -Range.clip(0, -1f, 1f);
            forceBackLeftMotor = Range.clip(0, -1f, 1f);
            forceBackRightMotor = -Range.clip(0, -1f, 1f);


        /*
            For lifting
         */
            forceLiftMotor = Range.clip(0 * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
            forceFrontRightServo = clamp(0, 0f, 1f, 0.2f, 1.0f); // 1~0.2
            forceFrontLeftServo = clamp(0, -1f, 0f, 0.2f, 1.0f); // 0.2-1

        /*
            For clips
         */
            forceClipServo = Range.clip(clamp(0, -1.0f, 0.0f, 0.7f, 1.0f), 0.7f, 1.0f);
        } else {
            throw new UnsupportedOperationException("inverselyUpdate() method not implemented for additional Inverse!");
        }
    }

    public void updateInitial() {
        init();
    }

    public void inverselyUpdate(Gamepad gp1, Gamepad gp2) {
        if (Configuration.inverse == Inverse.X_AXIS) {
        /*
            For wheels
         */

            // when first gamepad is not controlling movement, second gamepad will take over
            float LF_1 = (gp2.left_stick_y - -gp2.left_stick_x - -gp2.right_stick_x)*1.0f + (gp1.left_stick_y - -gp1.left_stick_x - 0)*0.4f;
            float RF_1 = (gp2.left_stick_y + -gp2.left_stick_x + -gp2.right_stick_x)*1.0f + (gp1.left_stick_y + -gp1.left_stick_x + 0)*0.4f;
            float LB_1 = (gp2.left_stick_y + -gp2.left_stick_x - -gp2.right_stick_x)*1.0f + (gp1.left_stick_y + -gp1.left_stick_x - 0)*0.4f;
            float RB_1 = (gp2.left_stick_y - -gp2.left_stick_x + -gp2.right_stick_x)*1.0f + (gp1.left_stick_y - -gp1.left_stick_x + 0)*0.4f;
            Float[] decMax_1 = new Float[]{Math.abs(LF_1), Math.abs(RF_1), Math.abs(LB_1), Math.abs(RB_1)};
            List<Float> a_1 = new ArrayList<>(Arrays.asList(decMax_1));
            float max_1 = Range.clip(Collections.max(a_1), 1f, Float.MAX_VALUE);
            LF_1 = (LF_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
            RF_1 = (RF_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
            LB_1 = (LB_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
            RB_1 = (RB_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;

            forceFrontLeftMotor = Range.clip(LF_1 , -1f, 1f);
            forceFrontRightMotor = -Range.clip(RF_1, -1f, 1f);
            forceBackLeftMotor = Range.clip(LB_1, -1f, 1f);
            forceBackRightMotor = -Range.clip(RB_1, -1f, 1f);


        /*
            For lifting
         */
            forceLiftMotor = Range.clip(gp1.right_stick_y * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
            forceFrontRightServo = clamp(gp1.left_trigger, 0f, 1f, 0.2f, 1.0f); // 1~0.2
            forceFrontLeftServo = clamp(-gp1.left_trigger, -1f, 0f, 0.2f, 1.0f); // 0.2-1

        /*
            For clips
         */
            forceClipServo = Range.clip(clamp(-gp1.right_trigger, -1.0f, 0.0f, 0.7f, 1.0f), 0.7f, 1.0f);
        } else {
            throw new UnsupportedOperationException("inverselyUpdate() method not implemented for additional Inverse!");
        }
    }

    public void update(Gamepad gp1, Gamepad gp2) {
        /*
            For wheels
         */

        // when first gamepad is not controlling movement, second gamepad will take over
        float LF_1 = (gp2.left_stick_y - gp2.left_stick_x - gp2.right_stick_x)*1.0f + (gp1.left_stick_y - gp1.left_stick_x - 0)*0.4f;
        float RF_1 = (gp2.left_stick_y + gp2.left_stick_x + gp2.right_stick_x)*1.0f + (gp1.left_stick_y + gp1.left_stick_x + 0)*0.4f;
        float LB_1 = (gp2.left_stick_y + gp2.left_stick_x - gp2.right_stick_x)*1.0f + (gp1.left_stick_y + gp1.left_stick_x - 0)*0.4f;
        float RB_1 = (gp2.left_stick_y - gp2.left_stick_x + gp2.right_stick_x)*1.0f + (gp1.left_stick_y - gp1.left_stick_x + 0)*0.4f;
        Float[] decMax_1 = new Float[]{Math.abs(LF_1), Math.abs(RF_1), Math.abs(LB_1), Math.abs(RB_1)};
        List<Float> a_1 = new ArrayList<>(Arrays.asList(decMax_1));
        float max_1 = Range.clip(Collections.max(a_1), 1f, Float.MAX_VALUE);
        LF_1 = (LF_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
        RF_1 = (RF_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
        LB_1 = (LB_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;
        RB_1 = (RB_1 / max_1) * Configuration.ABSOLUTE_SPEED *0.6f;

        forceFrontLeftMotor = Range.clip(LF_1 , -1f, 1f);
        forceFrontRightMotor = -Range.clip(RF_1, -1f, 1f);
        forceBackLeftMotor = Range.clip(LB_1, -1f, 1f);
        forceBackRightMotor = -Range.clip(RB_1, -1f, 1f);


        /*
            For lifting
         */
        forceLiftMotor = Range.clip(gp1.right_stick_y * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
        forceFrontRightServo = clamp(gp1.left_trigger, 0f, 1f, 0.2f, 1.0f); // 1~0.2
        forceFrontLeftServo = clamp(-gp1.left_trigger, -1f, 0f, 0.2f, 1.0f); // 0.2-1

        /*
            For clips
         */
        forceClipServo = Range.clip(clamp(-gp1.right_trigger, -1.0f, 0.0f, 0.7f, 1.0f), 0.7f, 1.0f);
    }
    public double getForceFrontLeftMotor() {
        return forceFrontLeftMotor;
    }

    public double getForceFrontRightMotor() {
        return forceFrontRightMotor;
    }

    public double getForceBackLeftMotor() {
        return forceBackLeftMotor;
    }

    public double getForceBackRightMotor() {
        return forceBackRightMotor;
    }

    public double getForceArmLeftMotor() {
        return forceArmLeftMotor;
    }

    public double getForceArmRightMotor() {
        return forceArmRightMotor;
    }

    public double getForceLiftMotor() {
        return forceLiftMotor;
    }

    public float getForceFrontLeftServo() {
        return forceFrontLeftServo;
    }

    public float getForceFrontRightServo() {
        return forceFrontRightServo;
    }

    public float getForceClipServo() {
        return forceClipServo;
    }

    public float getForceTouchServo() { return forceTouchServo;}

    @Override
    public GamepadManager clone() {
        try{
            return (GamepadManager)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public float clamp(float value, float originalFrom, float originalTo, float from, float to) {
        float newRange = Math.abs(from - to);
        float originalRange = Math.abs(originalFrom - originalTo);
        return (value - originalFrom) / originalRange * newRange + from;
    }
}
