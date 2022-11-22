package logging;

import java.util.Map;
import java.util.stream.Collectors;

public class LogItem {
    private final String callerName;
    private final String result;
    private final Map<String, String> additionalInfo;

    public LogItem(String callerName, Object result, Map<String,Object> additionalInfo) {
        this.callerName = callerName;
        this.result = result.toString();
        this.additionalInfo = additionalInfo
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> String.valueOf((entry.getValue()))
                        )
                );
    }

    public String getResult(){
        return result;
    }

    @Override
    public String toString() {
        return "logger[" +
                "callerName:" + callerName +
                ", result:" + result +
                ", additionalInfo:" + additionalInfo +
                "]";
    }
}
