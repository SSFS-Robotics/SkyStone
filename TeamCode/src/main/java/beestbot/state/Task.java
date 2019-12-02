package beestbot.state;

import java.lang.reflect.Method;

import beestbot.io.GamepadManager;
import beestbot.util.Configuration;

public class Task {
    private double lastTime;

    private double startTime;
    private double endTime;
    private Method initMethod;
    private Method loopMethod;
    private Method endMethod;

    public Task(double lastTime, Method initMethod, Method loopMethod, Method endMethod) {
        this.lastTime = lastTime;
        this.initMethod = initMethod;
        this.loopMethod = loopMethod;
        this.endMethod = endMethod;
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
//    public Method getInitMethod() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
//        Class<?> c = Task.class; // for non-static
//        Object object = c.newInstance(); // for non-static
//        return c.getMethod("move", int.class, int.class);
//    }
//
//
//    public static void move(int i, int j) {
//        Configuration.getFileName(); // TODO
//    }
    public Method getStaticMethod(String name) throws NoSuchMethodException {
        if (name.equals("")) {
            return Task.class.getMethod(name, int.class);
        } else if (name.equals("...")) {
            return Task.class.getMethod(name, GamepadManager.class);
        }
        throw new NoSuchMethodException("The name of the method does not exist.");
    }

    /*
    init methods
     */
    public static void stop() {
        Configuration.gamepadManager.forceArmLeftMotor = 0;
    }
    public static void moveFront() {
        stop();
        Configuration.gamepadManager.forceArmLeftMotor = 1;
    }


    /*
    loop methods
     */
    public static void doNothing() {
        return;
    }



    /*
    end methods
     */
    public static void decide() {
        // decide what to do next after scan finish
    }

}
