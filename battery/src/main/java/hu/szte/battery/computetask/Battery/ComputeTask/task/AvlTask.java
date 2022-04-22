package hu.szte.battery.computetask.Battery.ComputeTask.task;

import hu.szte.battery.computetask.Battery.ComputeTask.utils.exception.NoInputException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AvlTask implements Task {

  private final Integer input;

  @Override
  public Long execute() {
    return numberOfAvlTrees(input);
  }

  private Long numberOfAvlTrees(Integer N) {
    validateInput(N);
    Long[][] array = new Long[N + 1][N + 1];
    array[0][0] = 1L;
    array[1][1] = 1L;
    for (int n = 0; n <= N; n++) {
      for (int h = 0; h <= N; h++) {
        if ((h > n) || (h == 0 && h != n) || (h == 1 && h != n)) {
          array[n][h] = 0L;
        }
        if (n >= h && h > 1) {
          Long sum = 0L;
          for (int l = 1; l <= n; l++) {

            sum +=
                array[l - 1][h - 1] * array[n - l][h - 1]
                    + array[l - 1][h - 1] * array[n - l][h - 2]
                    + array[l - 1][h - 2] * array[n - l][h - 1];
          }
          array[n][h] = sum;
        }
      }
    }
    Long sum = 0L;

    for (int i = 0; i <= N; i++) {
      sum += array[N][i];
    }
    return sum;
  }

  private void validateInput(Integer n) {
    Integer integer = Optional.ofNullable(input).orElseThrow(NoInputException::new);
    if (integer < 0) {
      throw new IllegalArgumentException("Input less than 0");
    }
  }
}
