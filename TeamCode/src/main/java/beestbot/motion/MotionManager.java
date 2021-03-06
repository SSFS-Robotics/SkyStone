package beestbot.motion;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import beestbot.io.GamepadManager;
import beestbot.util.Configuration;

public class MotionManager {

    /*
        MOTOR
     */
    private DcMotor leftFrontWheel = null;
    private DcMotor rightFrontWheel = null;
    private DcMotor leftBackWheel = null;
    private DcMotor rightBackWheel = null;
    private DcMotor liftMotor = null;
    private DcMotor armLeftMotor = null;
    private DcMotor armRightMotor = null;

    /*
        SERVO
     */
    private Servo leftFrontServo = null;
    private Servo rightFrontServo = null;
    private Servo clipServo = null;
    private Servo touchServo = null;
    private Servo turningServo = null;
    private Servo pushServo = null;

    /*
        SENSOR
     */
    private DigitalChannel touch = null;
    private DistanceSensor leftDistance = null;
    private DistanceSensor rightDistance = null;
    private ColorSensor color = null;

    /*
        OTHER
     */
    private final float hsvValues[] = {0F, 0F, 0F};
    private final float values[] = hsvValues;
    private View relativeLayout;

    private Telemetry telemetry;

    public MotionManager(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;

        this.leftFrontWheel = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.FRONT_LEFT_MOTOR);
        this.rightFrontWheel = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.FRONT_RIGHT_MOTOR);
        this.leftBackWheel = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.BACK_LEFT_MOTOR);
        this.rightBackWheel = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.BACK_RIGHT_MOTOR);
        this.liftMotor = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.LIFT_MOTOR);
        this.armLeftMotor = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.ARM_LEFT_MOTOR);
        this.armRightMotor = MotionExceptions.getWithException(hardwareMap, DcMotor.class, Configuration.ARM_RIGHT_MOTOR);

        this.leftFrontServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.FRONT_LEFT_SERVO);
        this.rightFrontServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.FRONT_RIGHT_SERVO);
        this.clipServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.CLIP_SERVO);
        this.touchServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.TOUCH_SERVO);
        this.turningServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.TURNING_SERVO);
        this.pushServo = MotionExceptions.getWithException(hardwareMap, Servo.class, Configuration.PUSH_SERVO);

        this.touch = MotionExceptions.getWithException(hardwareMap, DigitalChannel.class, Configuration.TOUCH_SENSOR);

        this.leftDistance = MotionExceptions.getWithException(hardwareMap, DistanceSensor.class, Configuration.LEFT_DISTANCE_SENSOR);
        this.rightDistance = MotionExceptions.getWithException(hardwareMap, DistanceSensor.class, Configuration.RIGHT_DISTANCE_SENSOR);

        this.color = MotionExceptions.getWithException(hardwareMap, ColorSensor.class, Configuration.COLOR_SENSOR);


