public class medicalRecord {
    private int recordId;
    private int petId;
    private String treatment;
    private String treatmentDate;

    public medicalRecord(int recordId, int petId, String treatment, String treatmentDate) {
        this.recordId = recordId;
        this.petId = petId;
        this.treatment = treatment;
        this.treatmentDate = treatmentDate;
    }

    public int getRecordId() { return recordId; }
    public int getPetId() { return petId; }
    public String getTreatment() { return treatment; }
    public String getTreatmentDate() { return treatmentDate; }
}