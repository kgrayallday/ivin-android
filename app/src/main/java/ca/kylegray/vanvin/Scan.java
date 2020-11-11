package ca.kylegray.vanvin;

//class for all Scan Tests
public class Scan {
    private String job;
    private String vin;
    private String stamp;
    private String reading;
    private String tire;
    private String comment;
    private String devid;

    public Scan(){}

    // CONSTRUCTOR FOR FULL SCANS
    public Scan (String job, String vin, String stamp, String reading, String tire, String comment, String devid){
        this.job = job;
        this.vin = vin;
        this.stamp = stamp;
        this.reading = reading;
        this.tire = tire;
        this.comment = comment;
        this.devid = devid;
    }

    // CONSTRUCTOR FOR GENERIC SCANS, REQUIRING VIN AND COMMENT ONLY
    // Leaves out:
    // 1) Reading
    // 2) Tire
    public Scan (String job, String vin, String stamp, String comment, String devid){
        this.job = job;
        this.vin = vin;
        this.stamp = stamp;
        this.comment = comment;
        this.devid = devid;

    }


    //GETTER METHODS FOR EACH INSTANCE/ATTRIBUTE OF THE CLASS "SCAN"
    public String getJob(){
        return job;
    }
    public String getVin() {
        return vin;
    }
    public String getStamp(){
        return stamp;
    }
    public String getReading() {
        return reading;
    }
    public String getTire(){
        return tire;
    }
    public String getComment(){
        return comment;
    }
    public String getDevid() {return devid;}

    //SETTER METHODS
    public void setJob(String newJob){ this.job = newJob; }
    public void setVin(String newVin){ this.vin = newVin; }
    public void setStamp(String newStamp){ this.stamp = newStamp; }
    public void setReading(String newReading){ this.reading = newReading; }
    public void setTire(String newTire){ this.tire = newTire; }
    public void setComment(String newComment){ this.comment = newComment; }
    public void setDevid(String newDevid){ this.devid = newDevid; }


    /* public String toString() {
        String result = "";
        result = getVin() + getStamp()+ getJob() + getReading() + getTire() + getComment();
        return result;
        }
    */
}
