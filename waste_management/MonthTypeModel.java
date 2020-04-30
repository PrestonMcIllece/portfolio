public class MonthTypeModel extends EntityTableModel<MonthType> {

    public final Attribute<String> DATE = new Attribute<>("Month", 
        String.class, MonthType::getMonthyear);

    public final MutableAttribute<String> WASTE_TYPE = new MutableAttribute<>("Type of Waste", 
        String.class, MonthType::getWaste_type, MonthType::setWaste_type);

    public final MutableAttribute<Double> WEIGHT = new MutableAttribute<>("Total Weight Picked Up (lbs)", 
        Double.class, MonthType::getWeight, MonthType::setWeight);

    public MonthTypeModel() {
        setColumns(DATE, WASTE_TYPE, WEIGHT);
    }
}