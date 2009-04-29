package osm.primitive.relation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import osm.primitive.Primitive;
import osm.primitive.PrimitiveTypeEnum;

/**
 * @author Ian Dees
 *
 */
public class Relation extends Primitive {

    private List<Member> members = new ArrayList<Member>();
    
    public void addMember(Member member) {
        members.add(member);
    }

    public Iterator<Member> getMemberIterator() {
        return members.iterator();
    }

    public PrimitiveTypeEnum getType() {
        return PrimitiveTypeEnum.relation;
    }
    
}
