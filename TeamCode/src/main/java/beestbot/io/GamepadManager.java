package beestbot.io;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

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

    public double forceArmLeftMotor;
    public double forceArmRightMotor;

    public double forceLiftMotor = 0;

//    private int positionArmMotor;
//    private boolean ifArmEncoder = false;

    public static Integer LIFTING_REVOLUTION = 4; //TODO: adjust lifting revolution
    /*
        SERVO
     */
    public float forceFrontLeftServo = 0f;
    public float forceFrontRightServo = 0f;
    public float forceClipServo = 0f;
    public float forceTouchServo = 0f;
    public float forceTurningServo = 0f;
    public float forcePushServo = 0f;

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
            forceLiftMotor = Range.clip(-0 * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
            forceFrontLeftServo = clamp(-0, -1f, 0f, 0.5f, 1.0f); // 0.2-1
            forceFrontRightServo = clamp(0, 0f, 1f, 0f, -0.5f); // 0.2-1

        /*
            For clips
         */
            forceClipServo = clamp(0, 0f, 1.0f, 0f, 0.5f);

        /*
            For Two Compliant Wheels
         */
            forceArmLeftMotor = clamp(Configuration.spinArmLeftMotor, 0f, 1f, 0f, 1.0f);
            forceArmRightMotor = clamp(-Configuration.spinArmRightMotor, 0f, 1f, 0f, 1.0f);

            forcePushServo = clamp(-0, -1f, 0f, 0.75f, 1f);
            forceTouchServo = clamp(0, 0f, 1.0f, 0.25f, 0.75f);
            forceTurningServo = clamp(0, 0f, 1.0f, 0f, 1f);
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

            gamepad 1 and gamepad 2 left control movement
            gamepad 2 right control roatation
         */

            // when first gamepad is not controlling movement, second gamepad will take over
            float LF_1 = (gp2.left_stick_y - -gp2.left_stick_x - -gp2.right_stick_x)*1.0f + (gp1.left_stick_y - -gp1.left_stick_x - 0)*0.4f;
            float RF_1 = (gp2.left_stick_y + -gp2.left_stick_x + -gp2.right_stick_x)*1.0f + (gp1.left_stick_y + -gp1.left_stick_x + 0)*0.4f;
            float LB_1 = (gp2.left_stick_y + -gp2.left_stick_x - -gp2.right_stick_x)*1.0f + (gp1.left_stick_y + -gp1.left_stick_x - 0)*0.4f;
            float RB_1 = (gp2.left_stick_y - -gp2.left_stick_x + -gp2.right_stick_x)*1.0f + (gp1.left_stick_y - -gp1.left_stick_x + 0)*0.4f;
            Float[] decMax_1 = new Float[]{Math.abs(LF_1), Math.abs(RF_1), Math.abs(LB_1), Math.abs(RB_1)};
            List<Float> a_1 = new ArrayList<>(Arrays.asList(decMax_1));
            float max_1 = Range.clip(Collections.max(a_1), 1f, Float.MAX_VALUE);
            LF_1 = (LF_1 / max_1) * Configuration.ABSOLUTE_SPEED;
            RF_1 = (RF_1 / max_1) * Configuration.ABSOLUTE_SPEED;
            LB_1 = (LB_1 / max_1) * Configuration.ABSOLUTE_SPEED;
            RB_1 = (RB_1 / max_1) * Configuration.ABSOLUTE_SPEED;

            forceFrontLeftMotor = Range.clip(LF_1 , -1f, 1f);
            forceFrontRightMotor = -Range.clip(RF_1, -1f, 1f);
            forceBackLeftMotor = Range.clip(LB_1, -1f, 1f);
            forceBackRightMotor = -Range.clip(RB_1, -1f, 1f);

        /*
            For lifting
         */
            float liftingForce = 0f;
            if (gp1.dpad_up) {
                liftingForce = 1f;
            } else if (gp1.dpad_down) {
                liftingForce = -1f;
            }
            forceLiftMotor = Range.clip(liftingForce * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
            forceFrontLeftServo = clamp(-gp2.right_trigger, -1f, 0f, 0.5f, 1.0f); // 0.2-1
            forceFrontRightServo = clamp(gp2.right_trigger, 0f, 1f, 0f, -0.5f); // 0.2-1

        /*
            For clips
         */
            forceClipServo = clamp(gp2.left_trigger, 0f, 1.0f, 0f, 0.5f);

        /*
            For Turning and Grabing Skyblock
         */
            if (gp1.dpad_up && !Configuration.upPressedGamepad1) {
                Configuration.upPressedGamepad1 = true;
                Configuration.downPressedGamepad1 = false;
            } else if (!gp1.dpad_up && Configuration.upPressedGamepad1) {
                Configuration.upPressedGamepad1 = false;
            }
            if (gp1.dpad_down && !Configuration.downPressedGamepad1) {
                Configuration.downPressedGamepad1 = true;
                Configuration.upPressedGamepad1 = false;
            } else if (!gp1.dpad_down && Configuration.downPressedGamepad1) {
                Configuration.downPressedGamepad1 = false;
            }

            if (gp1.dpad_left && !Configuration.leftPressedGamepad1) {
                Configuration.leftPressedGamepad1 = true;
                Configuration.rightPressedGamepad1 = false;
            } else if (!gp1.dpad_left && Configuration.leftPressedGamepad1) {
                Configuration.leftPressedGamepad1 = false;
            }
            if (gp1.dpad_right && !Configuration.rightPressedGamepad1) {
                Configuration.rightPressedGamepad1 = true;
                Configuration.leftPressedGamepad1 = false;
            } else if (!gp1.dpad_right && Configuration.rightPressedGamepad1) {
                Configuration.rightPressedGamepad1 = false;
            }

        /*
            For Two Compliant Wheels
         */
            if (gp1.y && !Configuration.yPressedGamepad1) {
                Configuration.yPressedGamepad1 = true;
                // fire
                if (Configuration.spinArmLeftMotor == -1) {
                    Configuration.spinArmLeftMotor = 0; // stop
                } else {
                    Configuration.spinArmLeftMotor = -1; // spinning out
                }
                if (Configuration.spinArmRightMotor == -1) {
                    Configuration.spinArmRightMotor = 0; // stop
                } else {
                    Configuration.spinArmRightMotor = -1; // spinning out
                }
            } else if (!gp1.y && Configuration.yPressedGamepad1) {
                Configuration.yPressedGamepad1 = false;
            }
            if (gp1.x && !Configuration.xPressedGamepad1) {
                Configuration.xPressedGamepad1 = true;
                // fire
                if (Configuration.spinArmLeftMotor == 1) {
                    Configuration.spinArmLeftMotor = 0; // stop
                } else {
                    Configuration.spinArmLeftMotor = 1; // spinning in
                }
                if (Configuration.spinArmRightMotor == 1) {
                    Configuration.spinArmRightMotor = 0; // stop
                } else {
                    Configuration.spinArmRightMotor = 1; // spinning in
                }
            } else if (!gp1.x && Configuration.xPressedGamepad1) {
                Configuration.xPressedGamepad1 = false;
            }

            forceArmLeftMotor = clamp(Configuration.spinArmLeftMotor, 0f, 1f, 0f, 1.0f);
            forceArmRightMotor = clamp(-Configuration.spinArmRightMotor, 0f, 1f, 0f, 1.0f);

            forcePushServo = clamp(-gp1.left_trigger, -1f, 0f, 0.75f, 1f);
            forceTouchServo = clamp(gp1.right_trigger, 0f, 1.0f, 0.20f, 0.65f);
            if (Configuration.leftPressedGamepad1) {
                forceTurningServo = clamp(0, 0f, 1.0f, 0f, 1f);
            } else if (Configuration.rightPressedGamepad1) {
                forceTurningServo = clamp(1, 0f, 1.0f, 0f, 1f);
            }
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
        LF_1 = (LF_1 / max_1) * Configuration.ABSOLUTE_SPEED;
        RF_1 = (RF_1 / max_1) * Configuration.ABSOLUTE_SPEED;
        LB_1 = (LB_1 / max_1) * Configuration.ABSOLUTE_SPEED;
        RB_1 = (RB_1 / max_1) * Configuration.ABSOLUTE_SPEED;

        forceFrontLeftMotor = Range.clip(LF_1 , -1f, 1f);
        forceFrontRightMotor = -Range.clip(RF_1, -1f, 1f);
        forceBackLeftMotor = Range.clip(LB_1, -1f, 1f);
        forceBackRightMotor = -Range.clip(RB_1, -1f, 1f);

        /*
            For lifting
         */
        float liftingForce = 0f;
        if (gp1.dpad_up) {
            liftingForce = 1f;
        } else if (gp1.dpad_down) {
            liftingForce = -1f;
        }
        forceLiftMotor = Range.clip(liftingForce * 0.2f, -1f, 1f);

        /*
            For Two Side servos
         */
        forceFrontLeftServo = clamp(-gp2.right_trigger, -1f, 0f, 0.5f, 1.0f); // 0.2-1
        forceFrontRightServo = clamp(gp2.right_trigger, 0f, 1f, 0f, -0.5f); // 0.2-1
//        forceFrontRightServo = clamp(gp2.right_trigger, 0f, 1f, 0.5f, 1.0f); // 1~0.2

        /*
            For clips
         */
        forceClipServo = clamp(gp2.left_trigger, 0f, 1.0f, 0f, 0.5f);

        /*
            For Turning and Grabing Skyblock
         */
        if (gp1.dpad_up && !Configuration.upPressedGamepad1) {
            Configuration.upPressedGamepad1 = true;
            Configuration.downPressedGamepad1 = false;
        } else if (!gp1.dpad_up && Configuration.upPressedGamepad1) {
            Configuration.upPressedGamepad1 = false;
        }
        if (gp1.dpad_down && !Configuration.downPressedGamepad1) {
            Configuration.downPressedGamepad1 = true;
            Configuration.upPressedGamepad1 = false;
        } else if (!gp1.dpad_down && Configuration.downPressedGamepad1) {
            Configuration.downPressedGamepad1 = false;
        }

        if (gp1.dpad_left && !Configuration.leftPressedGamepad1) {
            Configuration.leftPressedGamepad1 = true;
            Configuration.rightPressedGamepad1 = false;
        } else if (!gp1.dpad_left && Configuration.leftPressedGamepad1) {
            Configuration.leftPressedGamepad1 = false;
        }
        if (gp1.dpad_right && !Configuration.rightPressedGamepad1) {
            Configuration.rightPressedGamepad1 = true;
            Configuration.leftPressedGamepad1 = false;
        } else if (!gp1.dpad_right && Configuration.rightPressedGamepad1) {
            Configuration.rightPressedGamepad1 = false;
        }

        /*
            For Two Compliant Wheels
         */
        if (gp2.y && !Configuration.yPressedGamepad1) {
            Configuration.yPressedGamepad1 = true;
            // fire
            if (Configuration.spinArmLeftMotor == -1) {
                Configuration.spinArmLeftMotor = 0; // stop
            } else {
                Configuration.spinArmLeftMotor = -1; // spinning out
            }
            if (Configuration.spinArmRightMotor == -1) {
                Configuration.spinArmRightMotor = 0; // stop
            } else {
                Configuration.spinArmRightMotor = -1; // spinning out
            }
        } else if (!gp2.y && Configuration.yPressedGamepad1) {
            Configuration.yPressedGamepad1 = false;
        }
        if (gp2.x && !Configuration.xPressedGamepad1) {
            Configuration.xPressedGamepad1 = true;
            // fire
            if (Configuration.spinArmLeftMotor == 1) {
                Configuration.spinArmLeftMotor = 0; // stop
            } else {
                Configuration.spinArmLeftMotor = 1; // spinning in
            }
            if (Configuration.spinArmRightMotor == 1) {
                Configuration.spinArmRightMotor = 0; // stop
            } else {
                Configuration.spinArmRightMotor = 1; // spinning in
            }
        } else if (!gp2.x && Configuration.xPressedGamepad1) {
            Configuration.xPressedGamepad1 = false;
        }

        forceArmLeftMotor = clamp(Configuration.spinArmLeftMotor, 0f, 1f, 0f, 1.0f);
        forceArmRightMotor = clamp(-Configuration.spinArmRightMotor, 0f, 1f, 0f, 1.0f);

        forcePushServo = clamp(-gp1.left_trigger, -1f, 0f, 0.75f, 1f);
        forceTouchServo = clamp(gp1.right_trigger, 0f, 1.0f, 0.20f, 0.65f);
        if (Configuration.leftPressedGamepad1) {
            forceTurningServo = clamp(0, 0f, 1.0f, 0f, 1f);
        } else if (Configuration.rightPressedGamepad1) {
            forceTurningServo = clamp(1, 0f, 1.0f, 0f, 1f);
        }
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

    public float getForceTurningServo() {
        return forceTurningServo;
    }

    public float getForcePushServo() {
        return forcePushServo;
    }

    @Override
    public GamepadManager clone() {
        try{
            return (GamepadManager)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private float clamp(float value, float originalFrom, float originalTo, float from, float to) {
        float newRange = Math.abs(from - to);
        float originalRange = Math.abs(originalFrom - originalTo);
        return (value - originalFrom) / originalRange * newRange + from;
    }
}

