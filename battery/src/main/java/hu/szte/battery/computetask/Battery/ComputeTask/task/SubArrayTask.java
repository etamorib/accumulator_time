package hu.szte.battery.computetask.Battery.ComputeTask.task;

import hu.szte.battery.computetask.Battery.ComputeTask.utils.exception.NoInputException;
import lombok.RequiredArgsConstructor;
import org.apache.uima.internal.util.rb_trees.RedBlackTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.szte.battery.computetask.Battery.ComputeTask.utils.Constants.*;

@RequiredArgsConstructor
public class SubArrayTask implements Task {

  private final String inputFile;

  @Override
  public Long execute() {
    validateInput(inputFile);

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(inputFile)).getFile());
    Path path = file.toPath();

    try (Stream<String> stream = Files.lines(path)) {
      List<String> inputValues = stream.collect(Collectors.toList());
      checkInputSize(inputValues, 3);
      String ignore = inputValues.get(0);
      List<Long> nAndM =
          Arrays.stream(inputValues.get(1).split(" "))
              .mapToLong(Long::parseLong)
              .boxed()
              .collect(Collectors.toList());

      List<Long> array =
          Arrays.stream(inputValues.get(2).split(" "))
              .mapToLong(Long::parseLong)
              .boxed()
              .collect(Collectors.toList());
      return maxSumMod(nAndM, array);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Long maxSumMod(List<Long> nAndM, List<Long> array) {
    checkInputSize(nAndM, 2);
    int size = nAndM.get(0).intValue();
    Long m = nAndM.get(1);
    Long[] sum = new Long[size];
    sum[0] = array.get(0) % m;
    RedBlackTree<Long> RB = new RedBlackTree<>();
    RB.put(sum[0].intValue(), sum[0]);
    Long result = sum[0];
    for (int i = 1; i < size; i++) {
      boolean flag = true;
      sum[i] = sum[i - 1] + array.get(i - 1);
      sum[i] %= m;
      int k = ceilingItem(RB, sum[i].intValue());
      if (k == -1) {
        result = Math.max(sum[i], result);
        flag = false;
      }
      if (flag) {
        result = Math.max((sum[i] - k) % m, result);
      }
      if (result == (m - 1)) {
        break;
      }
      RB.put(sum[i].intValue(), sum[i]);
    }
    return result;
  }

  private int ceilingItem(RedBlackTree<Long> RB, int key) {
    return Arrays.stream(RB.keySet()).filter(value -> value >= key).sorted().findFirst().orElse(-1);
  }

  private <T> void checkInputSize(List<T> values, int size) {
    if (values.size() != size) {
      throw new IllegalArgumentException("Not supported input size");
    }
  }

  private void validateInput(String input) {
    List<String> inputs = List.of(INPUT1, INPUT2, INPUT3);
    String nonNullInput = Optional.ofNullable(input).orElseThrow(NoInputException::new);
    if (!inputs.contains(nonNullInput)) {
      throw new IllegalArgumentException("Invalid input. Not among possible input files");
    }
  }
}
