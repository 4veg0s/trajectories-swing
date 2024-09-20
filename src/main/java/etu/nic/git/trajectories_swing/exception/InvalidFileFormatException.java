package etu.nic.git.trajectories_swing.exception;

/**
 * Исключение, выбрасываемое в случае, файл траекторной информации имеет неверный формат
 */
public class InvalidFileFormatException extends Exception{
    public InvalidFileFormatException() {
        super();
    }

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
