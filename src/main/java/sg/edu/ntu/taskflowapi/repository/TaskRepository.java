package sg.edu.ntu.taskflowapi.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import sg.edu.ntu.taskflowapi.model.Task;

@Repository
public class TaskRepository {

    private final Map<Long, Task> tasks = new HashMap<>();
    private Long nextId = 1L;

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task findById(Long id) {
        return tasks.get(id);
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public boolean existsById(Long id) {
        return tasks.containsKey(id);
    }

    public Task deleteById(Long id) {
        return tasks.remove(id);
    }
}
