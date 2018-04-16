package org.Amruta.FindingHotel_ShoppingMalls;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class FindClient {
	
public static void main (String args[]){
		
		System.out.println("Enter the city name where you want to find hotels");
		Scanner scan=new Scanner(System.in);
		String location=scan.nextLine();		
		location = location.replaceAll(" ", "");
		double result[]=findlatitudelogitude(location);
		double latitude=result[0];
		double longitude=result[1];
		
		String hotelname=findhotelsnearby(latitude, longitude, location);
		
		double hotel_result[]=findlatitudelogitude(hotelname);
		double hotel_latitude=hotel_result[0];
		double hotel_longitude=hotel_result[1];
		
		System.out.println("----------------------------------------------------------");
		System.out.println("Shopping malls near "+hotelname+" hotel");
		System.out.println("----------------------------------------------------------");

		Shoppingmallsnearby(hotel_latitude,hotel_longitude);
		
	}
	
	public static double[] findlatitudelogitude(String location){
		
		String googlekey="AIzaSyDKxktrXm6fxd3JwP1tQXXxYR-hXigOqI8";
		String locationURL="https://maps.googleapis.com/maps/api/geocode/json?";
		
		Client locclient=ClientBuilder.newClient();
		WebTarget loctarget=locclient.target(locationURL+"address="+location+"&key="+googlekey);
		String JSONOutput=loctarget.request(MediaType.APPLICATION_JSON).get(String.class);
					
		StringReader in = new StringReader(JSONOutput);
		JsonReader jsonReader = Json.createReader(in);
		JsonObject jsonObject = jsonReader.readObject();
		
		JsonObject locationObject = jsonObject.getJsonArray("results").getJsonObject(0).getJsonObject("geometry").getJsonObject("location");
		 
		JsonValue jsonlat=locationObject.get("lat");		
		JsonValue jsonlong=locationObject.get("lng");
		
		double latVal= Double.parseDouble(jsonlat.toString());
		double lngVal= Double.parseDouble(jsonlong.toString());
		
		return new double[]{latVal,lngVal};
		
	}
	
	public static  String findhotelsnearby(double latitude,double longitude,String location){
		
		String hotelkey="AIzaSyCtZzd7LRm5fyjtYefcZ1RlT1hAQPXJNy4";
		String hotelURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

		Client nearbyplacesclient=ClientBuilder.newClient();
		HashMap<Integer, String> hmap = new HashMap<Integer, String>();

		WebTarget nearbyplacestarget=nearbyplacesclient.target(hotelURL+"location="+latitude+","+longitude+"&radius=800&type=lodging&keyword=Inn&key="+hotelkey);
		
		String nearbyplacesOutput =nearbyplacestarget.request(MediaType.APPLICATION_JSON).get(String.class);
		
		
		StringReader inplaces = new StringReader(nearbyplacesOutput);
		JsonReader jsonReaderplaces = Json.createReader(inplaces);
		JsonObject jsonObjectplaces = jsonReaderplaces.readObject();
		
		JsonArray array=jsonObjectplaces.getJsonArray("results");
		System.out.println("-----------------------------");
		System.out.println("Hotels Near by "+location);
		System.out.println("-----------------------------");
		for (int i = 0; i <array.size(); i++) {
			JsonObject nearbyplacesObject1=array.getJsonObject(i);
			System.out.println(i+1+" "+nearbyplacesObject1.getString("name"));  
			hmap.put(i+1, nearbyplacesObject1.getString("name"));
			}
		
		System.out.println("Choose hotel(Integer):");
		Scanner intscan=new Scanner(System.in);
		
		int index=intscan.nextInt();
		String hotelname=hmap.get(index);
		hotelname = hotelname.replaceAll(" ", "");	
		return hotelname;

	}
	
	public static void Shoppingmallsnearby(double hotel_latitude,double hotel_longitude){
		
		String foursquareURL="https://api.foursquare.com/v2/venues/search?";
		String client_id="YHSTJSMDAMELJ4QBSDC2Z1HQF5LMFHI5HW1NLL5NGXJOX1BF";
		String secret_id="AMPVYWRVTF5TI41GFWO52CGW2RV3W2UYDSTSJSPML0GPYA0H";
		
		Client shoppingclient=ClientBuilder.newClient();
		WebTarget shoppingtarget=shoppingclient.target(foursquareURL+"v=20161016&ll="+hotel_latitude+","+hotel_longitude+"&query=shopping&client_id="+client_id+"&client_secret="+secret_id);
		String shoppingOutput =shoppingtarget.request(MediaType.APPLICATION_JSON).get(String.class);
		
		StringReader inshopping = new StringReader(shoppingOutput);
		JsonReader jsonReadershopping = Json.createReader(inshopping);
		JsonObject jsonObjectshopping = jsonReadershopping.readObject();
		
		JsonArray shoppingarray=jsonObjectshopping.getJsonObject("response").getJsonArray("venues");
		
		for (int i = 0; i < shoppingarray.size(); i++) {
			JsonObject shoppingObject1=shoppingarray.getJsonObject(i);
			System.out.println((i+1)+" "+shoppingObject1.getString("name"));  
			  
			}
	}

}
