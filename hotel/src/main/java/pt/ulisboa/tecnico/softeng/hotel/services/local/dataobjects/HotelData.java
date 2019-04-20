package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelData {
	private String code;
	private String name;
	private String nif;
	private String iban;
	private Long priceSingle;
	private Long priceDouble;
	private List<RoomData> rooms;

	public HotelData() {
	}

	public HotelData(Hotel hotel) {
		this.code = hotel.getCode();
		this.name = hotel.getName();

		this.rooms = hotel.getRoomSet().stream().sorted(Comparator.comparing(Room::getNumber))
				.map(RoomData::new).collect(Collectors.toList());
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Double getPriceSingle() {
		Double d = Double.valueOf(this.priceSingle);
		return d / 1000;
	}

	public void setPriceSingle(Double priceSingle) {
		Long l = (Long)(priceSingle.longValue() * 1000);
		this.priceSingle = l;
	}

	public Double getPriceDouble() {
		Double d = Double.valueOf(this.priceDouble);
		return d / 1000;
	}

	public void setPriceDouble(Double priceDouble) {
		Long l = (Long)(priceDouble.longValue() * 1000);
		this.priceDouble = l;
	}

	public List<RoomData> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<RoomData> rooms) {
		this.rooms = rooms;
	}

}
