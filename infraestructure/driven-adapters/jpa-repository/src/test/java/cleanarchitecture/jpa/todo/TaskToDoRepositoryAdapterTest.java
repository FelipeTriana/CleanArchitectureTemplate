package cleanarchitecture.jpa.todo;

import static org.assertj.core.api.Assertions.*;
import static reactor.core.publisher.Flux.just;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import cleanarchitecture.domain.todo.TaskToDo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskToDoRepositoryAdapterTest {

    @Autowired
    private TaskToDoRepositoryAdapter adapter;

    private final TaskToDo task1 = TaskToDo.builder().id("1").name("Task 1").description("Task Desc 1").assignedUserId("56").build();
    private final TaskToDo task2 = TaskToDo.builder().id("2").name("Task 2").description("Task Desc 2").assignedUserId("56").build();
    private final TaskToDo task3 = TaskToDo.builder().id("3").name("Task 3").description("Task Desc 3").assignedUserId("57").build();
    private final TaskToDo task4 = TaskToDo.builder().id("4").name("Task 4").description("Task Desc 4").assignedUserId("56").done(true).build();

    @BeforeEach
    public void saveInitialData() {
        final Mono<Void> result = adapter.saveAll(just(task1, task2, task3, task4));
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    public void shouldFindAll() {
        StepVerifier.create(adapter.findAll().collectList())
            .assertNext(tasks -> assertThat(tasks).contains(task1, task2, task3, task4)).verifyComplete();
    }


    @Test
    public void findAllUserOpenTasks() {
        final Flux<TaskToDo> openTasks = adapter.findAllUserOpenTasks("56");
        StepVerifier.create(openTasks.collectList()).assertNext(tasks ->
            assertThat(tasks).containsOnly(task1, task2))
            .verifyComplete();
    }
}