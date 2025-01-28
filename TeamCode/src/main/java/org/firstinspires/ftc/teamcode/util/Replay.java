package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;

public class Replay extends SDKSubsystem {

    public static final Replay INSTANCE = new Replay();
    private Replay() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}
    //Dependencies for Mercurial
    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Replay.Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private HashMap<String, Double> loopStorage = new HashMap<>();
    private HashMap<String, Double> lastLoopStorage = new HashMap<>();
    public void pushValues(String key, Double value){
        loopStorage.put(key, value);
    }
    private OpMode opmode;

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        this.opmode = opmode;
    }

    private double previousRuntime;

    public void update() {
        double runtime = opmode.getRuntime();
        pushValues("deltaTime", runtime-previousRuntime);
        writeData();
        lastLoopStorage = loopStorage;
        loopStorage = new HashMap<>();
        previousRuntime = runtime;
    }

    private void writeData() {
        try {
            writeToFile(loopStorage, "data.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> getLoopStorage() {
        return loopStorage;
    }

    public HashMap<String, Double> getLastLoopStorage() {
        return lastLoopStorage;
    }

    public Double getPreviousLoopValue(String key) {
        return lastLoopStorage.get(key);
    }

    public Set<String> getKeys() {
        return loopStorage.keySet();
    }

    public  void writeToFile (HashMap<String, Double> hashmap, String toFileName) throws JSONException {
        Object json = toJSON(hashmap);
        // Using the properties of the specified "to" file name,
        // declare a filename to be used in this method.  See Note 1 above.
        File myFileName = AppUtil.getInstance().getSettingsFile(toFileName);

        // Write the provided number to the newly declared filename.
        // See Note 3 above.
        ReadWriteFile.writeFile(myFileName, String.valueOf(json));

        opmode.telemetry.addData("Filename", toFileName);

    }

    public Object toJSON(Object object) throws JSONException {
        if (object instanceof HashMap) {
            JSONObject json = new JSONObject();
            HashMap map = (HashMap) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable) object)) {
                json.put(toJSON(value));
            }
            return json;
        }
        else {
            return object;
        }
    }

}
