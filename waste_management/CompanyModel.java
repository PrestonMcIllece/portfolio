public class CompanyModel extends EntityTableModel<Company> {

    public final Attribute<Integer> ID = new Attribute<>("id", 
        Integer.class, Company::getId);

    public final MutableAttribute<String> NAME = new MutableAttribute<>("Name", 
        String.class, Company::getName, Company::setName);

    public final MutableAttribute<String> ADDRESS = new MutableAttribute<>("Address", 
        String.class, Company::getAddress, Company::setAddress);

    public final MutableAttribute<String> DESC = new MutableAttribute<>("Description", 
        String.class, Company::getDescription, Company::setDescription);

    public CompanyModel() {
        setColumns(NAME, ADDRESS, DESC);
    }
}