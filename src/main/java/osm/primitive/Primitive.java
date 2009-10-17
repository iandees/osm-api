package osm.primitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Ian Dees
 *
 */
public abstract class Primitive {

    private int id;
    private List<Tag> tagsList = new ArrayList<Tag>();
    
    public void setID(int id) {
        this.id = id;
    }
    
    public int getID() {
        return this.id;
    }

    public Iterator<Tag> getTagIterator() {
        return tagsList.iterator();
    }

    public boolean hasTags() {
        return (tagsList.size() > 0);
    }

    public void addTag(Tag tag) {
        tagsList.add(tag);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Primitive) {
            return ((Primitive) obj).id == id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getTagValue(String key) {
        for (Tag tag : tagsList) {
            if (key.equals(tag.getKey())) {
                return tag.getValue();
            }
        }
        return null;
    }

    /**
     * @return
     */
    public abstract PrimitiveTypeEnum getType();

}
