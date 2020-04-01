package data;

public class TransformerData {

    private int id;
    private float current;
    private float voltage;
    private float load;
    private String status;
    private String loc;

    public TransformerData(int id,float current, float voltage, String loc, String status) {
        this.id = id;
        this.current = current;
        this.voltage = voltage;
        this.loc = loc;
        this.status = status;
        this.load = voltage/current;
    }

    public int getId() {
        return id;
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
    
}