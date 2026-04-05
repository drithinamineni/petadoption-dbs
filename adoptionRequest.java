public class adoptionRequest {
    private int requestId, petId, adopterId;
    private String status;

    public adoptionRequest(int requestId, int petId, int adopterId, String status) {
        this.requestId = requestId;
        this.petId = petId;
        this.adopterId = adopterId;
        this.status = status;
    }

    public int getRequestId() { return requestId; }
    public int getPetId() { return petId; }
    public int getAdopterId() { return adopterId; }
    public String getStatus() { return status; }
}