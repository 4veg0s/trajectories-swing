package etu.nic.git.trajectories_swing.display_components;

/**
 * Сущность, которая может возвращать состояние по умолчанию
 */
public interface Restorable {
    /**
     * Возвращает сущность в первоначальное состояние
     */
    void restoreDefaultState();
}
