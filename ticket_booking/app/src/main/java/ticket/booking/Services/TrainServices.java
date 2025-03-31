package ticket.booking.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket.booking.Entities.*;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class TrainServices{

    Train train;
    List<Train> trainList;
    ObjectMapper obj;
    private final String TRAINS_PATH="app/src/main/java/ticket/booking/localDb/trains.json";


    public TrainServices() throws IOException {loadTrains();}

    public TrainServices(Train train) throws IOException {
        this.train= train;
        loadTrains();
    }


    public void loadTrains() throws IOException {
        File trains= new File(TRAINS_PATH);
        obj= new ObjectMapper();
        obj.enable(SerializationFeature.INDENT_OUTPUT);
        trainList=obj.readValue(trains, new TypeReference<List<Train>>(){});
    }

    public void searchTrains(String source, String destination){
        validTrain(source,destination);

        //List<Train> tr=trainList.stream().filter(train1 -> {return train1.getStations().contains(source) && train1.getStations().contains(destination);}).collect(Collectors.toList());
        List<Train> tr= trainList.stream().filter(train1 -> {return train1.getStationTimes().containsKey(source) && train1.getStationTimes().containsKey(destination);}).collect(Collectors.toList());

        if(tr.isEmpty()){
            System.out.println("No Trains Available");
        }
        else {
            for (Train i : tr) {
                System.out.println(i.getTrainInfo());
                System.out.println("Train will be going through the following stations.");
                System.out.println(i.getStationTimes());
                System.out.println("Seats");
                System.out.println(i.getSeats());

            }
        }
    }

    public void validTrain(String source, String destination){

        int sourceIndex=-1;
        int destinationIndex=-1;


        for(Train i: trainList){
             sourceIndex=i.getStations().indexOf(source);
             destinationIndex=i.getStations().indexOf(destination);
        }

        if(sourceIndex==-1 || destinationIndex==-1 || sourceIndex>=destinationIndex){
            System.out.println("Source or Destination is invalid");
        }
        }



    }

