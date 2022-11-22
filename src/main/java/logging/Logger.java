package logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Logger {

    private static Logger instance;
    private List<LogItem> log;

    private Logger(){
        this.log = new ArrayList<>();
    }

    public static Logger getInstance(){
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logAction(String callerName, Object result) {
        logAction(callerName, result, Collections.emptyMap());
    }

    public void logAction(String callerName, Object result, Map<String, Object> additionalInfo) {
        LogItem newItem = new LogItem(callerName, result, additionalInfo);
        log.add(newItem);
    }

    public List<LogItem> getLog() {
        return log;
    }

    public void clearLog() {
        log = new ArrayList<>();
    }
}
