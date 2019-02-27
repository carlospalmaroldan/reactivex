package ApplyingToExistingApplications;

import org.h2.Driver;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

@Configuration
@ComponentScan
public class TestConfig {


    @Bean
    public DataSource dataSource() {
        final String driverClassName = "org.h2.Driver";
        final String jdbcUrl = "jdbc:h2:mem:test";
        final String username = "sa";
        final String password = "";
        final DataSource dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(jdbcUrl).username(username).password(password).build();


        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("UTF-8")
            .addScript("schema.sql")
            .addScript("test-data.sql").build();
    }



    @Bean
    JdbcTemplate jdbcTemplate() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return new JdbcTemplate(dataSource());
    }


}
