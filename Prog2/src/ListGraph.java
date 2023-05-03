import java.io.Console;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class ListGraph<T> implements Graph<T>, Serializable {

    private final Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {

        if (!nodes.keySet().contains(node2) || !nodes.keySet().contains(node1)) {
            throw new NoSuchElementException("No such city");
        }

        for (Entry<T, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node1)) {
                node.getValue().add(new Edge<>(node2, weight, name));
                System.out.println("Added " + node2 + " to " + node1);
            }
        }

        for (Entry<T, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node2)) {
                node.getValue().add(new Edge<>(node1, weight, name));
                System.out.println("Added " + node1 + " to " + node2);
            }
        }

    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {

    }

    @Override
    public Set<T> getNodes() {
        return new HashSet<T>(nodes.keySet());
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {

        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Didn't find any nodes!");
        }
        return nodes.get(node);

    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }

        for (Edge<T> edge : getEdgesFrom(node1)) {
            if (edge.getDestination().equals(node2)) {
                return edge;
            }
        }

        return null;

    }

    @Override
    public void disconnect(T node1, T node2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }

    @Override
    public void remove(T node) {
        Set<Edge<T>> edges = nodes.get(node);
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException();
        }

        edges.clear();

        for (Set<Edge<T>> edgesForNode : nodes.values()) {
            for (Iterator<Edge<T>> i = edgesForNode.iterator(); i.hasNext();) {
                Edge<T> element = i.next();
                if (element.getDestination() == node) {
                    i.remove();
                }

            }
            nodes.remove(node);

        }

    }

    @Override
    public boolean pathExists(T from, T to) {
        if (getEdgeBetween(from, to) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPath'");
    }

}