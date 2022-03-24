import java.util.Date;

/**
 * @author Anagh Mehran
 */
public class Sales {
    private String country;
    private String item_type;
    private char order_priority;
    private Date order_date;
    private long order_ID;
    private Date ship_date;
    private int units_sold;
    private float unit_price;
    private float unit_cost;
    private double revenue;
    private double total_cost;
    private double total_profit;

    public Sales(String country, String item_type, char order_priority, Date order_date, long order_ID,
                 Date ship_date, int units_sold, float unit_price, float unit_cost, double revenue, double total_cost
            , double total_profit) {
        this.country = country;
        this.item_type = item_type;
        this.order_priority = order_priority;
        this.order_date = order_date;
        this.order_ID = order_ID;
        this.ship_date = ship_date;
        this.units_sold = units_sold;
        this.unit_price = unit_price;
        this.unit_cost = unit_cost;
        this.revenue = revenue;
        this.total_cost = total_cost;
        this.total_profit = total_profit;
    }

    public Sales(String[] fields) {
        this(fields[0], fields[1], fields[2].charAt(0), new Date(fields[3]),
                Long.parseLong(fields[4]), new Date(fields[5]), Integer.parseInt(fields[6]),
                Float.parseFloat(fields[7]), Float.parseFloat(fields[8]), Double.parseDouble(fields[9]),
                Double.parseDouble(fields[10]), Double.parseDouble(fields[11]));
    }

    public boolean equals(Sales sale) {
        return (this.order_ID == sale.order_ID);
    }

    public boolean equals(long order_ID) {
        return (this.order_ID == order_ID);
    }

    @Override
    public String toString() {
        return country + '\t' +
                item_type + '\t' +
                order_priority + '\t' +
                dateConvert(order_date) + '\t' +
                order_ID + '\t' +
                dateConvert(ship_date) + '\t' +
                units_sold + '\t' +
                unit_price + '\t' +
                unit_cost + '\t' +
                revenue + '\t' +
                total_cost + '\t' +
                total_profit;
    }

    private String dateConvert(Date date) {
        return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
    }

    public long getOrder_ID() {
        return order_ID;
    }
}
