; Reserve space on arithmetic stack

        section utility

        include dev8_keys_bv
        include dev8_keys_qlv

        xdef    ri_resrv

;+++
; Reserve space on arithmetic stack and point to bottom of free space,
; so that the parameter may be filled in directly.
; The space needed is evened up, so that word access is possible.
;
;               Entry                           Exit
;       d1.l    space needed                    smashed
;       a1      pointer to arithmetic-stack     updated
;       a6      SuperBASIC master pointer       preserved
;
;       Error return: out of memory
;       Condition codes set
;---
ri_resrv
        addq.l  #1,d1           ; even up space
        bclr    #0,d1
        movem.l d1-d3/a2,-(sp)  ; preserve registers
        move.w  qa.resri,a2
        jsr     (a2)            ; try to get space
        move.l  bv_rip(a6),a1
        movem.l (sp)+,d1-d3/a2  ; get registers
        sub.l   d1,a1           ; to start of free space
        tst.l   d0
        rts

        end
