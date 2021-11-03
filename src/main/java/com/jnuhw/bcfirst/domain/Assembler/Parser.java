package com.jnuhw.bcfirst.domain.Assembler;

import com.jnuhw.bcfirst.UnknownInstructionException;
import com.jnuhw.bcfirst.domain.Cpu.CPUEngine;
import com.jnuhw.bcfirst.view.OutputView;

import java.util.*;

public class Parser {

    private LcCounter lcCounter = new LcCounter();
    private List<Label> addressLabelTable = new ArrayList<>();

    public int getStartLc(String firstLine) {
        List<String> args = Arrays.asList(firstLine.split(" "));
        if (args.get(0).toLowerCase(Locale.ROOT).equals("ORG")) {
            return Integer.parseInt(args.get(1));
        }

        return 1;
    }

    public void parseFirstPass(List<String> program) throws UnknownInstructionException {
        for (String command : program) {
            List<String> args = Arrays.asList(command.split(" "));
            String instruction = args.get(0);

            if (isLabelInstruction(instruction)) {
                addSymbolTable(args);
            }

            if (instruction.equals("ORG")) {
                executeORG(Integer.parseInt(args.get(1)));
                continue;
            }

            lcCounter.increaseLc();
        }
    }

    private boolean isPseudoInstruction(String instruction) {
        switch (instruction.toUpperCase(Locale.ROOT)) {
            case "ORG":
            case "END":
            case "DEC":
            case "HEX":
                return true;

            default:
                return false;
        }
    }

    private boolean isLabelInstruction(String command) {
        return command.contains(",");
    }

    private void addSymbolTable(List<String> args) {
        String label = args.get(0);
        label = label.substring(0, label.length() - 1);
        String data = args.get(2);

        if (args.get(1).equals("HEX"))
            addressLabelTable.add(new Label(label, lcCounter.getCurrentLc(), Integer.parseInt(data, 16)));
        else
            addressLabelTable.add(new Label(label, lcCounter.getCurrentLc(), Integer.parseInt(data)));

    }



    private void executeORG(int location) {
        lcCounter.setLc(location);
    }

    public void parseSecondPass(List<String> program) throws UnknownInstructionException {
        lcCounter.resetLc();

        for (String command : program) {
            List<String> args = Arrays.asList(command.split(" "));
            String instruction = args.get(0);

            if(isLabelInstruction(instruction))
                instruction = args.get(1);

            if (isPseudoInstruction(instruction)) {
                executePseudoInstruction(args);
                if (instruction.equals("ORG")) continue;
            } else {
                try {
                    executeNonPseudoInstruction(args);
                } catch (IllegalArgumentException e) {
                    OutputView.printUnknownInstructionError(lcCounter.getCurrentLc(), command);
                    throw new UnknownInstructionException();
                }
            }

            lcCounter.increaseLc();
        }
    }

    private void executePseudoInstruction(List<String> args) {
        String instruction = args.get(0);

        switch (instruction) {
            case "END":
                return;
            case "ORG":
                executeORG(Integer.parseInt(args.get(1)));
                return;

        }

        if (isLabelInstruction(instruction)) {
            String labelName = instruction.substring(0, instruction.length()-1);
            Label label = getLabelByName(labelName);
            // @deprecated
            CPUEngine.getInstance().initializeMemoryData(lcCounter.getCurrentLc(), true, label.getData());
        }
    }

    private void executeNonPseudoInstruction(List<String> args) throws IllegalArgumentException {
        boolean isIndirect = false;
        if (args.size() == 3 && args.get(2).equals("I")) {
            isIndirect = true;
        }

        Instruction instruction = Instruction.valueOf(args.get(0));
        instruction.setIsInDirect(isIndirect);
        int instructionHexCode = instruction.getHexaCode();
        if (instruction.isMri()) {
            instructionHexCode += getLabelByName(args.get(1)).getLc();
        }

        // @deprecated
        CPUEngine.getInstance().initializeMemoryData(lcCounter.getCurrentLc(), false, instructionHexCode);
    }

    private Label getLabelByName(String name) {
        return addressLabelTable.stream()
                .filter(l -> l.getName().equals(name))
                .findAny().get();
    }
}