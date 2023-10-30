package cleanarchitecture.web.task;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.usecase.todo.CreateTasksUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CreateTasksService.class)
public class CreateTasksServiceTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private CreateTasksUseCase useCase;
    private final String name = "Task 1";
    private final String description = "Desc Test task";

    @BeforeEach
    public void init() {
        when(useCase.createNew(name, description)).then(i ->
            just(TaskToDo.builder().name(name).description(description).id("01").build()));
    }

    @Test
    public void createNewTaskRestTest() {

        final WebTestClient.ResponseSpec spec = testClient.post().uri("/task")
            .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Task 1\", \"description\": \"Desc Test task\"}")
            .exchange();

        spec.expectBody(TaskToDoDTO.class).consumeWith(res -> {
            final HttpStatusCode status = res.getStatus();
            final TaskToDoDTO body = res.getResponseBody();
            assertThat(status.is2xxSuccessful()).isTrue();
            assertThat(body).extracting(TaskToDoDTO::getId, TaskToDoDTO::getName, TaskToDoDTO::getDescription)
                .containsExactly("01", name, description);
        });

    }

    @Data
    private static class TaskToDoDTO {
        private String id;
        private String name;
        private String description;

    }

}