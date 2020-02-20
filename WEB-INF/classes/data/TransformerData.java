package data;

import db.Database;
import db.Table;

public class TransformerData {
    private int id;
    private float current;
    private float voltage;
    private float load;
    private String status;
    private String loc;

    private static Database db;
    private static Table trTable;

    public TransformerData(int id,float current, float voltage, String loc, String status) {
        if(db == null) {
            db =  Database.getDatabase("employee");
            trTable = db.getTable("trData");
        }
        this.id = id;
        this.current = current;
        this.voltage = voltage;
        this.loc = loc;
        this.status = status;
        this.load = voltage/current;
    }

    public float getCurrent() {
        return current;
    }

    public float getVoltage() {
        return voltage;
    }

    public String getLoc() {
        return loc;
    }

    public String getStatus() {
        return status;
    }

    public float getLoad() {
        return load;
    }

    public void update() {
        trTable.updateColumn(
            "id="+this.id,
                new String[] {"current","voltage","status","location"},
                new String[]{current+"",voltage+"",status, loc});
    }
    
}