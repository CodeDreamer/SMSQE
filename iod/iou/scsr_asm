; IO Utilities Scan Slave Blocks  V1.00    1988   Tony Tebby QJUMP

        section iou

        xdef    iou_scsr

        include 'dev8_keys_sys'
        include 'dev8_keys_sbt'
;+++
; IO Utilities scan slave blocks in range
;
;       d0 cr   drive ID / status byte (less ID and file bit) passed to action
;       a0 c  p lower end of slave block range
;       a1 c  p upper end of slave block range
;       a1      passed as pointer to slave block table
;       a2 c  p pointer to action routine / passed as pointer to slave block 
;       all other registers except d7 passed
;
;       error status as returned from action routine
;---
iou_scsr
ios.reg  reg    d7/a0/a1/a2
ios_act  equ    $0c
ios.wrk  reg    a0/a1/a2
ios.wrkl equ    $0c

        movem.l ios.reg,-(sp)
        move.b  d0,d7
        lsl.b   #4,d7                    ; drive id in msnibble of lsbyte
        bset    #sbt..fsb,d7             ; filing system

        exg     a0,a1
        move.l  a1,d0                    ; find base of slave block
        sub.l   sys_sbtb(a6),d0          ; position in table
        lsl.l   #sbt.shft,d0
        lea     (a6,d0.l),a2             ; shifted + sysvar base

ios_loop
        moveq   #sbt.driv,d0             ; mask of drive bits
        and.b   sbt_stat(a1),d0
        cmp.b   d0,d7                    ; right drive?
        bne.s   ios_next                 ; ... no

        moveq   #sbt.inus,d0
        and.b   sbt_stat(a1),d0          ; set status
        beq.s   ios_next
        movem.l ios.wrk,-(sp)            ; save work regs 
        pea     ios_rest
        move.l  ios_act+ios.wrkl+4(sp),-(sp) ; call action routine
        rts
ios_rest
        movem.l (sp)+,ios.wrk            ; restore work regs
        bne.s   ios_exit
ios_next
        addq.l  #8,a1                    ; next slave block
        add.w   #512,a2
        cmp.l   a0,a1                    ; at top?
        bls.s   ios_loop

        moveq   #0,d0

ios_exit
        movem.l (sp)+,ios.reg
        rts
        end
