; QL Arithmetic FLOAT      V2.01   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_float
        xdef    qa_lfloat
        xdef    qa_flotd

;+++
; QL Arithmetic: LFLOAT
;
;       This routine pops the long word at TOS, converts to Float and pushes it.
;
;       d0  r   error return 0
;       a1 c  u pointer to arithmetic stack
;       status return standard
;---
qa_lfloat
qaf.reg reg     d1/d2/d3
        movem.l qaf.reg,-(sp)
        moveq   #0,d2
        move.l  (a1)+,d1
        bra.s   qaf_ldo

;+++
; QL Arithmetic: FLOAT
;
;       This routine pops the integer at TOS, converts to Float and pushes it.
;
;       d0  r   error return 0
;       a1 c  u pointer to arithmetic stack
;       status return standard
;---
qa_float
        movem.l qaf.reg,-(sp)

        move.w  (a1)+,d1
        bra.s   qaf_do

;+++
; QL Arithmetic: FLOAT D1
;
;       This routine converts an integer to Float and pushes it.
;
;       d0  r   error return 0
;       d1 c  p integer to float
;       a1 c  u pointer to arithmetic stack
;       status return standard
;---
qa_flotd
        movem.l qaf.reg,-(sp)
qaf_do
        moveq   #0,d2
        ext.l   d1                       ; long integer
qaf_ldo
        beq.s   qaf_push                 ; ... zero, push it
        move.w  #$081f,d2                ; dummy mantissa

        moveq   #16,d0                   ; initial shift
qaf_loop
        move.l  d1,d3                    ; save mantissa
        asl.l   d0,d3                    ; shift
        bvs.s   qaf_next
        move.l  d3,d1                    ; ... keep it
        sub.w   d0,d2
qaf_next
        lsr.w   #1,d0
        bne.s   qaf_loop

qaf_push
        move.l  d1,-(a1)
        move.w  d2,-(a1)                 ; push exponent

        moveq   #0,d0
        movem.l (sp)+,qaf.reg
        rts
        end
