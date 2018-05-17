; Convert decimal string to long word  V2.01   1990  Tony Tebby  QJUMP

        section cv

        xdef    cr_deciw
        xdef    cr_decil

        xref    cv_decil

        include dev8_keys_err
;+++
; Decimal conversion: characters to word on stack
;
;       This routine converts the string at (a6,a0) to an integer.
;       Leading spaces are skipped.
;       It stops when a non-decimal digit is encountered, enough
;       digits have been read or the end of buffer is reached.
;
;       d0  r   error return 0 or ERR.OVFL or ERR.IEXP (no digits in number)
;       d1  r   (long) result
;       d7 c  p 0 or end of buffer
;       a0 c  u pointer to buffer containing string
;       a1 c  u pointer to stack to take result
;       a6      base address
;       status return standard
;---
cr_deciw
        add.l   a6,a0
        add.l   a6,a1
        add.l   a6,d7
        jsr     cv_decil
        bne.s   cdi_exit
        move.w  d1,d0
        ext.l   d0
        cmp.l   d1,d0                    ; true word?
        bne.s   cdi_ov1                  ; ... no
        move.w  d1,-(a1)                 ; put result on stack
        moveq   #0,d0                    ; and set ok
cdi_exit
        sub.l   a6,a0
        sub.l   a6,a1
        sub.l   a6,d7
        tst.l   d0
        rts

cdi_ov1
        moveq   #err.ovfl,d0
        bra.s   cdi_exit

;+++
; Decimal conversion: characters to long word on stack
;
;       This routine converts the string at (a6,a0) to an integer.
;       Leading spaces are skipped.
;       It stops when a non-decimal digit is encountered, enough
;       digits have been read or the end of buffer is reached.
;
;       d0  r   error return 0 or ERR.OVFL or ERR.IEXP (no digits in number)
;       d1  r   (long) result
;       d7 c  p 0 or end of buffer
;       a0 c  u pointer to buffer containing string
;       a1 c  u pointer to stack to take result
;       a6      base address
;       status return standard
;---
cr_decil
        add.l   a6,a0
        add.l   a6,a1
        add.l   a6,d7
        jsr     cv_decil
        bne.s   cdl_exit
        move.l  d1,-(a1)
cdl_exit
        sub.l   a6,a0
        sub.l   a6,a1
        sub.l   a6,d7
        tst.l   d0
        rts

        end
