import java.util.Scanner;

public class Main {
    private static WardManager manager = new WardManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = -1;

        do {
            System.out.println("---Hospital Admission System---");
            System.out.println("1. Register Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient Details");
            System.out.println("4. Delete Patient");
            System.out.println("5. Allocate Bed to Inpatient");
            System.out.println("6. Release Bed");
            System.out.println("7. Display Ward Bed Layout");
            System.out.println("8. Display All Patients");
            System.out.println("9. Generate Ward Occupancy Report");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    registerPatientUI();
                    break;
                case 2:
                    searchPatientUI();
                    break;
                case 3:
                    updatePatientUI();
                    break;
                case 4:
                    deletePatientUI();
                    break;
                case 5:
                    allocateBedUI();
                    break;
                case 6:
                    releaseBedUI();
                    break;
                case 7:
                    manager.displayWardLayout();
                    break;
                case 8:
                    displayPatientsUI();
                    break;
                case 9:
                    manager.generateWardReport();
                    break;
                case 0:
                    System.out.println("Exiting System... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
                    break;
            }
        } while (choice != 0);
    }

    private static void registerPatientUI() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter First Name: ");
        String fname = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lname = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Medical Condition: ");
        String condition = scanner.nextLine();

        System.out.println("Select Category: 1. INPATIENT  2. OUTPATIENT  3. EMERGENCY");
        int catChoice = Integer.parseInt(scanner.nextLine());

        Patient p;
        if (catChoice == 1) {
            p = new Inpatient(id, fname, lname, age, gender, condition, null);
        } else if (catChoice == 2) {
            p = new Patient(id, fname, lname, age, gender, condition, PatientCategory.OUTPATIENT);
        } else {
            p = new Patient(id, fname, lname, age, gender, condition, PatientCategory.EMERGENCY);
        }

        if (manager.registerPatient(p)) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Registration Failed: Patient ID already exists.");
        }
    }

    private static void searchPatientUI() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        Patient p = manager.findPatientById(id);
        if (p != null) {
            p.displayDetails();
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void updatePatientUI() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter New First Name: ");
        String fname = scanner.nextLine();
        System.out.print("Enter New Last Name: ");
        String lname = scanner.nextLine();
        System.out.print("Enter New Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter New Condition: ");
        String condition = scanner.nextLine();

        if (manager.updatePatient(id, fname, lname, age, condition)) {
            System.out.println("Patient updated successfully.");
        } else {
            System.out.println("Patient ID not found.");
        }
    }

    private static void deletePatientUI() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine();
        if (manager.deletePatient(id)) {
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Patient ID not found.");
        }
    }

    private static void allocateBedUI() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        manager.displayWardLayout();
        System.out.print("Enter Bed Code to Allocate (e.g., B01): ");
        String bedCode = scanner.nextLine();

        if (manager.allocateBed(id, bedCode)) {
            System.out.println("Bed allocated successfully.");
        }
    }

    private static void releaseBedUI() {
        System.out.print("Enter Patient ID to release bed: ");
        String id = scanner.nextLine();
        if (manager.releaseBedByPatientId(id)) {
            System.out.println("Bed released successfully.");
        } else {
            System.out.println("Release failed:Patient ID is invalid or has no bed assigned.");
        }
    }

    private static void displayPatientsUI() {
        manager.sortPatientsByName();
        System.out.println("---- ALL Registered Patients------");
        for (int i = 0; i < manager.getPatients().size(); i++) {
            manager.getPatients().get(i).displayDetails();
        }
    }
}