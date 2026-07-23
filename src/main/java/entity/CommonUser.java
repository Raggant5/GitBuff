package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private float weight;
    private float height;

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.height = 0.0f;
        this.weight = 0.0f;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setWeight(float weight) { this.weight = weight; }

    @Override
    public float getWeight() { return this.weight; }

    @Override
    public void setHeight(float height) { this.height = height; }

    @Override
    public float getHeight() { return this.height; }


}
