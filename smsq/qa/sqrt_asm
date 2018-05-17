; QL Arithmetic Square Root  V2.01   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_sqrt

        include 'dev8_keys_err'
;+++
; QL Arithmetic: Square Root
;
;       This routine takes the square root of TOS. Algorithm TT
;
;       d0  r   error return 0 or ERR.IEXP
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_sqrt
qsq.reg reg     d1/d2/d3
        move.l  2(a1),d0                 ; negative square root?
        bgt.s   qsq_exp                  ; ... no
        beq.s   qsq_rts                  ; ... no, nothing
        moveq   #err.iexp,d0
qsq_rts
        rts
qsq_exp
        movem.l qsq.reg,-(sp)
        move.w  (a1),d2

qsq_norm
        add.l   d0,d0                    ; move mantissa up
        bmi.s   qsq_exp2                 ; ... ok, halve exponent

        subq.w  #1,d2                    ; negative exp!!
        bra.s   qsq_norm

qsq_exp2
        add.l   d0,d0                    ; get rid of the bit we know about
        add.w   #$801,d2                 ; double the offset
        lsr.w   #1,d2
        roxr.l  #1,d0                    ; (x-1)/2 (0.5 to 0.99999 is negative)
        bge.s   qsq_sexp
        asr.l   #1,d0
qsq_sexp
        move.w  d2,(a1)                   ; save exponent
        move.l  d0,d1
        swap    d1
        asr.w   #1,d1
        move.w  d1,d2                    ; 0.25x
        asr.w   #1,d2
        add.w   d1,d2                    ; 0.375x
        muls    d1,d2                    ; 0.09375x^2  (MSW)
        swap    d2
        sub.w   d1,d2                    ; -0.25x+0.09375x^2
        muls    d1,d2                    ; -0.0625x^2+0.0234375x^3  (MSW)
        clr.w   d2
        swap    d2
        add.w   d1,d2                    ; (+0.5x-0.125x^2+0.046875x^3)/2
        add.w   #$7f87,d2                ; (.9963+0.5x-0.125x^2+0.046875x^3)/2
                                 ; d2.w is now msw of result accurate to 7 bits
        bchg    #31,d0
        beq.s   qsq_upper
        add.w   d2,d2            ; other half!
        move.l  d0,d1
        divu    d2,d1
        swap    d1
        clr.w   d1
        swap    d1
        lsr.l   #1,d2                    ; second approximation
        add.w   d1,d2
        bcc.s   qsq_app3
        bset    #31,d0                   ; trivial
        move.l  d0,d2
        bra.s   qsq_res

qsq_app3
        divu    d2,d0                    ; msw of half third approx in LSW
        move.l  d0,d1
        clr.w   d1
        divu    d2,d1                    ; lsw of half third approx in LSW
        swap    d0
        move.w  d1,d0
        swap    d2
        lsr.l   #1,d2
        add.l   d0,d2                    ; third approx
        bra.s   qsq_res

qsq_upper
        lsr.l   #1,d0
qsq_app2
        move.l  d0,d1
        divu    d2,d1
        swap    d1
        clr.w   d1
        swap    d1
        add.l   d1,d2
        lsr.l   #1,d2                    ; second approximation
        divu    d2,d0                    ; msw of half third approx in LSW
        move.l  d0,d1
        clr.w   d1
        divu    d2,d1                    ; lsw of half third approx in LSW
        swap    d0
        move.w  d1,d0
        swap    d2
        add.l   d0,d2                    ; twice third approx
        roxr.l  #1,d2                    ; third approx
qsq_res
        lsr.l   #1,d2                    ; normalised
        bcc.s   qsq_ok
        addq.l  #1,d2                    ; add carry
        bpl.s   qsq_ok
        lsr.l   #1,d2                    ; and re-normalise
        addq.w  #1,(a1)
qsq_ok
        move.l  d2,2(a1)

        moveq   #0,d0
qsq_exit
        movem.l (sp)+,qsq.reg
        rts

        end
