package opu.android.best.practice.utils;

/**
 * Created by Md.Fazla Rabbi OPu on 8/6/2018.
 */

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Md.Fazla Rabbi OPu on 7/30/2018.
 */

public class EfficientTimer extends TimerTask {

    private static volatile EfficientTimer instance;
    private CopyOnWriteArrayList<TimeChangeListener> observers;
    private Map<Integer, Long> listenerDurationMap;
    public static long DEFAULT_TIME = 1000L;
    private Timer timer;
    private long lastTickTime;

    private EfficientTimer(TimeChangeListener listener) {

        observers = new CopyOnWriteArrayList<>();
        listenerDurationMap = Collections.synchronizedMap(new LinkedHashMap<Integer, Long>());
        addObserver(listener);
        start();

    }

    private void start() {
        timer = new Timer(EfficientTimer.class.getName(), true);
        timer.scheduleAtFixedRate(this, DEFAULT_TIME, 1000);
    }

    private void addObserver(TimeChangeListener listener) {
        if (listener != null && !listenerDurationMap.containsKey(listener.hashCode())) {
            observers.add(listener);
            listenerDurationMap.put(listener.hashCode(), getIntervalInMilli(listener));
        }
    }

    public static void removeObserver(TimeChangeListener listener) {
        if (instance != null && listener != null) {
            if (instance.listenerDurationMap.containsKey(listener.hashCode())) {
                instance.observers.remove(listener);
                instance.listenerDurationMap.remove(listener.hashCode());

            }
            if (instance.observers.size() == 0) {
                instance.dispose();
            }
        }
    }

    private void dispose() {
        observers.clear();
        listenerDurationMap.clear();
        timer.cancel();
        timer = null;
        instance = null;

    }

    private long getIntervalInMilli(TimeChangeListener listener) {
        long interval = listener.getInterval() == 0L ? DEFAULT_TIME : listener.getInterval();
        long curTime = System.currentTimeMillis();
        long diff = 0;
        if (lastTickTime > 0) {
            diff = curTime - lastTickTime;
        }
        long time = curTime + interval + diff;
        return time;
    }

    public static void start(TimeChangeListener listener) {
        if (instance == null) {
            synchronized (EfficientTimer.class) {
                if (instance == null)
                    instance = new EfficientTimer(listener);
            }
        } else {
            instance.addObserver(listener);
        }
        //return instance;
    }

    public static boolean isStarted(TimeChangeListener listener) {
        if (instance != null && instance.observers.contains(listener)) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        long timePassed = System.currentTimeMillis();
        lastTickTime = timePassed;
        for (int i = 0; i < observers.size(); i++) {
            final TimeChangeListener listener = observers.get(i);
            final int listenerHashCode = listener.hashCode();
            if (listenerDurationMap.containsKey(listenerHashCode)) {
                long diff = listenerDurationMap.get(listenerHashCode) - timePassed;
                if (diff <= 0) {
                    listener.onTick();
                    listenerDurationMap.put(listenerHashCode, timePassed + listener.getInterval());
                }
            }

        }
    }
}