package cleanarchitecture;

import cleanarchitecture.usecase.todo.*;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cleanarchitecture.domain.common.EventsGateway;
import cleanarchitecture.domain.todo.gateway.TaskToDoRepository;
import cleanarchitecture.domain.user.gateway.UserGateway;
import cleanarchitecture.domain.user.gateway.UserScoreGateway;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateTasksUseCase createTasksUseCase(TaskToDoRepository tasks, EventsGateway eventGateway) {
        return new CreateTasksUseCase(tasks, eventGateway);
    }

    @Bean
    public AssignTasksUseCase assignTasksUseCase(TaskToDoRepository tasks, UserGateway users, EventsGateway eventsGateway) {
        return new AssignTasksUseCase(tasks, users, eventsGateway);
    }

    @Bean
    public CompleteTasksUseCase completeTasksUseCase(TaskToDoRepository tasks, EventsGateway eventsGateway, UserScoreGateway userScoreGateway) {
        return new CompleteTasksUseCase(tasks, eventsGateway, userScoreGateway);
    }

    @Bean
    public ReAssignUserTasksUseCase reAssignUserTasksUseCase(TaskToDoRepository tasks) {
        return new ReAssignUserTasksUseCase(tasks);
    }

    @Bean
    public QueryTasksUseCase queryTasksUseCase(TaskToDoRepository tasks, UserGateway usersGateway) {
        return new QueryTasksUseCase(tasks, usersGateway);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

}
