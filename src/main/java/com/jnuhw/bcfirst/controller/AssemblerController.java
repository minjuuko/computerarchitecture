package com.jnuhw.bcfirst.controller;

import com.jnuhw.bcfirst.UnknownInstructionException;
import com.jnuhw.bcfirst.background.BusSystem;
import com.jnuhw.bcfirst.domain.Assembler;
import com.jnuhw.bcfirst.domain.Executor;
import com.jnuhw.bcfirst.view.OutputView;

import java.util.List;

public class AssemblerController {

    Assembler assembler = new Assembler();
    Executor executor = Executor.getInstance();

    public void run(List<String> program) {
        try {
            assembler.parseFirstPass(program);
            assembler.parseSecondPass(program);
            executor.execute();
        } catch (UnknownInstructionException exception) {
            OutputView.printErrorAnnouncement();
        }


        // (디버깅용) 정상적으로 프로그램이 실행 됬는지, Memory 주소 006 확인
        printMemoryByAddress(0x0000);
        printMemoryByAddress(0x0001);
        printMemoryByAddress(0x0002);
        printMemoryByAddress(0x0003);
        printMemoryByAddress(0x0004);
        printMemoryByAddress(0x0005);
        printMemoryByAddress(0x0006);
        //
    }

    public void printMemoryByAddress(int address) {
        OutputView.printMemory(address, BusSystem.getInstance().getMemoryData(address));
    }
}
