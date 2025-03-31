package ticket.booking.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket.booking.Entities.*;
import ticket.booking.util.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class UserBookingServices {

    List<User> userList;
    List<Ticket> ticketList;
    private final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";
    TrainServices trainServices;
    ObjectMapper obj;
    Scanner sc;

    public UserBookingServices() throws Exception {loadUsers();}

    public void signUp() throws Exception {
        sc = new Scanner(System.in);
        System.out.println("Welcome to Sign Up");
        System.out.println("Please enter the username");
        String nametoSignUp = sc.next();
        Optional<User> usersFound = userList.stream().filter((user1) -> user1.getName().equalsIgnoreCase(nametoSignUp)).findFirst();

        if (usersFound.isPresent()) {
            System.out.println("Username already taken");
        } else {
            System.out.println("Please enter the password");
            String passwordtoSignUp = sc.next();

            User us = new User(nametoSignUp, passwordtoSignUp, UserServiceUtil.hashPassword(passwordtoSignUp), UUID.randomUUID().toString(), new ArrayList<>());
            userList.add(us);
            saveToUsers();
            System.out.println("Username " + us.getName() + " is added");
        }
    }
    public void loginUser() throws Exception {
        sc= new Scanner(System.in);
        System.out.println("Welcome to Login");
        System.out.println("Please enter the username");
        String nametoLogin= sc.next();

        List<User> checkIfUserExists= userList.stream().filter((user1)-> user1.getName().equalsIgnoreCase(nametoLogin)).collect(Collectors.toList());

        if(checkIfUserExists.isEmpty()) {
            System.out.println("This user is not present. Do you wish to create one?");
            System.out.println("1. Yes");
            System.out.println("2. No, return to the home screen");
            int choice= sc.nextInt();

            switch (choice){
                case 1:
                    signUp();
                    break;
                default:
                    System.out.println("Didnt select the appropriate response.");
                    break;
            }
        }
        else{
                System.out.println("Please enter the password");
                String passwordtoLogin= sc.next();
                Optional<User> userLogin= userList.stream().filter((user1)->{return user1.getName().equalsIgnoreCase(nametoLogin) && user1.getPassword().equalsIgnoreCase(passwordtoLogin);}).findFirst();
                if(userLogin.isEmpty()){
                    System.out.println("Wrong Password, sending you back to login page");
                    loginUser();
                }
                String id= userLogin.get().getUserId();
                String name= userLogin.get().getName();

                System.out.println("Hello "+name);
                System.out.println("Your user ID is "+id);
                System.out.println("What do you want to do?");
                System.out.println("1. Fetch Bookings");
                System.out.println("2. Book a Seat");
                System.out.println("3. Cancel Booking");
                System.out.println("4. Return to Main Menu");

                int choice= sc.nextInt();
                switch (choice){
                    case 1:
                        fetchBookings(userLogin);
                        break;
                    case 2:
                        bookTickets(userLogin);
                        break;
                    case 3:
                        cancelTickets(userLogin);
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Didnt Select the appropriate response.");
                        break;
                }
        }
    }
    public void bookTickets(Optional<User> userFetched) throws IOException {
        sc = new Scanner(System.in);
        System.out.println("Please Enter the Source");
        String source = sc.next();
        System.out.println("Please Enter the Destination");
        String destination = sc.next();
        System.out.println("Please enter the date of travel");
        String date = sc.next();

        trainServices = new TrainServices();
        if (trainServices.validTrain(source, destination)) {

            Optional<Train> trainRequested = trainServices.trainList.stream().filter(train1 -> {
                return train1.getStationTimes().containsKey(source) && train1.getStationTimes().containsKey(destination);
            }).findFirst();
            Ticket ticket1 = new Ticket(UUID.randomUUID().toString(), userFetched.get().getUserId(), source, destination, date, new Train(trainRequested.get().getTrainId(), trainRequested.get().getTrainNo(), trainRequested.get().getSeats(), trainRequested.get().getStationTimes(), trainRequested.get().getStations()));

            ticketList = new ArrayList<>();
            ticketList.add(ticket1);
            if (userFetched.get().getTicketsBooked().isEmpty() == Boolean.FALSE) {
                for (Ticket i : userFetched.get().getTicketsBooked()) {
                    ticketList.add(i);
                }
            }
            userFetched.get().setTicketsBooked(ticketList);
            Iterator<User> it = userList.iterator();
            while (it.hasNext()) {
                User us = it.next();
                if (us.getUserId().equalsIgnoreCase(userFetched.get().getUserId())) {
                    it.remove();
                }
            }
            userList.add(userFetched.get());
            saveToUsers();
            System.out.println("Ticket has been booked");
        }
        else{
            System.out.println("Invalid Source or Destination");
        }
    }
    public void cancelTickets(Optional<User> userFetched) throws Exception {
        //fetchBookings(userFetched);
        System.out.println("Please Enter the ticket ID");
        sc= new Scanner(System.in);
        String ticketId= sc.next();

        Boolean isTicketCancelled=userFetched.get().getTicketsBooked().removeIf(ticket1 ->{return ticket1.getTicketId().equalsIgnoreCase(ticketId);});
        if(isTicketCancelled){
        saveToUsers();
        System.out.println("Your ticket has been Cancelled");
        }
        else{
            System.out.println("The Ticket with Ticket ID: "+ticketId+" could not be found.");
        }
    }
    public void fetchBookings(Optional<User> userFetched) throws Exception {
        if (userFetched.get().getTicketsBooked().isEmpty()==Boolean.FALSE) {
            userFetched.get().printTickets();
        }
        else {System.out.println("No Tickets Found");}
    }
    public void loadUsers() throws Exception{
        File users= new File(USERS_PATH);
        obj= new ObjectMapper();
        obj.enable(SerializationFeature.INDENT_OUTPUT);
        userList=obj.readValue(users, new TypeReference<List<User>>(){});
    }
    public void saveToUsers() throws IOException {
        File users= new File(USERS_PATH);
        obj= new ObjectMapper();
        obj.writeValue(users,userList);
    }
}