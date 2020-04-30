public class PickupTableModel extends EntityTableModel<Pickup> {

    public final Attribute<Integer> ID = new Attribute<>("ID", 
        Integer.class, Pickup::getId);

    public final MutableAttribute<Double> WEIGHT = new MutableAttribute<>("Weight (lbs)", 
        Double.class, Pickup::getWeight, Pickup::setWeight);

    public final MutableAttribute<String> DATE = new MutableAttribute<>("Date", 
        String.class, Pickup::getDate, Pickup::setDate);

    public final MutableAttribute<String> SITE = new MutableAttribute<>("Picked up from", 
        String.class, Pickup::getSite, Pickup::setSite);

    public final MutableAttribute<String> WASTE_TYPE = new MutableAttribute<>("Type of Waste", 
        String.class, Pickup::getWaste_type, Pickup::setWaste_type);

    public final MutableAttribute<String> COMPANY = new MutableAttribute<>("Company", 
        String.class, Pickup::getCompany, Pickup::setCompany);



    public PickupTableModel() {
        setColumns(ID, WEIGHT, DATE, SITE, WASTE_TYPE, COMPANY);
    }
}