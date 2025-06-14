package simms.gov.hanhwa_ticket.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private String QUERY ="eQgxJQe=50ede2970f5c93593ca67083352e7d19acac3332c6ec881fbaf24a9d99858186d5ce3c09527ad72885828cd2d822265cacbb9491218d77542ffd87b439d3505d48c67e9763248fcdaecb4de9d14b53f8b872ee8525903efa9a5ddf867e3d065f068b3df924cb4be9fd347f2e3ec7ffd526c0bb65faf50f137148907d00513a6dad78cd7021d729eb445004c90a4127d9eb0a3db39cb12f186c3f4dca49d856286ceeae592d18f8b2cbcedf5307b8c042554374ffd18424c445491611be64c32733f81c4d6f8801c1a2dda3913f50106981110a78cf75ef42cf7ce6632d26e8ba41a434560ef7ebcace4ba9a6c07467e5cbcedf5307b8c04269f9e09a28a52d726276860d5cd395caf03b6054fe62ecfb478d42fd26c99593445004c90a4127d91cb94bf0d45dcb67cbcedf5307b8c042948e8d6e1ce60f82831dacd55757509f33f81c4d6f8801c147d63646449d16d3e756eb92430070f3bf3ae6dde689b0f9f42a4f5f06146fa585cdc020bfae58c715c6cb05c89aa0493e34e5b53d858294ad659001a6af0e50bfca33a9ca3f93e7285512690442d4fc6f1e407469be9c4e1581ddc65f1ba4a26135b0ecb3a4c775b41a78f46f5b3d5004daf6b4068a154e742a19a4c3d76894fd055ad4645bde58ca502d9eaf2ef2e938450adf2744a6b297a75f6fc264d43804b785bd00eae56bd61e91dd58d6117e72116839f635a0602f58f029140e3b1a5fdc22622624e8cf35629e0c249206ed8f854858a7c561d04e018eed801bd5b0a52a808651c6ad0b750cfbba3d87bf332c82abafa38725749a2b236fe5b0fe2ec8d5f2c85a7d4f702b78fb84e6928cc2120f610785aac69f&mueIn5pAl=gmgi5QncgL8VuP3hZS8yZiJCgN505WMYFRbYZ83YZRuhdRD3uy8CFS8VbL8hbh5FtUJStU54Z14YZ8ue8S8ybhJYlS8QbpbVlP5StUbE5k5qUwFHrm1qtUJStU543J1E3JHJM8EYZ83YZRuhlL8QbL8huk5FtUJStU54rWOYZ83YZRuDUS8QbL8hu4nFtUJStU54vmXYZ8t%252BUiOYZ83YZRuD8x8QbNsxUS8QbL8huWYTtUJS0LYqtUJStU54vmXYZ8t%252BxREYZ83YZRuDdy8QbNsCdy8QbL8huWYqtUJS0L8ybhBYZ83YZRuD8x8QbNsj8x8QbL8huWYqtUJS0pJFtUJStU54vJ4YZ8t%252BMmBYZ83YZRuDUS8QbpuzlS8QbL8huWYutUJSu4uTtUJStU54vmXYZ8t4JNXYZ83YZRuDlL8QbpbVUS8QbL8huWYTtUJSFmeqtUJStU54vxTTgPbyaw4RMV5jgUpTaPMRaW3yFC1jMUuLgUpCZwgiFSTTgPbyaw4RMV5jgUpTaPMRaW3yFC1jMUuLgUpCZwgiFS8yZiZH5WJNdhtsxwbYFRbfFVrYFjgRgw1cxwbYFRbCFy8yZNZRMmtRuW1Rgx8VuP3EFj8EZj4ytU3CgwspuW1Rgx8VuP3EFj8EaU4y";



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
    public ResponseEntity<String> getNewSchedules() {

        refreshEncrypt();
        String response = getData();
        return ResponseEntity.ok(response);
    }




    public void setSchedule() {
        Schedule.LIST = getData();
    }


    private void refreshEncrypt() {
        String url1 = "https://mapi.ticketlink.co.kr/mapi/sports/schedules?"+QUERY;

        try {
            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("OPTIONS");

            // 요청 헤더 설정
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Origin", "https://www.ticketlink.co.kr");
            conn.setRequestProperty("Access-Control-Request-Method", "GET");
            conn.setRequestProperty("Access-Control-Request-Headers", "x-tl-locale");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getData() {
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
                return content.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping("/schedules")
    public ResponseEntity<String> getSchedules() throws IOException {
        if(StringUtils.isEmpty(Schedule.LIST)) {
            return  ResponseEntity.ok("");
        }else{
            return ResponseEntity.ok(Schedule.LIST);
        }

    }
}
