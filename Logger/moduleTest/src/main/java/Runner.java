import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {
    public static void main(String[] args) {
        ConsoleLogger consoleLogger = new ConsoleLogger();
        consoleLogger.log("log1");
        FileLogger fileLogger = new FileLogger();
        fileLogger.log("log2");
    }

}
