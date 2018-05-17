; QL Arithmetic Range Reduction     V2.01   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_range

        xref    qa_muld
        xref    qa_add
        xref    qa_dup
        xref    qa_nlint
        xref    qa_lfloat

        include 'dev8_keys_err'

;+++
; QL Arithmetic: Range reduction.
;
;       This routine POPs the argument on the TOS and reduces the range to
;       a long word multipler and a floating point remainder with magnitude
;       less than or equal to half the range.
;       Truncation errors may cause the magnitude of the remainder to exceed
;       half the range.
;
;       It requires a pointer to the end of a block of parameters:
;
;                       FP      -range (negative) ls bits
;                       FP      -range (negative) ms bits
;                       long    maximum range multiplier
;                       long    minimum range multiplier
;                       FP      multiplier to find reduction (=1/range)
;
;       The reduced argument is returned in registers.
;
;       d0  r   error return 0 or ERR.OVFL
;       d1  r   reduced argument mantissa (+ or -)
;       d2  r   reduced argument exponent
;       d3 c  u long word, range multiplier (normally 0 on call)
;       a1 c  u pointer to arithmetic stack
;       a2 c  u pointer to range data
;       status return standard
;---
qa_range
        move.l  -(a2),d1
        move.w  -(a2),d2
        jsr     qa_dup
        jsr     qa_muld                  ; preliminary reduction
        jsr     qa_nlint                 ; nearest long integer
        bne.s   qrg_exit
        add.l   (a1),d3                  ; keep it
        cmp.l   -(a2),d3                 ; ... in range?
        blt.s   qrg_ovfl
        cmp.l   -(a2),d3
        bgt.s   qrg_ovfl
        jsr     qa_lfloat                ; M, x
        move.w  6(a1),-6(a1)
        move.l  8(a1),-4(a1)
        move.w  (a1),6(a1)
        move.l  2(a1),8(a1)              ; (x), M, M
        move.l  -(a2),d1
        move.w  -(a2),d2
        jsr     qa_muld                  ; (x), -M*r2
        subq.l  #6,a1
        jsr     qa_add                   ; x-M*r2, M
        addq.l  #6,a1
        move.l  -(a2),d1
        move.w  -(a2),d2                 ; the more significant bit
        jsr     qa_muld                  ; (x-M*r2), -M*r1
        subq.l  #6,a1
        jsr     qa_add                   ; x-m*r2-m*r1
        move.w  (a1),d2
        move.l  2(a1),d1
        moveq   #0,d0
qrg_exit
        rts

qrg_ovfl
        moveq   #err.ovfl,d0
        rts
        end
