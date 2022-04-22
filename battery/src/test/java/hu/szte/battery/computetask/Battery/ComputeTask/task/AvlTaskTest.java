package hu.szte.battery.computetask.Battery.ComputeTask.task;

import hu.szte.battery.computetask.Battery.ComputeTask.utils.exception.NoInputException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AvlTaskTest {

    AvlTask avlTask;

    @Test
    public void shouldReturn1() {
        avlTask = new AvlTask(1);
        Long execute = avlTask.execute();
        assertEquals(1, execute);
    }

    @Test
    public void shouldReturn60() {
        avlTask = new AvlTask(10);
        Long execute = avlTask.execute();
        assertEquals(60, execute);
    }

    @Test
    public void shouldReturn61397433951772() {
        avlTask = new AvlTask(55);
        Long execute = avlTask.execute();
        assertEquals(61397433951772L, execute);
    }

    @Test
    public void shouldThrowNoInputError() {
        assertThrows(NoInputException.class, () -> {
            avlTask = new AvlTask(null);
            Long execute = avlTask.execute();
        });
    }

    @Test
    public void shouldIllegalArgumentError() {
        assertThrows(IllegalArgumentException.class, () -> {
            avlTask = new AvlTask(-1);
            Long execute = avlTask.execute();
        });
    }
}