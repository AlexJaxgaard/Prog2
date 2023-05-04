import java.io.Serializable;

public class Edge<T> implements Serializable {

    private final T destination;
    private int weight;
    private final String name;

    public Edge(T destination, int weight, String name){
        this.destination = destination;
        this.weight = weight;
        this.name = name;
    }


    public T getDestination(){
        return this.destination;
    }

    public int getWeight(){
        return this.weight;
    }

    public String getName(){
        return this.name;
    }

     public void setWeight(int newWeight){
        this.weight = newWeight;
    }

    @Override
    public String toString(){
        return "till " + destination + " med " + name + " tar " + weight;
    }

}