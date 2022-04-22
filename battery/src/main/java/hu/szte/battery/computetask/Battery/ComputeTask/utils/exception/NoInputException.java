package hu.szte.battery.computetask.Battery.ComputeTask.utils.exception;

import static hu.szte.battery.computetask.Battery.ComputeTask.utils.Constants.DEFAULT_NO_INPUT_ERROR;

public class NoInputException extends RuntimeException {

  public NoInputException() {
    super(DEFAULT_NO_INPUT_ERROR);
  }

  public NoInputException(String reason) {
    super(reason);
  }
}
