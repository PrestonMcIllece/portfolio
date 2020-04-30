public class Waste_TypeModel extends EntityTableModel<Waste_Type> {

    public final Attribute<Integer> ID = new Attribute<>("id", 
        Integer.class, Waste_Type::getId);

    public final MutableAttribute<String> NAME = new MutableAttribute<>("Name", 
        String.class, Waste_Type::getName, Waste_Type::setName);

    public Waste_TypeModel() {
        setColumns(NAME);
    }
}