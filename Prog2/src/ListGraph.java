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

        if (weight < 0){
            throw new IllegalArgumentException();
        }

        if (!nodes.containsKey(node2) || !nodes.containsKey(node1)) {
            throw new NoSuchElementException("No such city");
        }

        if (getEdgeBetween(node1, node2) != null){
            throw new IllegalStateException();
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



        if (!nodes.containsKey(node2) || !nodes.containsKey(node1)) {
            throw new NoSuchElementException("No such city");
        }
        if (getEdgeBetween(node1, node2) == null){
            throw new IllegalStateException();
        }

        if (!pathExists(node1, node2)){
            throw new IllegalStateException();
        }

        for (Entry<T, Set<Edge<T>>> node : nodes.entrySet()){
            if (node.getKey().equals(node2)){
                node.getValue().removeIf(edge -> edge.getDestination().equals(node1));
            }
        }

        for (Entry<T, Set<Edge<T>>> node : nodes.entrySet()){
            if (node.getKey().equals(node1)){
                node.getValue().removeIf(edge -> edge.getDestination().equals(node2));
            }
        }


    }

    @Override
    public void remove(T node) {

        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException();
        }



        for (Set<Edge<T>> edgesOfNode : nodes.values()) {
            edgesOfNode.removeIf(element -> element.getDestination() == node);
        }
        nodes.remove(node);
    }


    private void dfs(T current, T searchedFor, Set <T> visited){
        visited.add(current);
        if(current.equals(searchedFor)){
            System.out.println("Found!");
            return;
        }

        if (nodes.get(current) == null){
            return;
        }

        for(Edge edge : nodes.get(current)){



            if(!visited.contains(edge.getDestination())){
                dfs((T) edge.getDestination(),searchedFor,visited);
            }
        }

    }
    @Override
    public boolean pathExists(T from, T to){



        Set<T> visited = new HashSet<>();
        dfs(from, to, visited);
        return visited.contains(to);
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPath'");
    }

}