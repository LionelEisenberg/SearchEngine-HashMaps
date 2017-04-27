public class HashMapTest extends MapBaseTest {
    @Override
    protected Map<Integer, Integer> createMap() {
        return new HashMapTwo<Integer, Integer>();
    }
}
