; Convert floating point string to fl point  V2.01   1990  Tony Tebby  QJUMP

        section cv

        xdef    cr_decfp
        xdef    ca_decfp

        xref    cv_decil

        xref    qa_muld
        xref    qa_mul
        xref    qa_div
        xref    qa_addd
        xref    qa_neg
        xref    qa_power
        xref    qa_pushd

        include dev8_keys_err
;+++
; Decimal conversion: FP characters to FP on stack.
;
;       This routine converts the string at (a6,a0) to a Float.
;       Leading spaces are skipped.  It stops when a non-decimal
;       digit is encountered or the end of buffer is reached.
;
;       d0  r   error return 0 or ERR.IEXP
;       d7 c  p 0 or end of buffer
;       a0 c  u pointer to buffer containing string
;       a1 c  u pointer to stack to take result
;       a6      base address
;       status return standard
;---
cr_decfp
        add.l   a6,a0
        add.l   a6,a1
        add.l   a6,d7
        bsr.s   ca_decfp
        sub.l   a6,a0
        sub.l   a6,a1
        sub.l   a6,d7
        tst.l   d0
        rts

;+++
; Decimal conversion: FP characters to FP on stack.
;
;       This routine converts the string at (a0) to a Float.
;       Leading spaces are skipped.  It stops when a non-decimal
;       digit is encountered or the end of buffer is reached.
;
;       d0  r   error return 0 or ERR.IEXP
;       d7 c  p 0 or end of buffer
;       a0 c  u pointer to buffer containing string
;       a1 c  u pointer to stack to take result
;       status return standard
;---
ca_decfp
cdf.reg  reg    d1/d2/d3/d4/a0/a1
cdf.regd reg    d1/d2/d3/d4
cdf.rega reg    a0/a1
cdf.skpa equ    $08
        movem.l cdf.reg,-(sp)

        clr.l   -(a1)                    ; start with nothing
        clr.w   -(a1)
        moveq   #0,d3                    ; ... no sign
        moveq   #0,d4

cdf_splp
        cmp.l   d7,a0                    ; end of buffer?
        beq.l   cdf_iexp                 ; ... yes
        cmp.b   #' ',(a0)+               ; space?
        beq.s   cdf_splp

        move.b  -(a0),d0
        cmp.b   #'+',d0                  ; +?
        beq.s   cdf_skip
        cmp.b   #'-',d0                  ; -?
        bne.s   cdf_loop                 ; ... no
        bset    #31,d3                   ; flag minus

cdf_skip
        addq.l  #1,a0                    ; skip sign
cdf_loop
        moveq   #0,d1
        cmp.l   d7,a0                    ; end of buffer
        beq.s   cdf_sexp                 ; ... yes, done, set exponent
        move.b  (a0)+,d4
        sub.b   #'0',d4                  ; digit?
        blt.s   cdf_dp                   ; ... no, is it DP
        cmp.b   #9,d4
        bgt.s   cdf_exp                  ; ... no, is it Exponent
        bset    #30,d3                   ; we have digit
        tst.w   d3                       ; are we doing DPs
        beq.s   cdf_ndig
        addq.b  #1,d3                    ; ... yes
        beq.s   cdf_exp                  ; now that is ridiculous!!
cdf_ndig
        moveq   #10,d1
        bsr.l   cdf_float
        jsr     qa_muld                  ; multiply up number

        move.w  d4,d1
        bsr.s   cdf_float                ; and add next digit
        jsr     qa_addd
        bra.s   cdf_loop

cdf_dp
        addq.b  #'0'-'.',d4              ; DP?
        bne.s   cdf_exp                  ; ... no, end of number
        bset    #15,d3                   ; we have DP now
        bne.s   cdf_iexp                 ; but we had it before!!
        bra.s   cdf_loop

cdf_exp
        moveq   #0,d1                    ; zero exponent
        subq.l  #1,a0                    ; assume not exponent

        sub.b   #'E'-'0',d4              ; 'E'
        beq.s   cdf_doexp                ; ... yes
        sub.b   #'e'-'E',d4
        bne.s   cdf_sexp                 ; ... no, set exp

cdf_doexp
        addq.l  #1,a0
        moveq   #3,d0
        jsr     cv_decil                 ; get integer
        bne.s   cdf_exit

cdf_sexp
        ext.w   d3
        sub.w   d3,d1                    ; total exponent
        beq.s   cdf_sign
        sgt     d3
        bgt.s   cdf_powr
        neg.w   d1                       ; positive
cdf_powr
        move.w  d1,-8(a1)                ; to the power of ...
        moveq   #10,d1                   ; ... raise 10
        bsr.s   cdf_float
        jsr     qa_pushd
        subq.l  #2,a1
        jsr     qa_power                 ; do power
        bne.s   cdf_exit

        tst.b   d3                       ; mult or div
        bne.s   cdf_mexp                 ; multiply

        jsr     qa_div                   ; negative exp
        bra.s   cdf_echk

cdf_mexp
        jsr     qa_mul                   ; positive exp
cdf_echk
        bne.s   cdf_exit

cdf_sign
        moveq   #0,d0                    ; preset OK

        add.l   d3,d3                    ; -ve?
        bcc.s   cdf_check                ; ... no
        jsr     qa_neg                   ; ... yes

cdf_check
        add.l   d3,d3                    ; any character found?
        bcc.s   cdf_iexp                 ; ... no, bit not set
cdf_exit
        movem.l (sp)+,cdf.regd           ; restore regs
        tst.l   d0
        beq.s   cdf_ex1
        movem.l (sp),cdf.rega            ; ... error, restore a0/a1 as well
cdf_ex1
        addq.l  #cdf.skpa,sp
        rts
cdf_iexp
        moveq   #err.iexp,d0             ; bad number
        bra.s   cdf_exit


cdf_float
        add.w   d1,d1
        move.w  cdf_expn(pc,d1.w),d2
        move.w  cdf_mant(pc,d1.w),d1
        swap    d1
        clr.w   d1
        rts

cdf_mant dc.w   0,$4000,$4000,$6000,$4000,$5000,$6000,$7000,$4000,$4800,$5000
cdf_expn dc.w   0,$0801,$0802,$0802,$0803,$0803,$0803,$0803,$0804,$0804,$0804
        end
