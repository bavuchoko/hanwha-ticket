package simms.gov.hanhwa_ticket.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.time.LocalDate;

@Controller
public class TicketController {

    private final RestTemplate restTemplate = new RestTemplate();

    private String QUERY;
    private String SCHEDULE;


    @GetMapping("/")
    public String getSchedule(Model model) {
        LocalDate today = LocalDate.now();

        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today.plusMonths(1));

        return "index";
    }


    @GetMapping("/url")
    public String setDirectUrl() {

        return "url";
    }

    @PostMapping("/submit")
    public String setDirectUrl(@RequestParam String value) {
        QUERY=value;
        setSchedule();
        return "index";
    }


    @GetMapping("/proxy/schedules")
    public ResponseEntity<String> requestSchedules(@RequestParam String startDate, @RequestParam String endDate) throws IOException {
//        String url = String.format("https://mapi.ticketlink.co.kr/mapi/sports/schedules?categoryId=137&teamId=63&startDate=%s&endDate=%s", startDate, endDate);
        String url1 = "https://mapi.ticketlink.co.kr/mapi/sports/schedules?"+QUERY;

        URL url = new URL(url1);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            return ResponseEntity.ok(content.toString());
            }
    }




    public void setSchedule() {
        String url1 = "https://mapi.ticketlink.co.kr/mapi/sports/schedules?"+QUERY;

        try {


            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                SCHEDULE = content.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<String> getSchedules() throws IOException {
        if(StringUtils.isEmpty(SCHEDULE)) {
            return  ResponseEntity.ok("");
        }else{
            return ResponseEntity.ok(SCHEDULE);
        }

    }
}
