public class adopter {
    private int adopterId;
    private String name, phone, address;

    public adopter(int adopterId, String name, String phone, String address) {
        this.adopterId = adopterId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getAdopterId() { return adopterId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}