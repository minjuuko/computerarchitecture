package com.jnuhw.bcfirst.domain;

import com.jnuhw.bcfirst.background.BusSystem;

import java.util.Arrays;

public class Executor {

    public void execute() {
        BusSystem busSystem = BusSystem.getInstance();

        // System.out.println("Code Execute");
        int pc = 0;
        while (pc < Memory.MEMORY_SIZE) {
            pc = busSystem.getOutData(BusSystem.RegisterType.PC); // PC 레지스터의 데이터
            int memoryData = busSystem.getMemoryData(pc); // M[PC]의 데이터 ( Instruction )
            int instructionIdentifier = memoryData;
            boolean isMri = Instruction.isMriHexCode(instructionIdentifier);
            boolean isInDirect = Instruction.isIndirectHexaCode(instructionIdentifier);
            int oprand;
            if (isMri) {
                instructionIdentifier = Instruction.getInstructionHexaCodeFromMemoryHexaCode(memoryData);
                int oprandAddress = Instruction.getDataHexaCodeFromMemoryHexaCode(memoryData);
                oprand = busSystem.getMemoryData(oprandAddress);
            }

            int finalInstructionIdentifier = instructionIdentifier;
            Instruction instruction = Arrays.stream(Instruction.values()).filter(i -> i.getHexaCode() == finalInstructionIdentifier).findAny().get();
            instruction.setIsInDirect(isInDirect);
            switch (instruction) {
                // MRI Instruction
                case AND:
                    executeAND();
                    break;
                case ADD:
                    executeADD();
                    break;
                case LDA:
                    executeLDA();
                    break;
                case STA:
                    executeSTA();
                    break;
                case BUN:
                    executeBUN();
                    break;
                case BSA:
                    executeBSA();
                    break;
                case ISZ:
                    executeISZ();
                    break;

                // Register Instruction
                case CLA:
                    executeCLA();
                    break;
                case CLE:
                    executeCLE();
                    break;
                case CMA:
                    executeCMA();
                    break;
                case CME:
                    executeCME();
                    break;
                case CIR:
                    executeCIR();
                    break;
                case CIL:
                    executeCIL();
                    break;
                case INC:
                    executeINC();
                    break;
                case SPA:
                    executeSPA();
                    break;
                case SNA:
                    executeSNA();
                    break;
                case SZA:
                    executeSZA();
                    break;

                // IO Instruction
                case INP:
                    executeINP();
                    break;
                case OUT:
                    executeOUT();
                    break;
                case SKI:
                    executeSKI();
                    break;
                case SKO:
                    executeSKO();
                    break;
                case ION:
                    executeION();
                    break;
                case IOF:
                    executeIOF();
                    break;
                // Data
                default:
                    break;
            }

            // END 명령어 판별 필요?

            busSystem.increaseRegister(BusSystem.RegisterType.PC);
        }
    }

    private void executeAND() {

    }

    private void executeADD() {

    }

    private void executeLDA() {

    }

    private void executeSTA() {

    }

    private void executeBUN() {

    }

    private void executeBSA() {

    }

    private void executeISZ() {

    }

    private void executeCLA() {

    }

    private void executeCLE() {

    }

    private void executeCMA() {

    }

    private void executeCME() {

    }

    private void executeCIR() {

    }

    private void executeCIL() {

    }

    private void executeINC() {

    }

    private void executeSPA() {

    }

    private void executeSNA() {

    }

    private void executeSZA() {

    }

    private void executeINP() {

    }

    private void executeOUT() {

    }

    private void executeSKI() {

    }

    private void executeSKO() {

    }

    private void executeION() {

    }

    private void executeIOF() {

    }
}
