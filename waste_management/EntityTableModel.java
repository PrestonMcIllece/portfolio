import java.util.*;
import java.util.function.*;
import javax.swing.table.AbstractTableModel;

public class EntityTableModel<T> extends AbstractTableModel {

    public class Attribute<F> {
        
        private final String name;
        private final Class<F> clazz;
        private final Function<T, F> accessor;

        public Attribute(String name, Class<F> clazz, Function<T, F> accessor) {
            this.name = name;
            this.clazz = clazz;
            this.accessor = accessor;
        }

        public F get(T instance) {
            return accessor.apply(instance);
        }

        public String getName() {
            return name;
        }
    }

    public class MutableAttribute<F> extends Attribute<F> {

        private final BiConsumer<T, F> mutator;

        public MutableAttribute(String name, Class<F> clazz, Function<T, F> accessor, BiConsumer<T, F> mutator) {
            super(name, clazz, accessor);
            this.mutator = mutator;
        }

        public void set(T instance, F value) throws IllegalArgumentException {
            mutator.accept(instance, value);
        }
    }


    private List<T> instances = new ArrayList<>();
    private List<Attribute<?>> attributes = new ArrayList<>();

    public EntityTableModel() {
    }

    @SuppressWarnings("unchecked")
    public void setColumns(Attribute... attributes) {
        this.attributes = new ArrayList(Arrays.asList(attributes));
        fireTableStructureChanged();
    }

    public <F> void addAttribute(Attribute<F> attribute) {
        attributes.add(attribute);
        fireTableStructureChanged();
    }

    public <F> void removeAttribute(Attribute<F> attribute) {
        if (attributes.remove(attribute))
            fireTableStructureChanged();
    }

    public Attribute<?> getAttribute(int col) {
        return attributes.get(col);
    }

    public List<Attribute<?>> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public List<T> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    public T getInstance(int row) {
        return instances.get(row);
    }

    public void addInstance(T instance) {
        instances.add(instance);
        fireTableRowsInserted(instances.size() - 1, instances.size() - 1);
    }

    public void setInstance(int row, T instance) {
        instances.set(row, instance);
        fireTableRowsUpdated(row, row);
    }

    public void addAll(Collection<? extends T> newInstances) {
        int oldSize = instances.size();
        instances.addAll(newInstances);
        fireTableRowsInserted(oldSize, instances.size() - 1);
    }

    public void clearInstances() {
        int oldSize = instances.size();
        instances.clear();
        fireTableRowsDeleted(0, oldSize - 1);
    }

    public <F extends Comparable<F>> void sort(Attribute<F> attribute) {
        sort(attribute, (i1, i2) -> { return i1.compareTo(i2); });
    }

    public <F> void sort(Attribute<F> attribute, Comparator<F> comparator) {
        Collections.sort(instances, (i1, i2) -> { return comparator.compare(attribute.get(i1), attribute.get(i2)); });
        fireTableDataChanged();
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(instances, comparator);
        fireTableDataChanged();
    }

    // ------ TableModel methods ------
    @Override
    public int getRowCount() {
        return instances.size();
    }

    @Override
    public int getColumnCount() {
        return attributes.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return attributes.get(col).get(instances.get(row));
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return attributes.get(col).clazz;
    }

    @Override
    public String getColumnName(int col) {
        return attributes.get(col).name;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return attributes.get(col) instanceof MutableAttribute;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValueAt(Object value, int row, int col) throws IllegalArgumentException {
        Attribute<?> attribute = attributes.get(col);

        if (!(attribute instanceof MutableAttribute))
            throw new IllegalArgumentException("Value not assignable");

        if (value == null || attribute.clazz.isAssignableFrom(value.getClass())) {
            ((MutableAttribute<Object>)attribute).set(instances.get(row), value);
            fireTableCellUpdated(row, col);
        }
    }
}
