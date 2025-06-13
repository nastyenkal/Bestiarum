package org.example.model;

public class Monstr {
    private String name;
    private String full_info;
    private String description;
    private String habitat;
    private String first_mention;
    private String immunities;
    private Potion potion;
    private String physical_characteristics;
    private String additional_info;
    //private String sourceType;

    // Геттеры и сеттеры для всех полей
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_info() {
        return full_info;
    }

    public void setFull_info(String full_info) {
        this.full_info = full_info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getFirst_mention() {
        return first_mention;
    }

    public void setFirst_mention(String first_mention) {
        this.first_mention = first_mention;
    }

    public String getImmunities() {
        return immunities;
    }

    public void setImmunities(String immunities) {
        this.immunities = immunities;
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    public String getPhysical_characteristics() {
        return physical_characteristics;
    }

    public void setPhysical_characteristics(String physical_characteristics) {
        this.physical_characteristics = physical_characteristics;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

//    public String getSourceType() {
//        return sourceType;
//    }

//    public void setSourceType(String sourceType) {
//        this.sourceType = sourceType;
//    }

    @Override
    public String toString() {
        //return name + " (" + sourceType + ")";
        return name;
    }
}