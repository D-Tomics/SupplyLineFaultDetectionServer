package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import db.Database;
import db.Table;

@WebServlet("/request")
public class DataRequestHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        if (id == null || id != null && id.equals("")) {
            out.println("invalid");
            return;
        }

        String current = req.getParameter("I");
        String voltage = req.getParameter("V");
        String freq = req.getParameter("f");
        String loc = req.getParameter("loc");
        String status = req.getParameter("status");

        Database db = Database.getDatabase("supplyline");
        Table trData = db.getTable("trLiveData");
        Table trDataLog = db.getTable("TrDataLog");

        if (trData.contains("id", id)) {
            trData.updateColumns("id=?", new String[] { "location", "current", "voltage","frequency", "status" }, loc,
                    Float.parseFloat(current), Float.parseFloat(voltage), Float.parseFloat(freq), status, id);
        }

        Date pressent_day = Date.valueOf(LocalDate.now());
        if (!trDataLog.contains("date", pressent_day)) {
            JSONObject newObj = new JSONObject();
            newObj.put("d", pressent_day.toString());
            newObj.put("max_keys", 0);
            trDataLog.insert(Integer.parseInt(id), pressent_day, newObj.toString());
        }

        String text = trDataLog.getString("dataLog", "id=? AND date=?", Integer.parseInt(id), pressent_day);
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(text);
            long max_keys = (long)json.get("max_keys");
            Time current_time = Time.valueOf(LocalTime.now());

            JSONObject prevLog = (JSONObject) json.get(Long.toString(max_keys - 1));
            if(prevLog == null) {
                JSONObject log = new JSONObject();
                log.put("i", Float.parseFloat(current));
                log.put("v", Float.parseFloat(voltage));
                log.put("f", Float.parseFloat(freq));
                log.put("t", current_time.toString());
                json.put(max_keys, log);
                json.replace("max_keys", max_keys + 1);
            } else {
                Time prevLogTime = Time.valueOf((String)prevLog.get("t"));
                long diff = (current_time.getTime() - prevLogTime.getTime()) / (60000); // in  minutes
                if(diff >= 1) {
                    JSONObject log = new JSONObject();
                    log.put("i", Float.parseFloat(current));
                    log.put("v", Float.parseFloat(voltage));
                    log.put("f", Float.parseFloat(freq));
                    log.put("t", current_time.toString());
                    json.put(max_keys, log);
                    json.replace("max_keys", max_keys + 1);
                }
            }

            trDataLog.updateColumns("id=? AND date=?", "datalog", json.toString(),Integer.parseInt(id),pressent_day);
        } catch (ParseException e) {
        }

        out.print(trData.getBoolean("isOn", "id=?",Integer.parseInt(id)) ? "on" : "off" ); 
    }

}