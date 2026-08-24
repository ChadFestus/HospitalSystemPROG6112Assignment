import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HospitalSystemsTest {
    private WardManager manager;

    // Runs before every single test to give a fresh WardManager instance
    @BeforeEach
    public void setUp() {
        manager = new WardManager();
    }


    @Test
    public void testRegisterPatient() {
        Patient p = new Patient("P01", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        boolean success = manager.registerPatient(p);

        assertTrue(success, "Patient registration should return true for a new ID");
        assertNotNull(manager.findPatientById("P01"), "Registered patient should be found in system");
    }

    @Test
    public void testSearchPatient() {
        Patient p = new Patient("P02", "Jane", "Smith", 25, "Female", "Fever", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        Patient found = manager.findPatientById("P02");
        assertNotNull(found, "Patient should be found");
        assertEquals("Jane", found.getFirstName(), "First name should match search result");
    }

    @Test
    public void testUpdatePatient() {
        Patient p = new Patient("P03", "Mark", "Taylor", 45, "Male", "Cough", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        boolean updated = manager.updatePatient("P03", "Marcus", "Taylor", 46, "Recovered");
        assertTrue(updated, "Update should succeed for existing patient");

        Patient check = manager.findPatientById("P03");
        assertEquals("Marcus", check.getFirstName(), "Updated name should reflect change");
        assertEquals(46, check.getAge(), "Updated age should reflect change");
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P04", "Sarah", "Connor", 35, "Female", "Observation", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        boolean deleted = manager.deletePatient("P04");
        assertTrue(deleted, "Delete should return true for valid patient");
        assertNull(manager.findPatientById("P04"), "Deleted patient should no longer exist");
    }

 //bed management
    @Test
    public void testAllocateBedToInpatient() {
        Inpatient inp = new Inpatient("P05", "Alice", "Brown", 50, "Female", "Surgery", null);
        manager.registerPatient(inp);

        boolean allocated = manager.allocateBed("P05", "B01");
        assertTrue(allocated, "Bed B01 should be successfully allocated to Inpatient");
        assertEquals(1, manager.getOccupiedBedCount(), "Occupied bed count should increase to 1");
    }

    @Test
    public void testReleaseBed() {
        Inpatient inp = new Inpatient("P06", "Bob", "White", 60, "Male", "Recovery", null);
        manager.registerPatient(inp);
        manager.allocateBed("P06", "B02");

        boolean released = manager.releaseBedByPatientId("P06");
        assertTrue(released, "Bed release should be successful");
        assertEquals(0, manager.getOccupiedBedCount(), "Occupied bed count should return to 0");
    }

    @Test
    public void testOccupancyCalculation() {
        Inpatient inp1 = new Inpatient("P07", "Tom", "Green", 40, "Male", "Surgery", null);
        Inpatient inp2 = new Inpatient("P08", "Lucy", "Grey", 28, "Female", "Observation", null);
        manager.registerPatient(inp1);
        manager.registerPatient(inp2);

        manager.allocateBed("P07", "B01");
        manager.allocateBed("P08", "B02");

        assertEquals(2, manager.getOccupiedBedCount(), "Total occupied beds should be 2");
    }

    // ==========================================
    // 3. VALIDATION & BOUNDARY TESTS (5 Marks)
    // ==========================================

    @Test
    public void testPreventDuplicatePatientID() {
        Patient p1 = new Patient("P10", "Sam", "Wilson", 30, "Male", "Checkup", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P10", "Duplicate", "User", 20, "Female", "Flu", PatientCategory.OUTPATIENT);

        manager.registerPatient(p1);
        boolean duplicateResult = manager.registerPatient(p2);

        assertFalse(duplicateResult, "System should prevent registering a duplicate Patient ID");
    }

    @Test
    public void testPreventBedAllocationToOutpatient() {
        Patient outpatient = new Patient("P11", "David", "Miller", 32, "Male", "Checkup", PatientCategory.OUTPATIENT);
        manager.registerPatient(outpatient);

        boolean allocated = manager.allocateBed("P11", "B01");
        assertFalse(allocated, "System should reject bed allocation for non-inpatients");
    }

    @Test
    public void testPreventDoubleBedAllocation() {
        Inpatient inp1 = new Inpatient("P12", "Evelyn", "Hall", 22, "Female", "Checkup", null);
        Inpatient inp2 = new Inpatient("P13", "Frank", "Wright", 65, "Male", "Observation", null);
        manager.registerPatient(inp1);
        manager.registerPatient(inp2);

        manager.allocateBed("P12", "B05");
        boolean doubleBook = manager.allocateBed("P13", "B05");

        assertFalse(doubleBook, "System should prevent allocating an already occupied bed");
    }

    @Test
    public void testPatientSortingByName() {
        Patient p1 = new Patient("P14", "Zack", "Zulu", 30, "Male", "Checkup", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P15", "Adam", "Alpha", 25, "Male", "Checkup", PatientCategory.OUTPATIENT);

        manager.registerPatient(p1);
        manager.registerPatient(p2);

        manager.sortPatientsByName();

        //sort by lastname
        assertEquals("Alpha", manager.getPatients().get(0).getLastName(), "First sorted patient should be Alpha");
        assertEquals("Zulu", manager.getPatients().get(1).getLastName(), "Second sorted patient should be Zulu");
    }
}