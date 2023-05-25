import javafx.scene.shape.Circle;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
// PROG2 VT2023, Inlämningsuppgift, del 1
// Grupp 112
// Alexander Jaxgård alja9460
// Emil Calmels emca3600

public class ListGraph<T> implements Graph<T>, Serializable {

    private Map<String, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        nodes.putIfAbsent((String) node, new HashSet<>());


    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {

        if (weight < 0) {
            throw new IllegalArgumentException();
        }

        if (!nodes.containsKey(node2) || !nodes.containsKey(node1)) {
            throw new NoSuchElementException("No such city");
        }

        if (getEdgeBetween(node1, node2) != null) {
            throw new IllegalStateException();
        }

        for (Entry<String, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node1)) {
                node.getValue().add(new Edge<>(node2, weight, name));

            }
        }

        for (Entry<String, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node2)) {
                node.getValue().add(new Edge<>(node1, weight, name));

            }
        }

    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {

        if (weight < 0) {
            throw new IllegalArgumentException();
        }

        Edge<T> edgeA = getEdgeBetween(node1, node2);
        Edge<T> edgeB = getEdgeBetween(node2, node1);

        edgeA.setWeight(weight);
        edgeB.setWeight(weight);


    }

    @Override
    public Set<T> getNodes() {
        return new HashSet<T>((Collection<? extends T>) nodes.keySet());
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {

        if (node instanceof Circle) {
            String name = PathFinder.getName((Circle) node);
            node = (T) name;
        }


        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Didn't find any nodes!");
        }
        return nodes.get(node);

    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {

        if (node1 instanceof Circle) {
            String name = PathFinder.getName((Circle) node1);
            node1 = (T) name;
        }

        if (node2 instanceof Circle) {
            String name = PathFinder.getName((Circle) node2);
            node2 = (T) name;
        }

        if (!nodes.containsKey((String) node1) || !nodes.containsKey((String) node2)) {
            throw new NoSuchElementException("Nodes not found in the graph");
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
        if (node1 instanceof Circle) {
            String name = PathFinder.getName((Circle) node1);
            node1 = (T) name;
        }
        final T nodeOne = node1;
        final T nodeTwo = node2;
        if (node2 instanceof Circle) {
            String name = PathFinder.getName((Circle) node2);
            node2 = (T) name;
        }

        if (!nodes.containsKey(node2) || !nodes.containsKey(node1)) {
            throw new NoSuchElementException("No such city");
        }
        if (getEdgeBetween(node1, node2) == null) {
            throw new IllegalStateException();
        }

        if (!pathExists(node1, node2)) {
            throw new IllegalStateException();
        }

        for (Entry<String, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node2)) {
                node.getValue().removeIf(edge -> edge.getDestination().equals(nodeOne));
            }
        }

        for (Entry<String, Set<Edge<T>>> node : nodes.entrySet()) {
            if (node.getKey().equals(node1)) {
                node.getValue().removeIf(edge -> edge.getDestination().equals(nodeTwo));
            }
        }


    }

    @Override
    public void remove(T node) {
        String name = "";
        if (node instanceof Circle) {
            name = PathFinder.getName((Circle) node);

        }

        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException();
        }


        for (Set<Edge<T>> edgesOfNode : nodes.values()) {
            edgesOfNode.removeIf(element -> element.getDestination() == node);
        }
        nodes.remove(node);
    }


    private void dfs(T current, T searchedFor, Set<T> visited) {
        visited.add(current);
        if (current.equals(searchedFor)) {

            return;
        }

        if (nodes.get(current) == null) {
            return;
        }

        for (Edge edge : nodes.get(current)) {


            if (!visited.contains(edge.getDestination())) {
                dfs((T) edge.getDestination(), searchedFor, visited);
            }
        }

    }

    @Override
    public boolean pathExists(T from, T to) {
        if (from instanceof Circle) {
            String name = PathFinder.getName((Circle) from);
            from = (T) name;
        }

        if (to instanceof Circle) {
            String name = PathFinder.getName((Circle) to);
            to = (T) name;
        }

        Set<T> visited = new HashSet<>();
        dfs(from, to, visited);
        return visited.contains(to);
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {

        if (from instanceof Circle) {
            String name = PathFinder.getName((Circle) from);
            from = (T) name;
        }

        if (to instanceof Circle) {
            String name = PathFinder.getName((Circle) to);
            to = (T) name;
        }


        Map<T, T> connections = new HashMap<>();
        connections.put(from, null);

        LinkedList<T> queue = new LinkedList<>();
        queue.add(from);
        while (!queue.isEmpty()) {
            T node = queue.pollFirst();
            for (Edge<T> edge : getEdgesFrom(node)) {
                T destination = edge.getDestination();
                if (!connections.containsKey(destination)) {
                    connections.put(destination, node);
                    queue.add(destination);
                }
            }
        }

        if (!connections.containsKey(to)) {
            return null;
        }

        return gatherPath(from, to, connections);


    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connection) {
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;
        while (!current.equals(from)) {
            T next = connection.get(current);
            Edge edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String city : nodes.keySet()) {
            sb.append(city).append(":").append(nodes.get(city)).append("\n");
        }
        return sb.toString();
    }
}