//        this.leftFrontWheel = hardwareMap.get(DcMotor.class, Configuration.FRONT_LEFT_MOTOR);
//        this.rightFrontWheel = hardwareMap.get(DcMotor.class, Configuration.FRONT_RIGHT_MOTOR);
//        this.leftBackWheel = hardwareMap.get(DcMotor.class, Configuration.BACK_LEFT_MOTOR);
//        this.rightBackWheel = hardwareMap.get(DcMotor.class, Configuration.BACK_RIGHT_MOTOR);
//        this.liftMotor = hardwareMap.get(DcMotor.class, Configuration.LIFT_MOTOR);
//        this.armLeftMotor = hardwareMap.get(DcMotor.class, Configuration.ARM_LEFT_MOTOR);
//        this.armRightMotor = hardwareMap.get(DcMotor.class, Configuration.ARM_RIGHT_MOTOR);
//        //servo
//        this.leftFrontServo = hardwareMap.get(Servo.class, Configuration.FRONT_LEFT_SERVO);
//        this.rightFrontServo = hardwareMap.get(Servo.class, Configuration.FRONT_RIGHT_SERVO);
//        //TODO: rightServo.setDirection(Servo.Direction.REVERSE);
//
//        //touch
//        this.touch = hardwareMap.get(DigitalChannel.class, Configuration.TOUCH_SENSOR);
//        //distance
//        this.leftDistance = hardwareMap.get(DistanceSensor.class, Configuration.LEFT_DISTANCE_SENSOR);
//        this.rightDistance = hardwareMap.get(DistanceSensor.class, Configuration.RIGHT_DISTANCE_SENSOR);
//        //color
//        this.color = hardwareMap.get(ColorSensor.class, Configuration.COLOR_SENSOR);

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        this.relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
    }

    public void updateWithException(GamepadManager gamepadManager) {
        if (Configuration.ENCODER) {
//            MotionExceptions.setModeWithException(leftFrontWheel, DcMotor.RunMode.RUN_USING_ENCODER);
//            MotionExceptions.setModeWithException(rightFrontWheel, DcMotor.RunMode.RUN_USING_ENCODER);
//            MotionExceptions.setModeWithException(leftBackWheel, DcMotor.RunMode.RUN_USING_ENCODER);
//            MotionExceptions.setModeWithException(rightBackWheel, DcMotor.RunMode.RUN_USING_ENCODER);
            MotionExceptions.setModeWithException(leftFrontWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(rightFrontWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(leftBackWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(rightBackWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            MotionExceptions.setModeWithException(leftFrontWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(rightFrontWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(leftBackWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(rightBackWheel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        MotionExceptions.setPowerWithException(leftFrontWheel, gamepadManager.getForceFrontLeftMotor());
//        leftFrontWheel.setPower(gamepadManager.getForceFrontLeftMotor());
        MotionExceptions.setPowerWithException(rightFrontWheel, gamepadManager.getForceFrontRightMotor());
//        rightFrontWheel.setPower(gamepadManager.getForceFrontRightMotor());
        MotionExceptions.setPowerWithException(leftBackWheel, gamepadManager.getForceBackLeftMotor());
//        leftBackWheel.setPower(gamepadManager.getForceBackLeftMotor());
        MotionExceptions.setPowerWithException(rightBackWheel, gamepadManager.getForceBackRightMotor());
//        rightBackWheel.setPower(gamepadManager.getForceBackRightMotor());

        telemetry.addData("M", String.format("leftFrontWheel(%s): %.2f %b, Encoder: %3d", Configuration.FRONT_LEFT_MOTOR, gamepadManager.getForceFrontLeftMotor(), leftFrontWheel!=null, MotionExceptions.getCurrentPositionWithException(leftFrontWheel)));
        telemetry.addData("M", String.format("rightFrontWheel(%s): %.2f %b, Encoder: %3d", Configuration.FRONT_RIGHT_MOTOR, gamepadManager.getForceFrontRightMotor(), rightFrontWheel!=null, MotionExceptions.getCurrentPositionWithException(rightFrontWheel)));
        telemetry.addData("M", String.format("leftBackWheel(%s): %.2f %b, Encoder: %3d", Configuration.BACK_LEFT_MOTOR, gamepadManager.getForceBackLeftMotor(), leftBackWheel!=null, MotionExceptions.getCurrentPositionWithException(leftBackWheel)));
        telemetry.addData("M", String.format("rightBackWheel(%s): %.2f %b, Encoder: %3d", Configuration.BACK_RIGHT_MOTOR, gamepadManager.getForceBackRightMotor(), rightBackWheel!=null, MotionExceptions.getCurrentPositionWithException(rightBackWheel)));

        if (Configuration.ENCODER) {
            MotionExceptions.setModeWithException(liftMotor, DcMotor.RunMode.RUN_USING_ENCODER);
//            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            MotionExceptions.setModeWithException(liftMotor, DcMotor.RunMode.RUN_TO_POSITION);
//            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MotionExceptions.setModeWithException(armLeftMotor, DcMotor.RunMode.RUN_USING_ENCODER);
//            armLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            MotionExceptions.setModeWithException(armRightMotor, DcMotor.RunMode.RUN_USING_ENCODER);
//            armRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            MotionExceptions.setModeWithException(liftMotor, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(armLeftMotor, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            armLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            MotionExceptions.setModeWithException(armRightMotor, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            armRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        MotionExceptions.setPowerWithException(liftMotor, gamepadManager.getForceLiftMotor());
//        liftMotor.setPower(0.75); //TODO: check power
        MotionExceptions.setTargetPositionWithException(liftMotor, Configuration.ANDYMARK_TICKS_PER_REV * GamepadManager.LIFTING_REVOLUTION * (int) gamepadManager.getForceLiftMotor());
//        liftMotor.setTargetPosition(Configuration.ANDYMARK_TICKS_PER_REV * GamepadManager.LIFTING_REVOLUTION * (int) gamepadManager.getForceLiftMotor()); //TODO: make sure the motor is from andymark
        telemetry.addData("M", String.format("liftMotor(%s): %.2f %b, Encoder: %3d", Configuration.LIFT_MOTOR, gamepadManager.getForceLiftMotor(), liftMotor!=null, MotionExceptions.getCurrentPositionWithException(liftMotor)));

        MotionExceptions.setPowerWithException(armLeftMotor, gamepadManager.getForceArmLeftMotor());
//        armLeftMotor.setPower(gamepadManager.getForceArmLeftMotor());
        MotionExceptions.setPowerWithException(armRightMotor, gamepadManager.getForceArmRightMotor());
//        armRightMotor.setPower(gamepadManager.getForceArmRightMotor());
        telemetry.addData("M", String.format("armLeftMotor(%s): %.2f %b, Encoder: %3d", Configuration.ARM_LEFT_MOTOR, gamepadManager.getForceArmLeftMotor(), armLeftMotor!=null, MotionExceptions.getCurrentPositionWithException(armLeftMotor)));
        telemetry.addData("M", String.format("armRightMotor(%s): %.2f %b, Encoder: %3d", Configuration.ARM_RIGHT_MOTOR, gamepadManager.getForceArmRightMotor(), armRightMotor!=null, MotionExceptions.getCurrentPositionWithException(armRightMotor)));

        //TODO: 0=0degree, 1=180degree, 0.5=90degree, 0.5=stop
        MotionExceptions.setPositionWithException(leftFrontServo, gamepadManager.getForceFrontLeftServo());
        MotionExceptions.setPositionWithException(rightFrontServo, gamepadManager.getForceFrontRightServo());
        MotionExceptions.setPositionWithException(clipServo, gamepadManager.getForceClipServo());
        MotionExceptions.setPositionWithException(touchServo, gamepadManager.getForceTouchServo());
        MotionExceptions.setPositionWithException(turningServo, gamepadManager.getForceTurningServo());
        MotionExceptions.setPositionWithException(pushServo, gamepadManager.getForcePushServo());
        telemetry.addData("SV", String.format("leftFrontServo(%s): %.4s %b", Configuration.FRONT_LEFT_SERVO, gamepadManager.getForceFrontLeftServo(), leftFrontServo!=null));
        telemetry.addData("SV", String.format("rightFrontServo(%s): %.4s %b", Configuration.FRONT_RIGHT_SERVO, gamepadManager.getForceFrontRightServo(), rightFrontServo!=null));
        telemetry.addData("SV", String.format("clipServo(%s): %.4s %b", Configuration.CLIP_SERVO, gamepadManager.getForceClipServo(), clipServo!=null));
        telemetry.addData("SV", String.format("touchServo(%s): %.4s %b", Configuration.TOUCH_SERVO, gamepadManager.getForceTouchServo(), touchServo!=null));
        telemetry.addData("SV", String.format("turningServo(%s): %.4s %b", Configuration.TURNING_SERVO, gamepadManager.getForceTurningServo(), turningServo!=null));
        telemetry.addData("SV", String.format("pushServo(%s): %.4s %b", Configuration.PUSH_SERVO, gamepadManager.getForcePushServo(), pushServo!=null));

        //touch
        MotionExceptions.setModeWithException(touch, DigitalChannel.Mode.INPUT);
        telemetry.addData("SS", String.format("touch: %b %b", !MotionExceptions.getStateWithException(touch), touch!=null));


        //distance
//        telemetry.addData("Distance", String.format("%.01f mm", leftDistance.getDistance(DistanceUnit.MM)));

        telemetry.addData("SS", String.format("leftDistance: %.01f cm %b", MotionExceptions.getDistanceWithException(leftDistance, DistanceUnit.CM), leftDistance!=null));
//        telemetry.addData("Distance", String.format("%.01f cm", leftDistance.getDistance(DistanceUnit.CM)));

//        telemetry.addData("Distance", String.format("%.01f m", leftDistance.getDistance(DistanceUnit.METER)));
//        telemetry.addData("Distance", String.format("%.01f in", leftDistance.getDistance(DistanceUnit.INCH)));
//        telemetry.addData("Distance", String.format("%.01f mm", rightDistance.getDistance(DistanceUnit.MM)));

        telemetry.addData("SS", String.format("rightDistance: %.01f cm %b", MotionExceptions.getDistanceWithException(rightDistance, DistanceUnit.CM), rightDistance!=null));
//        telemetry.addData("Distance", String.format("%.01f cm", rightDistance.getDistance(DistanceUnit.CM)));

//        telemetry.addData("Distance", String.format("%.01f m", rightDistance.getDistance(DistanceUnit.METER)));
//        telemetry.addData("Distance", String.format("%.01f in", rightDistance.getDistance(DistanceUnit.INCH)));

        // TODO: read sensor, encoder
        //color
        Color.RGBToHSV((int) (MotionExceptions.redWithException(color) * 255),
                (int) (MotionExceptions.greenWithException(color) * 255),
                (int) (MotionExceptions.blueWithException(color) * 255),
                hsvValues);
//        Color.RGBToHSV((int) (color.red() * 255),
//                (int) (color.green() * 255),
//                (int) (color.blue() * 255),
//                hsvValues);

        telemetry.addData("Alpha", MotionExceptions.alphaWithException(color));
        telemetry.addData("Red  ", MotionExceptions.redWithException(color));
        telemetry.addData("Green", MotionExceptions.greenWithException(color));
        telemetry.addData("Blue ", MotionExceptions.blueWithException(color));
//        telemetry.addData("Alpha", color.alpha());
//        telemetry.addData("Red  ", color.red());
//        telemetry.addData("Green", color.green());
//        telemetry.addData("Blue ", color.blue());
        telemetry.addData("Hue", hsvValues[0]);
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
    }
}
