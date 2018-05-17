; QL Arithmetic Polynomial Evaluation   V2.00   1990  Tony Tebby  QJUMP

        section qa

        xdef    qa_poly
        xdef    qa_polyo
        xdef    qa_poly1
        xdef    qa_polye

        xref    qa_dup
        xref    qa_square
        xref    qa_mul
        xref    qa_muld
        xref    qa_add
        xref    qa_addd

;+++
; QL Arithmetic: Odd polynomial of TOS, nth order, (n+1)/2 terms, c1=1, so
; only (n-1)/2 terms are specified.
;
; This is like POLYO, but the coefficient of x is one. To save rounding errors
; this routine adds x as the last operation. P=x+x(Pe(x^2))
;
;       d0  r   error return 0
;       a1 c  p pointer to arithmetic stack
;       a2 c  u pointer to n (word), Cn, Cn-2, ... C3 - in reverse order
;       status return standard
;---
qa_poly1
        jsr     qa_dup                   ; duplicate
        jsr     qa_dup                   ; duplicate
        jsr     qa_square                ; and square it
        jsr     qa_dup                   ; duplicate that
        move.w  -(a2),d0                 ; order
        lsr.w   #1,d0                    ; number of terms
        subq.w  #1,d0
        bsr.s   qpy_do
        jsr     qa_mul                   ; raise the order by 2
        jsr     qa_mul                   ; ... by one more
        jmp     qa_add
;+++
; QL Arithmetic: Odd polynomial of TOS, nth order, (n+1)/2 terms
;
;       d0  r   error return 0
;       a1 c  p pointer to arithmetic stack
;       a2 c  u pointer to n (word), Cn, Cn-2, .... C1 - in reverse order
;       status return standard
;---
qa_polyo
        jsr     qa_dup                   ; duplicate
        bsr.s   qa_polye                 ; even polynomial
        jmp     qa_mul                   ; ... odd

;+++
; QL Arithmetic: Even polynomial of TOS, nth order, n/2+1 terms
;
;       d0  r   error return 0
;       a1 c  p pointer to arithmetic stack
;       a2 c  u pointer to n (word), Cn, Cn-2, .... C0 - in reverse order
;       status return standard
;---
qa_polye
        jsr     qa_square                ; square it
        move.w  -(a2),d0                 ; order
        lsr.w   #1,d0                    ; number of terms -1
        bra.s   qpy_do

;+++
; QL Arithmetic: Polynomial of TOS, nth order, n+1 terms
;
;       d0  r   error return 0
;       a1 c  p pointer to arithmetic stack
;       a2 c  u pointer to n (word), Cn, Cn-1, .... C0 - in reverse order
;       status return standard
;---
qa_poly
qpy.reg reg     d1/d2/d3/d4/d5
        move.w  -(a2),d0
qpy_do
        movem.l qpy.reg,-(sp)
        move.w  d0,d5                    ; number of terms -1

        move.w  (a1)+,d4                 ; x
        move.l  (a1)+,d3
        move.l  -(a2),-(a1)
        move.w  -(a2),-(a1)              ; last term
        bra.s   qpy_eloop

qpy_loop
        move.w  d4,d2
        move.l  d3,d1                    ; x
        jsr     qa_muld                  ; multiply up
        move.l  -(a2),d1                 ; previous term
        move.w  -(a2),d2
        beq.s   qpy_eloop
        jsr     qa_addd                  ; add coefficient
qpy_eloop
        dbra    d5,qpy_loop

qpy_exit
        movem.l (sp)+,qpy.reg
qpy_rts
        rts
        end
