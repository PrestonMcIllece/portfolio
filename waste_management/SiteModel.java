public class SiteModel extends EntityTableModel<Site> {

    public final Attribute<Integer> ID = new Attribute<>("id", 
        Integer.class, Site::getId);

    public final MutableAttribute<String> NAME = new MutableAttribute<>("Name", 
        String.class, Site::getName, Site::setName);

    public final MutableAttribute<String> ADDRESS = new MutableAttribute<>("Address", 
        String.class, Site::getAddress, Site::setAddress);

    public SiteModel() {
        setColumns(NAME, ADDRESS);
    }
}