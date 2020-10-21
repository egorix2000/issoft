import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleLogger {
    final Logger logger;

    public ConsoleLogger() {
        logger = LoggerFactory.getLogger(ConsoleLogger.class);
    }

    public void log(String message) {
        logger.info(message);
    }

}
