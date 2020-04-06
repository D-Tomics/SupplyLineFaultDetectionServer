package data;

public class TransformerData {

    private int     id;
    private float   current;
    private float   voltage;
    private float   frequency;
    private String  status;
    private String  loc;
    private boolean isOn;

    public TransformerData(int id,float current, float voltage, float frequency, String loc, String status, boolean isOn) {
        this.id         = id;
        this.current    = current;
        this.voltage    = voltage;
        this.frequency  = frequency;
        this.loc        = loc;
        this.status     = status;
        this.isOn       = isOn;
    }

    public int      getId       () { return id;         }
    public float    getCurrent  () { return current;    }
    public float    getVoltage  () { return voltage;    }
    public float    getFrequency() { return frequency;  }
    public String   getLoc      () { return loc;        }
    public String   getStatus   () { return status;     }
    public boolean  isOn        () { return isOn;       }

    public String toString() {
        return "{ id :"+id+", current : "+current+", voltage : "+voltage+", freq : "+frequency+", location : "+loc+", status : "+status+" }";
    }
    
}