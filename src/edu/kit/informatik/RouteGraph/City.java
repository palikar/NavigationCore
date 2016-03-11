package edu.kit.informatik.RouteGraph;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public class City {

    private final String name;

    /**
     * Basic constructor
     *
     * @param name The name of the city to be created
     */
    public City(String name) {
        this.name = name;
    }

    /**
     * Basic getter
     *
     * @return The name of the city
     */
    public String getName() {
        return name;
    }

    /**
     * A factory method to be used when temporary city is used...cus...you know,
     * 'new' is bad for such situations
     *
     * @param name The name that the city should have
     * @return A city
     */
    public static City getEmptyCity(String name) {
        return new City(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        return ((City) (obj)).getName().equals(this.getName());
    }

}
