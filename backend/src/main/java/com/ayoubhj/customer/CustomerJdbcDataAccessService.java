package com.ayoubhj.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJdbcDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                   SELECT id,name,email,age FROM customer;  
                   """;

        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper);

        return customers ;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                   SELECT id,name,email,age FROM customer where id = ?;  
                   """;

        Optional<Customer> customer = jdbcTemplate.query(sql,customerRowMapper,id)
                                                  .stream().findFirst();

        return customer ;
    }

    @Override
    public void insertCustomer(Customer customer) {
         var sql = """
                   INSERT INTO customer (name,email,age) VALUES (?,?,?) 
                   """;
         int update =
                 jdbcTemplate.update(sql,
                         customer.getName(),
                         customer.getEmail(),
                         customer.getAge()
                 );
        System.out.println("jdbcTemplate.update from insert method = " + update);
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                   DELETE from customer  where id = ? 
                   """;
        int update =
                jdbcTemplate.update(sql,id);

        System.out.println("jdbcTemplate.update from delete method = " + update);
    }

    @Override
    public void updateCustomer(Customer customer) {

        if(customer.getName() != null){
            String sql = "UPDATE customer SET name = ? where id = ?";

            int update =
                    jdbcTemplate.update(sql,
                            customer.getName(),
                            customer.getId()
                    );

            System.out.println("jdbcTemplate.update from update name method = " + update);

        }

        if(customer.getEmail() != null){
            String sql = "UPDATE customer SET email = ? where id = ? ";

            int update =
                    jdbcTemplate.update(sql,
                            customer.getEmail(),
                            customer.getId()
                    );
            System.out.println("jdbcTemplate.update from update email method = " + update);
        }

        if(customer.getAge() != null){
            String sql = "UPDATE customer SET age = ? where id = ? ";

            int update =
                    jdbcTemplate.update(sql,
                            customer.getAge(),
                            customer.getId()
                    );

            System.out.println("jdbcTemplate.update from update age method = " + update);
        }

    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                   SELECT count(id) FROM customer where email = ?;  
                   """;

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);

        return count != null && count > 0  ;

    }

    @Override
    public boolean existPersonWithId(Long id) {
        var sql = """
                   SELECT count(id) FROM customer where id = ?;  
                   """;

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);

        return count != null && count > 0  ;
    }
}
