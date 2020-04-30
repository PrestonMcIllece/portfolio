public class Waste_Type {
    private int id;
    private String name;

    public Waste_Type(int id, String name) {
        this.id = id;
        this.name = name;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}