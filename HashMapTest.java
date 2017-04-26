public class AvlTreeMapTest extends MapBaseTest {
    @Override
    protected Map<String, LinkedList<String>> createMap() {
        return new HashMapTwo<String, LinkedList<String>>();
    }
}
