; QL Arithmetic EXP        V2.01   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_exp

        xref    qa_range
        xref    qa_poly
        xref    qa_pushd
        xref    qa_dup
        xref    qa_add1
        xref    qa_subr
        xref    qa_mul
        xref    qa_muld
        xref    qa_div

;+++
; QL Arithmetic: EXP
;
;       This routine finds the exponential of TOS. Algorithm Cody and Waite / TT
;
;       d0  r   error return 0 or ERR.OVFL
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_exp
qxp.reg reg     d1/d2/d3/a2
        movem.l qxp.reg,-(sp)
        lea     qxp_tab,a2               ; range table
        moveq   #0,d3                    ; no range offset
        jsr     qa_range
        beq.s   qxp_go
        neg.l   d3                       ; negative or positive overflow?
        blt.s   qxp_exit                 ; ... with correct cond codes!
        clr.w   (a1)
        clr.l   2(a1)                    ; zero
        bra.s   qxp_ok
qxp_go
        jsr     qa_pushd                 ; duplicate #g
        jsr     qa_muld                  ; #z, g
        move.w  (a1),d2
        move.l  2(a1),d1
        jsr     qa_poly                  ; #P, g
        jsr     qa_mul                   ; #g*P
        jsr     qa_dup                   ; #g*P, #g*P
        jsr     qa_pushd                 ; #z, #g*P
        jsr     qa_poly                  ; #Q, #g*P, #g*P
        jsr     qa_subr                  ; #(Q-g*P), #g*P
        jsr     qa_div                   ; #g*P/(Q-g*P)
        addq.w  #1,(a1)                  ; *2
        jsr     qa_add1                  ; +1
        add.w   d3,(a1)                  ; and the exponent
        bge.s   qxp_ok
        move.w  (a1)+,d2
        neg.w   d2
        move.l  (a1),d1
        clr.l   (a1)
        cmp.w   #32,d2                   ; shift mantissa
        bgt.s   qxp_ok2
        asr.l   d2,d1
        move.l  d1,(a1)                  ; some bits left
QXP_ok2
        clr.w   -(a1)                    ; but no exponent
qxp_ok
        moveq   #0,d0
qxp_exit
        movem.l (sp)+,qxp.reg
        rts

        dc.w    $0800,$4000,$0000
        dc.w    $07FC,$6DB4,$CE83
        dc.w    $07F5,$4DEF,$09CA
        dc.w    2                        ; Q polynomial

        dc.w    $07FF,$4000,$0000
        dc.w    $07F9,$617D,$E4BB
        dc.w    1                        ; P polynomial

;----   dc.w    $0800,$58B9,$0BFB,$E8E8  ; range six byte mantissa
;----   dc.w    $0800,$A746,$F404,$1718  ; - range six byte mantissa
;----   dc.w    $07f0,$F404,$1718        ; - range lslw
        dc.w    $07ED,$A020,$B8c0        ; - range lslw normalised
        dc.w    $0800,$A747,$0000        ; - range msw
        dc.l    $000007f0                ; maxexp
        dc.l    $fffff7f0                ; minexp
        dc.w    $0801,$5C55,$1D95        ; 1/range approx
qxp_tab

        end
