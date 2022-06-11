import java.io.File;
import java.io.IOException;

public class StateFileNotFoundException extends IOException {
    public StateFileNotFoundException(String message) {
        super(message);
    }
    
    public StateFileNotFoundException(File file) {
        super("File '" + file.getAbsolutePath() + "' could not be found.");
    }
}
