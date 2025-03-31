package ticket.booking.Entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.*;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)

public class User {

    private String name;
    private String password;
    private String hashedPassword;
    private String userId;
    private List<Ticket> ticketsBooked;

    public User(String name,String password,String hashedPassword,String userId,List<Ticket> ticketsBooked){
        this.name=name;
        this.password=password;
        this.hashedPassword= hashedPassword;
        this.userId=userId;
        this.ticketsBooked= ticketsBooked;


    }
    public User(){}

    public void printTickets(){
        for(Ticket t: ticketsBooked){
            System.out.println(t.getTicketInfo());
        }
    }



    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public String getUserId(){
        return userId;
    }


    public void setName(String name){
        this.name=name;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




}
