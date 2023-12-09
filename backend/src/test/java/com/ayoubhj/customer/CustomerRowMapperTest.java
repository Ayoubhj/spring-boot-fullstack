package com.ayoubhj.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        Customer expected = new Customer(
                1L,"ayoub","ayoub@gmail.com",22
        );
        when(resultSet.getLong("id")).thenReturn(expected.getId());
        when(resultSet.getString("name")).thenReturn(expected.getName());
        when(resultSet.getString("email")).thenReturn(expected.getEmail());
        when(resultSet.getInt("age")).thenReturn(expected.getAge());

        // WHEN

        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //THEN
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }
}