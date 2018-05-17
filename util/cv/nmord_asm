; Sub-string comparison for Name order     1989  Tony Tebby   QJUMP

        section cv

        xdef    cv_nmord

        xref    cv_notab
;+++
; Compare two sub-strings for name order. Embedded numbers are ordered
; using their values. The comparison continues until a non-printing character
; is found in one or the other name. It is possible that this routine may find
; both names to be equivalent, but that is not its function.
;
;       Registers:
;               Entry                           Exit
;       D0                                      -1 (a0) < (a1)
;                                                0 (a0) = (a1)
;                                               +1 (a0) > (a1)
;       A0      name 0 start                    preserved
;       A1      name 1 start                    preserved
;---
cv_nmord
cno.reg reg     d1/d2/a0/a1/a2
        movem.l cno.reg,-(sp)
        lea     cv_notab(pc),a2          ; point to name order table
        moveq   #0,d0                    ; make name 0 word OK
        moveq   #0,d1                    ; make name 1 word OK
        moveq   #0,d2                    ; make numbers the same

cno_cmpl
        move.b  (a0)+,d0
        move.b  0(a2,d0.w),d0            ; get order char from name 0
        beq.s   cno_end0                 ; ... end of name 0
        blt.s   cno_dig0                 ; ... digit in name zero
        move.b  (a1)+,d1
        cmp.b   0(a2,d1.w),d0            ; is it same as name 1?
        beq.s   cno_cmpl                 ; ... yes, carry on
        move.b  0(a2,d1.w),d1            ; ... no, see which is larger
        bge.s   cno_cd0                  ; ... ... not digit, compare directly
        moveq   #$40,d1                  ; ... it was digit, use dummy value
        bra.s   cno_cd0

cno_dig0
        move.b  (a1)+,d1                 ; name 0 was first digit
        move.b  0(a2,d1.w),d1            ; what about name 1?
        blt.s   cno_dig1                 ; ... yes, compare values
        sub.b   #$40,d1                  ; ... no, separator or letter?
        ble.s   cno_expl                 ; ... separator, name 0 is greater
        bra.s   cno_exmi                 ; ... letter, name 0 is lesser

cno_ndig
        move.b  (a0)+,d0                 ; next digit name 0
        move.b  0(a2,d0.w),d0            ; is it?
        bge.s   cno_endd                 ; ... no, end of digits in name zero
        move.b  (a1)+,d1                 ; name 0 was digit, what about name 1?
        move.b  0(a2,d1.w),d1
        bge.s   cno_expl                 ; ... not digit, name 0 is greater
cno_dig1
        tst.b   d2                       ; number order set?
        bne.s   cno_ndig                 ; ... yes, look for next digit
        move.b  d1,d2
        sub.b   d0,d2                    ; ... no, set number order
        bra.s   cno_ndig

cno_endd
        move.b  (a1),d1                  ; end if digits in name 0
        tst.b   0(a2,d1.w)               ; end of digits in name 1?
        bmi.s   cno_exmi                 ; ... no, name 1 is greater
        move.b  d2,d0                    ; which is greater?
        bne.s   cno_sd0                  ; ... one or the other
        subq.l  #1,a0                    ; backspace name 0
        bra.s   cno_cmpl                 ; numbers the same, carry on checking

cno_end0
        move.b  (a1)+,d1                 ; end of name 0
        tst.b   (a2,d1.w)                ; of name 1 too?
        beq.s   cno_exz                  ; ... yes
        bra.s   cno_exmi                 ; ... no, name 0 is less

cno_cd0
        sub.b   d1,d0                    ; set return code
cno_sd0
        bmi.s   cno_exmi
        beq.s   cno_exz
cno_expl
        moveq   #1,d0                    ; name0 > name 1
cno_exz
        movem.l (sp)+,cno.reg
        rts
cno_exmi
        moveq   #-1,d0                   ; name0 < name 1
        movem.l (sp)+,cno.reg
        rts

        end
