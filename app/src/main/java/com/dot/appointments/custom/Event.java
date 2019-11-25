package com.dot.appointments.custom;

public class Event {
	private int mStartTimeInMinutes;
	private int mEndTimeInMinutes;
	private String mName;
	private String mStartTime;
	private String mEndTime;
	private int mId;

	public Event(int id, String mName, String mStartTime, String mEndTime) {
		this.mId = id;
		this.mName = mName;

		this.mStartTime = mStartTime; //Standard in minutes
		this.mEndTime = mEndTime;

		this.mStartTimeInMinutes = convertToMinutes(mStartTime); //9AM to 9PM
		this.mEndTimeInMinutes = convertToMinutes(mEndTime);
	}

	public String getName() {
		return mName;
	}

	public int getStartTimeInMinutes() {
		return mStartTimeInMinutes;
	}

	public int getEndTimeInMinutes() {
		return mEndTimeInMinutes;
	}

	public int getStartTimeInt() {
		return Integer.parseInt(mStartTime);
	}

	public int getEndTimeInt() {
		return Integer.parseInt(mEndTime);
	}

	public String getStartTime() {
		return mStartTime;
	}

	public String getEndTime() {
		return mEndTime;
	}

	private int convertToMinutes(String string){//taking 9AM as 0
		int a = Integer.parseInt(string);
		a = a - 900; //Starting time
		int hour = Integer.parseInt((string.charAt(0)+""+string.charAt(1)));
		a = a - (40 *(hour - 9));
		return  a;
	}

	public int getId() {
		return mId;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Event event = (Event) o;

		return mId == event.mId;
	}

	@Override
	public int hashCode() {
		return mId;
	}
}
