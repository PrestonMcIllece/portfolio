public class AggroTableModel extends EntityTableModel<Aggro> {

    public final Attribute<String> WASTE_TYPE = new Attribute<>("Type of Waste",
    String.class, Aggro::getWaste_type);

    public final MutableAttribute<Double> SUM = new MutableAttribute<>("Total weight picked up",
    Double.class, Aggro::getSum, Aggro::setSum);

    public final MutableAttribute<Double> AVERAGE = new MutableAttribute<>("Average pickup weight",
    Double.class, Aggro::getAverage, Aggro::setAverage);

    public AggroTableModel() {
        setColumns(WASTE_TYPE, SUM);
    }
}