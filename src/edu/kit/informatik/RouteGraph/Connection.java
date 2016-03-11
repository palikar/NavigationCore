package edu.kit.informatik.RouteGraph;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public class Connection {

    private City toCity;
    private City fromCity;
    private int distance;
    private int minutes;
    private int optimal;

    /**
     * Makes connections only with the name of the cities. The other attributes
     * are set to 0
     *
     * @param toCity The first city
     * @param fromCity The second city
     */
    public Connection(City toCity, City fromCity) {
        this(toCity, fromCity, 0, 0);
    }

    /**
     * Make a connection with the given values for the attributes. The optimal
     * value is seta according to the 'Oprtimal value rule ' in the Route Graph
     * class
     *
     * @param toCity The first city
     * @param fromCity The second city
     * @param distance The distance between the cities in km
     * @param minutes The tine it takes to get from the one city to the other in
     * min
     */
    public Connection(City toCity, City fromCity, int distance, int minutes) {
        this.toCity = toCity;
        this.fromCity = fromCity;
        this.distance = distance;
        this.minutes = minutes;
        this.optimal = RouteGraph.getOptimalWeigthRule(distance, minutes);
    }

    /**
     * Basic getter
     *
     * @return The first city
     */
    public City getToCity() {
        return toCity;
    }

    /**
     * Basic setter
     *
     * @param toCity The first city
     */
    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    /**
     * Basic getter
     *
     * @return The second city
     */
    public City getFromCity() {
        return fromCity;
    }

    /**
     * Basic setter
     *
     * @param fromCity The second city
     */
    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    /**
     * Basic getter
     *
     * @return The distance between the cities in km
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Basic setter of the distance
     *
     * @param distance Distance in km
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Basic getter
     *
     * @return The time between the cities in min
     */
    public int getTime() {
        return minutes;
    }

    /**
     * Basic setter of the distance
     *
     * @param minutes Time in min
     */
    public void setTime(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Basic getter
     *
     * @return The optimal value for the length of the path between the two
     * cities
     */
    public int getOptimal() {
        return optimal;
    }

    /**
     * Basic setter
     *
     * @param optimal Optimal value
     */
    public void setOptimal(int optimal) {
        this.optimal = optimal;
    }

    @Override
    public String toString() {
        return toCity.getName() + ";" + fromCity.getName() + ";" + distance + ";" + minutes;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Connection con = ((Connection) (obj));
        return (con.getToCity().getName().equals(toCity.getName())
                && con.getFromCity().getName().equals(fromCity.getName()))
                || (con.getToCity().getName().equals(fromCity.getName())
                && con.getFromCity().getName().equals(toCity.getName()));
    }

    /**
     * A factory method to be used when temporary connection is used...cus...you
     * know, 'new' is bad for such situations
     *
     * @param city1 The name of the first city that the connection
     * @param city2 The name of the second city that the connection
     * @return A connections
     */
    public static Connection getEmptyConnection(String city1, String city2) {
        return new Connection(new City(city1), new City(city2), 0, 0);
    }

}
