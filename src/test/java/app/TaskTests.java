package app;

import controllers.TaskController;
import dto.TaskDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
class TaskTests {
    @Autowired
    Application app;
    @Autowired
    TaskController controller;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @Test
    @WithMockUser(roles = {"AUTHOR"})
    void checkFetchAllTasks() {
        final int numberOfIterations = 1;

        try {
            int counter = 0;

            List<Future<ResponseEntity<List<TaskDto>>>> futureList = new ArrayList<>();
            ExecutorService executorService = Executors.newCachedThreadPool();

            for (int i = 0; i < numberOfIterations; i++) {
                Callable<ResponseEntity<List<TaskDto>>> task = () -> controller.fetchAllTasks(null);

                futureList.add(executorService.submit(task));

                List<TaskDto> taskDtoList = futureList.get(i).get().getBody();

                assert taskDtoList != null;
                if (taskDtoList.get(i) != null) {
                    counter++;
                }
            }

            assertEquals(numberOfIterations, counter);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

            fail();
        }
    }

}
