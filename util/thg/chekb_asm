; Set Check Byte          V0.00   1989  Tony Tebby  QJUMP

        section thing

        xdef    th_chekb

        xref    cv_lctab

;+++
; Set the CHECK byte to standard value
;
;       d0  r   check byte value
;       a0 c  p pointer to name
;       status return arbitrary
;---
th_chekb
tcb.reg reg     d1/d2/a0/a2
        movem.l tcb.reg,-(sp)
        lea     cv_lctab,a2              ; lowercase table
        moveq   #0,d0                    ; initialise check byte
        moveq   #0,d1                    ; and char register
        move.w  (a0)+,d2                 ; number of chars
        bra.s   tcb_eloop
tcb_loop
        move.b  (a0)+,d1                 ; next char
        add.b   (a2,d1.w),d0             ; lowercased
        ror.b   #1,d0                    ; and rotated
tcb_eloop
        dbra    d2,tcb_loop              ; next byte

        movem.l (sp)+,tcb.reg
        rts
        end
