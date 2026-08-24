public class Inpatient extends Patient {
    private String bedId;

    //constructor with super()
    public Inpatient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition, String bedId) {
        super(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.bedId = bedId;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    //method extending super functiion
    @Override
    public void displayDetails() {
        super.displayDetails();
        if (bedId == null) {
            System.out.println("   [Inpatient Info] Assigned Bed: None");
        } else {
            System.out.println("   [Inpatient Info] Assigned Bed: " + bedId);
        }
    }
}