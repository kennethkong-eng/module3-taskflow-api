package sg.edu.ntu.taskflowapi.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import sg.edu.ntu.taskflowapi.model.Task;
import sg.edu.ntu.taskflowapi.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ChatClient chatClient;

    public TaskService(TaskRepository taskRepository, ChatClient.Builder chatClientBuilder) {
        this.taskRepository = taskRepository;
        this.chatClient = chatClientBuilder.build();
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        // Ignore any id sent by the client so the repository generates one
        task.setId(null);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        if (!taskRepository.existsById(id)) {
            return null;
        }
        // Use the id from the URL, not the request body
        task.setId(id);
        return taskRepository.save(task);
    }

    public Task deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }

    public Task markTaskAsComplete(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            return null;
        }
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    public String getTaskSummary() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return "There are no tasks yet.";
        }

        StringBuilder taskList = new StringBuilder();
        for (Task task : tasks) {
            taskList.append("- ")
                    .append(task.getTitle())
                    .append(task.isCompleted() ? " (done)" : " (pending)")
                    .append("\n");
        }

        return chatClient.prompt()
                .user("Here is my task list:\n" + taskList
                        + "\nIn 2 to 3 sentences of plain English, summarise what is still pending and what is done.")
                .call()
                .content();
    }
}
