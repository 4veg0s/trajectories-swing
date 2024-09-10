package etu.nic.git.trajectories_swing.exceptions;

/**
 * Исключение, выбрасываемое в случае, если в строке файла траекторной информации неверное количество параметров
 */
public class InvalidAmountOfParametersException extends Exception {
    public InvalidAmountOfParametersException() {
        super();
    }

    public InvalidAmountOfParametersException(String message) {
        super(message);
    }
}
