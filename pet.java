public class pet {
    private int id;
    private String name, breed, status;

    public pet(int id, String name, String breed, String status) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public String getStatus() { return status; }
}