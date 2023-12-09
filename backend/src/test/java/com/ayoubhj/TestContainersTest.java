package com.ayoubhj;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainersTest extends AbstractUnitTestContainer {


    @Test
    void canStartPostgresDb() {
        assertThat(POSTGRE_SQL_CONTAINER.isRunning()).isTrue();
        assertThat(POSTGRE_SQL_CONTAINER.isCreated()).isTrue();
    }

}
