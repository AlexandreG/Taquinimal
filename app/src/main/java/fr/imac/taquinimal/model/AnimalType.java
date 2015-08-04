package fr.imac.taquinimal.model;

/**
 * Created by AG on 18/07/2015.
 */
public enum AnimalType {
    CROCO(1),
    BEAR(2),
    CAT(3),
    OWL(4),
    SNAKE(5);

    private int code;

    AnimalType(int code) {
        this.code = code;
    }

    public static AnimalType getAnimalFromCode(int code) {
        for (AnimalType e : AnimalType.values()) {
            if (code == e.code) return e;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    /**
     * Specify if I can kill the given animal
     *
     * @param typeToEat animal to eat
     * @return true if killable, false else
     */
    public boolean canIEatThis(AnimalType typeToEat) {
        if (this.code == AnimalType.values().length && typeToEat.getCode() == 1) {
            return true;
        } else if (this.code == typeToEat.getCode() - 1) {
            return true;
        }
        return false;
    }
}
