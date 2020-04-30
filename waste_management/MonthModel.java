public class MonthModel extends EntityTableModel<Month> {

    public final Attribute<String> DATE = new Attribute<>("Month", 
        String.class, Month::getMonthyear);

    public final MutableAttribute<Double> WEIGHT = new MutableAttribute<>("Total Weight Picked Up (lbs)", 
        Double.class, Month::getWeight, Month::setWeight);

    public MonthModel() {
        setColumns(DATE, WEIGHT);
    }
}