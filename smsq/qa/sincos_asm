; QL Arithmetic SIN COS TAN COT  V2.01   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_sin
        xdef    qa_cos
        xdef    qa_tan
        xdef    qa_cot
        xdef    qa_pi
        xdef    qa_pim
        xdef    qa.pim
        xdef    qa.pimsw
        xdef    qa.pi3m

        xref    qa_range
        xref    qa_poly1
        xref    qa_polye
        xref    qa_push1
        xref    qa_neg
        xref    qa_abs
        xref    qa_swap
        xref    qa_dup
        xref    qa_div

;+++
; QL Arithmetic: COS
;
;       This routine finds the cosine of TOS. Algorithm TT
;
;       d0  r   error return 0 or ERR.OVFL
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_cos
qsn.reg reg     d1/d2/d3/a2
        movem.l qsn.reg,-(sp)
        moveq   #1,d3                    ; cos table in base range
        jsr     qa_abs                   ; make positive
        bra.s   qsn_do

;+++
; QL Arithmetic: SIN
;
;       This routine finds the sine of TOS. Algorithm TT
;
;       d0  r   error return 0 or ERR.OVFL
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_sin
        movem.l qsn.reg,-(sp)
        moveq   #0,d3
        tst.b   2(a1)                    ; - sin?
        bge.s   qsn_do
        moveq   #2,d3                    ; offset the range
        jsr     qa_neg                   ; negate
qsn_do
        lea     qsn_range,a2             ; range table
        jsr     qa_range
        bne.s   qsn_exit

        lsr.w   #1,d3                    ; sine or cosine table?
        bcs.s   qsn_cos

        lea     qsn_stab,a2
        jsr     qa_poly1                 ; evaluate sine
        bra.s   qsn_sign

qsn_cos
        lea     qsn_ctab,a2
        jsr     qa_polye                 ; evaluate cosine

qsn_sign
        lsr.b   #1,d3                    ; negate?
        bcc.s   qsn_ok
        jsr     qa_neg
qsn_ok
        moveq   #0,d0
qsn_exit
        movem.l (sp)+,qsn.reg
qsn_rts
        rts
qsn_ex2
        addq.l  #2,sp
        bra.s   qsn_exit

;+++
; QL Arithmetic: COT
;
;       This routine finds the cotangent of TOS. Algorithm TT
;
;       d0  r   error return 0 or ERR.OVFL
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_cot
        jsr     qa_dup                   ; duplicate
        bsr     qa_cos                   ; cosine
        bne.s   qsn_rts
        jsr     qa_swap
        bsr     qa_sin                   ; divided by sine
        jmp     qa_div
;+++
; QL Arithmetic: TAN
;
;       This routine finds the tangent of TOS. Algorithm TT
;
;       d0  r   error return 0 or ERR.OVFL
;       a1 c  p pointer to arithmetic stack
;       status return standard
;---
qa_tan
        jsr     qa_dup                   ; duplicate
        bsr     qa_sin                   ; sine
        bne.s   qsn_rts
        jsr     qa_swap
        bsr     qa_cos                   ; divided by cosine
        jmp     qa_div

;+++
; QL Arithmetic: PI
;
;       This routine pushes PI onto the stack. Algorithm TT
;
;       d0  r   error return 0
;       a1 c  u pointer to arithmetic stack
;       status return standard
;---
qa_pi
qa.pim  equ     $6487ed51                ; actually accurate to 35 bits!
qa.pimsw equ    $6488
qa.pi3m equ     $430548E1                ; nearly .375 lsb error

qa_pim  equ     *+2
        move.l  #qa.pim,-(a1)            ; actually accurate to 35 bits!
        move.w  #$0802,-(a1)             ; exponent of PI
        moveq   #0,d0
        rts


;----   dc.w    $0800,$6487,$ED51,0000   ; exactly internal value of PI
;----   dc.w    $0800,$9B78,$12AF,0000   ; ... -
        dc.w    $07f5,$812A,$F000        ; - range ls bits
        dc.w    $0801,$9b80,$0000        ; - range ms bits
        dc.l    $000fffff                ; max cycle
        dc.l    $fff00000                ; min cycle
        dc.w    $0800,$517C,$C1B7        ; 1/range approx
qsn_range

;        dc.w    $07fe,$5555,$5555        ; 1/3!
;        dc.w    $07fa,$4444,$44c8        ; (1+1.150832e-07)/5!
;        dc.w    $07F4,$6806,$1ab4        ; (1-4.158077e-05)/7!
;        dc.w    $07ee,$5d56,$7351        ; (1+9.416089e-02)/9!
;        dc.w    9                        ; tan polynomial
;qsn_ttab

        dc.w    $07fe,$aaaa,$aaab        ;-1/3!
        dc.w    $07fa,$4444,$43a5        ; (1-1.383316e-07)/5!
        dc.w    $07F4,$97fa,$fb54        ;-(1-5.570051e-05)/7!
        dc.w    $07ee,$5b74,$96a6        ; (1-1.094006e-02)/9!
        dc.w    9                        ; sin polynomial
qsn_stab

        dc.w    $0801,$4000,$0000        ; 1
        dc.w    $07ff,$8000,$0000        ; 1/2!
        dc.w    $07fc,$5555,$53aa        ;
        dc.w    $07f7,$a4fc,$4b29
        dc.w    $07f1,$66a8,$1126
        dc.w    8                        ; cos polynomial
qsn_ctab

        end
