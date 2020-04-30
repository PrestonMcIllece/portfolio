public class Pickup {

    private int id;
    private double weight;
    private String date;
    private String site;
    private String waste_type;
    private String company;
    private String smalldate;

    public Pickup (int id, double weight, String date, String site, String waste_type, String company, String smalldate) {
        this.id = id;
        this.weight = weight;
        this.date = date;
        this.site = site;
        this.waste_type = waste_type;
        this.company = company;
        this.smalldate = smalldate;
        }

   
    public int getId() {
        return id;
    }

    /**
     * @return the smalldate
     */
    public String getSmalldate() {
        return smalldate;
    }

    /**
     * @param smalldate the smalldate to set
     */
    public void setSmalldate(String smalldate) {
        this.smalldate = smalldate;
    }
    
    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }
    
    /**
     * @return the waste_type
     */
    public String getWaste_type() {
        return waste_type;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @param waste_type the waste_type to set
     */
    public void setWaste_type(String waste_type) {
        this.waste_type = waste_type;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }


    @Override
    public String toString(){
        return "id: " + id + ", weight: " + weight + ", date: " + date + ", site: " + site + ", waste type: " + waste_type + ", company: " + company;
    }
}