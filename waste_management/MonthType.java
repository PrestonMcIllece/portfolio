public class MonthType {
    private String monthyear;
    private String waste_type;
    private double weight;


    public MonthType (String smalldate, String waste_type, double weight) {
        this.waste_type = waste_type;
        this.weight = weight;

        String year = smalldate.substring(0,4);
        String month = smalldate.substring(4, smalldate.length());
        month = monthfinder(month);
        this.monthyear = year + " " + month;
    }

    private String monthfinder(String tempMonth){
        String month_word = "";  
             if (tempMonth.equals("01")){month_word = "Jan";}
             if (tempMonth.equals("02")){month_word = "Feb";} 
             if (tempMonth.equals("03")){month_word = "Mar";}  
             if (tempMonth.equals("04")){month_word = "Apr";}  
             if (tempMonth.equals("05")){month_word = "May";}
             if (tempMonth.equals("06")){month_word = "Jun";}
             if (tempMonth.equals("07")){month_word = "Jul";}
             if (tempMonth.equals("08")){month_word = "Aug";}  
             if (tempMonth.equals("09")){month_word = "Sep";}  
             if (tempMonth.equals("10")){month_word = "Oct";}  
             if (tempMonth.equals("11")){month_word = "Nov";}
             if (tempMonth.equals("12")){month_word = "Dec";} 
             return month_word; 
       }

    /**
     * @return the monthyear
     */
    public String getMonthyear() {
        return monthyear;
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
    public String toString() {
        return "date: " + monthyear + ", waste_type: " + waste_type + ", weight: " + weight;
    }

}