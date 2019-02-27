package ApplyingToExistingApplications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import rx.Observable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
public class ComposingObservables {

    @Autowired
    JdbcTemplate jdbcTemplate;


    private static final int PAGE_SIZE=10;


    List<Person> listPeople(int page) {
        return query(
                "SELECT * FROM PEOPLE ORDER BY id LIMIT ? OFFSET ?",
                PAGE_SIZE,
                page * PAGE_SIZE);
    }

    Observable<Person> allPeople(int initialPage) {
        /*According to the book this should not generate a stackoverflow error, but it does*/
        return Observable.defer(() -> Observable.from(listPeople(initialPage)))
                .concatWith(Observable.defer(() ->
                        allPeople(initialPage + 1)));
    }


    List<Person> listAll(){
        return jdbcTemplate.query("SELECT * FROM PEOPLE",new PeopleMapper());
    }


    public <T >List<T> query(String query,int pageSize, int start){
        return jdbcTemplate.query(query,new Object[]{pageSize,start},new PeopleMapper());
    }

    private static class PeopleMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong("id"));
            person.setName(rs.getString("name"));
            person.setAge(rs.getInt("age"));
            return person;
        }
    }

}
