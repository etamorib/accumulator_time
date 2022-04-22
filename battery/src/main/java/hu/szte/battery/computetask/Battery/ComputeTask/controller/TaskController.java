package hu.szte.battery.computetask.Battery.ComputeTask.controller;

import hu.szte.battery.computetask.Battery.ComputeTask.service.TaskService;
import hu.szte.battery.computetask.Battery.ComputeTask.task.AvlTask;
import hu.szte.battery.computetask.Battery.ComputeTask.task.SubArrayTask;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static hu.szte.battery.computetask.Battery.ComputeTask.utils.Constants.*;

@RestController
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping(value = "/avl/{n}")
  public void runAvl(@PathVariable("n") int n) {
    int input = -1;
    switch (n) {
      case 1: input = 1000;
      break;
      case 2: input = 2000;
      break;
      case 3: input = 3000;
      break;
    }
    taskService.run(new AvlTask(input));
  }

  @GetMapping(value = "/sub_array/{input}")
  public void runSubArray(@PathVariable("input") int input) {
    String runWith = null;
    switch (input) {
      case 1:
        runWith = INPUT1;
        break;
      case 2:
        runWith = INPUT2;
        break;
      case 3:
        runWith = INPUT3;
        break;
      default:
        throw new IllegalArgumentException("Pathvariable should be 1-3");
    }
    taskService.run(new SubArrayTask(runWith));
  }
}
