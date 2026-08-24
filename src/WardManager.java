import java.util.ArrayList;

public class WardManager {
    private ArrayList<Patient> patients = new ArrayList<Patient>();
    private String[][] bedLayout = new String[4][5]; // 4x5 2D array for 20 beds

    public WardManager() {
        initializeBeds();
    }

    // Fills the 2D array with B01, B02 ... B20
    private void initializeBeds() {
        int count = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (count < 10) {
                    bedLayout[i][j] = "B0" + count;
                } else {
                    bedLayout[i][j] = "B" + count;
                }
                count++;
            }
        }
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public String[][] getBedLayout() {
        return bedLayout;
    }

    // Register patient and check for duplicate IDs
    public boolean registerPatient(Patient patient) {
        if (findPatientById(patient.getPatientId()) != null) {
            return false; // ID already exists
        }
        patients.add(patient);
        return true;
    }

    // Search patient by ID using a loop
    public Patient findPatientById(String id) {
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            if (p.getPatientId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // Update existing patient details
    public boolean updatePatient(String id, String firstName, String lastName, int age, String condition) {
        Patient p = findPatientById(id);
        if (p != null) {
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setAge(age);
            p.setMedicalCondition(condition);
            return true;
        }
        return false;
    }

    // Delete patient record
    public boolean deletePatient(String id) {
        Patient p = findPatientById(id);
        if (p != null) {
            if (p instanceof Inpatient) {
                releaseBedByPatientId(id);
            }
            patients.remove(p);
            return true;
        }
        return false;
    }

    // Allocate a bed to an inpatient
    public boolean allocateBed(String patientId, String bedCode) {
        Patient p = findPatientById(patientId);
        if (p == null) {
            System.out.println("Error: Patient not found.");
            return false;
        }

        if (p.getCategory() != PatientCategory.INPATIENT) {
            System.out.println("Error: Only INPATIENTS can be allocated a bed.");
            return false;
        }

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 5; c++) {
                if (bedLayout[r][c].equalsIgnoreCase(bedCode)) {
                    if (bedLayout[r][c].startsWith("[X]")) {
                        System.out.println("Error: Bed is already occupied.");
                        return false;
                    }
                    bedLayout[r][c] = "[X]" + bedCode;
                    Inpatient inp = (Inpatient) p;
                    inp.setBedId(bedCode);
                    return true;
                }
            }
        }
        System.out.println("Error: Bed ID not found.");
        return false;
    }

    // Release bed assigned to an inpatient
    public boolean releaseBedByPatientId(String patientId) {
        Patient p = findPatientById(patientId);
        if (p != null && p instanceof Inpatient) {
            Inpatient inp = (Inpatient) p;
            String bedCode = inp.getBedId();
            if (bedCode != null) {
                for (int r = 0; r < 4; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (bedLayout[r][c].equalsIgnoreCase("[X]" + bedCode)) {
                            bedLayout[r][c] = bedCode;
                            inp.setBedId(null);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Count occupied beds
    public int getOccupiedBedCount() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (bedLayout[i][j].startsWith("[X]")) {
                    count++;
                }
            }
        }
        return count;
    }

    //show ward
    public void displayWardLayout() {
        System.out.println(" Ward Bed Layout (4x5) ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(bedLayout[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //sort in alphebetical order
    public void sortPatientsByName() {
        for (int i = 0; i < patients.size() - 1; i++) {
            for (int j = 0; j < patients.size() - i - 1; j++) {
                String lastName1 = patients.get(j).getLastName();
                String lastName2 = patients.get(j + 1).getLastName();
                if (lastName1.compareToIgnoreCase(lastName2) > 0) {
                    Patient temp = patients.get(j);
                    patients.set(j, patients.get(j + 1));
                    patients.set(j + 1, temp);
                }
            }
        }
    }
    public void generateWardReport() {
        int totalBeds = 20;
        int occupied = getOccupiedBedCount();
        int available = totalBeds - occupied;
        double percentage = ((double) occupied / totalBeds) * 100;

        System.out.println("-----------------------------");
        System.out.println("Ward Occupancy Report");
        System.out.println("----------------------------------");
        System.out.println("Total Registered Patients : " + patients.size());
        System.out.println("Total Beds                : " + totalBeds);
        System.out.println("Occupied Beds             : " + occupied);
        System.out.println("Available Beds            : " + available);
        System.out.println("Bed Occupancy Rate        : " + percentage + "%");
        System.out.println("---------------------------------------------------");
    }
}