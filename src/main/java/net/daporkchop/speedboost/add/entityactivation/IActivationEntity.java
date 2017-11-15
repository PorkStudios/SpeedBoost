package net.daporkchop.speedboost.add.entityactivation;

public interface IActivationEntity {
    void inactiveTick();

    byte getActivationType();

    long getActivatedTick();

    void setActivatedTick(long tick);

    boolean defaultActivationState();
}
