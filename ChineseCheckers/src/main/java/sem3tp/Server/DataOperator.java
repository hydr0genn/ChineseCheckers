//package sem3tp.Server;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//public class DataOperator {
//    @Bean
//    private static DataSource getDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/warcaby");
//        dataSource.setUsername("admin");
//        dataSource.setPassword("admin");
//        return dataSource;
//    }
//
//    @Bean
//    public static JdbcTemplate jdbcTemplate() {
//        DataSource dataSource=getDataSource();
//        return new JdbcTemplate(dataSource);
//    }
//}