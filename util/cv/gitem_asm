* Get an "item" from a string           v0.00   July 1988  J.R.Oakley  QJUMP
*
        section cv
*
        include 'dev8_keys_err'
        include 'dev8_keys_k'
*
        xdef    cv_gitem
*+++
* Copy an "item" from a string to a buffer. The item is enclosed by spaces
* or the end of the string.  The string pointer and length are updated
* to point to the first space after the item, or to the end (length=0).
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or ERR.BFFL
*       D1      string length                   updated
*       D7      end of buffer                   preserved
*       A0      buffer pointer                  updated
*       A1      string pointer                  updated
*---
cv_gitem
gitreg  reg     d2
        movem.l gitreg,-(sp)
        moveq   #k.space,d2
        tst.w   d1                      ; anything in string?
uci_rmls
        beq.s   uci_exok                ; no, that's OK
        cmp.b   (a1)+,d2                ; yes, is it space?
        bne.s   uci_cpts                ; no, copy from here
        subq.w  #1,d1
        bra.s   uci_rmls
uci_cpts
        subq.l  #1,a1                   ; back to non-space character
uci_cplp
        cmp.l   a0,d7                   ; off end of buffer?
        ble.s   uci_exbf                ; yes
        move.b  (a1)+,d0                ; no, get character
        cmp.b   d0,d2                   ; is it a space?
        beq.s   uci_sfnd                ; yes, finished
        move.b  d0,(a0)+                ; no, copy it
        subq.w  #1,d1                   ; one character fewer in source
        bgt.s   uci_cplp                ; there's more to come
        bra.s   uci_exok                ; there isn't, we're OK
uci_sfnd
        subq.l  #1,a1                   ; backspace to terminating space
uci_exok
        moveq   #0,d0
uci_exit
        movem.l (sp)+,gitreg
        rts
uci_exbf
        moveq   #err.bffl,d0
        bra.s   uci_exit
*
        end

