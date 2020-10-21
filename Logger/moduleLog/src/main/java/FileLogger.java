import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLogger {

    final Logger logger;

    public FileLogger() {
        logger = LoggerFactory.getLogger(FileLogger.class);
    }

    public void log(String message) {
        logger.info(message);
    }

}
