package se.edugrade.carrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import se.edugrade.carrental.services.StatisticsService;

@Controller
@RequestMapping("/api/v1/admin")
public class StatisticsController
{
    private StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService)
    {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics/{type}")
    @ResponseBody
    public ResponseEntity<?> getStatistics(@PathVariable String type)
    {
        switch (type)
        {
            case "period":
                return ResponseEntity.ok(statisticsService.getMostPopularPeriod());
            default:
                return ResponseEntity.badRequest().body("Unknown type");
        }
    }
}
