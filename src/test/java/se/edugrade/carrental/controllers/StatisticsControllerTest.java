package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.edugrade.carrental.services.StatisticsService;

import static org.junit.jupiter.api.Assertions.*;

/*Erik Edman*/

@SpringBootTest
class StatisticsControllerTest
{
    @Autowired
    private StatisticsController controller;

    @Autowired
    private StatisticsService service;

    @Test
    void getStatistics()
    {

    }
}