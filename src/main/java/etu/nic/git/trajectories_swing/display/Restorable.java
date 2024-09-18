package etu.nic.git.trajectories_swing.display;

/**
 * Сущность, которая может возвращать состояние по умолчанию
 */
public interface Restorable {
    /**
     * Возвращает сущность в первоначальное состояние
     */
    void restoreDefaultState();
}
