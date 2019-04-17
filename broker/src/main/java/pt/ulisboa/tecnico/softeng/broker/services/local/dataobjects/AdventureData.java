package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface.Type;

public class AdventureData {
	private String id;
	private Type roomType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	private Integer age;
	private String iban;
	private Long margin;
	private Boolean room;
	private Boolean vehicle;
	private Long amount;
	private Adventure.State state;

	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String activityConfirmation;
	private String activityCancellation;

	public AdventureData() {
	}

	public AdventureData(Adventure adventure) {
		this.id = adventure.getID();
		this.roomType = adventure.getRoomType();
		this.begin = adventure.getBegin();
		this.end = adventure.getEnd();
		this.age = adventure.getAge();
		this.iban = adventure.getIban();
		this.margin = adventure.getMargin();
		this.vehicle = adventure.getRentVehicle();
		this.room = adventure.getBookRoom();
		this.state = adventure.getState().getValue();

		this.paymentConfirmation = adventure.getPaymentConfirmation();
		this.paymentCancellation = adventure.getPaymentCancellation();
		this.roomConfirmation = adventure.getRoomConfirmation();
		this.roomCancellation = adventure.getRoomCancellation();
		this.activityConfirmation = adventure.getActivityConfirmation();
		this.activityCancellation = adventure.getActivityCancellation();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getRoomType() {
		return this.roomType;
	}

	public void setRoomType(Type roomType) {
		this.roomType = roomType;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Double getAmount() {
		Double d = Double.valueOf(this.amount);
		return d / 1000;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Adventure.State getState() {
		return this.state;
	}

	public void setState(Adventure.State state) {
		this.state = state;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public void setPaymentConfirmation(String paymentConfirmation) {
		this.paymentConfirmation = paymentConfirmation;
	}

	public String getPaymentCancellation() {
		return this.paymentCancellation;
	}

	public void setPaymentCancellation(String paymentCancellation) {
		this.paymentCancellation = paymentCancellation;
	}

	public String getRoomConfirmation() {
		return this.roomConfirmation;
	}

	public void setRoomConfirmation(String roomConfirmation) {
		this.roomConfirmation = roomConfirmation;
	}

	public String getRoomCancellation() {
		return this.roomCancellation;
	}

	public void setRoomCancellation(String roomCancellation) {
		this.roomCancellation = roomCancellation;
	}

	public String getActivityConfirmation() {
		return this.activityConfirmation;
	}

	public void setActivityConfirmation(String activityConfirmation) {
		this.activityConfirmation = activityConfirmation;
	}

	public String getActivityCancellation() {
		return this.activityCancellation;
	}

	public void setActivityCancellation(String activityCancellation) {
		this.activityCancellation = activityCancellation;
	}

	public Double getMargin() {
		Double d = Double.valueOf(this.margin);
		return d / 1000;
	}

	public void setMargin(Long margin) {
		this.margin = margin;
	}

	public Boolean getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Boolean vehicle) {
		this.vehicle = vehicle;
	}

	public Boolean getRoom() {
		return this.room;
	}

	public void setRoom(Boolean room) {
		this.room = room;
	}

}
