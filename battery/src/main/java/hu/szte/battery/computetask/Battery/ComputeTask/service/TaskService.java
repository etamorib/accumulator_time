package hu.szte.battery.computetask.Battery.ComputeTask.service;

import hu.szte.battery.computetask.Battery.ComputeTask.task.Task;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  public void run(@NonNull Task task) {
    task.execute();
  }
}
