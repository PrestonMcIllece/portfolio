public class Month {
    private String monthyear;
    private double weight;


    public Month (String smalldate, double weight) {
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
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "date: " + monthyear + ", weight: " + weight;
    }

}