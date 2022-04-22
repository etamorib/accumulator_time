package hu.szte.battery.computetask.Battery.ComputeTask.controller;

import hu.szte.battery.computetask.Battery.ComputeTask.data.BatteryTime;
import hu.szte.battery.computetask.Battery.ComputeTask.data.RunTime;
import hu.szte.battery.computetask.Battery.ComputeTask.repository.BatteryTimeRepository;
import hu.szte.battery.computetask.Battery.ComputeTask.repository.RunTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/save")
@RequiredArgsConstructor
public class SaveMeasures {

  private final RunTimeRepository runTimeRepository;
  private final BatteryTimeRepository batteryTimeRepository;

  @PostMapping("/battery")
  public BatteryTime save(@RequestBody BatteryTime batteryTime) {
    return batteryTimeRepository.save(batteryTime);
  }

  @PostMapping("/run")
  public RunTime save(@RequestBody RunTime runTime) {
    return runTimeRepository.save(runTime);
  }

  @GetMapping("/batteries")
  public Collection<BatteryTime> getAllBatteries() {
    return StreamSupport.stream(batteryTimeRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @GetMapping("/runs")
  public Collection<RunTime> getAllRuns() {
    return StreamSupport.stream(runTimeRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }
}
