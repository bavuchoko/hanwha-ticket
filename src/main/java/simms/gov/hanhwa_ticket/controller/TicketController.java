package simms.gov.hanhwa_ticket.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Controller
public class TicketController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String getSchedule(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today.plusMonths(1));

        return "index";
    }


    @GetMapping("/proxy/schedules")
    public ResponseEntity<String> getSchedules(@RequestParam String startDate, @RequestParam String endDate) {
        String url = String.format("https://mapi.ticketlink.co.kr/mapi/sports/schedules?categoryId=137&teamId=63&startDate=%s&endDate=%s", startDate, endDate);
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}
