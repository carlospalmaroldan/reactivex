package ApplyingToExistingApplications;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableAutoConfiguration(exclude={ DataSourceAutoConfiguration.class})
@SpringBootTest
public class ComposingObservablesIT {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ComposingObservables composingObservables;

    @Test
    public void shouldReturnPersonFromDatabase(){
        List<Person> persons=composingObservables.listPeople(0);
        System.out.println(persons);
    }


    @Test
    public void shouldReturnEverything(){
        List<Person> persons=composingObservables.listAll();
        System.out.println(persons);
    }

    @Test
    public void shouldReturnSeveralPages(){
         composingObservables.allPeople(0).subscribe(System.out::println);
    }

}
