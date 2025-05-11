package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;
import se.edugrade.carrental.services.StatisticsService;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;





/*Erik Edman*/

@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMostPopularPeriodShouldReturnMostPopularPeriod() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/period"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Most popular period: 2025-04-02 to 2025-04-03")));
    }

    @Test
    void getMostRentedBrandShouldReturnMostRentedBrand() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/brand"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Toyota")));
    }

    @Test
    void getCarRentalCountsShouldReturnMapWithEachRentedCarAndItsCounts() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/rental"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UET267").value(1))
                .andExpect(jsonPath("$.GYC001").value(1))
                .andExpect(jsonPath("$.ABC123").value(1));
    }

    @Test
    void getAverageBookingCostShouldReturnAverageBookingCost() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/bookingcost"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1869));
    }

    @Test
    void getRevenuePerCarShouldReturnRevenuePerCar() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/revenuepercar"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UET267").value(1084))
                .andExpect(jsonPath("$.GYC001").value(1887))
                .andExpect(jsonPath("$.ABC123").value(2636));
    }

    @Test
    void getTotalRevenueShouldReturnTotalRevenue() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/totalrevenue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5607));
    }

    @Test
    void sendingDefaultParamShouldGetUnknownType() throws Exception
    {
        mockMvc.perform(get("/api/v1/admin/statistics/default"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("Unknown type")));
    }

}
