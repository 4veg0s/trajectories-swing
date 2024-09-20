package etu.nic.git;

import etu.nic.git.trajectories_swing.ApplicationAssembler;

/**
 * Assemble and show application
 *
 */
public class App {
    public static void main( String[] args )
    {
        ApplicationAssembler assembler = new ApplicationAssembler();
        assembler.assemble();
        assembler.showGUI();
    }
}
