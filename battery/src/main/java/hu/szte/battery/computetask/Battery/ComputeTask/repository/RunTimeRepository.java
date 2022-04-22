package hu.szte.battery.computetask.Battery.ComputeTask.repository;

import hu.szte.battery.computetask.Battery.ComputeTask.data.RunTime;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface RunTimeRepository extends CrudRepository<RunTime, String> {}
