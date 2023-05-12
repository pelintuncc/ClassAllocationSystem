import java.util.*;

interface ClassroomIterator {
    boolean hasNext();

    Classroom next();
}

class ConcreteClassroomIterator implements ClassroomIterator {
    private final Deque<CampusComponent> stack = new ArrayDeque<>();

    public ConcreteClassroomIterator(CampusComponent root) {
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Classroom next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        CampusComponent node = stack.pop();
        if (node instanceof Classroom) {
            return (Classroom) node;
        } else {
            Iterator<CampusComponent> iter = node.getChildren().iterator();
            while (iter.hasNext()) {
                stack.push(iter.next());
            }
            return next();
        }
    }
}