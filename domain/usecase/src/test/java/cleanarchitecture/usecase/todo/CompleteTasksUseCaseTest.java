package cleanarchitecture.usecase.todo;

import cleanarchitecture.domain.common.EventsGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.domain.todo.events.TaskCompleted;
import cleanarchitecture.domain.todo.gateway.TaskToDoRepository;
import cleanarchitecture.domain.user.gateway.UserScoreGateway;

import static org.mockito.Mockito.*;
import static cleanarchitecture.domain.todo.TaskToDo.TaskReportStatus.ASSIGNED;

@ExtendWith(MockitoExtension.class)
public class CompleteTasksUseCaseTest {

    @InjectMocks
    private CompleteTasksUseCase useCase;

    @Mock
    private TaskToDoRepository repository;

    @Mock
    private EventsGateway eventsGateway;

    @Mock
    private UserScoreGateway scoreGateway;

    private ArgumentCaptor<TaskCompleted> captor = ArgumentCaptor.forClass(TaskCompleted.class);
    private final String taskId = "42";
    private final TaskToDo taskInDB = TaskToDo.builder().id(taskId).name("TaskName").description("TaskDesc").assignedUserId("56")
        .reportStatus(ASSIGNED).build();

    @BeforeEach
    public void init() {
        when(repository.findById(taskId)).thenReturn(Mono.just(taskInDB));
        when(repository.save(any())).then(inv -> Mono.just(inv.getArgument(0)));
        when(eventsGateway.emit(any())).thenReturn(Mono.empty());
    }

    @Test
    public void shouldMarkAsDone() {
        when(scoreGateway.addPointsToUserScore("56", CompleteTasksUseCase.COMPLETION_POINTS)).thenReturn(Mono.empty());
        final Mono<Void> result = useCase.markAsDone(taskId);

        //Test no action happens until subscription
        verify(repository, never()).save(any());
        verify(eventsGateway, never()).emit(any());

        StepVerifier.create(result).verifyComplete();

        verify(repository).save(any());
        verify(eventsGateway).emit(captor.capture());

        final TaskCompleted completed = captor.getValue();
        Assertions.assertThat(completed.getTask()).extracting(TaskToDo::getId, TaskToDo::getName, TaskToDo::isDone)
            .containsExactly(taskInDB.getId(), taskInDB.getName(), true);
    }

    @Test
    public void shouldMarkAsDoneDespiteCommandFail() {
        when(scoreGateway.addPointsToUserScore("56", CompleteTasksUseCase.COMPLETION_POINTS)).thenReturn(Mono.error(new RuntimeException()));
        final Mono<Void> result = useCase.markAsDone(taskId);

        StepVerifier.create(result).verifyComplete();
    }
}