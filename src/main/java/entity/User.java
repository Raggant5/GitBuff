package entity;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Sets height of user in metres;
     */
    void setHeight(float height);

    /**
     * Returns the height of the user in metres.
     * @return the height of the user.
     */
    float getHeight();

    /**
     * Sets weight of user in kg;
     */
    void setWeight(float weight);

    /**
     * Returns the weight of the user in kg.
     * @return the weight of the user.
     */
    float getWeight();

    /**
     * Return the BMI of the user given weight and height.
     * @return the body mass index of the user.
     */
    default double getBMI() {
        if (this.getWeight() != 0.0f && this.getHeight() != 0.0f) {
            return this.getWeight() / Math.pow(this.getHeight(), 2);
        }
        return 0.0d;
    }

}
