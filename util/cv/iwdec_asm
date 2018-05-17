* Convert word to ASCII number          v0.00   J.R.Oakley  Dec 1986  QJUMP
*
        section cv
*
        include 'dev8_keys_qlv'
*
        xdef    cv_iwdec
*+++
* Convert a word to a series of characters in a buffer.
*
*       Registers:
*               Entry                           Exit
*       A0      character buffer                updated
*       A1      pointer to word                 preserved
*---
cv_iwdec
*
cnvreg  reg     d0-d3/a1-a3
        movem.l cnvreg,-(sp)
*
        sub.l   a6,a1                   ; point to buffer, A6 relative 
        sub.l   a6,a0                   ; and to result, A6 rel
        move.w  cv.iwdec,a2             ; convert word to decimal string
        jsr     (a2)
        add.l   a6,a0                   ; make running pointer absolute again
*
        movem.l (sp)+,cnvreg
        rts
*
        end
