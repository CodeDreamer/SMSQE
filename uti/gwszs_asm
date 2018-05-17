; Get window size for a string          1989 Jochen Merz V0.01

        section utility

        xdef    ut_gwszs

;+++
; This routine calculates the number of lines and characters per line needed to
; put in a string into a window. The string may contain the \ characters which
; are replaced by <LF>. The pointer to the string may be 0.
;
;               Entry                           Exit
;       D1.l                                    no of lines/max no of chars
;       A1      pointer to string               preserved
;---
ut_gwszs
gwsz_reg reg    d2-d4/a1
        movem.l gwsz_reg,-(sp)
        move.l  a1,d1                   ; zero pointer?
        beq.s   gwsz_ret                ; yes, return zero size
        moveq   #0,d1                   ; presume empty string
        moveq   #0,d2                   ; max characters counter
        move.w  (a1)+,d3                ; no of characters
        beq.s   gwsz_ret                ; does not contain any character!
gwsz_lin
        move.b  (a1)+,d4                ; get character
        cmp.b   #'\',d4                 ; do we have to replace it?
        bne.s   gwsz_nrp                ; no!
        moveq   #10,d4                  ; it is <LF> now
        move.b  d4,-1(a1)
gwsz_nrp
        cmp.b   #10,d4                  ; new line?
        bne.s   gwsz_chr                ; no
        add.l   #$00010000,d1           ; add a line
        cmp.w   d1,d2                   ; is the current line longest one?
        bls.s   gwsz_cct                ; no
        move.w  d2,d1                   ; put it in!
gwsz_cct
        moveq   #0,d2                   ; reset counter
        bra.s   gwsz_nxt

gwsz_chr
        addq.w  #1,d2                   ; add one character
gwsz_nxt
        subq.w  #1,d3                   ; decrement string length
        bne.s   gwsz_lin                ; until end of string
        cmp.b   #10,d4                  ; did the line end with <LF> ?
        beq.s   gwsz_ret                ; yes!
        add.l   #$00010000,d1           ; no, so add one line
        cmp.w   d1,d2                   ; is current line longest one?
        bls.s   gwsz_ret
        move.w  d2,d1                   ; yes, so use it
gwsz_ret
        movem.l (sp)+,gwsz_reg
        rts

        end
