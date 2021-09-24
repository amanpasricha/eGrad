package com.egrad;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class StudentDataModel implements Parcelable {
    public static final Creator<StudentDataModel> CREATOR = new Creator<StudentDataModel>() {
        @Override
        public StudentDataModel createFromParcel(Parcel in) {
            return new StudentDataModel(in);
        }

        @Override
        public StudentDataModel[] newArray(int size) {
            return new StudentDataModel[size];
        }
    };
    private long registrationNum;
    private String studentName;
    private String emailAddress;
    private String password;
    private Timestamp dateOfBirth;
    private String fathersName;
    private String mothersName;
    private String bloodGroup;
    private String state;
    private String city;
    private long mobileNumber;
    private long passingYear;


    public StudentDataModel() {
        // Empty constructor required by firestore
    }

    public StudentDataModel(long registrationNum, String studentName, String emailAddress, String password,
                            Timestamp dateOfBirth, String fathersName, String mothersName, String bloodGroup,
                            String state, String city, long mobileNumber, long passingYear) {

        this.registrationNum = registrationNum;
        this.studentName = studentName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.fathersName = fathersName;
        this.mothersName = mothersName;
        this.bloodGroup = bloodGroup;
        this.state = state;
        this.city = city;
        this.mobileNumber = mobileNumber;
        this.passingYear = passingYear;
    }

    protected StudentDataModel(Parcel in) {
        registrationNum = in.readLong();
        studentName = in.readString();
        emailAddress = in.readString();
        password = in.readString();
        dateOfBirth = in.readParcelable(Timestamp.class.getClassLoader());
        fathersName = in.readString();
        mothersName = in.readString();
        bloodGroup = in.readString();
        state = in.readString();
        city = in.readString();
        mobileNumber = in.readLong();
        passingYear = in.readLong();
    }

    public long getRegistrationNum() {
        return registrationNum;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFathersName() {
        return fathersName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getMothersName() {
        return mothersName;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public long getPassingYear() {
        return passingYear;
    }

    @NonNull
    @Override
    public String toString() {
        return studentName + " : " + registrationNum + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(registrationNum);
        dest.writeString(studentName);
        dest.writeString(emailAddress);
        dest.writeString(password);
        dest.writeParcelable(dateOfBirth, flags);
        dest.writeString(fathersName);
        dest.writeString(mothersName);
        dest.writeString(bloodGroup);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeLong(mobileNumber);
        dest.writeLong(passingYear);
    }
}
