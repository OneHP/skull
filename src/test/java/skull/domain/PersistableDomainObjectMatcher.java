package skull.domain;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class PersistableDomainObjectMatcher extends BaseMatcher<PersistableDomainObject> {

    private PersistableDomainObject toMatch;

    public PersistableDomainObjectMatcher(PersistableDomainObject toMatch){
        this.toMatch = toMatch;
    }

    @Override
    public boolean matches(Object item) {
        if(item instanceof PersistableDomainObject){
            return toMatch.deepEquals(item);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toMatch.toString());
    }
}
