package beestbot.state;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import beestbot.io.GamepadManager;
import beestbot.util.Configuration;
import beestbot.vision.SkyStoneVsionManager;


/* these lines are for reference only

    public Method getInitMethod() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> c = Task.class; // for non-static
        Object object = c.newInstance(); // for non-static
        return c.getMethod("move", int.class, int.class);
    }


    public static void move(int i, int j) {
        Configuration.getFileName();
    }
 */
public class Task {
    private double lastTime;

    private double startTime;
    private double endTime;
    private Method initMethod;
    private Method loopMethod;
    private Method endMethod;

    public Task(double lastTime, Method initMethod, Method loopMethod, Method endMethod) throws NoSuchMethodException {
        this.lastTime = lastTime;
        this.initMethod = initMethod;
        this.loopMethod = loopMethod;
        this.endMethod = endMethod;

        if (initMethod == null || loopMethod == null || endMethod == null) {
            Method m = getMethod("doNothing");
            if (this.initMethod == null) {this.initMethod = m;}
            if (this.loopMethod == null) {this.loopMethod = m;}
            if (this.endMethod == null) {this.endMethod = m;}
        }
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    public double getStartTime() {
        return startTime;
    }
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    public double getEndTime() {
        return endTime;
    }
    public double getLastTime() {
        return lastTime;
    }
    public Method getInitMethod() {
        return initMethod;
    }
    public Method getLoopMethod() {
        return loopMethod;
    }
    public Method getEndMethod() {
        return endMethod;
    }

    // getDeclaredMethods includes all methods declared by the class itself, whereas getMethods returns only public methods, but also those inherited from a base class (here from java.lang.Object).
    // getMethod(methodName, inputParameter)
    public static Method getMethod(String name) throws NoSuchMethodException {
        switch (name) {
            case "stop":
                return Task.class.getMethod(name);
            case "doNothing":
                return Task.class.getMethod(name);
            case "goToSkyStone":
                return Task.class.getMethod(name);
            case "grabBlock":
                return Task.class.getMethod(name);
            case "dropBlock":
                return Task.class.getMethod(name);
            case "turnLeft":
                return Task.class.getMethod(name);
            case "turnRight":
                return Task.class.getMethod(name);
            case "moveLeft":
                return Task.class.getMethod(name);
            case "moveRight":
                return Task.class.getMethod(name);
            case "moveFront":
                return Task.class.getMethod(name);
            case "moveBack":
                return Task.class.getMethod(name);
        }
        throw new NoSuchMethodException("The method name '" + name + "' does not exist.");
    }

    /*
    put all non-loop methods here
     */
    public void stop() {
        Configuration.gamepadManager.forceFrontLeftMotor = 0;
        Configuration.gamepadManager.forceFrontRightMotor = 0;
        Configuration.gamepadManager.forceBackLeftMotor = 0;
        Configuration.gamepadManager.forceBackRightMotor = 0;
    }


    /*
    put all loop methods here
     */
    public void doNothing() {
    }
    public void goToSkyStone() {
        VectorF vector = ((SkyStoneVsionManager)Configuration.visionManager).getVuforiaVisionManager().loop();
        if (vector != null) {
            Float x = vector.get(0); // x: if the robot is close to the object, the value will be 0, otherwise bigger than 0
            Float y = vector.get(1); // y: if the robot is on the left of the object, the value will be negative, otherwise positive
            // z: if the robot is higher than the object, the value will be positive, otherwise negative

            float LF_1 = (y - -x - -0) * 1.0f + (y - -x - 0) * 0.4f;
            float RF_1 = (y + -x + -0) * 1.0f + (y + -x + 0) * 0.4f;
            float LB_1 = (y + -x - -0) * 1.0f + (y + -x - 0) * 0.4f;
            float RB_1 = (y - -x + -0) * 1.0f + (y - -x + 0) * 0.4f;
            Float[] decMax_1 = new Float[]{Math.abs(LF_1), Math.abs(RF_1), Math.abs(LB_1), Math.abs(RB_1)};
            List<Float> a_1 = new ArrayList<>(Arrays.asList(decMax_1));
            float max_1 = Range.clip(Collections.max(a_1), 1f, Float.MAX_VALUE);
            LF_1 = (LF_1 / max_1) * Configuration.ABSOLUTE_SPEED * 0.6f;
            RF_1 = (RF_1 / max_1) * Configuration.ABSOLUTE_SPEED * 0.6f;
            LB_1 = (LB_1 / max_1) * Configuration.ABSOLUTE_SPEED * 0.6f;
            RB_1 = (RB_1 / max_1) * Configuration.ABSOLUTE_SPEED * 0.6f;

            Configuration.gamepadManager.forceFrontLeftMotor = Range.clip(LF_1, -1f, 1f);
            Configuration.gamepadManager.forceFrontRightMotor = -Range.clip(RF_1, -1f, 1f);
            Configuration.gamepadManager.forceBackLeftMotor = Range.clip(LB_1, -1f, 1f);
            Configuration.gamepadManager.forceBackRightMotor = -Range.clip(RB_1, -1f, 1f);

            if (x * y < 10) {
                Configuration.gamepadManager.forceFrontLeftMotor = 0;
                Configuration.gamepadManager.forceFrontRightMotor = 0;
                Configuration.gamepadManager.forceBackLeftMotor = 0;
                Configuration.gamepadManager.forceBackRightMotor = 0;

                if (this.getEndMethod() != null) {
                    try {
                        this.endMethod.invoke(this, 0);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    Configuration.currentTask = null;
                    return;
                }

            }
        } else {
            // TODO how to search for block?
        }

    }
    public void grabBlock() {
        Configuration.gamepadManager.forceTouchServo = 1;
    }
    public void dropBlock() {
        Configuration.gamepadManager.forceTouchServo = -1;
    }
    public void turnLeft() {

    }
    public void turnRight() {

    }
    public void moveLeft() {

    }
    public void moveRight() {

    }
    public void moveFront() {
        Configuration.gamepadManager.forceFrontLeftMotor = Range.clip(1 , -1f, 1f);
        Configuration.gamepadManager.forceFrontRightMotor = -Range.clip(1, -1f, 1f);
        Configuration.gamepadManager.forceBackLeftMotor = Range.clip(1, -1f, 1f);
        Configuration.gamepadManager.forceBackRightMotor = -Range.clip(1, -1f, 1f);
    }
    public void moveBack() {
        Configuration.gamepadManager.forceFrontLeftMotor = Range.clip(-1 , -1f, 1f);
        Configuration.gamepadManager.forceFrontRightMotor = -Range.clip(-1, -1f, 1f);
        Configuration.gamepadManager.forceBackLeftMotor = Range.clip(-1, -1f, 1f);
        Configuration.gamepadManager.forceBackRightMotor = -Range.clip(-1, -1f, 1f);

    }

}
