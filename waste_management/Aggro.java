public class Aggro {

    private String waste_type;
    private double sum;
    private double average;

    public Aggro (String waste_type, double sum, double average) {
        this.waste_type = waste_type;
        this.sum = sum;
        this.average = average;
    }

    /**
     * @return the average
     */
    public double getAverage() {
        return average;
    }

    /**
     * @param average the average to set
     */
    public void setAverage(double average) {
        this.average = average;
    }

    /**
     * @return the sum
     */
    public double getSum() {
        return sum;
    }

    /**
     * @param sum the sum to set
     */
    public void setSum(double sum) {
        this.sum = sum;
    }

    /**
     * @return the waste_type
     */
    public String getWaste_type() {
        return waste_type;
    }

    @Override
    public String toString(){
        return "waste type: " + waste_type + ", sum: " + sum + ", average: " + average;
    }

    // /**
    //  * @param waste_type the waste_type to set
    //  */
    // public void setWaste_type(String waste_type) {
    //     this.waste_type = waste_type;
    // }

}